package com.kashier.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.harium.postgrest.Condition;
import com.harium.postgrest.Insert;
import com.kashier.models.InventoryItem;
import com.kashier.models.Item;

import java.io.IOException;
import java.util.HashMap;

import static com.kashier.App.supabase;


public class InventoryController {
    private HashMap<String, InventoryItem> inventory;

    public InventoryController() throws  IOException {
        ItemController itemController = new ItemController();
        String result = supabase.database().findAll("inventory");
        JsonArray data = (JsonArray) JsonParser.parseString(result);
        inventory = new HashMap<>();
        for (Object o : data) {
            JsonObject itemObj = (JsonObject) o;
            InventoryItem item = new Gson().fromJson(itemObj, InventoryItem.class);
            Item details = itemController.getItemByQR(item.getQR());
            if (details == null) throw new IOException("Item not found: " + item.getQR());

            item.setName(details.getName());
            item.setPrice(details.getPrice());
            inventory.put(item.getQR(), item);
        }
    }

    public InventoryItem addItem(String qr, int quantity) throws IOException {
        // Check if item already exists
        InventoryItem item = getItemByQR(qr);

        // If item exists in the inventory, update quantity
        if (item != null) {
            item = inventory.get(qr);
            item.setStock(item.getStock() + quantity);
        } else {
            // Grab item details from items table
            item = new InventoryItem();
            Item details = new ItemController().getItemByQR(qr);
            if (details == null) throw new IOException("Item not found: " + qr);
            item.setName(details.getName());
            item.setPrice(details.getPrice());
            item.setQR(qr);
            item.setStock(quantity);
        }

        // Save to database
        Insert.Row data = Insert.row()
            .column("qr", item.getQR())
            .column("stock", item.getStock());
        supabase.database().save("inventory", data);

        // Add to inventory
        inventory.put(item.getQR(), item);
        return inventory.get(item.getQR());
    }

    public InventoryItem removeItem(String qr, int quantity) throws Exception {
        // Check if item already exists
        InventoryItem item = getItemByQR(qr);

        // If item exists in the inventory, update quantity
        if (item != null) {
            item = inventory.get(qr);
            item.setStock(item.getStock() - quantity);

            if (item.getStock() <= 0) {
                // Delete from database
                Condition conditions = Condition.and(
                        Condition.eq("qr", item.getQR())
                );
                supabase.database().delete("items", conditions);

                // Remove from inventory
                inventory.remove(item.getQR());
            } else {
                // Save to database
                Insert.Row data = Insert.row()
                        .column("qr", item.getQR())
                        .column("stock", item.getStock());
                supabase.database().save("inventory", data);

                // Update item quantity
                inventory.put(item.getQR(), item);
            }

            return inventory.get(item.getQR());
        }

        return null;
    }

    public HashMap<String, InventoryItem> getInventory() {
        return inventory;
    }

    public InventoryItem getItemByQR(String qr) {
        if (inventory.containsKey(qr)) {
            return inventory.get(qr);
        }

        return null;
    }
}

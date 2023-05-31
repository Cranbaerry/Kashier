package com.kashier.controllers;

import com.harium.postgrest.Condition;
import com.harium.postgrest.Insert;
import com.kashier.models.InventoryItem;
import com.kashier.models.Item;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import static com.kashier.App.parser;
import static com.kashier.App.supabase;


public class InventoryController {
    private HashMap<String, InventoryItem> inventory;

    public InventoryController() throws ParseException, IOException {
        ItemController itemController = new ItemController();
        String result = supabase.database().findAll("inventory");
        JSONArray data = (JSONArray) parser.parse(result);
        inventory = new HashMap<String, InventoryItem>();
        for (Object o : data) {
            JSONObject itemObj = (JSONObject) o;
            Item item = itemController.getItemByQR((String) itemObj.get("qr"));
            if (item == null) continue;
            System.out.println(item.getQR());
            itemObj.put("name", item.getName());
            itemObj.put("price", item.getPrice());

            InventoryItem inventoryItem = new InventoryItem(itemObj);
            inventoryItem.setStock(Integer.parseInt(itemObj.get("stock").toString()));
            inventory.put(inventoryItem.getQR(), inventoryItem);
        }
    }

    public void addItem(InventoryItem itemObj, int quantity) throws IOException {
        // Check if item already exists
        InventoryItem existingItem = getItemByQR(itemObj.getQR());

        // If item exists, increment quantity
        if (existingItem != null) {
            itemObj.setStock(existingItem.getStock() + quantity);
        }

        // Save to database
        Insert.Row data = Insert.row()
                .column("qr", itemObj.getQR())
                .column("stock", itemObj.getStock());
        supabase.database().save("inventory", data);

        // Add to inventory
        inventory.put(itemObj.getQR(), itemObj);
    }

    public void removeItem(InventoryItem itemObj, int quantity) throws Exception {
        // Check if item already exists
        InventoryItem existingItem = getItemByQR(itemObj.getQR());

        // If item exists, decrease quantity
        if (existingItem != null) {
            itemObj.setStock(existingItem.getStock() - quantity);
            // If item quantity is 0, remove item

            if (itemObj.getStock() <= 0) {
                // Delete from database
                Condition conditions = Condition.and(
                        Condition.eq("qr", itemObj.getQR())
                );
                supabase.database().delete("items", conditions);
                System.out.println("Deleted item: " + itemObj.getQR());

                // Remove from inventory
                inventory.remove(itemObj.getQR());
            } else {
                // Save to database
                Insert.Row data = Insert.row()
                        .column("qr", itemObj.getQR())
                        .column("stock", itemObj.getStock());
                supabase.database().save("inventory", data);
                System.out.println("Updated item: " + itemObj.getQR());

                // Update item quantity
                inventory.put(itemObj.getQR(), itemObj);
            }
        } else {
            throw new Exception("Item does not exist");
        }
    }

    public InventoryItem getItemByQR(String qr) {
        if (inventory.containsKey(qr)) {
            return inventory.get(qr);
        }

        return null;
    }

    public void printInventory() {
        for (String key : inventory.keySet()) {
            System.out.println(inventory.get(key).getQR() + " " + inventory.get(key).getStock());
        }
    }
}

package com.kashier.controllers;

import com.harium.postgrest.Condition;
import com.harium.postgrest.Insert;
import com.kashier.models.Inventory;
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
    private Inventory inventory;

    public InventoryController() throws ParseException, IOException {

        ItemController itemController = new ItemController();
        String result = supabase.database().findAll("inventory");
        JSONArray data = (JSONArray) parser.parse(result);
        this.inventory = new Inventory();
        for (Object o : data) {
            JSONObject itemObj = (JSONObject) o;
            Item item = itemController.getItemByQR((String) itemObj.get("qr"));
            if (item == null) continue;
            System.out.println(item.getQR());
            itemObj.put("name", item.getName());
            itemObj.put("price", item.getPrice());

            InventoryItem inventoryItem = new InventoryItem(itemObj);
            inventoryItem.setStock(Integer.parseInt(itemObj.get("stock").toString()));
            System.out.println("Name: " + inventoryItem.getQR() + " Stock: " + inventoryItem.getStock());
            this.inventory.upsertItem(inventoryItem);
        }
    }

    public HashMap<String, InventoryItem> getItems() {
        return this.inventory.getItems();
    }

    public void addItem(InventoryItem itemObj) throws IOException {
        // Check if item already exists
        InventoryItem existingItem = this.inventory.getItemByQR(itemObj.getQR());

        // If item exists, increment quantity
        if (existingItem != null) {
            itemObj.setStock(existingItem.getStock() + itemObj.getStock());
        }

        // Save to database
        Insert.Row data = Insert.row()
                .column("qr", itemObj.getQR())
                .column("stock", itemObj.getStock());
        supabase.database().save("inventory", data);

        // Add to inventory
        inventory.upsertItem(itemObj);
    }

    public void removeItem(InventoryItem itemObj) throws IOException {
        // Check if item already exists
        InventoryItem existingItem = this.inventory.getItemByQR(itemObj.getQR());

        // If item exists, decrease quantity
        if (existingItem != null) {
            System.out.println("Existing item: " + existingItem.getQR() + " " + existingItem.getStock());
            System.out.println("Item to remove: " + itemObj.getQR() + " " + itemObj.getStock());
            itemObj.setStock(existingItem.getStock() - itemObj.getStock());
            // If item quantity is 0, remove item
            if (itemObj.getStock() <= 0) {
                // Delete from database
//                Condition conditions = Condition.and(
//                        Condition.eq("qr", itemObj.getQR())
//                );
//                supabase.database().delete("items", conditions);
                System.out.println("Deleted item: " + itemObj.getQR());

                itemObj.setStock(0);
                inventory.getItems().remove(itemObj.getQR());
            } else {
                // Save to database
                Insert.Row data = Insert.row()
                        .column("qr", itemObj.getQR())
                        .column("stock", itemObj.getStock());
                supabase.database().save("inventory", data);
                System.out.println("Updated item: " + itemObj.getQR());

                // Update item quantity
                inventory.upsertItem(itemObj);
            }
        }
    }
}

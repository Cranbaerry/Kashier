package com.kashier.controllers;

import com.harium.postgrest.Condition;
import com.harium.postgrest.Insert;
import com.kashier.models.Inventory;
import com.kashier.models.Item;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static com.kashier.App.parser;
import static com.kashier.App.supabase;


public class ItemController {
    public Item upsertItem(Item itemObj) {
        try {
            Insert.Row data = Insert.row()
                    .column("qr", itemObj.getQR())
                    .column("name", itemObj.getName())
                    .column("price", itemObj.getPrice());
            supabase.database().save("items", data);
            return itemObj;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Item removeItem(Item itemObj) {
        try {
            // Soft deletion is done by setting the deleted_at column to the current timestamp
            Instant instant = Instant.now();
            Insert.Row data = Insert.row()
                    .column("qr", itemObj.getQR())
                    .column("deleted_at", instant.toString());

            supabase.database().save("items", data);
            return itemObj;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Item> getItems() {
        try {
            String result = supabase.database().findAll("items");
            JSONArray itemsData = (JSONArray) parser.parse(result);
            ArrayList<Item> items = new ArrayList<Item>();
            for (Object o : itemsData) {
                JSONObject itemObj = (JSONObject) o;
                // Skip soft deleted items
                if (itemObj.get("deleted_at") != null) continue;
                Item item = new Item(itemObj);
                items.add(item);
            }

            return items;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Item getItemByQR(String qr) {
        try {
            // Bug in postgrest-java library, cannot use Condition.eq("qr", qr) directly (must use Condition.and)
            Condition conditions = Condition.and(
                    Condition.eq("qr", "123")
            );
            String result = supabase.database().find("items", conditions);
            JSONArray data = (JSONArray) parser.parse(result);

            if (data.size() == 0) return null;
            JSONObject itemObj = (JSONObject) data.get(0);
            Item item = new Item(itemObj);
            return item;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}

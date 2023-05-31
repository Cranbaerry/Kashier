package com.kashier.models;

import com.kashier.interfaces.IInventoryItem;
import com.kashier.interfaces.IItem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory {
    private HashMap<String, InventoryItem> items = new HashMap<String, InventoryItem>();

//    public Inventory(JSONArray dataArr) {
//        for (Object o : dataArr) {
//            JSONObject itemObj = (JSONObject) o;
//            System.out.println(itemObj);
//            InventoryItem item = new InventoryItem(itemObj);
//            this.items.put(item.getQR(), item);
//        }
//    }

    public void upsertItem(InventoryItem item) {
        this.items.put(item.getQR(), item);
    }

    public HashMap<String, InventoryItem> getItems() {
        return this.items;
    }

    public InventoryItem getItemByQR(String qr) {
        if (this.items.containsKey(qr)) {
            return this.items.get(qr);
        }
        return null;
    }
}

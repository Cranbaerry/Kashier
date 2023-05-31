package com.kashier.models;

import org.json.simple.JSONObject;
import com.kashier.interfaces.IInventoryItem;

public class InventoryItem extends Item implements IInventoryItem {
    int stock;

    public InventoryItem(JSONObject obj) {
        super(obj);
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("qr", this.qr);
        obj.put("name", this.name);
        obj.put("price", this.price);
        obj.put("stock", this.stock);
        return obj;
    }
}

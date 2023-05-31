package com.kashier.models;

import com.kashier.interfaces.IItem;
import org.json.simple.JSONObject;

public class Item implements IItem {
    String name;
    double price;
    String qr;

    String deleted_at;

    public Item(JSONObject obj) {
        this.qr = obj.get("qr").toString();
        this.name = obj.get("name").toString();
        this.price = Double.parseDouble(obj.get("price").toString());

        if (obj.get("deleted_at") != null)
            this.deleted_at = obj.get("deleted_at").toString();
    }

    public String getQR() {
        return this.qr;
    }

    public String getName() {
        return this.name;
    }

    public String getDeletedAt() {
        return this.deleted_at;
    }

    public double getPrice() {
        return this.price;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("qr", this.qr);
        obj.put("name", this.name);
        obj.put("price", this.price);
        return obj;
    }
}

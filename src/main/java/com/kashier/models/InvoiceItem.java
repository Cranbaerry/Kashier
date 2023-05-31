package com.kashier.models;

import com.kashier.interfaces.IInvoiceItem;
import org.json.simple.JSONObject;

public class InvoiceItem extends Item implements IInvoiceItem {
    private int quantity;

    public InvoiceItem(JSONObject obj) {
        super(obj);
    }

    public double getSubtotal() {
        return this.price * this.quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        }
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("qr", this.qr);
        obj.put("name", this.name);
        obj.put("price", this.price);
        obj.put("quantity", this.quantity);
        return obj;
    }
}

package com.kashier.controllers;

import com.kashier.models.InventoryItem;
import com.kashier.models.InvoiceItem;
import com.kashier.models.Item;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class InvoiceController {
    private HashMap<String, InvoiceItem> items = new HashMap<String, InvoiceItem>();

    public Double getTotal() {
        double total = 0;
        for (InvoiceItem item : this.items.values()) {
            System.out.println(item.getPrice());
            total += item.getPrice() * item.getQuantity();
        }

        return total;
    }

    public void addItem(InvoiceItem item) {
        this.items.put(item.getQR(), item);
    }
}

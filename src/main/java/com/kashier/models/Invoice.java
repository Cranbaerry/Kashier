package com.kashier.models;

import java.util.ArrayList;

public class Invoice {
    private float total, discount, fee;
    private String id;
    private ArrayList<InvoiceItem> items;

    public float getTotal() {
        return this.total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getDiscount() {
        return this.discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getFee() {
        return this.fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<InvoiceItem> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<InvoiceItem> items) {
        this.items = items;
    }
}

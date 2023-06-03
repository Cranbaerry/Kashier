package com.kashier.models;

import com.kashier.interfaces.IItem;

public class Item implements IItem {
    String name;
    Double price;
    String qr;
    String deleted_at = null;

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public void setDeletedAt(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getQr() {
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
}

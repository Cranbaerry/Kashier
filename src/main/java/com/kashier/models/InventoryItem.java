package com.kashier.models;

import com.kashier.interfaces.IInventoryItem;

public class InventoryItem extends Item implements IInventoryItem {
    int stock;

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

}

package com.kashier.models;

import com.kashier.interfaces.IInventoryItem;

public class InvoiceItem extends Item {
    String invoice_id;
    int quantity;
    float subtotal;
}

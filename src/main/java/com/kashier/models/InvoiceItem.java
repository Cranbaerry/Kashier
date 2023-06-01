package com.kashier.models;

import com.kashier.interfaces.IInvoiceItem;

public class InvoiceItem extends Item implements IInvoiceItem {
    String invoice_id;
    int quantity;
    float subtotal;

    public String getInvoiceId() {
        return this.invoice_id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public float getSubtotal() {
        return this.subtotal;
    }

    public void setInvoiceId(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }
}

package com.kashier.models;

import com.kashier.interfaces.IInvoiceItem;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class InvoiceItem extends Item implements IInvoiceItem {
    private String invoice_id;
    private int quantity;
    private SimpleDoubleProperty subtotal = new SimpleDoubleProperty(0);
    public String getInvoiceId() {
        return this.invoice_id;
    }
    public int getQuantity() {
        return this.quantity;
    }
    public double getSubtotal() {
        return this.subtotal.get();
    }
    public DoubleProperty getSubtotalProperty() {
        return this.subtotal;
    }
    public void setInvoiceId(String invoice_id) {
        this.invoice_id = invoice_id;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setSubtotal(double subtotal) {
        this.subtotal.set(subtotal);
    }
}

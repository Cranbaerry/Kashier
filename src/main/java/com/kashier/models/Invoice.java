package com.kashier.models;

import com.kashier.interfaces.IInvoice;

import java.util.ArrayList;

public class Invoice implements IInvoice {
    private double total, discount, fee, subtotal;
    private String id;
    private ArrayList<InvoiceItem> items;

    private String issued_by;

    public double getTotal() {
        return this.total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscount() {
        return this.discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getFee() {
        return this.fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getSubtotal() { return this.subtotal; }

    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

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

    public String getIssuedBy() {
        return this.issued_by;
    }

    public void setIssuedBy(String issued_by) {
        this.issued_by = issued_by;
    }
}

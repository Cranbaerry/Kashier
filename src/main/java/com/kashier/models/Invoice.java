package com.kashier.models;

import com.kashier.interfaces.IInvoice;

import java.util.ArrayList;

public class Invoice implements IInvoice {
    private double total, discount, fee, subtotal;
    private String id, created_at;
    private ArrayList<InvoiceItem> items;

    private String issuer;

    private String issued_by;

    public double getTotal() {
        return this.total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getIssuer() {
        return this.issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
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

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String createdAt) {
        this.created_at = createdAt;
    }

    public void setIssued_by(String issuedBy) {
        this.issued_by = issuedBy;
    }

    public String getIssued_by() {
        return this.issued_by;
    }
}

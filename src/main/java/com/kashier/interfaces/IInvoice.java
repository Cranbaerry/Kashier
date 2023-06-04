package com.kashier.interfaces;

import com.kashier.models.InvoiceItem;

import java.util.ArrayList;

public interface IInvoice  {

    public double getTotal();

    public double getDiscount();

    public double getFee();

    public String getId();

    public ArrayList<InvoiceItem> getItems();

    public void setTotal(double total);

    public void setDiscount(double discount);

    public void setFee(double fee);

    public void setId(String id);

    public void setItems(ArrayList<InvoiceItem> items);
}

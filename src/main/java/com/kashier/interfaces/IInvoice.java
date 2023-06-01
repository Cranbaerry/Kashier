package com.kashier.interfaces;

import com.kashier.models.InvoiceItem;

import java.util.ArrayList;

public interface IInvoice  {

    public float getTotal();

    public float getDiscount();

    public float getFee();

    public String getId();

    public ArrayList<InvoiceItem> getItems();

    public void setTotal(float total);

    public void setDiscount(float discount);

    public void setFee(float fee);

    public void setId(String id);

    public void setItems(ArrayList<InvoiceItem> items);
}

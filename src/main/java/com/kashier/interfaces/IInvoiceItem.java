package com.kashier.interfaces;

public interface IInvoiceItem extends IItem  {

    public String getInvoiceId();

    public int getQuantity();

    public float getSubtotal();

    public void setInvoiceId(String invoice_id);

    public void setQuantity(int quantity);

    public void setSubtotal(float subtotal);

}

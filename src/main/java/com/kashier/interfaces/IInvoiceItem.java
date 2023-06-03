package com.kashier.interfaces;

public interface IInvoiceItem extends IItem  {

    String getInvoiceId();

    int getQuantity();

    float getSubtotal();

    void setInvoiceId(String invoice_id);

    void setQuantity(int quantity);

    void setSubtotal(float subtotal);

}

package com.kashier.interfaces;

public interface IInvoiceItem extends IItem  {

    String getInvoiceId();

    int getQuantity();

    double getSubtotal();

    void setInvoiceId(String invoice_id);

    void setQuantity(int quantity);

    void setSubtotal(double subtotal);

}

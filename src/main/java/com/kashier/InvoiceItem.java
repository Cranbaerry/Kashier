package com.kashier;

public class InvoiceItem extends Item{
	double subtotal;
	
	public InvoiceItem(Item item, int stock) {
		super(item.itemName, stock, item.itemPrice, item.itemQR);
		this.subtotal = item.itemPrice * super.itemQty;
	}
}
package com.kashier;
import java.util.ArrayList;

public class Invoice {
	double total;
	
	private ArrayList<InvoiceItem> items;
	
	public Invoice() {
		this.items = new ArrayList<InvoiceItem>();
		this.total = 0;
	}
	
	
	public void addItem(Item item, int stock) {
		this.items.add(new InvoiceItem(item, stock));
		System.out.println(item.itemName + item.itemQty+ item.itemPrice);
	}
	
	public void checkout(String paymentMethod) {
		total = 0;
		for (InvoiceItem item : this.items) {
			System.out.println(item.subtotal);
			total += item.subtotal;
		}
		System.out.println("Total: " + this.total);
		// save to database.
	}
	
}


package com.kashier;

public class Item {
	String itemName;
	int itemQty;
	double itemPrice;
	String itemQR;
	
	
	public Item(String itemName, int itemQty, double itemPrice, String itemQR) {
		this.itemName = itemName;
		this.itemQty = itemQty;
		this.itemPrice = itemPrice;
		this.itemQR = itemQR;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public int getItemQty() {
		return itemQty;
	}
	
	public void setItemQty(int itemQty) {
		this.itemQty = itemQty;
	}
	
	public double getItemPrice() {
		return itemPrice;
	}
	
	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}
	
	public String getItemQR() {
		return itemQR;
	}
	
	public void setItemQR(String itemQR) {
		this.itemQR = itemQR;
	}
	
}

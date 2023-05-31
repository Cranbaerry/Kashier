//package com.kashier.placeholders;
//
//import com.kashier.models.Item;
//
//import java.util.ArrayList;
//
//public class Inventory {
//	private ArrayList<Item> inventory = new ArrayList<Item>();
//
//	    public Item searchByName(String name){
//	        int l = 0, r = this.inventory.size() - 1;
//	        while (l <= r) {
//	            int m = l + (r - l) / 2;
//	            Item item = this.inventory.get(m);
//	            if (item.getItemName().toLowerCase() == name.toLowerCase())
//	                return item;
//	            if (item.getItemName().compareToIgnoreCase(item.getItemName()) < 0)
//	                l = m + 1;
//	            else
//	                r = m - 1;
//	        }
//	        System.out.println("Item Not Found!");
//	        return null;
//	    }
//
//	    public Item searchByQR(String qrcode){
//	        int l = 0, r = this.inventory.size() - 1;
//	        while (l <= r) {
//	            int m = l + (r - l) / 2;
//	            Item item = this.inventory.get(m);
//	            if (item.getItemQR().toLowerCase() == qrcode.toLowerCase())
//	                return item;
//	            if (item.getItemQR().compareToIgnoreCase(item.getItemQR()) < 0)
//	                l = m + 1;
//	            else
//	                r = m - 1;
//	        }
//	        System.out.println("Item Not Found!");
//	        return null;
//	    }
//
//	    public void addItem(String name, int qty, double price, String qr) {
//	    	Item item = searchByName(name);
//	    	if (item == null) {
//	    		item = searchByQR(qr);
//	    	}
//
//	    	if(item != null) {
//	    		searchByName(name).itemQty =+ qty;
//	    	}else {
//	    		item = new Item(name, qty, price, qr);
//	    		inventory.add(item);
//	    	}
//
//	    	System.out.println("Item added!");
//	    }
//
//	    public void updateItem(Item item, String name, int qty, double price, String qr) {
//	    	item.setItemName(name);
//	    	item.setItemQty(qty);
//	        item.setItemPrice(price);
//	        item.setItemQR(qr);
//	        System.out.println("Item updated!");
//	    }
//
//	    public void updateItemIndividual(Item item, int qty) {
//	    	item.itemQty -= qty;
//	    }
//
//	    public void removeItem(String name) {
//	    	if(searchByName(name) != null) {
//	    		inventory.remove(searchByName(name));
//	    		System.out.println("Item removed!");
//	    	}
//	    	else return;
//	    }
//}

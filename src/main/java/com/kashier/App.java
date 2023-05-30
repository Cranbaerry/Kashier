package com.kashier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    Inventory inventory = new Inventory();
    Scanner scan = new Scanner(System.in);
     

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    

    public static void main(String[] args) {
    	launch();
    	        
        Scanner sc  = new Scanner(System.in);
		String user;
		String pass;
		int flag=0;
		
		ArrayList<Account> accounts = new ArrayList<Account>();
		Account temp1 = new Account("admin1", "password1");
		accounts.add(temp1);
		Account temp2 = new Account("admin2", "password2");
		accounts.add(temp2);
		Account temp3 = new Account("admin3", "password3");
		accounts.add(temp3);
		
		do {
			System.out.println("Welcome to Kashier! Please input your credentials to proceed.");
			System.out.printf("Enter username: ");
			user = sc.nextLine();
			System.out.printf("Enter password: ");
			pass = sc.nextLine();
			
			for(int i = 0; i<accounts.size(); i++) {
				if(accounts.get(i).getUser().contains(user) && accounts.get(i).getPass().contains(pass)) {
					flag++;
				}
			}
			
			if(flag==0){ 
				System.out.println("Wrong username or password!");
			}
		}while(flag==0);
		System.out.println("Log In Sucessful!");
        
		int choice;
		int selection;
		do {
			System.out.println("Pick Your Role:");
			System.out.println("1. Cashier");
			System.out.println("2. Back Office");
			
			choice = sc.nextInt();
			if(choice == 1) {
				Cashier c = new Cashier(inventory);
				System.out.println("Pick action:");
				System.out.println("1. Create Order");
				System.out.println("2. Add Item");
				System.out.println("3. Check Out");
				selection = sc.nextInt();
				c.Initiate(selection, sc);
			} else if(choice == 2) {
				BackOffice b = new BackOffice(inventory);
				System.out.println("Pick action:");
				System.out.println("1. Search Item");
				System.out.println("2. Add Item");
				System.out.println("3. Update Item");
				System.out.println("4. Remove Item");
				selection = sc.nextInt();
				b.Initiate(selection, sc);
			}
		} while(choice != 1 || choice != 2);
		
		
    }

}

class BackOffice{
	
	Inventory inventory;
	public BackOffice(Inventory inven) {
		this.inventory = inven;
	}
	
	public void Initiate(int selection, Scanner sc) {
		String name;
    	int qty;
    	double price;
    	String qr;
    	Item item;
		switch (selection) {
			case 1:
				name = sc.nextLine();
				item = inventory.searchByName(name);
				if(item != null) {
					System.out.println("Product Name: " + item.itemName);
					System.out.println("Product Price: " + item.itemPrice);
					System.out.println("Product Qty: " + item.itemQty);
				}
				break;
			case 2:
		    	System.out.print("Input item name: ");
		    	name = sc.nextLine();
		    	
		    	System.out.print("Input item quantity: ");
		    	qty = sc.nextInt(); sc.nextLine();
		    	
		    	System.out.print("Input item price: ");
		    	price = sc.nextDouble(); sc.nextLine();
		    	
		    	System.out.print("Input item qr: ");
		    	qr = sc.nextLine();
		    	
				inventory.addItem(name, qty, price, qr);
				break;
			case 3:
				System.out.print("Input item name: ");
		    	name = sc.nextLine();
		    	
		    	item = inventory.searchByName(name);
				if(item != null) {
					System.out.print("Input new item name: ");
		    		name = null;
		        	name = sc.nextLine();
		        	
		        	System.out.print("Input new item quantity: ");
		        	qty = sc.nextInt(); sc.nextLine();
		        	
		        	System.out.print("Input new item price: ");
		        	price = sc.nextDouble(); sc.nextLine();
		        	
		        	System.out.print("Input new item qr: ");
		        	qr = sc.nextLine();
		        	
		        	inventory.updateItem(item, name, qty, price, qr);
				}
								
				break;
			case 4:
				System.out.print("Input item name: ");
		    	name = sc.nextLine();
		    	
				inventory.removeItem(name);
				break;
		}
	}
}

class Cashier{
	
	Inventory inventory;
	
	Invoice currentInvoice;
	
	private ArrayList<Invoice> invoices;
	
	public Cashier(Inventory inven) {
		this.inventory = inven;
	}
		
	public void createOrder() {
		this.currentInvoice = new Invoice();
	}
	
	public void addItem(Item item, int stock) {
		this.currentInvoice.addItem(item, stock);
		inventory.updateItemIndividual(item, stock);
	}
	
	public void checkout(String PaymentMethod) {
		
		this.currentInvoice.checkout(PaymentMethod);
		this.invoices.add(this.currentInvoice);
		this.currentInvoice = null;
	}
	
	public void Initiate(int selection, Scanner sc) {
		Item item = null;
		String name;
		String qr;
		int qty;
		int choice;
		switch (selection) {
			case 1:
				this.createOrder();
				break;
			case 2:
				do {
					System.out.println("Pick the method:");
					System.out.println("1. Add by Name");
					System.out.println("2. Add by QR");
					choice = sc.nextInt();
					if(choice == 1) {
						System.out.print("Input item name: ");
				    	name = sc.nextLine();
				    	item = inventory.searchByName(name);
					} else if(choice == 2) {
						System.out.print("Input item qr: ");
				    	qr = sc.nextLine();
				    	item = inventory.searchByQR(qr);
					}
					
					if (item != null) {
						System.out.print("Input item quantity: ");
				    	qty = sc.nextInt();
				    	this.addItem(item, qty);
					}
					
				} while(choice != 1 || choice != 2);
				break;
			case 3:
				System.out.println("Pick Your Payment Method: ");
				System.out.println("1. Cash");
				System.out.println("2. Debit");
				System.out.println("3. Digital Payment");
				System.out.println("4. Credit");
				String[] PaymentMethod = {"Cash", "Debit", "Digital Payment", "Credit"};
				choice = sc.nextInt();
				this.checkout(PaymentMethod[choice]);
				break;
				
		}
	}
}
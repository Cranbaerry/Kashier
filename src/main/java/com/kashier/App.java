package com.kashier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
    Scanner scan = new Scanner(System.in);
    public static ArrayList<Item> inventory = new ArrayList<Item>();
    
    int search(ArrayList<Item> arr, Item item){
        int l = 0, r = arr.size() - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (arr.get(m).getItemName().toLowerCase() == item.getItemName().toLowerCase() && arr.get(m).getItemQR().toLowerCase() == item.getItemQR().toLowerCase())
                return m; 
            if (arr.get(m).getItemName().compareToIgnoreCase(item.getItemName()) < 0)
                l = m + 1;
            else
                r = m - 1;
        }
 
        return -1;
    }
    
    public void addItem() {
    	String name;
    	int qty;
    	double price;
    	String qr;
    	System.out.print("Input item name: ");
    	name = scan.nextLine();
    	
    	System.out.print("Input item quantity: ");
    	qty = scan.nextInt(); scan.nextLine();
    	
    	System.out.print("Input item price: ");
    	price = scan.nextDouble(); scan.nextLine();
    	
    	System.out.print("Input item qr: ");
    	qr = scan.nextLine();
    	
    	Item item = new Item(name, qty, price, qr);
    	
    	if(search(inventory, item) > 0) {
    		inventory.get(search(inventory, item)).setItemQty(inventory.get(search(inventory, item)).getItemQty() + item.getItemQty());
    	}else {
    		inventory.add(item);
    	}
    	
    	System.out.println("Item added!");
    }
    
    public void updateItem() {
    	String name, qr;
    	int qty; double price;
    	System.out.print("Input item name: ");
    	name = scan.nextLine();
    	
    	System.out.print("Input item qr: ");
    	qr = scan.nextLine();
    	
    	Item item = new Item(name, 0, 0, qr);
    	
    	if(search(inventory, item) < 0) {
    		System.out.println("Item Not Found");
    		return;
    	}else {
    		System.out.print("Input new item name: ");
        	name = scan.nextLine();
        	
        	System.out.print("Input new item quantity: ");
        	qty = scan.nextInt(); scan.nextLine();
        	
        	System.out.print("Input new item price: ");
        	price = scan.nextDouble(); scan.nextLine();
        	
        	System.out.print("Input new item qr: ");
        	qr = scan.nextLine();
        	
        	int idx = search(inventory, item);
        	inventory.get(idx).setItemName(name);
        	inventory.get(idx).setItemQty(qty);
        	inventory.get(idx).setItemPrice(price);
        	inventory.get(idx).setItemQR(qr);
        	
        	System.out.println("Item updated!");
    	}
    		
    }
    
    public void removeItem() {
    	String name, qr;
    	System.out.print("Input item name: ");
    	name = scan.nextLine();
    	
    	System.out.print("Input item qr: ");
    	qr = scan.nextLine();
    	
    	Item item = new Item(name, 0, 0, qr);
    	
    	if(search(inventory, item) < 0) {
    		System.out.println("Item Not Found");
    		return;
    	}else {
    		inventory.remove(search(inventory, item));
    		System.out.println("Item removed!");
    	}
    	
    	
    }
  

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
    	
        Item item1 = new Item("Ramen Set", 10, 200000, "qr1");
        Item item2 = new Item("Mushroom Cream Soup", 10, 50000, "qr2");
        Item item3 = new Item("Chicken Katsu Set", 10, 100000, "qr3");
        Item item4 = new Item("Chicken Corden Blue", 10, 150000, "qr4");
        Item item5 = new Item("French Fries", 10, 20000, "qr5");
        Item item6 = new Item("Spaghetti Carbonara", 10, 50000, "qr6");
        Item item7 = new Item("Fish'n Chips", 10, 100000, "qr7");
        Item item8 = new Item("Chicken Teriyaki Set", 10, 100000, "qr8");
        Item item9 = new Item("Fettucine Mushroom & Cream", 10, 200000, "qr9");
        
        inventory.add(item1);
        inventory.add(item2);
        inventory.add(item3);
        inventory.add(item4);
        inventory.add(item5);
        inventory.add(item6);
        inventory.add(item7);
        inventory.add(item8);
        inventory.add(item9);
        
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
        
//        launch();
    }

}
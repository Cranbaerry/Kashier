package com.kashier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.kashier.controllers.AuthController;
import com.kashier.controllers.InventoryController;
import com.kashier.controllers.ItemController;
import com.kashier.models.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.harium.supabase.SupabaseClient;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    private static final String SUPABASE_URL = "https://xgswsvlpfuhdaqrywzhf.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inhnc3dzdmxwZnVoZGFxcnl3emhmIiwicm9sZSI6ImFub24iLCJpYXQiOjE2ODU1MTk1MzYsImV4cCI6MjAwMTA5NTUzNn0.64nZasGodkopk4TF0uSU95s4uwvAGavsYbjL9P2Bkh4";
    Scanner scan = new Scanner(System.in);
    public static JSONParser parser = new JSONParser();
    public static SupabaseClient supabase;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        //launch();
        try {
            supabase = new SupabaseClient(SUPABASE_URL, SUPABASE_KEY);

            // Auth controller test: Login
            AuthController account = new AuthController();
            System.out.println(account.login("uwu", "uwu"));

            // Item controller test: Insert
            ItemController item = new ItemController();
            JSONObject obj = new JSONObject();
            obj.put("name", "test2");
            obj.put("price", 1.0);
            obj.put("qr", "123");
            Item itemObj = new Item(obj);
            System.out.println(item.upsertItem(itemObj));

            // Item controller test: Delete
            System.out.println(item.removeItem(itemObj));

            // Item controller test: Get
            ArrayList<Item> items = item.getItems();
            for (Item i : items) {
                System.out.println(i.getDeletedAt());
            }

            // Inventory controller test: Get Items
            InventoryController inventory = new InventoryController();
            HashMap<String, InventoryItem> inv = inventory.getItems();
            for (String key : inv.keySet()) {
                System.out.println("Key: " + key + " Value: " + inv.get(key).getStock());
            }

            // Inventory controller test: Upsert Item
            // Dummy item, qr is important; rest is not
            obj = new JSONObject();
            obj.put("qr", "123");
            obj.put("stock", 10);
            obj.put("price", 69);
            obj.put("name", "WHAT");

            InventoryItem invItem = new InventoryItem(obj);
            invItem.setStock(10);
            inventory.addItem(invItem);

            // Inventory controller test: Delete Item (set stock to 1)
            invItem.setStock(1);
            inventory.removeItem(invItem);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
        //Scanner sc = new Scanner(System.in);
//        String user;
//        String pass;
//        int flag = 0;
//
//        ArrayList<Account> accounts = new ArrayList<Account>();
//        Account temp1 = new Account("admin1", "password1", 1);
//        accounts.add(temp1);
//        Account temp2 = new Account("admin2", "password2", 1);
//        accounts.add(temp2);
//        Account temp3 = new Account("admin3", "password3", 1);
//        accounts.add(temp3);
//
//        do {
//            System.out.println("Welcome to Kashier! Please input your credentials to proceed.");
//            System.out.printf("Enter username: ");
//            user = sc.nextLine();
//            System.out.printf("Enter password: ");
//            pass = sc.nextLine();
//
//            for (int i = 0; i < accounts.size(); i++) {
//                if (accounts.get(i).getUser().contains(user) && accounts.get(i).getPass().contains(pass)) {
//                    flag++;
//                }
//            }
//
//            if (flag == 0) {
//                System.out.println("Wrong username or password!");
//            }
//        } while (flag == 0);
//        System.out.println("Log In Sucessful!");

//		int choice;
//		int selection;
//		do {
//			System.out.println("Pick Your Role:");
//			System.out.println("1. Cashier");
//			System.out.println("2. Back Office");
//
//			choice = sc.nextInt();
//			if(choice == 1) {
//				Cashier c = new Cashier(inventory);
//				System.out.println("Pick action:");
//				System.out.println("1. Create Order");
//				System.out.println("2. Add Item");
//				System.out.println("3. Check Out");
//				selection = sc.nextInt();
//				c.Initiate(selection, sc);
//			} else if(choice == 2) {
//				BackOffice b = new BackOffice(inventory);
//				System.out.println("Pick action:");
//				System.out.printl`n("1. Search Item");
//				System.out.println("2. Add Item");
//				System.out.println("3. Update Item");
//				System.out.println("4. Remove Item");
//				selection = sc.nextInt();
//				b.Initiate(selection, sc);
//			}
//		} while(choice != 1 || choice != 2);
//
//
    }
}


package com.kashier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.kashier.controllers.AuthController;
import com.kashier.controllers.InventoryController;
import com.kashier.controllers.InvoiceController;
import com.kashier.controllers.ItemController;
import com.kashier.models.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.harium.supabase.SupabaseClient;
import com.dynamsoft.dbr.*;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    private static final String SUPABASE_URL = "https://xgswsvlpfuhdaqrywzhf.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inhnc3dzdmxwZnVoZGFxcnl3emhmIiwicm9sZSI6ImFub24iLCJpYXQiOjE2ODU1MTk1MzYsImV4cCI6MjAwMTA5NTUzNn0.64nZasGodkopk4TF0uSU95s4uwvAGavsYbjL9P2Bkh4";

    public static final String DYNAMSOFT_LICENSE = "t0073oQAAADu5LbghXCWDSmoiWEKJB/NXefhMH+urpIkKJl/9LHGsOHyqDrGZ0TIs1EjH78dz1PYE2K+3nWoWCWtaT0ibDc8FBWwjCQ==";
    public static SupabaseClient supabase;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("secondary"), 640, 480);
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
        launch();
        try {
//            supabase = new SupabaseClient(SUPABASE_URL, SUPABASE_KEY);
//
//            // Auth controller test: Login
//            AuthController auth = new AuthController();
//            boolean login = auth.login("uwu", "uwu");
//            Account account = auth.getAccount();
//            System.out.println(account.getId());
//
//            // Item controller test: Insert
//            ItemController item = new ItemController();
//
//            Item itemObj = new Item();
//            itemObj.setName("Test UwU5");
//            itemObj.setPrice(69);
//            itemObj.setQR("Ahh2");
//            System.out.println("Item inserted: " + item.upsertItem(itemObj).getName());
//
//            // Item controller test: Delete
//            if (item.removeItem("Ahh2")) {
//                System.out.println("Item deleted");
//            } else {
//                System.out.println("Item not deleted");
//            }
//
//            // Item controller test: Get
//            System.out.println("Items: ");
//            ArrayList<Item> items = item.getItems();
//            for (Item i : items) {
//                System.out.println(i.getName());
//            }
//
//            // Inventory controller test: Get Items
//            System.out.println("Inventory items: ");
//            InventoryController inventory = new InventoryController();
//            HashMap<String, InventoryItem> invItems = inventory.getInventory();
//            for (String key : invItems.keySet()) {
//                System.out.println(invItems.get(key).getName());
//            }
//
//            BarcodeReader.initLicense("DLS2eyJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSJ9");
//
//            // 2.Create an instance of Barcode Reader.
//            BarcodeReader dbr = new BarcodeReader();
//            TextResult[] results = dbr.decodeFileInMemory();
//            dbr.de
//
//            // 4.Output the barcode text.
//            if (results != null && results.length > 0) {
//                for (int i = 0; i < results.length; i++) {
//                    TextResult result = results[i];
//                    System.out.println("Barcode " + i + ":" + result.barcodeText);
//                }
//            } else {
//                System.out.println("No data detected.");
//            }

            // Inventory controller test: Upsert Item
//            InventoryItem invItem = inventory.addItem("123", 1);
//            System.out.println("QR code: " + invItem.getQR() + " updated stock to " + invItem.getStock());

            // Inventory controller test: Remove Item
//            invItem = inventory.removeItem("123", 5);
//            System.out.println("QR code: " + invItem.getQR() + " updated stock to " + invItem.getStock());

            // Get all invoices
//            InvoiceController invoice = new InvoiceController();
//            ArrayList<Invoice> invoices = invoice.getInvoices();
//            for (Invoice i : invoices) {
//                System.out.println(i.getId());
//            }

//            // Get invoice by id
//            InvoiceController invoice = new InvoiceController();
//            Invoice invoiceObj = invoice.getInvoice("81bf85a6-54d6-4914-b443-57e7fad4cc29");
//            System.out.println("Invoice id: " + invoiceObj.getId());
//
//            // Get invoice items
//            ArrayList<InvoiceItem> invoiceItems = invoiceObj.getItems();
//            for (InvoiceItem i : invoiceItems) {
//                System.out.println(i.getName());
//            }
//
//            // Read barcode
//            BarcodeReader dbr = new BarcodeReader();


        } catch (Exception e) {
            e.printStackTrace();
        }


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


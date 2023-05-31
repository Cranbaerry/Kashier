//package com.kashier.placeholders;
//
//import com.kashier.models.Item;
//
//import java.util.ArrayList;
//import java.util.Scanner;
//
//class Cashier {
//
//    Inventory inventory;
//
//    Invoice currentInvoice;
//
//    private ArrayList<Invoice> invoices;
//
//    public Cashier(Inventory inven) {
//        this.inventory = inven;
//    }
//
//    public void createOrder() {
//        this.currentInvoice = new Invoice();
//    }
//
//    public void addItem(Item item, int stock) {
//        this.currentInvoice.addItem(item, stock);
//        inventory.updateItemIndividual(item, stock);
//    }
//
//    public void checkout(String PaymentMethod) {
//
//        this.currentInvoice.checkout(PaymentMethod);
//        this.invoices.add(this.currentInvoice);
//        this.currentInvoice = null;
//    }
//
//    public void Initiate(int selection, Scanner sc) {
//        Item item = null;
//        String name;
//        String qr;
//        int qty;
//        int choice;
//        switch (selection) {
//            case 1:
//                this.createOrder();
//                break;
//            case 2:
//                do {
//                    System.out.println("Pick the method:");
//                    System.out.println("1. Add by Name");
//                    System.out.println("2. Add by QR");
//                    choice = sc.nextInt();
//                    if (choice == 1) {
//                        System.out.print("Input item name: ");
//                        name = sc.nextLine();
//                        item = inventory.searchByName(name);
//                    } else if (choice == 2) {
//                        System.out.print("Input item qr: ");
//                        qr = sc.nextLine();
//                        item = inventory.searchByQR(qr);
//                    }
//
//                    if (item != null) {
//                        System.out.print("Input item quantity: ");
//                        qty = sc.nextInt();
//                        this.addItem(item, qty);
//                    }
//
//                } while (choice != 1 || choice != 2);
//                break;
//            case 3:
//                System.out.println("Pick Your Payment Method: ");
//                System.out.println("1. Cash");
//                System.out.println("2. Debit");
//                System.out.println("3. Digital Payment");
//                System.out.println("4. Credit");
//                String[] PaymentMethod = {"Cash", "Debit", "Digital Payment", "Credit"};
//                choice = sc.nextInt();
//                this.checkout(PaymentMethod[choice]);
//                break;
//
//        }
//    }
//}

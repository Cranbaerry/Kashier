//package com.kashier.placeholders;
//
//import com.kashier.models.Item;
//
//import java.util.Scanner;
//
//class BackOffice {
//
//    Inventory inventory;
//
//    public BackOffice(Inventory inven) {
//        this.inventory = inven;
//    }
//
//    public void Initiate(int selection, Scanner sc) {
//        String name;
//        int qty;
//        double price;
//        String qr;
//        Item item;
//        switch (selection) {
//            case 1:
//                name = sc.nextLine();
//                item = inventory.searchByName(name);
//                if (item != null) {
//                    System.out.println("Product Name: " + item.itemName);
//                    System.out.println("Product Price: " + item.itemPrice);
//                    System.out.println("Product Qty: " + item.itemQty);
//                }
//                break;
//            case 2:
//                System.out.print("Input item name: ");
//                name = sc.nextLine();
//
//                System.out.print("Input item quantity: ");
//                qty = sc.nextInt();
//                sc.nextLine();
//
//                System.out.print("Input item price: ");
//                price = sc.nextDouble();
//                sc.nextLine();
//
//                System.out.print("Input item qr: ");
//                qr = sc.nextLine();
//
//                inventory.addItem(name, qty, price, qr);
//                break;
//            case 3:
//                System.out.print("Input item name: ");
//                name = sc.nextLine();
//
//                item = inventory.searchByName(name);
//                if (item != null) {
//                    System.out.print("Input new item name: ");
//                    name = null;
//                    name = sc.nextLine();
//
//                    System.out.print("Input new item quantity: ");
//                    qty = sc.nextInt();
//                    sc.nextLine();
//
//                    System.out.print("Input new item price: ");
//                    price = sc.nextDouble();
//                    sc.nextLine();
//
//                    System.out.print("Input new item qr: ");
//                    qr = sc.nextLine();
//
//                    inventory.updateItem(item, name, qty, price, qr);
//                }
//
//                break;
//            case 4:
//                System.out.print("Input item name: ");
//                name = sc.nextLine();
//
//                inventory.removeItem(name);
//                break;
//        }
//    }
//}

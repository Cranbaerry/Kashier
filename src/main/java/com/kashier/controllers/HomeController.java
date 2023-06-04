package com.kashier.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kashier.App;
import com.kashier.models.InventoryItem;
import com.kashier.models.Item;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.kashier.App.supabase;

public class HomeController extends PageController  {
    @FXML private Text earningText, salesText, productsText;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        pageTitle.setText("Home");
        System.out.println("HelloWorld");

        fetchDashboard();
    }

    private void fetchDashboard() {
        new Thread(){
            @Override
            public void run(){
                try {
                    ItemController itemController = new ItemController();
                    String result = supabase.database().findAll("inventory");
                    JsonArray data = (JsonArray) JsonParser.parseString(result);
//                    for (Object o : data) {
//                        JsonObject itemObj = (JsonObject) o;
//                        InventoryItem item = new Gson().fromJson(itemObj, InventoryItem.class);
//                        Item details = itemController.getItemByQR(item.getQr());
//                        if (details == null) throw new IOException("Item not found: " + item.getQr());
//                        item.setQr(details.getQr());
//                        item.setName(details.getName());
//                        item.setPrice(details.getPrice());
//                        inventory.add(item);
//                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Platform.runLater(() -> {
                });
            }
        }.start();
    }
}


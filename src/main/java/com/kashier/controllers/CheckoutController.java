package com.kashier.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kashier.App;
import com.kashier.models.Item;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.kashier.App.supabase;

public class CheckoutController extends PageController  {
    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane grid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        pageTitle.setText("Checkout");
        System.out.println("HelloWorld");

        try {
            String result = supabase.database().findAll("items");
            JsonArray itemsData = (JsonArray) JsonParser.parseString(result);
            ArrayList<Item> items = new ArrayList<>();
            int row = 0, column = 0;
            for (Object o : itemsData) {
                JsonObject itemObj = (JsonObject) o;
                Item item = new Gson().fromJson(itemObj, Item.class);
                // Skip soft deleted items
                if (item.getDeletedAt() != null) continue;
                items.add(item);

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("item.fxml"));
                Pane anchorPane = fxmlLoader.load();
                ItemController itemController = fxmlLoader.getController();
                System.out.println("Item: " + item.getName());

                itemController.setData(item);


                if (column == 2) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row); //(child,column,row)

                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(5));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


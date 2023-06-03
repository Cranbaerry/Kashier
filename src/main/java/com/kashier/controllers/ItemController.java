package com.kashier.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.harium.postgrest.Condition;
import com.harium.postgrest.Insert;
import com.kashier.models.Item;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

import static com.kashier.App.supabase;


public class ItemController extends PageController {
    @FXML
    private Text itemName;
    @FXML
    private Text price;
    @FXML
    private ImageView img;
    private Item item;
    @FXML
    private void onItemClick() {
        System.out.println("Item clicked!");
    }

    public void setData(Item item) {
        this.item = item;
        this.itemName.setText(item.getName());
        this.price.setText("$" + item.getPrice());
        //Image image = new Image(this.getClass().getResourceAsStream(fruit.getImgSrc()));
        //this.img.setImage(image);
    }
    public Item upsertItem(Item itemObj) {
        try {
            Insert.Row data = Insert.row()
                .column("qr", itemObj.getQr())
                .column("name", itemObj.getName())
                .column("price", itemObj.getPrice());
            supabase.database().save("items", data);
            return itemObj;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean removeItem(String qr) {
        try {
            // Soft deletion is done by setting the deleted_at column to the current timestamp
            Instant instant = Instant.now();
            Insert.Row data = Insert.row()
                .column("qr", qr)
                .column("deleted_at", instant.toString());

            supabase.database().save("items", data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Item> getItems() {
        try {
            String result = supabase.database().findAll("items");
            JsonArray itemsData = (JsonArray) JsonParser.parseString(result);
            ArrayList<Item> items = new ArrayList<>();
            for (Object o : itemsData) {
                JsonObject itemObj = (JsonObject) o;
                Item item = new Gson().fromJson(itemObj, Item.class);
                // Skip soft deleted items
                if (item.getDeletedAt() != null) continue;
                items.add(item);
            }

            return items;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Item getItemByQR(String qr) {
        try {
            // Bug in postgrest-java library, cannot use Condition.eq("qr", qr) directly (must use Condition.and)
            Condition conditions = Condition.and(
                Condition.eq("qr", qr)
            );
            String result = supabase.database().find("items", conditions);
            JsonArray data = (JsonArray) JsonParser.parseString(result);

            if (data.size() == 0) return null;
            JsonObject itemObj = (JsonObject) data.get(0);
            return new Gson().fromJson(itemObj, Item.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

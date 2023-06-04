package com.kashier.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.harium.postgrest.Condition;
import com.harium.postgrest.Insert;
import com.kashier.interfaces.IItemCard;
import com.kashier.models.InventoryItem;
import com.kashier.models.InvoiceItem;
import com.kashier.models.Item;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Locale;

import static com.kashier.App.supabase;


public class ItemController extends PageController {
    @FXML
    private Text itemName, price, outOfStockText;

    @FXML
    private ImageView img;

    @FXML
    private Pane itemPane;

    private InventoryItem item;

    private IItemCard cardEvent;

    // https://stackoverflow.com/questions/4685563/how-to-pass-a-function-as-a-parameter-in-java
    @FXML
    private void onItemClick() {
        cardEvent.onClickEvent(item);
    }

    public void setData(InventoryItem item, IItemCard cardEvent) {
        Locale myIndonesianLocale = new Locale("in", "ID");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(myIndonesianLocale);

        this.item = (InventoryItem) item;
        this.itemName.setText(item.getName());
        this.price.setText(currencyFormat.format(item.getPrice()));
        this.cardEvent = cardEvent;

        if (item.getStock() == 0) {
            // this.itemPane.setDisable(true);
            this.itemPane.setOpacity(0.5);
            this.outOfStockText.setVisible(true);
        }

        //Image image = new Image(this.getClass().getResourceAsStream(fruit.getImgSrc()));
        //this.img.setImage(image);
    }
    public InventoryItem getItem() {
        return this.item;
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

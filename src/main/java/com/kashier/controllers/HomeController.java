package com.kashier.controllers;

import com.google.gson.*;
import com.harium.postgrest.Condition;
import com.kashier.models.InventoryItem;
import com.kashier.models.Invoice;
import com.kashier.models.InvoiceItem;
import com.kashier.models.Item;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.kashier.App.supabase;

public class HomeController extends PageController  {
    @FXML private Text earningText, salesText, productsText;
    @FXML private TableView invoiceTable;
    @FXML private TableColumn<Invoice, String> invoiceColumn, issuerColumn;
    @FXML private TableColumn<Invoice, String> dateColumn;
    @FXML private TableColumn<Invoice, Double> totalColumn;

    private ObservableList<Invoice> invoices;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        invoices = FXCollections.observableArrayList();
        invoiceColumn.setCellValueFactory(new PropertyValueFactory<Invoice, String>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Invoice, String>("created_at"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<Invoice, Double>("total"));
        issuerColumn.setCellValueFactory(new PropertyValueFactory<Invoice, String>("issuer"));

        Locale myIndonesianLocale = new Locale("in", "ID");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(myIndonesianLocale);
        totalColumn.setCellFactory(tc -> new TableCell<Invoice, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });

        invoiceColumn.setCellFactory(tc -> new TableCell<Invoice, String>() {
            @Override
            protected void updateItem(String id, boolean empty) {
                super.updateItem(id, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(id.toUpperCase());
                }
            }
        });
        fetchDashboard();
    }

    public static String getRoughNumber(Double value) {
        if (value <= 999) {
            return String.valueOf(value);
        }

        final String[] units = new String[]{"", "K", "M", "B", "P"};
        int digitGroups = (int) (Math.log10(value) / Math.log10(1000));
        return new DecimalFormat("#,##0.#").format(value / Math.pow(1000, digitGroups)) + "" + units[digitGroups];

    }

    private void fetchDashboard() {
        new Thread(){
            @Override
            public void run() {
                try {
                    String result = supabase.database().find("invoices", Condition.select("id", "created_at", "total", "users(username)"));
                    JsonArray salesData = JsonParser.parseString(result).getAsJsonArray();
                    double total = 0;
                    for (Object o : salesData) {
                        JsonObject itemObj = (JsonObject) o;
                        JsonElement user = itemObj.get("users");

                        // Invoice item = new Gson().fromJson(itemObj, Invoice.class);
                        // Cannot use GSON to parse the array of items property, so we do it manually

                        Invoice invoice = new Invoice();
                        invoice.setId(itemObj.get("id").getAsString());
                        invoice.setCreated_at(itemObj.get("created_at").getAsString());
                        invoice.setTotal(itemObj.get("total").getAsDouble());
                        invoice.setIssuer(user.getAsJsonObject().get("username").getAsString());
                        invoices.add(invoice);
                        total += invoice.getTotal();
                    }

                    result = supabase.database().find("items", Condition.select("qr"));
                    JsonArray productsData = JsonParser.parseString(result).getAsJsonArray();

                    salesText.setText(String.valueOf(salesData.size()));
                    productsText.setText(String.valueOf(productsData.size()));
                    earningText.setText(getRoughNumber(total));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Platform.runLater(() -> {
                    invoiceTable.setItems(invoices);
                });
            }
        }.start();
    }
}


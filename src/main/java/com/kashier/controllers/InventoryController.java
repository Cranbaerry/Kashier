package com.kashier.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.harium.postgrest.Condition;
import com.harium.postgrest.Insert;
import com.kashier.models.InventoryItem;
import com.kashier.models.Item;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import static com.kashier.App.*;

public class InventoryController extends PageController {
    @FXML private TableView inventoryTable;
    @FXML private TableColumn<InventoryItem, String> qrColumn;
    @FXML private TableColumn<InventoryItem, String> nameColumn;
    @FXML private TableColumn<InventoryItem, Double> priceColumn;
    @FXML private TableColumn<InventoryItem, Integer> stockColumn;
    @FXML private TextField qrInput, nameInput, priceInput, stockInput, searchInput;
    @FXML private Button submitButton, deleteButton, clearButton, scanButton;
    @FXML private Text errorText;
    private ObservableList<InventoryItem> inventory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        inventory = FXCollections.observableArrayList();
        qrColumn.setCellValueFactory(new PropertyValueFactory<InventoryItem, String>("qr"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<InventoryItem, String>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<InventoryItem, Double>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<InventoryItem, Integer>("stock"));
        searchInput.textProperty().addListener((obs, oldText, newText) -> {
            applyFilter();
        });
        Locale myIndonesianLocale = new Locale("in", "ID");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(myIndonesianLocale);
        priceColumn.setCellFactory(tc -> new TableCell<InventoryItem, Double>() {
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
        fetchInventory();
    }

    private void fetchInventory() {
        InventoryItem selectedItem = (InventoryItem) inventoryTable.getSelectionModel().getSelectedItem();
        inventory.clear();
        new Thread(){
            @Override
            public void run(){
                try {
                    ItemController itemController = new ItemController();
                    String result = supabase.database().findAll("inventory");
                    JsonArray data = (JsonArray) JsonParser.parseString(result);
                    for (Object o : data) {
                        JsonObject itemObj = (JsonObject) o;
                        InventoryItem item = new Gson().fromJson(itemObj, InventoryItem.class);
                        Item details = itemController.getItemByQR(item.getQr());
                        if (details == null) throw new IOException("Item not found: " + item.getQr());
                        item.setQr(details.getQr());
                        item.setName(details.getName());
                        item.setPrice(details.getPrice());
                        inventory.add(item);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> {
                    inventoryTable.setItems(inventory);
                    inventoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            InventoryItem item = (InventoryItem) newSelection;
                            fillItemEditor(item);
                            System.out.println("Selected: " + item.getName());
                        }
                    });

                    // Select previously selected item
                    if (selectedItem != null) {
                        for (Object item : inventoryTable.getItems()) {
                            if (((InventoryItem) item).getQr().equals(selectedItem.getQr())) {
                                inventoryTable.getSelectionModel().select(item);
                                System.out.println("Selecting: " + ((InventoryItem) item).getName());
                                break;
                            }
                        }
                    }

                    // Apply filter
                    applyFilter();
                });
            }
        }.start();
    }

    private void applyFilter() {
        String keyword = searchInput.getText();
        if (keyword.equals("")) {
            inventoryTable.setItems(inventory);
        } else {
            ObservableList<InventoryItem> filteredData = FXCollections.observableArrayList();
            for (InventoryItem item : inventory) {
                if (item.getName().contains(keyword) || item.getQr().contains(keyword))
                    filteredData.add(item);
            }

            inventoryTable.setItems(filteredData);
        }
    }
    private void fillItemEditor(InventoryItem item) {
        qrInput.setText(item.getQr());
        nameInput.setText(item.getName());
        priceInput.setText(String.valueOf(item.getPrice()));
        stockInput.setText(String.valueOf(item.getStock()));
        errorText.setText("");
        qrInput.setDisable(true);
        scanButton.setDisable(true);
        deleteButton.setDisable(false);
        submitButton.setText("Update Item");
    }

    @FXML
    private void onClearClick() {
        qrInput.setText("");
        nameInput.setText("");
        priceInput.setText("");
        stockInput.setText("");
        errorText.setText("");
        qrInput.setDisable(false);
        scanButton.setDisable(false);
        deleteButton.setDisable(true);
        submitButton.setText("Add Item");
    }

    @FXML
    private void onSubmitClick() {
        String qr, name;
        double price;
        int stock;

        try {
            qr = qrInput.getText();
            name = nameInput.getText();
            price = Double.parseDouble(priceInput.getText());
            stock = Integer.parseInt(stockInput.getText());
            submitButton.setDisable(true);
        } catch (NumberFormatException e) {
            errorText.setText("Invalid input");
            return;
        }

        InventoryItem item = new InventoryItem();
        item.setQr(qr);
        item.setName(name);
        item.setPrice(price);
        item.setStock(stock);

        new Thread(){
            @Override
            public void run(){
            try {
                // Upsert Item
                Insert.Row data = Insert.row()
                        .column("qr", item.getQr())
                        .column("name", item.getName())
                        .column("price", item.getPrice());
                String res = supabase.database().save("items", data);
                System.out.println("Upsert Item: " + res);

                // Update Stock
                data = Insert.row()
                        .column("qr", item.getQr())
                        .column("stock", item.getStock());
                supabase.database().save("inventory", data);

                // Refresh inventory
                fetchInventory();

                System.out.print("Item added: " + item.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                submitButton.setDisable(false);
            });
            }
        }.start();
    }

    @FXML private void onDeleteClick() {
        System.out.println("Delete Clicked");
        InventoryItem item = (InventoryItem) inventoryTable.getSelectionModel().getSelectedItem();
        if (item == null) {
            errorText.setText("Please select an item");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Delete Item");
        alert.setHeaderText("Are you sure you want to delete this item?");
        alert.setContentText("This will delete the following item from the inventory and the database." +
                "\n\nItem: " + item.getName() +
                "\nQR: " + item.getQr());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            new Thread(){
                @Override
                public void run(){
                    try {
                        supabase.database().delete("items", Condition.and(
                                Condition.eq("qr", item.getQr())
                        ));

                        fetchInventory();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.start();
        }
    }

    @FXML
    private void onScanClick() {
        new Thread() {
            @Override
            public void run() {
                try {
                    final CountDownLatch latch = new CountDownLatch(1);
                    System.out.println("Executing");
                    scanBarcode(latch);
                    latch.await();
                    System.out.println("Released");

                    if (found) {
                        System.out.println("Barcode found: " + barcodeResult);
                        qrInput.setText(barcodeResult);
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }
}

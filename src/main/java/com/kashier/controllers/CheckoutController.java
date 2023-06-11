package com.kashier.controllers;

import com.google.gson.*;
import com.harium.postgrest.Condition;
import com.harium.postgrest.Insert;
import com.kashier.App;
import com.kashier.interfaces.IItemCard;
import com.kashier.models.InventoryItem;
import com.kashier.models.Invoice;
import com.kashier.models.InvoiceItem;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.kashier.App.*;

public class CheckoutController extends PageController  {
    @FXML
    private ScrollPane scroll;

    @FXML
    private Text detailName, detailPrice, detailStock;

    @FXML
    private TextField detailQuantity, searchInput;

    @FXML
    private GridPane grid;

    @FXML
    private Button addButton, removeButton, chargeButton, scanButton, resetButton;

    @FXML private TableColumn<InvoiceItem, String> qrColumn;
    @FXML private TableColumn<InvoiceItem, String> nameColumn;
    @FXML private TableColumn<InvoiceItem, Double> priceColumn;
    @FXML private TableColumn<InvoiceItem, Double> subTotalColumn;
    @FXML private TableColumn<InvoiceItem, Integer> quantityColumn;

    private ObservableList<InvoiceItem> invoiceItems;
    private InventoryItem selectedItem;
    @FXML
    private TableView invoiceTable;
    private Locale myIndonesianLocale = new Locale("in", "ID");
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(myIndonesianLocale);
    @FXML private Text errorText, subtotalText, taxText, totalText;
    private ArrayList<InventoryItem> items;
    private double total, subtotal, tax;
    private int row = 0, column = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        pageTitle.setText("Checkout");
        invoiceItems = FXCollections.observableArrayList(item ->
            new Observable[] {
                item.getSubtotalProperty(),
            }
        );

        searchInput.textProperty().addListener((obs, oldText, newText) -> {
            applyFilter();
        });
        qrColumn.setCellValueFactory(new PropertyValueFactory<InvoiceItem, String>("qr"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<InvoiceItem, String>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<InvoiceItem, Double>("price"));
        subTotalColumn.setCellValueFactory(new PropertyValueFactory<InvoiceItem, Double>("subtotal"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<InvoiceItem, Integer>("quantity"));
        priceColumn.setCellFactory(tc -> new TableCell<InvoiceItem, Double>() {
            @Override
            protected void updateItem(Double subtotal, boolean empty) {
                super.updateItem(subtotal, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(subtotal));
                }
            }
        });
        subTotalColumn.setCellFactory(tc -> new TableCell<InvoiceItem, Double>() {
            @Override
            protected void updateItem(Double subtotal, boolean empty) {
                super.updateItem(subtotal, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(subtotal));
                }
            }
        });

        // Add listener on items change
        invoiceItems.addListener((ListChangeListener.Change<? extends InvoiceItem> c) -> {
            while (c.next()) {
                // sum subtotal from all items
                double subtotal = invoiceItems.stream().mapToDouble(InvoiceItem::getSubtotal).sum();
                subtotalText.setText(currencyFormat.format(subtotal));
                this.subtotal = subtotal;

                // calculate tax
                double tax = subtotal * 0.1;
                taxText.setText(currencyFormat.format(tax));
                this.tax = tax;

                // calculate total
                double total = subtotal + tax;
                totalText.setText(currencyFormat.format(total));
                this.total = total;
            }
        });

        fetchItems();
    }

    private void fetchItems()  {
        new Thread() {
            @Override
            public void run() {
                try {
                    String result = supabase.database().find("items", Condition.select("qr", "name", "price", "inventory(stock)"));
                    System.out.println(result);
                    JsonArray data = (JsonArray) JsonParser.parseString(result);

                    items = new ArrayList<>();
                    row = column = 0;

                    for (Object o : data) {
                        JsonObject itemObj = (JsonObject) o;
                        JsonElement nullabeInventory = itemObj.get("inventory");
                        Integer stock = (nullabeInventory instanceof JsonNull) ? 0 : nullabeInventory.getAsJsonObject().get("stock").getAsInt();
                        InventoryItem item = new Gson().fromJson(itemObj, InventoryItem.class);
                        item.setStock(stock);

                        if (item.getStock() == 0) {
                            System.out.println("Stock not found: " + item.getQr());
                        }

                        items.add(item);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Platform.runLater(() -> {
                        refreshGrid(items);
                    });
                }
            }
        }.start();
    }

    private void refreshGrid(List<InventoryItem> itemsList) {
        grid.getChildren().clear();
        row = column = 0;
        for (InventoryItem item : itemsList) {
            try {
                addItemToGrid(item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void addItemToGrid(InventoryItem item) throws IOException {
        IItemCard cardEvent = new IItemCard() {
            @Override
            public void onClickEvent(InventoryItem item) {
                selectedItem = item;
                detailName.setText(selectedItem.getName());
                detailPrice.setText(currencyFormat.format(selectedItem.getPrice()));
                detailStock.setText(Integer.toString(selectedItem.getStock()));
                detailQuantity.setText("1");

                if (selectedItem.getStock() == 0) {
                    addButton.setDisable(true);
                    removeButton.setDisable(true);
                } else {
                    addButton.setDisable(false);
                    removeButton.setDisable(false);
                }
            }
        };

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(App.class.getResource("item.fxml"));
        Pane anchorPane = fxmlLoader.load();
        ItemController itemController = fxmlLoader.getController();
        itemController.setData(item, cardEvent);

        if (column == 3) {
            column = 0;
            row++;
        }

        grid.add(anchorPane, column++, row);
        grid.setMinWidth(Region.USE_COMPUTED_SIZE);
        grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
        grid.setMaxWidth(Region.USE_PREF_SIZE);
        grid.setMinHeight(Region.USE_COMPUTED_SIZE);
        grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
        grid.setMaxHeight(Region.USE_PREF_SIZE);

        GridPane.setMargin(anchorPane, new Insets(5));
    }

    @FXML
    private void onAddButtonClick() {
        try {
            errorText.setText("");
            AtomicBoolean exceedStock = new AtomicBoolean(false);
            InvoiceItem item = new InvoiceItem();
            item.setQr(selectedItem.getQr());
            item.setName(selectedItem.getName());
            item.setPrice(selectedItem.getPrice());
            item.setQuantity(Integer.parseInt(detailQuantity.getText()));
            item.setSubtotal((float) (selectedItem.getPrice() * item.getQuantity()));

            if (item.getQuantity() > selectedItem.getStock()) {
                exceedStock.set(true);
            }

            // find existing item
            AtomicBoolean isExist = new AtomicBoolean(false);
            invoiceItems.forEach(existing -> {
                if (existing.getQr().equals(selectedItem.getQr())) {
                    if (existing.getQuantity() + item.getQuantity() > selectedItem.getStock()) {
                        exceedStock.set(true);
                        return;
                    }

                    existing.setQuantity(existing.getQuantity() + item.getQuantity());
                    existing.setSubtotal((float) (selectedItem.getPrice() * existing.getQuantity()));
                    invoiceTable.refresh();
                    isExist.set(true);
                }
            });

            if (exceedStock.get()) {
                errorText.setText("Quantity exceed stock");
                return;
            }

            if (!isExist.get()) {
                invoiceItems.add(item);
                invoiceTable.setItems(invoiceItems);
            }
        } catch (NumberFormatException ex) {
            errorText.setText("Invalid quantity");
        }
    }

    @FXML
    private void onRemoveButtonClick() {
        errorText.setText("");
        invoiceItems.removeIf(item -> item.getQr().equals(selectedItem.getQr()));
        invoiceTable.setItems(invoiceItems);
    }

    @FXML
    private void onResetButtonClick() {
        errorText.setText("");
        invoiceItems.clear();
        invoiceTable.setItems(invoiceItems);
    }

    private void applyFilter() {
        String filter = searchInput.getText().toLowerCase();
        List<InventoryItem> filteredItems = items.stream().filter(item -> item.getName().toLowerCase().contains(filter)).collect(Collectors.toList());
        refreshGrid(filteredItems);
    }

    @FXML
    private void onGenerateInvoiceClick() {
        new Thread(){
            @Override
            public void run(){
            try {
                if (invoiceItems.size() == 0) {
                    errorText.setText("Cart is empty");
                    return;
                }

                chargeButton.setDisable(true);
                errorText.setText("");
                String invoiceId = UUID.randomUUID().toString();
                Invoice invoice = new Invoice();
                invoice.setId(invoiceId);
                invoice.setDiscount(0);
                invoice.setFee(tax);
                invoice.setSubtotal(subtotal);
                invoice.setTotal(total);
                invoice.setIssued_by(App.account.getId());

                Insert.Row data = Insert.row()
                    .column("id", invoice.getId())
                    .column("discount", invoice.getDiscount())
                    .column("fee", invoice.getFee())
                    .column("subtotal", invoice.getSubtotal())
                    .column("total", invoice.getTotal())
                    .column("issued_by", invoice.getIssued_by());

                supabase.database().save("invoices", data);

                for (InvoiceItem item : invoiceItems) {
                    // save invoice item
                    Insert.Row invoiceItemData = Insert.row()
                        .column("invoice_id", invoice.getId())
                        .column("qr", item.getQr())
                        .column("price", item.getPrice())
                        .column("quantity", item.getQuantity())
                        .column("subtotal", item.getSubtotal());

                    String result = supabase.database().save("invoice_items", invoiceItemData);
                    System.out.println("Saved invoice item: " + item.getQr());
                    System.out.println("Result: " + result);

                    // Get current stock
                    result = supabase.database().find("inventory", Condition.and(Condition.eq("qr", item.getQr())));
                    JsonArray resultArray = JsonParser.parseString(result).getAsJsonArray();
                    int currentStock = 0;
                    if (resultArray.size() == 0) {
                        System.out.println("Item is out of stock: " + item.getQr());
                    } else {
                        JsonObject resultObject = resultArray.get(0).getAsJsonObject();
                        currentStock = resultObject.get("stock").getAsInt();
                    }

                    // update stock
                    Insert.Row stockData = Insert.row()
                        .column("qr", item.getQr())
                        .column("stock", currentStock - item.getQuantity());

                    result = supabase.database().save("inventory", stockData);
                    System.out.println("Saved invoice item: " + item.getQr());
                    System.out.println("Result: " + result);

                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.initStyle(StageStyle.UTILITY);
                        alert.setTitle("New Invoice");
                        alert.setHeaderText("A new invoice has been successfully generated.");
                        alert.setContentText("Invoice ID: " + invoice.getId() + "\n" +
                                "Subtotal: " + subtotalText.getText() + "\n" +
                                "Tax: " + taxText.getText() + "\n" +
                                "Total: " + totalText.getText());

                        alert.showAndWait();
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Platform.runLater(() -> {
                    chargeButton.setDisable(false);
                });
            }
            }
        }.start();
    }

    @FXML private void onScanAction() {
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
                        Platform.runLater(() -> {
                            // Search for item
                            AtomicBoolean foundItem = new AtomicBoolean(false);
                            items.forEach(item -> {
                                if (item.getQr().toLowerCase().equals(barcodeResult.toLowerCase())) {
                                    selectedItem = item;
                                    detailName.setText(item.getName());
                                    detailPrice.setText(String.valueOf(item.getPrice()));
                                    detailStock.setText(String.valueOf(item.getStock()));
                                    foundItem.set(true);
                                }
                            });

                            if (!foundItem.get()) {
                                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                                alert2.initStyle(StageStyle.UTILITY);
                                alert2.setTitle("Scan Result");
                                alert2.setHeaderText("Item not found in inventory");
                                alert2.setContentText("Barcode detected: " + barcodeResult);
                                alert2.showAndWait();
                            }
                        });
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }
}


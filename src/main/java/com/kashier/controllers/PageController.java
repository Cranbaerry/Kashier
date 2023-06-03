package com.kashier.controllers;

import com.kashier.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PageController implements Initializable {
    @FXML public Text pageTitle;

    @FXML
    private void onHomeClick() throws IOException {
        System.out.println("Home clicked!");
        App.setRoot("home");
    }

    @FXML
    private void onInventoryClick() throws IOException {
        System.out.println("Inventory clicked!");
        App.setRoot("inventory");
    }

    @FXML
    private void onCheckOutClick() throws IOException {
        System.out.println("Check out clicked!");
        App.setRoot("checkout");
    }

    @FXML
    private void onSignOffClick() throws IOException {
        System.out.println("Sign off clicked!");
        App.setRoot("auth");
    }
    @FXML
    private void onExitClick() {
        System.out.println("Exit clicked!");
        System.exit(0);
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App.setSize(900, 500);
    }
}


package com.kashier.controllers;

import com.kashier.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Sign Off");
        alert.setHeaderText("Are you sure you want to sign off?");
        alert.setContentText("Press OK to sign off.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            System.out.println("OK clicked!");
            App.setRoot("auth");
        }
    }
    @FXML
    private void onExitClick() {
        System.out.println("Exit clicked!");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Press OK to exit.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            System.out.println("OK clicked!");
            System.exit(0);
        }
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App.setSize(900, 500);
    }
}


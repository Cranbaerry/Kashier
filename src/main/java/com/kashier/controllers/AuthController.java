package com.kashier.controllers;

import com.google.gson.*;
import com.harium.postgrest.Condition;
import com.kashier.App;
import com.kashier.models.Account;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.kashier.App.supabase;

public class AuthController implements Initializable {
    private Account account;

    @FXML private Text errorText;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button loginBtn;

    public boolean login(String username, String password) throws IOException {
        Condition conditions = Condition.and(
            Condition.eq("username", username),
            Condition.eq("password", password)
        );

        String result = supabase.database().find("users", conditions);
        System.out.println(result);


        JsonArray arrResult = JsonParser.parseString(result).getAsJsonArray();
        if (arrResult.size() < 1)  return false;

        JsonObject loginDetails = arrResult.get(0).getAsJsonObject();
        loginDetails.get("id");
        loginDetails.get("password");
        account = new Gson().fromJson(loginDetails, Account.class);
        return true;
    }
    public Account getAccount() {
        return account;
    }

    @FXML
    private void onLoginClick() {
        new Thread(){
            @Override
            public void run(){
                loginBtn.setDisable(true);
                boolean status = false;
                try {
                    status = login(username.getText(), password.getText());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                final boolean finalStatus = status;
                Platform.runLater(() -> {
                    if (finalStatus) {
                        App.account = getAccount();
                        try {
                            App.setRoot("home");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("Login successful!");
                    } else {
                        errorText.setText("Invalid credentials!");
                        System.out.println("Login failed!");
                    }
                    loginBtn.setDisable(false);
                });
            }
        }.start();
    }

    @FXML
    private void onExitClick() {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App.setSize(615, 400);
    }
}


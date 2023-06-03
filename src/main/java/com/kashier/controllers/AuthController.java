package com.kashier.controllers;

import com.google.gson.*;
import com.harium.postgrest.Condition;
import com.kashier.App;
import com.kashier.models.Account;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

import static com.kashier.App.supabase;

public class AuthController {
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
        JsonArray arrResult = JsonParser.parseString(result).getAsJsonArray();
        if (arrResult.size() < 1)  return false;

        JsonObject loginDetails = arrResult.get(0).getAsJsonObject();
        account = new Gson().fromJson(loginDetails, Account.class);
        return true;
    }
    public Account getAccount() {
        return account;
    }

    @FXML
    private void onLoginClick() throws IOException {
        loginBtn.setDisable(true);
        boolean status = login(username.getText(), password.getText());
        if (status) {
            App.account = getAccount();
            App.setRoot("primary");
        } else {
            errorText.setText("Invalid credentials!");
        }

        loginBtn.setDisable(false);
    }

    @FXML
    private void onExitClick() {
        System.exit(0);
    }
}


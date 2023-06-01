package com.kashier.controllers;

import com.google.gson.*;
import com.harium.postgrest.Condition;
import com.kashier.models.Account;

import java.io.IOException;

import static com.kashier.App.supabase;

public class AuthController {
    private Account account;
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
}


package com.kashier.controllers;

import com.harium.postgrest.Condition;
import com.kashier.models.Account;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import static com.kashier.App.parser;
import static com.kashier.App.supabase;

public class AuthController {
    public boolean login(String username, String password) throws IOException, ParseException {
        Condition conditions = Condition.and(
                Condition.eq("username", username),
                Condition.eq("password", password)
        );
        String result = supabase.database().find("users", conditions);
        JSONArray data = (JSONArray) parser.parse(result);

        if (data.size() == 0) {
            return false;
        }

        Account account = new Account();
        account.setData((JSONObject) data.get(0));
        return true;
    }
}

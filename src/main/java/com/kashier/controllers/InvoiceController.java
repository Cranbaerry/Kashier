package com.kashier.controllers;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.harium.postgrest.Condition;
import com.kashier.models.*;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.ArrayList;

import static com.kashier.App.supabase;

public class InvoiceController {

    public ArrayList<Invoice> getInvoices() throws IOException {
        String result = supabase.database().findAll("invoices");
        JsonArray data = (JsonArray) JsonParser.parseString(result).getAsJsonArray();
        ArrayList<Invoice> invoices = new ArrayList<Invoice>();
        for (int i = 0; i < data.size(); i++) {
            Invoice invoice = new Gson().fromJson(data.get(i).getAsJsonObject(), Invoice.class);
            String invoiceItemsRes = supabase.database().find("invoice_items", Condition.and(
                Condition.eq("invoice_id", invoice.getId())
            ));

            JsonArray arrResult = JsonParser.parseString(invoiceItemsRes).getAsJsonArray();
            ArrayList<InvoiceItem> items = getInvoiceItems(arrResult);
            invoices.add(invoice);
        }

        return invoices;
    }

    public Invoice getInvoice(String id) throws IOException {
        String result = supabase.database().find("invoices", Condition.and(
            Condition.eq("id", id)
        ));
        JsonArray arrResult = JsonParser.parseString(result).getAsJsonArray();
        if (arrResult.size() < 1)  return null;
        JsonObject jsonObj = arrResult.get(0).getAsJsonObject();
        Invoice invoice = new Gson().fromJson(jsonObj, Invoice.class);

        result = supabase.database().find("invoice_items", Condition.and(
            Condition.eq("invoice_id", id)
        ));

        arrResult = JsonParser.parseString(result).getAsJsonArray();
        ArrayList<InvoiceItem> items = getInvoiceItems(arrResult);
        invoice.setItems(items);
        return invoice;
    }

    public ArrayList<InvoiceItem> getInvoiceItems(JsonArray arrResult) throws IOException {
        ArrayList<InvoiceItem> items = new ArrayList<InvoiceItem>();
        for (int i = 0; i < arrResult.size(); i++) {
            System.out.println(arrResult.size());
            JsonObject jsonObj = arrResult.get(i).getAsJsonObject();
            InvoiceItem item = new Gson().fromJson(jsonObj, InvoiceItem.class);

            String itemResult = supabase.database().find("items", Condition.and(
                Condition.eq("qr", item.getQr())
            ));

            JsonArray arrItemResult = JsonParser.parseString(itemResult).getAsJsonArray();
            if (arrItemResult.size() < 1)  {
                System.out.println("Item not found in database");
                continue;
            }

            JsonObject detail = arrItemResult.get(0).getAsJsonObject();
            item.setName(detail.get("name").getAsString());
            item.setQr(detail.get("qr").getAsString());
            item.setPrice(detail.get("price").getAsFloat());
            System.out.println("Item found: " + item.getName());
            items.add(item);
        }

        return items;
    }
}

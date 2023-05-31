package com.kashier.interfaces;

import org.json.simple.JSONObject;

public interface IItem {

    public String getQR();

    public String getName();

    public double getPrice();

    public JSONObject toJSON();

}

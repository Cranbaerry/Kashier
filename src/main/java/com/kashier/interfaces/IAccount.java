package com.kashier.interfaces;

import org.json.simple.JSONObject;

public interface IAccount {
    public void setData(JSONObject obj);

    public String getUuid();

    public String getUsername();

    public String getPassword();

    public Integer getRoleId();

}

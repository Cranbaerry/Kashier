package com.kashier.models;

import com.kashier.interfaces.IAccount;
import org.json.simple.JSONObject;

public class Account implements IAccount {
    private String id, username, password;
    private Integer roleId;

    public void setData(JSONObject obj) {
        this.id = obj.get("id").toString();
        this.username = obj.get("username").toString();
        this.password = obj.get("password").toString();
        this.roleId = Integer.parseInt(obj.get("roleId").toString());
    }

    public String getUuid() {
        return username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getRoleId() {
        return roleId;
    }
}

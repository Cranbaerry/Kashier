package com.kashier.models;


import com.kashier.interfaces.IAccount;

public class Account implements IAccount {
    private String id, username, password;
    private Integer roleId;

    public String getId() {
        return id;
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


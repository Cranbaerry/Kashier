package com.kashier;

public class Account {
	String username;
	String password;
	
	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUser() {
		return username;
	}
	
	public String getPass() {
		return password;
	}
	
	public boolean accValidation(String user, String pass) {
		if(user == username && pass == password) {
			return true;
		}else {
			return false;
		}
	}
	
}

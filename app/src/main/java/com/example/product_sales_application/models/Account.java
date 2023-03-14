package com.example.product_sales_application.models;

public class Account {
    private String username;
    private String fullName;
    private String password;
    private int id;


    public Account(String username, String fullName, String password, int id) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.id = id;
    }

    public Account() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

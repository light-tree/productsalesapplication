package com.example.product_sales_application.models;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private int imageResource;
    private String description;
    private double price;
    private  int quantity;

    public int getImageResource() {
        return imageResource;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(int id,String name, int imageResource, double price, int quantity, String description) {
        this.name = name;
        this.imageResource = imageResource;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }



    public String getDescription() {
        return description;
    }
}

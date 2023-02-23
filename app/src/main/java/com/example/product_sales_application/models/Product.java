package com.example.product_sales_application.models;

public class Product {
    private String url;
    private String name;
    private float price;
    private String description;

    public Product(String url, String name, float price, String description) {
        this.url = url;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Product(String url, String name, float price) {
        this.url = url;
        this.name = name;
        this.price = price;
    }

    public Product() {
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


}

package com.example.product_sales_application.models;

public class ProductTypeDomain {
    private long id;
    private String name;
    private String urlImange;

    public ProductTypeDomain(long id, String name, String urlImange) {
        this.id = id;
        this.name = name;
        this.urlImange = urlImange;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImange() {
        return urlImange;
    }

    public void setUrlImange(String urlImange) {
        this.urlImange = urlImange;
    }
}

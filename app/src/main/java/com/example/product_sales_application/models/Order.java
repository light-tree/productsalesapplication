package com.example.product_sales_application.models;

import com.example.product_sales_application.models.Cart;

public class Order {

    private int id;
    private Cart cart;

    public Order(int id, Cart cart) {
        this.id = id;
        this.cart = cart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}

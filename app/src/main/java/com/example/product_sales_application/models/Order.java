package com.example.product_sales_application.models;

import com.example.product_sales_application.models.Cart;

import java.util.List;

public class Order {

    private int id;
    private Cart cart;
    private List<Product> productList;
    private String numberPhone;
    private String customerName;
    private String address;
    private String orderDate;
    private String orderSaler;

    public String getOrderSaler() {
        return orderSaler;
    }

    public void setOrderSaler(String orderSaler) {
        this.orderSaler = orderSaler;
    }

    public Order(int id, Cart cart) {
        this.id = id;
        this.cart = cart;
    }

    public Order(String numberPhone, String customerName, int id, Cart cart){
        this.numberPhone = numberPhone;
        this.customerName = customerName;
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

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}

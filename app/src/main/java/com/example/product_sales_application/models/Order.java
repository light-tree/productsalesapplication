package com.example.product_sales_application.models;

import com.example.product_sales_application.models.Cart;

import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private Cart cart;
    private String customerPhone;
    private String customerFullName;
    private String customerAddress;
    private Date orderedDate;
    private Date requiredDate;
    private int staffId;

    public Order(int id, Cart cart) {
        this.id = id;
        this.cart = cart;
    }

    public Order(int id, Cart cart, String customerPhone, String customerFullName, String customerAddress, Date orderedDate, Date requiredDate, int staffId) {
        this.id = id;
        this.cart = cart;
        this.customerPhone = customerPhone;
        this.customerFullName = customerFullName;
        this.customerAddress = customerAddress;
        this.orderedDate = orderedDate;
        this.requiredDate = requiredDate;
        this.staffId = staffId;
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

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public Date getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
    }

    public Date getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(Date requiredDate) {
        this.requiredDate = requiredDate;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
}

package com.example.product_sales_application.models;

import com.example.product_sales_application.models.Cart;

import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private List<OrderDetail> orderDetailList;
    private String customerPhone;
    private String customerFullName;
    private String customerAddress;
    private Date orderedDate;
    private Date requiredDate;
    private Account staff;

    public Order() {
    }

    public Order(int id, List<OrderDetail> orderDetailList, String customerPhone, String customerFullName, String customerAddress, Date orderedDate, Date requiredDate, Account staff) {
        this.id = id;
        this.orderDetailList = orderDetailList;
        this.customerPhone = customerPhone;
        this.customerFullName = customerFullName;
        this.customerAddress = customerAddress;
        this.orderedDate = orderedDate;
        this.requiredDate = requiredDate;
        this.staff = staff;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Account getStaff() {
        return staff;
    }

    public void setStaff(Account staff) {
        this.staff = staff;
    }
}

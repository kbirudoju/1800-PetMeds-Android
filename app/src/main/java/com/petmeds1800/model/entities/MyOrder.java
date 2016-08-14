package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 8/4/2016.
 */
public class MyOrder implements Serializable{
    private int count;
    private Status status;
    private ArrayList<OrderList> orderList;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<OrderList> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<OrderList> orderList) {
        this.orderList = orderList;
    }

    private String orderNumber;
    private String orderDate;
    private String orderStatus;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public MyOrder(String orderNumber, String orderDate, String orderStatus) {

        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }
}

package com.petmeds1800.model;

import java.io.Serializable;

/**
 * Created by user on 8/4/2016.
 */
public class MyOrder implements Serializable{
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

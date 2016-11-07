package com.petmeds1800.model.entities;

/**
 * Created by Sdixit on 07-11-2016.
 */

public class OrderDetailResponse {

    private OrderList orderDetails;
    private Status status;
    public OrderList getOrderDetails() {
        return orderDetails;
    }
    public void setOrderDetails(OrderList orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

}

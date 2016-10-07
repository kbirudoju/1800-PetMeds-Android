package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by Digvijay on 9/28/2016.
 */
public class CommitOrderResponse implements Serializable {

    private Order order;

    private Status status;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Status getStatus() {
        return status;
    }
}

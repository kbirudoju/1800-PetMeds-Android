package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by Digvijay on 9/28/2016.
 */
public class CommitOrderResponse implements Serializable {

    private CommitOrder order;

    private Status status;

    public CommitOrder getOrder() {
        return order;
    }

    public void setOrder(CommitOrder order) {
        this.order = order;
    }

    public Status getStatus() {
        return status;
    }
}

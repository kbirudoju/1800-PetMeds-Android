package com.petmeds1800.model.entities;

import java.util.List;

/**
 * Created by Digvijay on 9/28/2016.
 */
public class CommitOrderResponse {

    private List<Order> order;

    private Status status;

    public List<Order> getOrder() {
        return order;
    }

    public Status getStatus() {
        return status;
    }
}

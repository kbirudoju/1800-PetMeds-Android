package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by Sdixit on 29-09-2016.
 */

public class OrderReviewSubmitResponse implements Serializable{
    private CheckoutSteps checkoutSteps;
    private Order order;
    private Status status;
    public CheckoutSteps getCheckoutSteps() {
        return checkoutSteps;
    }

    public void setCheckoutSteps(CheckoutSteps checkoutSteps) {
        this.checkoutSteps = checkoutSteps;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }


}

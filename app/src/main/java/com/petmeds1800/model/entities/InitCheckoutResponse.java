package com.petmeds1800.model.entities;

/**
 * Created by Abhinav on 24/9/16.
 */
public class InitCheckoutResponse {

    //TODO add a shopping cart object as well

    private CheckoutSteps checkoutSteps;

    private Status status;

    public CheckoutSteps getCheckoutSteps() {
        return checkoutSteps;
    }

    public void setCheckoutSteps(CheckoutSteps checkoutSteps) {
        this.checkoutSteps = checkoutSteps;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

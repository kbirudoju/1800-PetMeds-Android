package com.petmeds1800.model.entities;

import com.petmeds1800.model.shoppingcart.response.ShoppingCart;

/**
 * Created by Abhinav on 24/9/16.
 */
public class InitCheckoutResponse {

    private ShoppingCart shoppingCart;

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

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
}

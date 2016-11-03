package com.petmeds1800.model;

import com.petmeds1800.model.entities.CheckoutSteps;
import com.petmeds1800.model.shoppingcart.response.ShippingMessageInfo;
import com.petmeds1800.model.shoppingcart.response.ShoppingCart;
import com.petmeds1800.model.shoppingcart.response.Status;

import java.io.Serializable;

/**
 * Created by pooja on 10/28/2016.
 */
public class PaypalResponse implements Serializable {

    public CheckoutSteps getCheckoutSteps() {
        return checkoutSteps;
    }

    public void setCheckoutSteps(CheckoutSteps checkoutSteps) {
        this.checkoutSteps = checkoutSteps;
    }

    private CheckoutSteps checkoutSteps;

    private ShoppingCart shoppingCart;

    private Status status;

    private int itemCount;

    private ShippingMessageInfo shippingMessageInfo;

    public ShoppingCart getShoppingCart ()
    {
        return shoppingCart;
    }

    public void setShoppingCart (ShoppingCart shoppingCart)
    {
        this.shoppingCart = shoppingCart;
    }

    public Status getStatus ()
    {
        return status;
    }

    public void setStatus (Status status)
    {
        this.status = status;
    }

    public int getItemCount ()
    {
        return itemCount;
    }

    public void setItemCount (int itemCount)
    {
        this.itemCount = itemCount;
    }

    public ShippingMessageInfo getShippingMessageInfo ()
    {
        return shippingMessageInfo;
    }

    public void setShippingMessageInfo (ShippingMessageInfo shippingMessageInfo)
    {
        this.shippingMessageInfo = shippingMessageInfo;
    }

}


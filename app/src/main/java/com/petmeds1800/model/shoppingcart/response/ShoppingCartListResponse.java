package com.petmeds1800.model.shoppingcart.response;

import com.petmeds1800.model.entities.CheckoutSteps;

import java.io.Serializable;

/**
 * Created by Sarthak on 9/23/2016.
 */

public class ShoppingCartListResponse implements Serializable {

    private ShoppingCart shoppingCart;

    private String promotionMessage;

    private Status status;

    private int itemCount;

    private ShippingMessageInfo shippingMessageInfo;

    public CheckoutSteps getCheckoutSteps() {
        return checkoutSteps;
    }

    public void setCheckoutSteps(CheckoutSteps checkoutSteps) {
        this.checkoutSteps = checkoutSteps;
    }

    private CheckoutSteps checkoutSteps;

    public ShoppingCart getShoppingCart ()
    {
        return shoppingCart;
    }

    public void setShoppingCart (ShoppingCart shoppingCart)
    {
        this.shoppingCart = shoppingCart;
    }

    public String getPromotionMessage() {
        return promotionMessage;
    }

    public void setPromotionMessage(String promotionMessage) {
        this.promotionMessage = promotionMessage;
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

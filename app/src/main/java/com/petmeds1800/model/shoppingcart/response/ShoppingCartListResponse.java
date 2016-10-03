package com.petmeds1800.model.shoppingcart.response;

import java.io.Serializable;

/**
 * Created by Sarthak on 9/23/2016.
 */

public class ShoppingCartListResponse implements Serializable {

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

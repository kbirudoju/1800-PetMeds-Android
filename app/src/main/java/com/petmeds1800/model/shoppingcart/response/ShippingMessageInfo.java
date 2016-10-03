package com.petmeds1800.model.shoppingcart.response;

import java.io.Serializable;

/**
 * Created by Sarthak on 9/23/2016.
 */

public class ShippingMessageInfo implements Serializable {

    private String message;
    private boolean isFreeShipping;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public boolean getIsFreeShipping ()
    {
        return isFreeShipping;
    }

    public void setIsFreeShipping (boolean isFreeShipping)
    {
        this.isFreeShipping = isFreeShipping;
    }

}

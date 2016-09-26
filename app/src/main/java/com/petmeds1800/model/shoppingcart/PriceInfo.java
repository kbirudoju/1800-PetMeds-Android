package com.petmeds1800.model.shoppingcart;

import java.io.Serializable;

/**
 * Created by Sarthak on 9/23/2016.
 */

public class PriceInfo implements Serializable {

    private float amount;
    private String currencyCode;
    private float rawShipping;

    public float getAmount ()
    {
        return amount;
    }

    public void setAmount (float amount)
    {
        this.amount = amount;
    }

    public String getCurrencyCode ()
    {
        return currencyCode;
    }

    public void setCurrencyCode (String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    public float getRawShipping ()
    {
        return rawShipping;
    }

    public void setRawShipping (float rawShipping)
    {
        this.rawShipping = rawShipping;
    }

}

package com.petmeds1800.model.shoppingcart.response;

import java.io.Serializable;

/**
 * Created by Sarthak on 9/23/2016.
 */

public class PaymentGroups implements Serializable {

    private float amount;
    private String currencyCode;
    private String expirationMonth;
    private String expirationYear;
    private String paymentId;
    private String creditCardNumber;
    private String expirationDayOfMonth;
    private BillingAddress billingAddress;
    private String creditCardType;
    private String orderId;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    private String paymentMethod;

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

    public String getExpirationMonth ()
    {
        return expirationMonth;
    }

    public void setExpirationMonth (String expirationMonth)
    {
        this.expirationMonth = expirationMonth;
    }

    public String getExpirationYear ()
    {
        return expirationYear;
    }

    public void setExpirationYear (String expirationYear)
    {
        this.expirationYear = expirationYear;
    }

    public String getPaymentId ()
    {
        return paymentId;
    }

    public void setPaymentId (String paymentId)
    {
        this.paymentId = paymentId;
    }

    public String getCreditCardNumber ()
    {
        return creditCardNumber;
    }

    public void setCreditCardNumber (String creditCardNumber)
    {
        this.creditCardNumber = creditCardNumber;
    }

    public String getExpirationDayOfMonth ()
    {
        return expirationDayOfMonth;
    }

    public void setExpirationDayOfMonth (String expirationDayOfMonth)
    {
        this.expirationDayOfMonth = expirationDayOfMonth;
    }

    public BillingAddress getBillingAddress ()
    {
        return billingAddress;
    }

    public void setBillingAddress (BillingAddress billingAddress)
    {
        this.billingAddress = billingAddress;
    }

    public String getCreditCardType ()
    {
        return creditCardType;
    }

    public void setCreditCardType (String creditCardType)
    {
        this.creditCardType = creditCardType;
    }

    public String getOrderId ()
    {
        return orderId;
    }

    public void setOrderId (String orderId)
    {
        this.orderId = orderId;
    }

}

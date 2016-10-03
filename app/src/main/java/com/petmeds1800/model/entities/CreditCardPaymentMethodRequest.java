package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sdixit on 29-09-2016.
 */

public class CreditCardPaymentMethodRequest {

    private String cardId;

    private String billingAddressId;

    @SerializedName("_dynSessConf")
    private String dynSessConf;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(String billingAddressId) {
        this.billingAddressId = billingAddressId;
    }


    public String getDynSessConf() {
        return dynSessConf;
    }

    public void setDynSessConf(String dynSessConf) {
        this.dynSessConf = dynSessConf;
    }

}

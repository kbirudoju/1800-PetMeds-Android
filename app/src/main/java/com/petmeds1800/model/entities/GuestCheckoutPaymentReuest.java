package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhinav on 5/10/16.
 */
public class GuestCheckoutPaymentReuest {

    private String cardId;

    private String billingAddressId;

    @SerializedName("_dynSessConf")
    private String sessionConfirmationNumber;

    public GuestCheckoutPaymentReuest(String cardId, String billingAddressId, String sessionConfirmationNumber) {
        this.cardId = cardId;
        this.billingAddressId = billingAddressId;
        this.sessionConfirmationNumber = sessionConfirmationNumber;
    }
}

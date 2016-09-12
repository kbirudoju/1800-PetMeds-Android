package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhinav on 11/9/16.
 */
public class UpdateCardRequest {

    private String exprMonth;

    private String exprYear;

    private boolean cardIsDefault;

    private String cardKey;

    private String billingAddressId;

    @SerializedName("_dynSessConf")
    private String sessionConfNumber;

    public UpdateCardRequest(String expirationMonth, String expirationYear, boolean cardIsDefault, String cardKey,
            String billingAddressId, String sessionConfNumber) {
        this.exprMonth = expirationMonth;
        this.exprYear = expirationYear;
        this.cardIsDefault = cardIsDefault;
        this.cardKey = cardKey;
        this.billingAddressId = billingAddressId;
        this.sessionConfNumber = sessionConfNumber;
    }
}

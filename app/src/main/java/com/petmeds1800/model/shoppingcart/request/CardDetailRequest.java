package com.petmeds1800.model.shoppingcart.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhinav on 24/10/16.
 */
public class CardDetailRequest {

    private String cardKey;

    @SerializedName("_dynSessConf")
    private String sessionConfNumber;

    public CardDetailRequest(String cardKey, String sessionConfNumber) {
        this.cardKey = cardKey;
        this.sessionConfNumber = sessionConfNumber;
    }
}

package com.petmeds1800.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhinav on 13/9/16.
 */
public class RemoveCardRequest {

    @SerializedName("_dynSessConf")
    private String sessionConfNumber;

    private String cardKey;

    public RemoveCardRequest(String sessionConfNumber, String cardKey) {
        this.sessionConfNumber = sessionConfNumber;
        this.cardKey = cardKey;
    }
}

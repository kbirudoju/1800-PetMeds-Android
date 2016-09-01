package com.petmeds1800.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhinav on 31/8/16.
 */
public class RemoveAddressRequest {
    @SerializedName("_dynSessConf")
    private String sessionConfNumber;

    private String addressId;

    public RemoveAddressRequest(String sessionConfNumber, String addressId) {
        this.sessionConfNumber = sessionConfNumber;
        this.addressId = addressId;
    }
}

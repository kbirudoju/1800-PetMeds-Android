package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Digvijay on 8/22/2016.
 */
public class ForgotPasswordRequest {

    private String email;

    @SerializedName("_dynSessConf")
    private String sessionConfNumber;

    public ForgotPasswordRequest(String email, String sessionConfNumber) {
        this.email = email;
        this.sessionConfNumber = sessionConfNumber;
    }
}

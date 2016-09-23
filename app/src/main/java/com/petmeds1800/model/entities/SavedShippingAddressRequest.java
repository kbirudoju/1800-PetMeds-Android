package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sdixit on 23-09-2016.
 */

public class SavedShippingAddressRequest {

    @SerializedName("_dynSessConf")
    private String sessionConfig;

    private String shippingAddressId;

    public String getSessionConfig() {
        return sessionConfig;
    }

    public void setSessionConfig(String sessionConfig) {
        this.sessionConfig = sessionConfig;
    }

    public String getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(String shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }
}

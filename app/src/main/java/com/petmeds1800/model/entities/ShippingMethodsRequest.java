package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sdixit on 29-09-2016.
 */

public class ShippingMethodsRequest {

    @SerializedName("_dynSessConf")
    private String dynSessConf;

    private String shippingMethod;

    public ShippingMethodsRequest(String shippingMethod, String dynSessConf) {
        this.shippingMethod = shippingMethod;
        this.dynSessConf = dynSessConf;
    }

    public String getDynSessConf() {
        return dynSessConf;
    }

    public void setDynSessConf(String dynSessConf) {
        this.dynSessConf = dynSessConf;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

}

package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 8/16/2016.
 */
public class CommerceItems implements Serializable{
    public String getSkuImageUrl() {
        return skuImageUrl;
    }

    public void setSkuImageUrl(String skuImageUrl) {
        this.skuImageUrl = skuImageUrl;
    }

    private String skuImageUrl;
}

package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 9/13/2016.
 */
public class RefillProduct implements Serializable{
    private String productImage;
    private String productId;
    private String displayName;

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

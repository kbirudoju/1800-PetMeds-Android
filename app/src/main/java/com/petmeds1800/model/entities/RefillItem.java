package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 9/13/2016.
 */
public class RefillItem implements Serializable {
    private String petName;
    private String orderRefId;
    private String sellingPrice;
    private String itemRefId;
    private String refillQuantity;
    private RefillProduct product;
    private Sku sku;

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getOrderRefId() {
        return orderRefId;
    }

    public void setOrderRefId(String orderRefId) {
        this.orderRefId = orderRefId;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getItemRefId() {
        return itemRefId;
    }

    public void setItemRefId(String itemRefId) {
        this.itemRefId = itemRefId;
    }

    public String getRefillQuantity() {
        return refillQuantity;
    }

    public void setRefillQuantity(String refillQuantity) {
        this.refillQuantity = refillQuantity;
    }

    public RefillProduct getProduct() {
        return product;
    }

    public void setProduct(RefillProduct product) {
        this.product = product;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }
}

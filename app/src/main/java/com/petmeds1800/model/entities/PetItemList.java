package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 9/16/2016.
 */
public class PetItemList implements Serializable {
    private String orderRefId;
    private String itemRefId;
    private int refillQuantity;
    private Sku sku;
    private String dueDate;



    public String getOrderRefId() {
        return orderRefId;
    }

    public void setOrderRefId(String orderRefId) {
        this.orderRefId = orderRefId;
    }
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }



    public String getItemRefId() {
        return itemRefId;
    }

    public void setItemRefId(String itemRefId) {
        this.itemRefId = itemRefId;
    }

    public int getRefillQuantity() {
        return refillQuantity;
    }

    public void setRefillQuantity(int refillQuantity) {
        this.refillQuantity = refillQuantity;
    }


    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }


}

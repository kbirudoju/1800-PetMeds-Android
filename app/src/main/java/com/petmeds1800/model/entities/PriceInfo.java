package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 9/16/2016.
 */
public class PriceInfo implements Serializable{
    private String sellingPrice;
    private String listPrice;
    private String salePrice;
    private String promoText;

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getListPrice() {
        return listPrice;
    }

    public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getPromoText() {
        return promoText;
    }

    public void setPromoText(String promoText) {
        this.promoText = promoText;
    }
}

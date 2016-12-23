package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 12/15/2016.
 */
public class RecentlyOrdered implements Serializable{
    private String pageUrl;
    private ParentProduct parentProduct;
    private PriceInfo priceInfo;
    private String skuImage;
    private String skuId;
    private String displayName;
    private int minQty;

    public int getMinQty() {
        return minQty;
    }

    public void setMinQty(int minQty) {
        this.minQty = minQty;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public ParentProduct getParentProduct() {
        return parentProduct;
    }

    public void setParentProduct(ParentProduct parentProduct) {
        this.parentProduct = parentProduct;
    }

    public PriceInfo getPriceInfo() {
        return priceInfo;
    }

    public void setPriceInfo(PriceInfo priceInfo) {
        this.priceInfo = priceInfo;
    }

    public String getSkuImage() {
        return skuImage;
    }

    public void setSkuImage(String skuImage) {
        this.skuImage = skuImage;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

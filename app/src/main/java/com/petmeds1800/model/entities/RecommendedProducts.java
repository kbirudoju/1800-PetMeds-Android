package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 9/19/2016.
 */
public class RecommendedProducts implements Serializable {
    private String priceLabel;
    private String expertReviewCount;
    private String seoDisplayName;
    private String ratingImage;
    private String productId;
    private String sellingPrice;
    private String productPageUrl;
    private String listPrice;

    public String getPriceLabel() {
        return priceLabel;
    }

    public void setPriceLabel(String priceLabel) {
        this.priceLabel = priceLabel;
    }

    public String getExpertReviewCount() {
        return expertReviewCount;
    }

    public void setExpertReviewCount(String expertReviewCount) {
        this.expertReviewCount = expertReviewCount;
    }

    public String getSeoDisplayName() {
        return seoDisplayName;
    }

    public void setSeoDisplayName(String seoDisplayName) {
        this.seoDisplayName = seoDisplayName;
    }

    public String getRatingImage() {
        return ratingImage;
    }

    public void setRatingImage(String ratingImage) {
        this.ratingImage = ratingImage;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getProductPageUrl() {
        return productPageUrl;
    }

    public void setProductPageUrl(String productPageUrl) {
        this.productPageUrl = productPageUrl;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    private String salePrice;
    private String group;
    private String displayName;
    private String productImageUrl;

    public String getPromoText() {
        return promoText;
    }

    public void setPromoText(String promoText) {
        this.promoText = promoText;
    }

    private String promoText;
}


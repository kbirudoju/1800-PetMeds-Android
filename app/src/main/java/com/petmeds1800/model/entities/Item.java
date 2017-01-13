package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by Sdixit on 29-09-2016.
 */

public class Item implements Serializable{

    private String itemImageURL;
    private Double sellingPrice;
    private String onSale;
    private Double price;
    private String reOrderReminderMonth;
    private String productPageUrl;
    private Integer itemQuantity;
    private Double listPrice;
    private String skuName;
    private String commerceItemId;
    private String productName;
    private boolean isRxItem;
    private String parentCategory;
    private String itemClinicName;
    private String itemPetName;
    private String itemDrName;

    public boolean isHasUnitLife() {
        return hasUnitLife;
    }

    public void setHasUnitLife(boolean hasUnitLife) {
        this.hasUnitLife = hasUnitLife;
    }

    private boolean hasUnitLife;

    public String getItemDrName() {
        return itemDrName;
    }

    public void setItemDrName(String itemDrName) {
        this.itemDrName = itemDrName;
    }

    public String getItemPetName() {
        return itemPetName;
    }

    public void setItemPetName(String itemPetName) {
        this.itemPetName = itemPetName;
    }

    public String getItemClinicName() {
        return itemClinicName;
    }

    public void setItemClinicName(String itemClinicName) {
        this.itemClinicName = itemClinicName;
    }

    public boolean isRxItem() {
        return isRxItem;
    }
    public void setRxItem(boolean rxItem) {
        isRxItem = rxItem;
    }
    public String getItemImageURL() {
        return itemImageURL;
    }
    public void setItemImageURL(String itemImageURL) {
        this.itemImageURL = itemImageURL;
    }
    public Double getSellingPrice() {
        return sellingPrice;
    }
    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    public String getOnSale() {
        return onSale;
    }

    public void setOnSale(String onSale) {
        this.onSale = onSale;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public String getReOrderReminderMonth() {
        return reOrderReminderMonth;
    }
    public void setReOrderReminderMonth(String reOrderReminderMonth) {
        this.reOrderReminderMonth = reOrderReminderMonth;
    }
    public String getProductPageUrl() {
        return productPageUrl;
    }
    public void setProductPageUrl(String productPageUrl) {
        this.productPageUrl = productPageUrl;
    }
    public Integer getItemQuantity() {
        return itemQuantity;
    }
    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
    public Double getListPrice() {
        return listPrice;
    }
    public void setListPrice(Double listPrice) {
        this.listPrice = listPrice;
    }
    public String getSkuName() {
        return skuName;
    }
    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getCommerceItemId() {
        return commerceItemId;
    }
    public void setCommerceItemId(String commerceItemId) {
        this.commerceItemId = commerceItemId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }


}
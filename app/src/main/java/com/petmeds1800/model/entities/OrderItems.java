package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 9/7/2016.
 */
public class OrderItems implements Serializable {
    private String productDisplayName;
    private String petId;
    private String imageUrl;
    private String skuDisplayName;
    private String petName;

    public String getProductDisplayName() {
        return productDisplayName;
    }

    public void setProductDisplayName(String productDisplayName) {
        this.productDisplayName = productDisplayName;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSkuDisplayName() {
        return skuDisplayName;
    }

    public void setSkuDisplayName(String skuDisplayName) {
        this.skuDisplayName = skuDisplayName;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public RefillReminder getRefillReminder() {
        return refillReminder;
    }

    public void setRefillReminder(RefillReminder refillReminder) {
        this.refillReminder = refillReminder;
    }

    public MedicationReminder getMedicationReminder() {
        return medicationReminder;
    }

    public void setMedicationReminder(MedicationReminder medicationReminder) {
        this.medicationReminder = medicationReminder;
    }

    private String itemId;
    private String sellingPrice;
    private String productPageUrl;
    private String quantity;
    private RefillReminder refillReminder;
    private MedicationReminder medicationReminder;

}

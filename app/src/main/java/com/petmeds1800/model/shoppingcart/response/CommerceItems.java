package com.petmeds1800.model.shoppingcart.response;

import java.io.Serializable;

/**
 * Created by Sarthak on 9/23/2016.
 */

public class CommerceItems implements Serializable {

    private String productGroup;
    private String petId;
    private String productDisplayName;
    private String imageUrl;
    private String skuDisplayName;
    private String petName;
    private String vetClinic;

    public boolean isRxItem() {
        return isRxItem;
    }

    public void setIsRxItem(boolean isRxItem) {
        this.isRxItem = isRxItem;
    }

    private boolean isRxItem;
    private float sellingPrice;
    private String productPageUrl;
    private float listPrice;
    private String skuId;
    private String commerceItemId;
    private String quantity;
    private String vetId;
   /* private String vetName;

    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }*/

    public String getProductGroup ()
    {
        return productGroup;
    }

    public void setProductGroup (String productGroup)
    {
        this.productGroup = productGroup;
    }

    public String getPetId ()
    {
        return petId;
    }

    public void setPetId (String petId)
    {
        this.petId = petId;
    }

    public String getProductDisplayName ()
    {
        return productDisplayName;
    }

    public void setProductDisplayName (String productDisplayName)
    {
        this.productDisplayName = productDisplayName;
    }

    public String getImageUrl ()
    {
        return imageUrl;
    }

    public void setImageUrl (String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getSkuDisplayName ()
    {
        return skuDisplayName;
    }

    public void setSkuDisplayName (String skuDisplayName)
    {
        this.skuDisplayName = skuDisplayName;
    }

    public String getPetName ()
    {
        return petName;
    }

    public void setPetName (String petName)
    {
        this.petName = petName;
    }

    public String getVetClinic ()
    {
        return vetClinic;
    }

    public void setVetClinic (String vetClinic)
    {
        this.vetClinic = vetClinic;
    }


    public float getSellingPrice ()
    {
        return sellingPrice;
    }

    public void setSellingPrice (float sellingPrice)
    {
        this.sellingPrice = sellingPrice;
    }

    public String getProductPageUrl ()
    {
        return productPageUrl;
    }

    public void setProductPageUrl (String productPageUrl)
    {
        this.productPageUrl = productPageUrl;
    }

    public float getListPrice ()
    {
        return listPrice;
    }

    public void setListPrice (float listPrice)
    {
        this.listPrice = listPrice;
    }

    public String getSkuId ()
    {
        return skuId;
    }

    public void setSkuId (String skuId)
    {
        this.skuId = skuId;
    }

    public String getCommerceItemId ()
    {
        return commerceItemId;
    }

    public void setCommerceItemId (String commerceItemId)
    {
        this.commerceItemId = commerceItemId;
    }

    public String getQuantity ()
    {
        return quantity;
    }

    public void setQuantity (String quantity)
    {
        this.quantity = quantity;
    }

    public String getVetId ()
    {
        return vetId;
    }

    public void setVetId (String vetId)
    {
        this.vetId = vetId;
    }

}

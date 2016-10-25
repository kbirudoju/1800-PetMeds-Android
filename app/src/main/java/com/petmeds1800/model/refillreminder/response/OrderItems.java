package com.petmeds1800.model.refillreminder.response;

import java.io.Serializable;

/**
 * Created by Sarthak on 21-Oct-16.
 */

public class OrderItems implements Serializable {

    private String sellingPrice;

    private String petId;

    private String productDisplayName;

    private String productPageUrl;

    private String petImageUrl;

    private String imageUrl;

    private ReminderMonth reminderMonth;

    private String skuDisplayName;

    private String quantity;

    private String itemId;

    private String petName;

    public String getSellingPrice ()
    {
        return sellingPrice;
    }

    public void setSellingPrice (String sellingPrice)
    {
        this.sellingPrice = sellingPrice;
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

    public String getProductPageUrl ()
    {
        return productPageUrl;
    }

    public void setProductPageUrl (String productPageUrl)
    {
        this.productPageUrl = productPageUrl;
    }

    public String getPetImageUrl ()
    {
        return petImageUrl;
    }

    public void setPetImageUrl (String petImageUrl)
    {
        this.petImageUrl = petImageUrl;
    }

    public String getImageUrl ()
    {
        return imageUrl;
    }

    public void setImageUrl (String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public ReminderMonth getReminderMonth ()
    {
        return reminderMonth;
    }

    public void setReminderMonth (ReminderMonth reminderMonth)
    {
        this.reminderMonth = reminderMonth;
    }

    public String getSkuDisplayName ()
    {
        return skuDisplayName;
    }

    public void setSkuDisplayName (String skuDisplayName)
    {
        this.skuDisplayName = skuDisplayName;
    }

    public String getQuantity ()
    {
        return quantity;
    }

    public void setQuantity (String quantity)
    {
        this.quantity = quantity;
    }

    public String getItemId ()
    {
        return itemId;
    }

    public void setItemId (String itemId)
    {
        this.itemId = itemId;
    }

    public String getPetName ()
    {
        return petName;
    }

    public void setPetName (String petName)
    {
        this.petName = petName;
    }
}

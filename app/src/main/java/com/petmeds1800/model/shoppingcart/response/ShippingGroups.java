package com.petmeds1800.model.shoppingcart.response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sarthak on 9/23/2016.
 */

public class ShippingGroups implements Serializable {

    private String id;
    private SpecialInstructions specialInstructions;
    private ArrayList<HandlingInstructions> handlingInstructions;
    private String trackingNumber;
    private PriceInfo priceInfo;
    private String description;
    private String submittedDate;
    private String actualShipDate;
    private String locationId;
    private float state;
    private String shipOnDate;
    private String shippingMethod;
    private ShippingAddress shippingAddress;
    private String stateDetail;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public SpecialInstructions getSpecialInstructions ()
    {
        return specialInstructions;
    }

    public void setSpecialInstructions (SpecialInstructions specialInstructions)
    {
        this.specialInstructions = specialInstructions;
    }

    public ArrayList<HandlingInstructions> getHandlingInstructions ()
    {
        return handlingInstructions;
    }

    public void setHandlingInstructions (ArrayList<HandlingInstructions> handlingInstructions)
    {
        this.handlingInstructions = handlingInstructions;
    }

    public String getTrackingNumber ()
    {
        return trackingNumber;
    }

    public void setTrackingNumber (String trackingNumber)
    {
        this.trackingNumber = trackingNumber;
    }

    public PriceInfo getPriceInfo ()
    {
        return priceInfo;
    }

    public void setPriceInfo (PriceInfo priceInfo)
    {
        this.priceInfo = priceInfo;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getSubmittedDate ()
    {
        return submittedDate;
    }

    public void setSubmittedDate (String submittedDate)
    {
        this.submittedDate = submittedDate;
    }

    public String getActualShipDate ()
    {
        return actualShipDate;
    }

    public void setActualShipDate (String actualShipDate)
    {
        this.actualShipDate = actualShipDate;
    }

    public String getLocationId ()
    {
        return locationId;
    }

    public void setLocationId (String locationId)
    {
        this.locationId = locationId;
    }

    public float getState ()
    {
        return state;
    }

    public void setState (float state)
    {
        this.state = state;
    }

    public String getShipOnDate ()
    {
        return shipOnDate;
    }

    public void setShipOnDate (String shipOnDate)
    {
        this.shipOnDate = shipOnDate;
    }

    public String getShippingMethod ()
    {
        return shippingMethod;
    }

    public void setShippingMethod (String shippingMethod)
    {
        this.shippingMethod = shippingMethod;
    }

    public ShippingAddress getShippingAddress ()
    {
        return shippingAddress;
    }

    public void setShippingAddress (ShippingAddress shippingAddress)
    {
        this.shippingAddress = shippingAddress;
    }

    public String getStateDetail ()
    {
        return stateDetail;
    }

    public void setStateDetail (String stateDetail)
    {
        this.stateDetail = stateDetail;
    }

}

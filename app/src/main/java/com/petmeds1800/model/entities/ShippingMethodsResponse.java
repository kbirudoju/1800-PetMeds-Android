package com.petmeds1800.model.entities;

import java.util.ArrayList;

/**
 * Created by Digvijay on 9/22/2016.
 */
public class ShippingMethodsResponse {

    private Status status;

    private ArrayList<ShippingMethod> shippingMethods;

    public Status getStatus() {
        return status;
    }

    public ArrayList<ShippingMethod> getShippingMethods() {
        return shippingMethods;
    }
}

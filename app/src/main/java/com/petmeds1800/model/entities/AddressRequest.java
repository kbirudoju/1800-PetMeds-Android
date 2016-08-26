package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhinav on 22/8/16.
 */
public class AddressRequest {

    //TODO need to change it to the boolean after backend correction
    private String useShippingAddressAsDefault;
    //TODO need to check if backend has merged it with firstname field
    private String lastName;
    private String state;
    private String address1;
    private String address2;
    private String country;
    private String city;
    private String postalCode;
    private String phoneNumber;
    //TODO need to change it to the boolean after backend correction
    private String useBillingAddressAsDefault;
    private String firstName;
    private String addressId;
    @SerializedName("_dynSessConf")
    private String sessionConfNumber;


    public AddressRequest(String useShippingAddressAsDefault, String lastName, String state, String address1, String address2, String country, String city, String postalCode, String phoneNumber, String useBillingAddressAsDefault, String firstName, String sessionConfNumber) {
        this.useShippingAddressAsDefault = useShippingAddressAsDefault;
        this.lastName = lastName;
        this.state = state;
        this.address1 = address1;
        this.address2 = address2;
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.useBillingAddressAsDefault = useBillingAddressAsDefault;
        this.firstName = firstName;
        this.sessionConfNumber = sessionConfNumber;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}

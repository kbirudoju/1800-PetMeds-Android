package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhinav on 22/8/16.
 */
public class AddressRequest {

    private boolean useShippingAddressAsDefault;
    private String lastName;
    private String state;
    private String address1;
    private String address2;
    private String country;
    private String city;
    private String postalCode;
    private String phoneNumber;
    private boolean useBillingAddressAsDefault;
    private String firstName;
    private String addressId;
    @SerializedName("_dynSessConf")
    private String sessionConfNumber;


    public AddressRequest(boolean useShippingAddressAsDefault, String lastName, String state, String address1, String address2, String country, String city, String postalCode, String phoneNumber, boolean useBillingAddressAsDefault, String firstName, String sessionConfNumber) {
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

    public boolean isUseShippingAddressAsDefault() {
        return useShippingAddressAsDefault;
    }

    public String getLastName() {
        return lastName;
    }

    public String getState() {
        return state;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isUseBillingAddressAsDefault() {
        return useBillingAddressAsDefault;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSessionConfNumber() {
        return sessionConfNumber;
    }
}

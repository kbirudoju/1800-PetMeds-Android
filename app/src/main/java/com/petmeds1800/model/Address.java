package com.petmeds1800.model;

/**
 * Created by Abhinav on 21/8/16.
 */
public class Address {

    //TODO need to change it to the boolean after backend correction
    private String isDefaultShippingAddress;
    //TODO need to check if backend has merged it with firstname field
    private String lastName;
    private String state;
    private String address1;
    private String address2;
    private String addressId;
    private String country;
    private String city;
    private String postalCode;
    private String phoneNumber;
    //TODO need to change it to the boolean after backend correction
    private String isDefaultBillingAddress;
    //TODO need to check if backend has merged it with lastname field
    private String firstName;

    public String getIsDefaultShippingAddress() {
        return isDefaultShippingAddress;
    }

    public void setIsDefaultShippingAddress(String isDefaultShippingAddress) {
        this.isDefaultShippingAddress = isDefaultShippingAddress;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIsDefaultBillingAddress() {
        return isDefaultBillingAddress;
    }

    public void setIsDefaultBillingAddress(String isDefaultBillingAddress) {
        this.isDefaultBillingAddress = isDefaultBillingAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}

package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sdixit on 05-10-2016.
 */

public class ShippingAddressRequest implements Serializable {
    @SerializedName("_dynSessConf")
    private String dynSessConf;
    private String shippingAddressFirstName;
    private String shippingAddressLastName;
    private String shippingAddress1;
    private String shippingAddressCity;
    private String shippingAddressState;
    private String shippingAddressPostalCode;
    private String shippingAddressCountry;
    private String shippingAddressPhoneNumber;
    private String email;
    private String confirmEmail;
    private Boolean useShippingAddressAsDefault;
    private String password;
    private String confirmPassword;

    public String getDynSessConf() {
        return dynSessConf;
    }

    public void setDynSessConf(String dynSessConf) {
        this.dynSessConf = dynSessConf;
    }

    public String getShippingAddressFirstName() {
        return shippingAddressFirstName;
    }


    public void setShippingAddressFirstName(String shippingAddressFirstName) {
        this.shippingAddressFirstName = shippingAddressFirstName;
    }

    public String getShippingAddressLastName() {
        return shippingAddressLastName;
    }

    public void setShippingAddressLastName(String shippingAddressLastName) {
        this.shippingAddressLastName = shippingAddressLastName;
    }

    public String getShippingAddress1() {
        return shippingAddress1;
    }

    public void setShippingAddress1(String shippingAddress1) {
        this.shippingAddress1 = shippingAddress1;
    }

    public String getShippingAddressCity() {
        return shippingAddressCity;
    }

    public void setShippingAddressCity(String shippingAddressCity) {
        this.shippingAddressCity = shippingAddressCity;
    }

    public String getShippingAddressState() {
        return shippingAddressState;
    }

    public void setShippingAddressState(String shippingAddressState) {
        this.shippingAddressState = shippingAddressState;
    }

    public String getShippingAddressPostalCode() {
        return shippingAddressPostalCode;
    }

    public void setShippingAddressPostalCode(String shippingAddressPostalCode) {
        this.shippingAddressPostalCode = shippingAddressPostalCode;
    }

    public String getShippingAddressCountry() {
        return shippingAddressCountry;
    }

    public void setShippingAddressCountry(String shippingAddressCountry) {
        this.shippingAddressCountry = shippingAddressCountry;
    }

    public String getShippingAddressPhoneNumber() {
        return shippingAddressPhoneNumber;
    }

    public void setShippingAddressPhoneNumber(String shippingAddressPhoneNumber) {
        this.shippingAddressPhoneNumber = shippingAddressPhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    public Boolean getUseShippingAddressAsDefault() {
        return useShippingAddressAsDefault;
    }

    public void setUseShippingAddressAsDefault(Boolean useShippingAddressAsDefault) {
        this.useShippingAddressAsDefault = useShippingAddressAsDefault;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

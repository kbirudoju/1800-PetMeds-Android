package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Digvijay on 9/9/2016.
 */
public class SignUpRequest {

    private String email;

    private String confirmEmail;

    private String password;

    private String confirmPassword;

    private String billingAddressFirstName;

    private String billingAddressLastName;

    private String billingAddress1;

    private String billingAddress2;

    private String billingAddressCity;

    private String billingAddressState;

    private String billingAddressPostalCode;

    private String billingAddressCountry;

    private String billingAddressPhoneNumber;

    private String shippingAddressFirstName;

    private String shippingAddressLastName;

    private String shippingAddress1;

    private String shippingAddress2;

    private String shippingAddressCity;

    private String shippingAddressState;

    private String shippingAddressPostalCode;

    private String shippingAddressCountry;

    private String shippingAddressPhoneNumber;

    @SerializedName("_dynSessConf")
    private String sessionConfNumber;

    public SignUpRequest(String email, String confirmEmail, String password, String confirmPassword,
            String billingAddressFirstName, String billingAddressLastName, String billingAddress1,
            String billingAddress2, String billingAddressCity, String billingAddressState,
            String billingAddressPostalCode, String billingAddressCountry, String billingAddressPhoneNumber,
            String shippingAddressFirstName, String shippingAddressLastName, String shippingAddress1,
            String shippingAddress2, String shippingAddressCity, String shippingAddressState,
            String shippingAddressPostalCode, String shippingAddressCountry, String shippingAddressPhoneNumber,
            String sessionConfNumber) {
        this.email = email;
        this.confirmEmail = confirmEmail;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.billingAddressFirstName = billingAddressFirstName;
        this.billingAddressLastName = billingAddressLastName;
        this.billingAddress1 = billingAddress1;
        this.billingAddress2 = billingAddress2;
        this.billingAddressCity = billingAddressCity;
        this.billingAddressState = billingAddressState;
        this.billingAddressPostalCode = billingAddressPostalCode;
        this.billingAddressCountry = billingAddressCountry;
        this.billingAddressPhoneNumber = billingAddressPhoneNumber;
        this.shippingAddressFirstName = shippingAddressFirstName;
        this.shippingAddressLastName = shippingAddressLastName;
        this.shippingAddress1 = shippingAddress1;
        this.shippingAddress2 = shippingAddress2;
        this.shippingAddressCity = shippingAddressCity;
        this.shippingAddressState = shippingAddressState;
        this.shippingAddressPostalCode = shippingAddressPostalCode;
        this.shippingAddressCountry = shippingAddressCountry;
        this.shippingAddressPhoneNumber = shippingAddressPhoneNumber;
        this.sessionConfNumber = sessionConfNumber;
    }

    public void setSessionConfNumber(String sessionConfNumber) {
        this.sessionConfNumber = sessionConfNumber;
    }

    public String getPassword() {
        return password;
    }
}

package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhinav on 17/8/16.
 */
public class CardRequest {

    @SerializedName("cardNumber")
    private String cardNumber;

    @SerializedName("exprMonth")
    private String exprMonth;

    @SerializedName("exprYear")
    private String exprYear;

    @SerializedName("cardIsDefault")
    private String cardIsDefault;

    @SerializedName("billingAddressId")
    private String billingAddressId;

    @SerializedName("cvv")
    private String cvv;

    @SerializedName("_dynSessConf")
    private String sessionConfNumber;

    public CardRequest(String cardNumber, String exprMonth, String exprYear, String cardIsDefault, String cvv, String sessionConfNumber) {
        this.cardNumber = cardNumber;
        this.exprMonth = exprMonth;
        this.exprYear = exprYear;
        this.cardIsDefault = cardIsDefault;
        this.billingAddressId = billingAddressId;
        this.cvv = cvv;
        this.sessionConfNumber = sessionConfNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExprMonth() {
        return exprMonth;
    }

    public void setExprMonth(String exprMonth) {
        this.exprMonth = exprMonth;
    }

    public String getExprYear() {
        return exprYear;
    }

    public void setExprYear(String exprYear) {
        this.exprYear = exprYear;
    }

    public String getCardIsDefault() {
        return cardIsDefault;
    }

    public void setCardIsDefault(String cardIsDefault) {
        this.cardIsDefault = cardIsDefault;
    }

    public String getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(String billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getSessionConfNumber() {
        return sessionConfNumber;
    }

    public void setSessionConfNumber(String sessionConfNumber) {
        this.sessionConfNumber = sessionConfNumber;
    }
}

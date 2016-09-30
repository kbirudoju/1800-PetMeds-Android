package com.petmeds1800.model.entities;

/**
 * Created by Sdixit on 29-09-2016.
 */

public class PaymentMethod {

    private String expirationMonth;
    private String paymentType;
    private String cardType;
    private String cardNumber;
    private String expirationyear;

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationyear() {
        return expirationyear;
    }
    public void setExpirationyear(String expirationyear) {
        this.expirationyear = expirationyear;
    }

}

package com.petmeds1800.model;

/**
 * Created by Abhinav on 11/8/16
 */
public class Card {

    private String id;
    private String cardNumber;
    private String cardType;
    private String expirationMonth;
    private String expirationYear;
    private boolean cardIsDefault;
    private String cardKey;

    public Card(String cardNumber, String cardType, String expirationYear, boolean mDefaultPayment) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.expirationYear = expirationYear;
        this.cardIsDefault = mDefaultPayment;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

    public boolean isCardIsDefault() {
        return cardIsDefault;
    }

    public void setCardIsDefault(boolean cardIsDefault) {
        this.cardIsDefault = cardIsDefault;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getCardKey() {
        return cardKey;
    }

    public void setCardKey(String cardKey) {
        this.cardKey = cardKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

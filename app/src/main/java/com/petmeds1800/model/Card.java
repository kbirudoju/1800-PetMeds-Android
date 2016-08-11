package com.petmeds1800.model;

/**
 * Created by Abhinav on 11/8/16.
 */
public class Card {

    private String mCardNumber;
    private String mCardType;
    private String mExpiryDate;
    private boolean mDefaultPayment;

    public Card(String mCardNumber, String mCardType, String mExpiryDate, boolean mDefaultPayment) {
        this.mCardNumber = mCardNumber;
        this.mCardType = mCardType;
        this.mExpiryDate = mExpiryDate;
        this.mDefaultPayment = mDefaultPayment;
    }

    public String getmCardNumber() {
        return mCardNumber;
    }

    public void setmCardNumber(String mCardNumber) {
        this.mCardNumber = mCardNumber;
    }

    public String getmCardType() {
        return mCardType;
    }

    public void setmCardType(String mCardType) {
        this.mCardType = mCardType;
    }

    public String getmExpiryDate() {
        return mExpiryDate;
    }

    public void setmExpiryDate(String mExpiryDate) {
        this.mExpiryDate = mExpiryDate;
    }

    public boolean ismDefaultPayment() {
        return mDefaultPayment;
    }

    public void setmDefaultPayment(boolean mDefaultPayment) {
        this.mDefaultPayment = mDefaultPayment;
    }
}

package com.petmeds1800.model;

import java.io.Serializable;

/**
 * Created by pooja on 1/24/2017.
 */
public class ContactUs implements Serializable{
    private String mainPhoneNumber;

    public String getMainPhoneNumber() {
        return mainPhoneNumber;
    }

    public void setMainPhoneNumber(String mainPhoneNumber) {
        this.mainPhoneNumber = mainPhoneNumber;
    }

    public String getPharmacyFaxNumber() {
        return pharmacyFaxNumber;
    }

    public void setPharmacyFaxNumber(String pharmacyFaxNumber) {
        this.pharmacyFaxNumber = pharmacyFaxNumber;
    }

    public String getPharmacyPhoneNumber() {
        return pharmacyPhoneNumber;
    }

    public void setPharmacyPhoneNumber(String pharmacyPhoneNumber) {
        this.pharmacyPhoneNumber = pharmacyPhoneNumber;
    }

    public String getTollFreeNumber() {
        return tollFreeNumber;
    }

    public void setTollFreeNumber(String tollFreeNumber) {
        this.tollFreeNumber = tollFreeNumber;
    }

    public String getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public SocialInfo getSocial() {
        return social;
    }

    public void setSocial(SocialInfo social) {
        this.social = social;
    }

    private String pharmacyFaxNumber;
    private String pharmacyPhoneNumber;
    private String tollFreeNumber;
    private String emailMessage;
    private String emailAddress;
    private String mailingAddress;
    private SocialInfo social;
}

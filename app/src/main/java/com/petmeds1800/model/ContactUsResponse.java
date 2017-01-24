package com.petmeds1800.model;

import com.petmeds1800.model.entities.Status;

import java.io.Serializable;

/**
 * Created by pooja on 1/24/2017.
 */
public class ContactUsResponse implements Serializable{
    private Status status;
    private ContactUs contactUs;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ContactUs getContactUs() {
        return contactUs;
    }

    public void setContactUs(ContactUs contactUs) {
        this.contactUs = contactUs;
    }
}

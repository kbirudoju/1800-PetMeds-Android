package com.petmeds1800.model.entities;

/**
 * Created by Digvijay on 8/16/2016.
 */
public class SessionConfNumberResponse {

    private String sessionConfirmationNumber;

    public SessionConfNumberResponse(String sessionConfirmationNumber) {
        this.sessionConfirmationNumber = sessionConfirmationNumber;
    }

    public String getSessionConfirmationNumber() {
        return sessionConfirmationNumber;
    }

    public void setSessionConfirmationNumber(String sessionConfirmationNumber) {
        this.sessionConfirmationNumber = sessionConfirmationNumber;
    }
}

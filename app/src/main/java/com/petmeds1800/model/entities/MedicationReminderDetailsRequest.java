package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sdixit on 27-10-2016.
 */

public class MedicationReminderDetailsRequest {
    @SerializedName("_dynSessConf")
    private String sessionConfNumber;
    private int reminderId;

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public String getSessionConfNumber() {
        return sessionConfNumber;
    }

    public void setSessionConfNumber(String sessionConfNumber) {
        this.sessionConfNumber = sessionConfNumber;
    }
}

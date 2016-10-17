package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sdixit on 18-10-2016.
 */

public class RemoveMedicationReminderRequest {

    public RemoveMedicationReminderRequest(String dynSessConf, String reminderId) {
        this.dynSessConf = dynSessConf;
        this.reminderId = reminderId;
    }

    private String reminderId;
    @SerializedName("_dynSessConf")
    private String dynSessConf;
    public String getReminderId() {
        return reminderId;
    }
    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }
    public String getDynSessConf() {
        return dynSessConf;
    }
    public void setDynSessConf(String dynSessConf) {
        this.dynSessConf = dynSessConf;
    }


}

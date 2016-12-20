package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sdixit on 15-12-2016.
 */

public class PushNotificationRequest {
    private String pushNotification;

    @SerializedName("_dynSessConf")
    private String sessionConfNumber;

    public PushNotificationRequest(String pushNotification, String sessionConfNumber) {
        this.pushNotification = pushNotification;
        this.sessionConfNumber = sessionConfNumber;
    }

    public String getPushNotification() {
        return pushNotification;
    }

    public String getSessionConfNumber() {
        return sessionConfNumber;
    }
}

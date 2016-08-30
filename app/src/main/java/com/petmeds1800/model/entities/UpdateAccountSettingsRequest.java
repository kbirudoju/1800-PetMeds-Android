package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhinav on 8/8/16.
 */
public class UpdateAccountSettingsRequest {
    private String pushNotification;
    private String userId;
    private String email;
    private String password;

    @SerializedName("_dynSessConf")
    private String sessionConfNumber;

    public UpdateAccountSettingsRequest(String email, String userId, String password, String sessionConfNumber) {
        this.email = email;
        this.userId = userId;
        this.password = password;
        this.sessionConfNumber = sessionConfNumber;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPushNotification() {
        return pushNotification;
    }

    public void setPushNotification(String pushNotification) {
        this.pushNotification = pushNotification;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionConfNumber() {
        return sessionConfNumber;
    }

    public void setSessionConfNumber(String sessionConfNumber) {
        this.sessionConfNumber = sessionConfNumber;
    }
}

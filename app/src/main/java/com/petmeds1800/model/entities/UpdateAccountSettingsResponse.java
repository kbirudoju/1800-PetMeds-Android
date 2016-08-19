package com.petmeds1800.model.entities;

/**
 * Created by Abhinav on 19/8/16.
 */
public class UpdateAccountSettingsResponse {

    private Status status;
    private Profile profile;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}

package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by Abhinav on 17/8/16.
 */
public class Profile implements Serializable {

    private User profile;

    public User getProfile() {
        return profile;
    }

    public void setProfile(User profile) {
        this.profile = profile;
    }
}

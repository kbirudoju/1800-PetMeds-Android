package com.petmeds1800.model.entities;

import android.support.annotation.NonNull;

/**
 * Created by Abhinav on 8/8/16.
 */
public class User {

    private String mName;
    private String mUsername;
    private String mPassword;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}

package com.petmeds1800.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.petmeds1800.model.entities.SessionConfNumberResponse;

/**
 * Created by Digvijay on 8/16/2016.
 */
public class GeneralPreferencesHelper {

    public static final String GENERAL_PREFS = GeneralPreferencesHelper.class.getSimpleName();

    public static final String SESSION_CONF_RESPONSE = "session_conf_response";

    public static final String LOGIN_EMAIL = "login_email";

    public static final String LOGIN_PASSWORD = "login_password";

    public static final String IS_NEW_USER = "is_new_user";

    private SharedPreferences mPreferences;
    public static final String IS_NOTIFICATION_ENABLED = "is_notification_enabled";
    public static final String IS_ISFINGER_PRINT_ENABLED = "is_fingerprint_enabled";
    public GeneralPreferencesHelper(Context context) {
        mPreferences = context.getSharedPreferences(GENERAL_PREFS, Context.MODE_PRIVATE);
    }

    public void saveSessionConfirmationResponse(SessionConfNumberResponse response) {
        mPreferences.edit().putString(SESSION_CONF_RESPONSE, new Gson().toJson(response)).apply();
    }

    public SessionConfNumberResponse getSessionConfirmationResponse() {
        String json = mPreferences.getString(SESSION_CONF_RESPONSE, null);
        if (json != null) {
            return new Gson().fromJson(json, SessionConfNumberResponse.class);
        }
        return null;
    }

    public void setLoginEmail(String email){
        mPreferences.edit().putString(LOGIN_EMAIL, email).apply();
    }

    public String getLoginEmail(){
        return mPreferences.getString(LOGIN_EMAIL, null);
    }

    public void setIsNewUser(boolean value){
        mPreferences.edit().putBoolean(IS_NEW_USER, value).apply();
    }

    public boolean getIsNewUser(){
        return mPreferences.getBoolean(IS_NEW_USER, true);
    }

    public void setIsPushNotificationEnableFlag(boolean value){
        mPreferences.edit().putBoolean(IS_NOTIFICATION_ENABLED, value).apply();
    }

    public boolean getIsPushNotificationEnableFlag(){
        return mPreferences.getBoolean(IS_NOTIFICATION_ENABLED, false);
    }
    public void setIsFingerPrintEnabled(boolean value){
        mPreferences.edit().putBoolean(IS_ISFINGER_PRINT_ENABLED, value).apply();
    }

    public boolean getIsFingerPrintEnabled(){
        return mPreferences.getBoolean(IS_ISFINGER_PRINT_ENABLED, true);
    }
}

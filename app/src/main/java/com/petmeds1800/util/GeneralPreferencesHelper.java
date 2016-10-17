package com.petmeds1800.util;

import com.google.gson.Gson;

import com.petmeds1800.model.entities.SessionConfNumberResponse;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Digvijay on 8/16/2016.
 */
public class GeneralPreferencesHelper {

    public static final String GENERAL_PREFS = GeneralPreferencesHelper.class.getSimpleName();

    public static final String SESSION_CONF_RESPONSE = "session_conf_response";

    public static final String LOGIN_EMAIL = "login_email";

    public static final String LOGIN_PASSWORD = "login_password";

    public static final String IS_USER_LOGGED_IN = "is_user_logged_in";

    public static final String HAS_USER_SEEN_INTRO = "has_user_seen_intro";

    private SharedPreferences mPreferences;

    public static final String IS_NOTIFICATION_ENABLED = "is_notification_enabled";

    public static final String IS_ISFINGER_PRINT_ENABLED = "is_fingerprint_enabled";

    public static final String IS_ACCOUNT_SCREEN = "is_account_screen";

    public GeneralPreferencesHelper(Context context) {
        mPreferences = context.getSharedPreferences(GENERAL_PREFS, MODE_PRIVATE);
    }

    public void clearGeneralPreferencesData() {
        mPreferences.edit().clear().apply();
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

    public void setLoginEmail(String email) {
        mPreferences.edit().putString(LOGIN_EMAIL, email).apply();
    }

    public String getLoginEmail() {
        return mPreferences.getString(LOGIN_EMAIL, null);
    }

    public void setLoginPassword(String password){
        mPreferences.edit().putString(LOGIN_PASSWORD, password).apply();
    }

    public String getLoginPassword(){
        return mPreferences.getString(LOGIN_PASSWORD, null);
    }

    public void setIsUserLoggedIn(boolean value) {
        mPreferences.edit().putBoolean(IS_USER_LOGGED_IN, value).apply();
    }

    public boolean getIsUserLoggedIn() {
        return mPreferences.getBoolean(IS_USER_LOGGED_IN, false);
    }

    public void setHasUserSeenIntro(boolean value) {
        mPreferences.edit().putBoolean(HAS_USER_SEEN_INTRO, value).apply();
    }

    public boolean getHaUserSeenIntro() {
        return mPreferences.getBoolean(HAS_USER_SEEN_INTRO, false);

    }

    public void setIsPushNotificationEnableFlag(boolean value) {
        mPreferences.edit().putBoolean(IS_NOTIFICATION_ENABLED, value).apply();
    }

    public boolean getIsPushNotificationEnableFlag() {
        return mPreferences.getBoolean(IS_NOTIFICATION_ENABLED, false);
    }

    public void setIsFingerPrintEnabled(boolean value) {
        mPreferences.edit().putBoolean(IS_ISFINGER_PRINT_ENABLED, value).apply();
    }

    public boolean getIsFingerPrintEnabled() {
        return mPreferences.getBoolean(IS_ISFINGER_PRINT_ENABLED, true);
    }

}

package com.petmeds1800.util;

import com.google.gson.Gson;

import com.petmeds1800.model.entities.SessionConfNumberResponse;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Digvijay on 8/16/2016.
 */
public class GeneralPreferencesHelper {

    public static final String GENERAL_PREFS = GeneralPreferencesHelper.class.getSimpleName();

    public static final String SESSION_CONF_RESPONSE = "session_conf_response";

    private SharedPreferences mPreferences;

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
}

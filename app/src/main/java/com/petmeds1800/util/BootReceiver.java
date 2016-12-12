package com.petmeds1800.util;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.intent.AddUpdateMedicationRemindersIntent;

import javax.inject.Inject;

/**
 * Created by Sdixit on 21-10-2016.
 */

public class BootReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        PetMedsApplication.getAppComponent().inject(this);
        if (mPreferencesHelper.getIsUserLoggedIn()) {
            context.startService(new AddUpdateMedicationRemindersIntent(context, false));
        }
    }
}

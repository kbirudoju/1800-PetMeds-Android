package com.petmeds1800.util;

import com.petmeds1800.intent.HomeIntent;
import com.urbanairship.AirshipReceiver;
import com.urbanairship.push.PushMessage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Sdixit on 26-10-2016.
 */
public class PetMedsAirShipReceiver extends AirshipReceiver {


    private static final String TAG = "PetMedsAirShipReceiver";

    /**
     * Intent action sent as a local broadcast to update the channel.
     */
    public static final String ACTION_UPDATE_CHANNEL = "ACTION_UPDATE_CHANNEL";

    @Override
    protected void onChannelCreated(@NonNull Context context, @NonNull String channelId) {
        Log.i(TAG, "Channel created. Channel Id:" + channelId + ".");

    }

    @Override
    protected void onChannelUpdated(@NonNull Context context, @NonNull String channelId) {
        Log.i(TAG, "Channel updated. Channel Id:" + channelId + ".");

    }

    @Override
    protected void onChannelRegistrationFailed(@NonNull Context context) {
        Log.i(TAG, "Channel registration failed.");
    }

    @Override
    protected void onPushReceived(@NonNull Context context, @NonNull PushMessage message, boolean notificationPosted) {
        message.getPublicNotificationPayload();
        Bundle bundle = message.getPushBundle();
        message.getMetadata();

        Log.i(TAG,
                "Received push message. Alert: " + message.getAlert() + ". posted notification: " + notificationPosted);
    }

    @Override
    protected void onNotificationPosted(@NonNull Context context, @NonNull NotificationInfo notificationInfo) {

        Log.i(TAG, "Notification posted. Alert: " + notificationInfo.getMessage().getAlert() + ". NotificationId: "
                + notificationInfo.getNotificationId());

    }

    @Override
    protected boolean onNotificationOpened(@NonNull Context context, @NonNull NotificationInfo notificationInfo) {
        Log.i(TAG, "Notification opened. Alert: " + notificationInfo.getMessage().getAlert() + ". NotificationId: "
                + notificationInfo.getNotificationId());
        Intent homeIntent = new HomeIntent(context);
        homeIntent.putExtra("screenFromPush", notificationInfo.getMessage().getAlert());
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(homeIntent);
        return true;
    }

    @Override
    protected boolean onNotificationOpened(@NonNull Context context, @NonNull NotificationInfo notificationInfo,
            @NonNull ActionButtonInfo actionButtonInfo) {
        Log.i(TAG,
                "Notification action button opened. Button ID: " + actionButtonInfo.getButtonId() + ". NotificationId: "
                        + notificationInfo.getNotificationId());
        return false;
    }

    @Override
    protected void onNotificationDismissed(@NonNull Context context, @NonNull NotificationInfo notificationInfo) {
        Log.i(TAG, "Notification dismissed. Alert: " + notificationInfo.getMessage().getAlert() + ". Notification ID: "
                + notificationInfo.getNotificationId());
    }

}

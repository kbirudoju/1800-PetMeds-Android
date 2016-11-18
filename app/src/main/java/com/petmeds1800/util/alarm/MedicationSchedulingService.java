package com.petmeds1800.util.alarm;

import com.petmeds1800.R;
import com.petmeds1800.intent.HomeIntent;
import com.petmeds1800.util.Constants;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * This {@code IntentService} does the app's actual work. {@code SampleAlarmReceiver} (a {@code
 * WakefulBroadcastReceiver}) holds a partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the wake lock.
 */
public class MedicationSchedulingService extends IntentService {

    public MedicationSchedulingService() {
        super("SchedulingService");
    }

    private NotificationManager mNotificationManager;

    NotificationCompat.Builder builder;

    private final int TYPE_MEDICATION_REMINDER__ALERT = 1;

    @Override
    protected void onHandleIntent(Intent intent) {
        sendNotification(intent.getIntExtra(MedicationAlarmReceiver.ALARM_ID, 0),
                intent.getStringExtra(MedicationAlarmReceiver.NOTIFICATION_MESSAGE),
                intent.getStringExtra(MedicationAlarmReceiver.REMINDER_ID));
        MedicationAlarmReceiver.completeWakefulIntent(intent);
    }

    // Post a notification indicating whether a doodle was found.
    private void sendNotification(int notificationId, String notificationMessage, String reminderId) {
        Intent homeIntent = new HomeIntent(getApplicationContext());
        homeIntent.putExtra(Constants.PUSH_SCREEN_TYPE, TYPE_MEDICATION_REMINDER__ALERT);
        homeIntent.putExtra(Constants.PUSH_EXTRA_ID, reminderId);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, notificationId,
                homeIntent,  0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.application_name))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(notificationMessage + "" + notificationId))
                        .setContentText(notificationMessage + "" + notificationId);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

}

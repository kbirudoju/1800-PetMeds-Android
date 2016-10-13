package com.petmeds1800.util.alarm;

import com.petmeds1800.R;
import com.petmeds1800.ui.HomeActivity;

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
    @Override
    protected void onHandleIntent(Intent intent) {
        sendNotification("Test", intent.getIntExtra(MedicationAlarmReceiver.ALARM_ID, 0));
        MedicationAlarmReceiver.completeWakefulIntent(intent);
    }
    // Post a notification indicating whether a doodle was found.
    private void sendNotification(String msg, int notificationId) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, notificationId,
                new Intent(this, HomeActivity.class), 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_logout)
                        .setContentTitle(getString(R.string.medicationAlert))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg );
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

}

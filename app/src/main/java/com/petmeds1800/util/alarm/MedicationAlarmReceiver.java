package com.petmeds1800.util.alarm;

import com.petmeds1800.intent.AlarmIntent;
import com.petmeds1800.model.entities.MedicationReminderTest;
import com.petmeds1800.util.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent and then starts the IntentService
 * {@code MedicationSchedulingService} to do some work.
 */
public class MedicationAlarmReceiver extends WakefulBroadcastReceiver {

    public static final String ALARM_ID = "alarmId";

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;

    // The pending intent that is triggered when the alarm fires.
    ArrayList<PendingIntent> mPendingIntents = new ArrayList<PendingIntent>();

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, MedicationSchedulingService.class);
        service.putExtra(ALARM_ID, intent.getIntExtra(ALARM_ID, 0));
        startWakefulService(context, service);
    }

    public void setAlarm(Context context, Calendar calendar, PendingIntent alarmIntent) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1 * 60 * 1000,
                alarmIntent);
    }
    // END_INCLUDE(set_alarm)

    public void addMultipleAlarms(Context context, ArrayList<MedicationReminderTest> medicationReminderTests) {

//        String currentDayFormatter = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        long millis = Calendar.getInstance().getTimeInMillis();
        String cuurentDate = Utils.changeDateFormat(millis, "yyyy/MM/dd");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy/MM/dd h:mm a");
        inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy/MM/dd h:mm a");
        outputDateFormat.setTimeZone(TimeZone.getDefault());
        int alarmId = 1;
        for (MedicationReminderTest medicationReminderTest : medicationReminderTests) {
            //get the hour and minutes
            Date date = null;
            try {
                //We need to concatenate the
                String dateWithTime = cuurentDate + " " + medicationReminderTest.getReminderTime();
                date = inputDateFormat.parse(dateWithTime);
                String localTimeString = outputDateFormat.format(date);
                Date localDate = outputDateFormat.parse(localTimeString);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(localDate);
                PendingIntent alarmIntent = null;
                //get the day in week
                String[] days = medicationReminderTest.getDayOfWeek().split(",");
                //We need an id in order to keep reference of alarms getting added into the system
                for (String eachDay : days) {
                    if (eachDay.equalsIgnoreCase("SUNDAY")) {
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    } else if (eachDay.equalsIgnoreCase("MONDAY")) {
                        //create the pending intent
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    } else if (eachDay.equalsIgnoreCase("TUESDAY")) {
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                    } else if (eachDay.equalsIgnoreCase("WEDNESDAY")) {
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    } else if (eachDay.equalsIgnoreCase("THURSDAY")) {
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                    } else if (eachDay.equalsIgnoreCase("FRIDAY")) {
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    } else if (eachDay.equalsIgnoreCase("SATURDAY")) {
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    }
                    //TODO We need a ask backend for unique ID for a medication reminder
                    AlarmIntent intent = new AlarmIntent(context, MedicationAlarmReceiver.class);
                    intent.putExtra(ALARM_ID, medicationReminderTest.getReminderId());
                    alarmIntent = PendingIntent.getBroadcast(context, medicationReminderTest.getReminderId(), intent,
                            FLAG_UPDATE_CURRENT);
                    mPendingIntents.add(alarmIntent);
                    setAlarm(context, calendar, alarmIntent);
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    public void cancelAlarm(Context context, int reminderId) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmIntent intent = new AlarmIntent(context, MedicationAlarmReceiver.class);
        intent.putExtra(ALARM_ID, reminderId);
        PendingIntent addedIntent = PendingIntent
                .getBroadcast(context, reminderId, intent,
                        FLAG_UPDATE_CURRENT);
        alarmMgr.cancel(addedIntent);
    }

    /**
     * Cancels the alarm.
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void cancelAllAlarms(Context context, ArrayList<MedicationReminderTest> medicationReminderTests) {
        // If the alarm has been set, cancel it.
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MedicationAlarmReceiver.class);
        for (int i = 0; i < medicationReminderTests.size(); i++) {
            PendingIntent addedIntent = PendingIntent
                    .getBroadcast(context, medicationReminderTests.get(i).getReminderId(), intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.cancel(addedIntent);
        }
    }
    // END_INCLUDE(cancel_alarm)
}

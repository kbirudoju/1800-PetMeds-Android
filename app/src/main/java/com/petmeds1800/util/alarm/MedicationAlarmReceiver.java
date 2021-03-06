package com.petmeds1800.util.alarm;

import com.petmeds1800.intent.AlarmIntent;
import com.petmeds1800.model.entities.MedicationRemindersAlarmData;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent and then starts the IntentService
 * {@code MedicationSchedulingService} to do some work.
 */
public class MedicationAlarmReceiver extends WakefulBroadcastReceiver {

    public static final String ALARM_ID = "alarmId";

    public static final String NOTIFICATION_MESSAGE = "notificationMessage";

    public static final String REMINDER_ID = "reminderId";

    public static final String START_TIME = "startTime";


    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;

    private final int MONTHLY_INTERVAL = 30;

    private final int DAILY_INTERVAL = 1;

    private final int WEEKLY_INTERVAL = 7;

    private final int INCREMENT_FACTOR = 10;

    private final int ZERO_INDEX = 0;


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, MedicationSchedulingService.class);
        service.putExtra(ALARM_ID, intent.getIntExtra(ALARM_ID, ZERO_INDEX));
        service.putExtra(REMINDER_ID, intent.getStringExtra(REMINDER_ID));
        service.putExtra(NOTIFICATION_MESSAGE, intent.getStringExtra(NOTIFICATION_MESSAGE));
        service.putExtra(START_TIME, intent.getLongExtra(START_TIME, ZERO_INDEX));
        startWakefulService(context, service);
    }

    public void setAlarm(Context context, Calendar calendar, int reminderTypeValue,
            int reminderFrequency, int reminderId, String notificationMessage, String medicationReminderId) {
        AlarmIntent intent = new AlarmIntent(context, MedicationAlarmReceiver.class);
        intent.putExtra(ALARM_ID, reminderId);
        intent.putExtra(NOTIFICATION_MESSAGE, notificationMessage);
        intent.putExtra(REMINDER_ID, medicationReminderId);
        intent.putExtra(START_TIME, calendar.getTimeInMillis());
        PendingIntent alarmIntent = PendingIntent
                .getBroadcast(context, reminderId, intent,
                        FLAG_UPDATE_CURRENT);
        long interval = AlarmManager.INTERVAL_DAY * getInterval(reminderTypeValue) * reminderFrequency;
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval,
                alarmIntent);


    }

    public long getInterval(int value) {
        switch (value) {
            case 0:
                return DAILY_INTERVAL;
            case 1:
                return WEEKLY_INTERVAL;
            case 2:
                return MONTHLY_INTERVAL;
        }
        return 0;
    }

    // END_INCLUDE(set_alarm)

    public void addMultipleAlarms(Context context,
            ArrayList<MedicationRemindersAlarmData> medicationRemindersAlarmDatas) {

//        String currentDayFormatter = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        long millis = now.getTimeInMillis();
        String currentDate = Utils.changeDateFormat(millis, "yyyy/MM/dd");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy/MM/dd h:mm a", Locale.ENGLISH);
        currentDate = setCurrentdate(medicationRemindersAlarmDatas, year, currentDate);
        {
            for (MedicationRemindersAlarmData medicationRemindersAlarmData : medicationRemindersAlarmDatas) {
                //get the hour and minutes
                Date date = null;
                try {
                    //We need to concatenate the
                    String dateWithTime = currentDate + " " + medicationRemindersAlarmData.getReminderTime();
                    date = inputDateFormat.parse(dateWithTime);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.get(Calendar.HOUR_OF_DAY);
                    calendar.get(Calendar.MINUTE);
                    //get the day in week
                    ArrayList<String> days = medicationRemindersAlarmData.getDaysOfWeek();
                    //We need an id in order to keep reference of alarms getting added into the system
                    if (days != null && days.size() > 0) {
                        int reminderIdIncremnetFactor = DAILY_INTERVAL;
                        for (String eachDay : days) {
                            setDayOfweek(calendar, eachDay);
                            //TODO We need a ask backend for unique ID for a medication reminder
                            setAlarm(context, calendar, medicationRemindersAlarmData.getRepeatFrequency().ordinal(),
                                    medicationRemindersAlarmData.getRepeatValue(),
                                    medicationRemindersAlarmData.getReminderId() * INCREMENT_FACTOR
                                            + reminderIdIncremnetFactor,
                                    medicationRemindersAlarmData.getNotificationMessage(),
                                    medicationRemindersAlarmData.getReminderId() + "");
                            reminderIdIncremnetFactor++;
                        }
                    } else {
                        setAlarm(context, calendar, medicationRemindersAlarmData.getRepeatFrequency().ordinal(),
                                medicationRemindersAlarmData.getRepeatValue(),
                                medicationRemindersAlarmData.getReminderId(),
                                medicationRemindersAlarmData.getNotificationMessage(),
                                medicationRemindersAlarmData.getReminderId() + "");
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void setDayOfweek(Calendar calendar, String eachDay) {
        switch (eachDay) {
            case "SUN":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
            case "MON":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                break;
            case "TUE":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                break;
            case "WED":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                break;
            case "THU":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                break;
            case "FRI":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                break;
            case "SAT":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                break;

        }
    }

    private String setCurrentdate(ArrayList<MedicationRemindersAlarmData> medicationRemindersAlarmDatas, int year,
            String currentDate) {
        if (medicationRemindersAlarmDatas != null && medicationRemindersAlarmDatas.size() > 0
                && medicationRemindersAlarmDatas
                .get(ZERO_INDEX).getRepeatFrequency() == Constants.RepeatFrequency.REPEAT_MONTHLY) {
            Calendar calendar = Calendar.getInstance();
            try {
                DateFormat formatter = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
                Date date = formatter.parse(medicationRemindersAlarmDatas
                        .get(ZERO_INDEX).getStartdate());
                calendar.setTime(date);
                calendar.set(Calendar.YEAR, year);
                currentDate = Utils.changeDateFormat(calendar.getTimeInMillis(), "yyyy/MM/dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return currentDate;
    }

    public void cancelAlarm(Context context, int reminderId, String notificationMessage, String medicationReminderId) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmIntent intent = new AlarmIntent(context, MedicationAlarmReceiver.class);
        intent.putExtra(ALARM_ID, reminderId);
        intent.putExtra(REMINDER_ID, medicationReminderId);
        intent.putExtra(NOTIFICATION_MESSAGE, notificationMessage);
        PendingIntent addedIntent = PendingIntent
                .getBroadcast(context, reminderId, intent,
                        FLAG_UPDATE_CURRENT);
        alarmMgr.cancel(addedIntent);
    }

    /**
     * Cancels the alarm.
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void cancelAllAlarms(Context context,
            ArrayList<MedicationRemindersAlarmData> medicationRemindersAlarmDatas) {
        // If the alarm has been set, cancel it.
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MedicationAlarmReceiver.class);
        for (int i = 0; i < medicationRemindersAlarmDatas.size(); i++) {
            PendingIntent addedIntent = PendingIntent
                    .getBroadcast(context, medicationRemindersAlarmDatas.get(i).getReminderId(), intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.cancel(addedIntent);
        }
    }
    // END_INCLUDE(cancel_alarm)
}

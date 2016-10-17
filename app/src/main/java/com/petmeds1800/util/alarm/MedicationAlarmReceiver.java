package com.petmeds1800.util.alarm;

import com.petmeds1800.intent.AlarmIntent;
import com.petmeds1800.model.entities.MedicationReminderTest;
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

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent and then starts the IntentService
 * {@code MedicationSchedulingService} to do some work.
 */
public class MedicationAlarmReceiver extends WakefulBroadcastReceiver {

    public static final String ALARM_ID = "alarmId";

    public static final String NOTIFICATION_MESSAGE = "notificationMessage";

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
        service.putExtra(ALARM_ID, intent.getIntExtra(ALARM_ID, 0));
        service.putExtra(NOTIFICATION_MESSAGE, intent.getStringExtra(NOTIFICATION_MESSAGE));
        startWakefulService(context, service);
    }

    public void setAlarm(Context context, Calendar calendar, int reminderTypeValue,
            int reminderFrequency, int reminderId, String notificationMessage) {
        AlarmIntent intent = new AlarmIntent(context, MedicationAlarmReceiver.class);
        intent.putExtra(ALARM_ID, reminderId);
        intent.putExtra(NOTIFICATION_MESSAGE, notificationMessage);
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

    public void addMultipleAlarms(Context context, ArrayList<MedicationReminderTest> medicationReminderTests) {

//        String currentDayFormatter = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        long millis = now.getTimeInMillis();
        String currentDate = Utils.changeDateFormat(millis, "yyyy/MM/dd");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy/MM/dd h:mm a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        if (medicationReminderTests != null && medicationReminderTests.size() > 0 && medicationReminderTests
                .get(ZERO_INDEX).getRepeatFrequency() == Constants.RepeatFrequency.REPEAT_MONTHLY) {
            Calendar calendar = Calendar.getInstance();
            try {
                DateFormat formatter = new SimpleDateFormat("MMM dd");
                Date date = formatter.parse(medicationReminderTests
                        .get(ZERO_INDEX).getStartdate());
                calendar.setTime(date);
                calendar.set(Calendar.YEAR, year);
                currentDate = Utils.changeDateFormat(calendar.getTimeInMillis(), "yyyy/MM/dd");

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        {
            for (MedicationReminderTest medicationReminderTest : medicationReminderTests) {
                //get the hour and minutes
                Date date = null;
                try {
                    //We need to concatenate the
                    String dateWithTime = currentDate + " " + medicationReminderTest.getReminderTime();
                    date = inputDateFormat.parse(dateWithTime);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.get(Calendar.HOUR_OF_DAY);
                    calendar.get(Calendar.MINUTE);
                    //get the day in week
                    ArrayList<String> days = medicationReminderTest.getDaysOfWeek();
                    //We need an id in order to keep reference of alarms getting added into the system
                    if (days != null && days.size() > 0) {
                        int reminderIdIncremnetFactor = DAILY_INTERVAL;
                        for (String eachDay : days) {

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
                            Date date11 = calendar.getTime();
                            //TODO We need a ask backend for unique ID for a medication reminder
                            setAlarm(context, calendar, medicationReminderTest.getRepeatFrequency().ordinal(),
                                    medicationReminderTest.getRepeatValue(),
                                    medicationReminderTest.getReminderId() * INCREMENT_FACTOR
                                            + reminderIdIncremnetFactor,
                                    medicationReminderTest.getNotificationMessage());
                            reminderIdIncremnetFactor++;
                        }
                    } else {
                        setAlarm(context, calendar, medicationReminderTest.getRepeatFrequency().ordinal(),
                                medicationReminderTest.getRepeatValue(), medicationReminderTest.getReminderId(),
                                medicationReminderTest.getNotificationMessage());
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void cancelAlarm(Context context, int reminderId, String notificationMessage) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmIntent intent = new AlarmIntent(context, MedicationAlarmReceiver.class);
        intent.putExtra(ALARM_ID, reminderId);
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

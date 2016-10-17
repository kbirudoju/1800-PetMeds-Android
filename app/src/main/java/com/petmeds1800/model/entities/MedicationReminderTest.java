package com.petmeds1800.model.entities;

import com.petmeds1800.util.Constants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Abhinav on 8/9/16.
 */
public class MedicationReminderTest implements Serializable {

    private String reminderTime;
    private ArrayList<String> daysOfWeek;
    private int reminderId;
    private Constants.RepeatFrequency mRepeatFrequency;
    private int mRepeatValue;
    private String notificationMessage;
    private String startdate;
    public MedicationReminderTest(String notificationMessage, int repeatValue,
            Constants.RepeatFrequency repeatFrequency, int reminderId, ArrayList<String> daysOfWeek,
            String reminderTime, String startdate) {
        this.notificationMessage = notificationMessage;
        mRepeatValue = repeatValue;
        mRepeatFrequency = repeatFrequency;
        this.reminderId = reminderId;
        this.daysOfWeek = daysOfWeek;
        this.reminderTime = reminderTime;
        this.startdate = startdate;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }
    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public Constants.RepeatFrequency getRepeatFrequency() {
        return mRepeatFrequency;
    }

    public void setRepeatFrequency(Constants.RepeatFrequency repeatFrequency) {
        mRepeatFrequency = repeatFrequency;
    }

    public ArrayList<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(ArrayList<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public int getRepeatValue() {
        return mRepeatValue;
    }

    public void setRepeatValue(int repeatValue) {
        mRepeatValue = repeatValue;
    }
}

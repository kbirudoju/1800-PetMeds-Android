package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 9/7/2016.
 */
public class MedicationReminder implements Serializable{
    private String reminderTimeZone;

    public String getReminderTimeZone() {
        return reminderTimeZone;
    }

    public void setReminderTimeZone(String reminderTimeZone) {
        this.reminderTimeZone = reminderTimeZone;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    private String reminderTime;
    private String dayOfWeek;
}

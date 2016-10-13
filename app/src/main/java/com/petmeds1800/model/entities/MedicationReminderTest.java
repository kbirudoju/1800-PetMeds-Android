package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by Abhinav on 8/9/16.
 */
public class MedicationReminderTest implements Serializable {

    private String reminderTime;

    private String dayOfWeek;

    private int reminderId;

    public MedicationReminderTest(String reminderTime, String dayOfWeek, int reminderId) {
        this.reminderTime = reminderTime;
        this.dayOfWeek = dayOfWeek;
        this.reminderId = reminderId;
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

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}

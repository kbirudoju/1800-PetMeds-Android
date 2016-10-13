package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by Sdixit on 13-10-2016.
 */

public class MedicationReminderItem implements Serializable {

    private String startDate;

    private String reminderType;

    private String timeZone;

    private String petName;

    private String daysOfWeek;

    private String timeHourMin;

    private String repeatInterval;

    private String shortDescription;

    private String scheduleDescription;

    private String reminderName;

    private String userId;

    private String reminderId;

    public String getStartDate() {
        return startDate;
    }

    public String getDate() {
        return (startDate != null) ? startDate.split(" ")[1] : null;
    }

    public String getMonth() {
        return (startDate != null) ? startDate.split(" ")[0] : null;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Object getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getTimeHourMin() {
        return timeHourMin;
    }

    public void setTimeHourMin(String timeHourMin) {
        this.timeHourMin = timeHourMin;
    }

    public String getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(String repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getScheduleDescription() {
        return scheduleDescription;
    }

    public void setScheduleDescription(String scheduleDescription) {
        this.scheduleDescription = scheduleDescription;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }
}

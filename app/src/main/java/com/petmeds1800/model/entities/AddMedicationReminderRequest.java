package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sdixit on 14-10-2016.
 */

public class AddMedicationReminderRequest implements Serializable {

    private String reminderName;

    private String shortDescription;

    private String reminderType;

    private String timeHourMin;

    private String petName;

    private String repeatInterval;

    private ArrayList<String> daysOfWeek;

    private String startDate;

    private Boolean disableReminder;

    private String reminderId;

    @SerializedName("_dynSessConf")
    private String dynSessConf;

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public String getTimeHourMin() {
        return timeHourMin;
    }

    public void setTimeHourMin(String timeHourMin) {
        this.timeHourMin = timeHourMin;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(String repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public ArrayList<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(ArrayList<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Boolean getDisableReminder() {
        return disableReminder;
    }

    public void setDisableReminder(Boolean disableReminder) {
        this.disableReminder = disableReminder;
    }

    public String getDynSessConf() {
        return dynSessConf;
    }

    public void setDynSessConf(String dynSessConf) {
        this.dynSessConf = dynSessConf;
    }

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }
}

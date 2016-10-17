package com.petmeds1800.model;

import com.petmeds1800.util.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sarthak on 10/14/2016.
 */

public class ReminderDialogData implements Serializable {

    private boolean isActive;

    private int mRepeatValue;

    private Date mStartDate;

    private Constants.RepeatFrequency repeatFrequency;

    private ArrayList<String> dayOfWeeks;

    public boolean isActive() {
        return isActive;
    }
    public boolean toggleValues[];

    public boolean[] getToggleValues() {
        return toggleValues;
    }

    public void setToggleValues(boolean[] toggleValues) {
        this.toggleValues = toggleValues;
    }

    public ReminderDialogData(boolean isActive, int mRepeatValue, Date mStartDate,
            Constants.RepeatFrequency repeatFrequency, ArrayList<String> dayOfWeeks) {
        this.isActive = isActive;
        this.mRepeatValue = mRepeatValue;
        this.mStartDate = mStartDate;
        this.repeatFrequency = repeatFrequency;
        this.dayOfWeeks = dayOfWeeks;
    }

    public ArrayList<String> getDayOfWeeks() {
        return dayOfWeeks;
    }

    public void setDayOfWeeks(ArrayList<String> dayOfWeeks) {
        this.dayOfWeeks = dayOfWeeks;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getmRepeatValue() {
        return mRepeatValue;
    }

    public void setmRepeatValue(int mRepeatValue) {
        this.mRepeatValue = mRepeatValue;
    }

    public Date getmStartDate() {
        return mStartDate;
    }

    public void setmStartDate(Date mStartDate) {
        this.mStartDate = mStartDate;
    }

    public Constants.RepeatFrequency getRepeatFrequency() {
        return repeatFrequency;
    }

    public void setRepeatFrequency(Constants.RepeatFrequency repeatFrequency) {
        this.repeatFrequency = repeatFrequency;
    }
}

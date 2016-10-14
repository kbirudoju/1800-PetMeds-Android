package com.petmeds1800.model;

import com.petmeds1800.util.Constants;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sarthak on 10/14/2016.
 */

public class ReminderDialogData implements Serializable {

    private boolean isActive;
    private int mRepeatValue;
    private Date mStartDate;
    private Constants.RepeatFrequency repeatFrequency;

    public boolean isActive() {
        return isActive;
    }

    public ReminderDialogData(boolean isActive, int mRepeatValue, Date mStartDate, Constants.RepeatFrequency repeatFrequency) {
        this.isActive = isActive;
        this.mRepeatValue = mRepeatValue;
        this.mStartDate = mStartDate;
        this.repeatFrequency = repeatFrequency;
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

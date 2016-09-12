package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 9/7/2016.
 */
public class EasyRefillReminder implements Serializable {
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<ReminderList> getEasyRefillReminder() {
        return easyRefillReminder;
    }

    public void setEasyRefillReminder(ArrayList<ReminderList> easyRefillReminder) {
        this.easyRefillReminder = easyRefillReminder;
    }

    private ArrayList<ReminderList> easyRefillReminder;
}

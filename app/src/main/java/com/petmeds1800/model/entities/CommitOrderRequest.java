package com.petmeds1800.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Sdixit on 07-10-2016.
 */

public class CommitOrderRequest {

    @SerializedName("_dynSessConf")
    private String _dynSessConf;

    private ArrayList<String> commerceItemIds;

    private ArrayList<Integer> reminderMonths;

    public ArrayList<Integer> getReminderMonths() {
        return reminderMonths;
    }

    public void setReminderMonths(ArrayList<Integer> reminderMonths) {
        this.reminderMonths = reminderMonths;
    }

    public ArrayList<String> getCommerceItemIds() {
        return commerceItemIds;
    }

    public void setCommerceItemIds(ArrayList<String> commerceItemIds) {
        this.commerceItemIds = commerceItemIds;
    }

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }
}

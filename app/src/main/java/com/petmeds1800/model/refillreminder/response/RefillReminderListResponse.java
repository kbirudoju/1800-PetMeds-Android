package com.petmeds1800.model.refillreminder.response;

import com.petmeds1800.model.shoppingcart.response.Status;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sarthak on 21-Oct-16.
 */

public class RefillReminderListResponse implements Serializable {

    private Status status;

    private ArrayList<EasyRefillReminder> easyRefillReminder;

    public Status getStatus ()
    {
        return status;
    }

    public void setStatus (Status status)
    {
        this.status = status;
    }

    public ArrayList<EasyRefillReminder> getEasyRefillReminder ()
    {
        return easyRefillReminder;
    }

    public void setEasyRefillReminder (ArrayList<EasyRefillReminder> easyRefillReminder)
    {
        this.easyRefillReminder = easyRefillReminder;
    }
}

package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 9/7/2016.
 */
public class RefillReminder implements Serializable{
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    private int month;
}

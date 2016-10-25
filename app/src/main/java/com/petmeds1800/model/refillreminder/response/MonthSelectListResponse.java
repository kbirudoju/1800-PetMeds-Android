package com.petmeds1800.model.refillreminder.response;

import com.petmeds1800.model.shoppingcart.response.Status;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sarthak on 24-Oct-16.
 */

public class MonthSelectListResponse implements Serializable {

    private Status status;

    private ArrayList<MonthList> monthList;

    public Status getStatus ()
    {
        return status;
    }

    public void setStatus (Status status)
    {
        this.status = status;
    }

    public ArrayList<MonthList> getMonthList ()
    {
        return monthList;
    }

    public void setMonthList (ArrayList<MonthList> monthList)
    {
        this.monthList = monthList;
    }
}

package com.petmeds1800.model.refillreminder.response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sarthak on 21-Oct-16.
 */

public class EasyRefillReminder implements Serializable {

    private String submittedDate;

    private String lastModifiedDate;

    private ArrayList<OrderItems> orderItems;

    private String orderId;

    public String getSubmittedDate ()
    {
        return submittedDate;
    }

    public void setSubmittedDate (String submittedDate)
    {
        this.submittedDate = submittedDate;
    }

    public String getLastModifiedDate ()
    {
        return lastModifiedDate;
    }

    public void setLastModifiedDate (String lastModifiedDate)
    {
        this.lastModifiedDate = lastModifiedDate;
    }

    public ArrayList<OrderItems> getOrderItems ()
    {
        return orderItems;
    }

    public void setOrderItems (ArrayList<OrderItems> orderItems)
    {
        this.orderItems = orderItems;
    }

    public String getOrderId ()
    {
        return orderId;
    }

    public void setOrderId (String orderId)
    {
        this.orderId = orderId;
    }
}

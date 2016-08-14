package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 8/17/2016.
 */
public class OrderHistoryFilter implements Serializable{
    private Status status;

    public ArrayList<OrderFilterList> getOrderFilterList() {
        return filterList;
    }

    public void setOrderFilterList(ArrayList<OrderFilterList> orderFilterList) {
        this.filterList = orderFilterList;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private ArrayList<OrderFilterList> filterList;
}

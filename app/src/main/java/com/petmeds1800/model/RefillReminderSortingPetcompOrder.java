package com.petmeds1800.model;

import com.petmeds1800.model.refillreminder.response.EasyRefillReminder;
import com.petmeds1800.model.refillreminder.response.OrderItems;

import java.util.ArrayList;

/**
 * Created by Sarthak on 21-Oct-16.
 */

public class RefillReminderSortingPetcompOrder {

    private OrderItems listed_orderItem;
    private EasyRefillReminder listed_easyRefillReminder;

    public OrderItems getListed_orderItem() {
        return listed_orderItem;
    }

    public void setListed_orderItem(OrderItems listed_orderItem) {
        this.listed_orderItem = listed_orderItem;
    }

    public EasyRefillReminder getListed_easyRefillReminder() {
        return listed_easyRefillReminder;
    }

    public void setListed_easyRefillReminder(EasyRefillReminder listed_easyRefillReminder) {
        this.listed_easyRefillReminder = listed_easyRefillReminder;
    }

    public RefillReminderSortingPetcompOrder(OrderItems listed_orderItem, EasyRefillReminder listed_easyRefillReminder) {
        this.listed_orderItem = listed_orderItem;
        this.listed_easyRefillReminder = listed_easyRefillReminder;
    }
}

package com.petmeds1800.model.refillreminder.request;

import java.io.Serializable;

/**
 * Created by Sarthak on 24-Oct-16.
 */

public class UpdateRefillReminderRequest implements Serializable {

    private String _dynSessConf;
    private String orderId;
    private String itemId;
    private String reminderMonth;

    public UpdateRefillReminderRequest(String _dynSessConf, String orderId, String itemId, String reminderMonth) {
        this._dynSessConf = _dynSessConf;
        this.orderId = orderId;
        this.itemId = itemId;
        this.reminderMonth = reminderMonth;
    }

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getReminderMonth() {
        return reminderMonth;
    }

    public void setReminderMonth(String reminderMonth) {
        this.reminderMonth = reminderMonth;
    }
}

package com.petmeds1800.model;

/**
 * Created by pooja on 10/5/2016.
 */
public class ReOrderRequest {
    private String orderId;
    private String _dynSessConf;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }

    public ReOrderRequest(String orderId, String _dynSessConf) {
        this.orderId = orderId;
        this._dynSessConf = _dynSessConf;
    }
}

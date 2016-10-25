package com.petmeds1800.model;

/**
 * Created by pooja on 10/25/2016.
 */
public class PayPalCheckoutRequest {

    public PayPalCheckoutRequest(String source, String _dynSessConf) {
        this.source = source;
        this._dynSessConf = _dynSessConf;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }

    private String source;
    private String _dynSessConf;
}

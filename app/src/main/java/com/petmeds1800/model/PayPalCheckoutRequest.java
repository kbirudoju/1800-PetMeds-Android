package com.petmeds1800.model;

/**
 * Created by pooja on 10/25/2016.
 */
public class PayPalCheckoutRequest {

    public PayPalCheckoutRequest(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    private String source;
    private String _dynSessConf;
}

package com.petmeds1800.model.shoppingcart.request;

import java.io.Serializable;

/**
 * Created by Sarthak on 9/27/2016.
 */

public class ApplyCouponRequestShoppingCart implements Serializable {

    String couponClaimCode;
    String _dynSessConf;

    public ApplyCouponRequestShoppingCart(String couponClaimCode, String _dynSessConf) {
        this.couponClaimCode = couponClaimCode;
        this._dynSessConf = _dynSessConf;
    }

    public String getCouponClaimCode() {
        return couponClaimCode;
    }

    public void setCouponClaimCode(String couponClaimCode) {
        this.couponClaimCode = couponClaimCode;
    }

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }
}

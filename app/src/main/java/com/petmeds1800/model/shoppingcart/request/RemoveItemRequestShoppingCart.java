package com.petmeds1800.model.shoppingcart.request;

import java.io.Serializable;

/**
 * Created by Sarthak on 9/27/2016.
 */

public class RemoveItemRequestShoppingCart implements Serializable {

    String commerceItemId;
    String _dynSessConf;

    public RemoveItemRequestShoppingCart(String commerceItemId, String _dynSessConf) {
        this.commerceItemId = commerceItemId;
        this._dynSessConf = _dynSessConf;
    }

    public String getCommerceItemId() {
        return commerceItemId;
    }

    public void setCommerceItemId(String commerceItemId) {
        this.commerceItemId = commerceItemId;
    }

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }
}

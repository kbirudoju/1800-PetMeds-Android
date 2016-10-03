package com.petmeds1800.model.shoppingcart.request;

import java.io.Serializable;

/**
 * Created by Sarthak on 9/27/2016.
 */

public class AddItemRequestShoppingCart implements Serializable {

    String skuId;
    String productId;
    int quantity;
    String _dynSessConf;

    public AddItemRequestShoppingCart(String skuId, String productId, int quantity, String _dynSessConf) {
        this.skuId = skuId;
        this.productId = productId;
        this.quantity = quantity;
        this._dynSessConf = _dynSessConf;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }
}

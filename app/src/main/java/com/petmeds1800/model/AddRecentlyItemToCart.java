package com.petmeds1800.model;

/**
 * Created by pooja on 1/27/2017.
 */
public class AddRecentlyItemToCart {


    private String skuId;
    private String productId;
    private String purchaseItemId;
    private String _dynSessConf;

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

    public String getPurchaseItemId() {
        return purchaseItemId;
    }

    public void setPurchaseItemId(String purchaseItemId) {
        this.purchaseItemId = purchaseItemId;
    }

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }

    public AddRecentlyItemToCart(String skuId, String productId, String purchaseItemId, String dynSessConf) {
        this.skuId = skuId;
        this.productId = productId;
        this.purchaseItemId = purchaseItemId;
        this._dynSessConf = dynSessConf;
    }

}

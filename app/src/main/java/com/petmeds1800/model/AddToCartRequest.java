package com.petmeds1800.model;

/**
 * Created by pooja on 10/7/2016.
 */
public class AddToCartRequest {

   private String skuId;
    private String productId;
    private int quantity;
    private String _dynSessConf;

    public AddToCartRequest(String skuId, String productId, int quantity, String dynSessConf) {
        this.skuId = skuId;
        this.productId = productId;
        this.quantity = quantity;
        this._dynSessConf = dynSessConf;
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

    public String getDynSessConf() {
        return _dynSessConf;
    }

    public void setDynSessConf(String dynSessConf) {
        this._dynSessConf = dynSessConf;
    }
}


package com.petmeds1800.model.shoppingcart.response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sarthak on 9/23/2016.
 */

public class ShoppingCart implements Serializable {

    private ArrayList<PaymentGroups> paymentGroups;
    private float subTotal;
    private String userId;
    private ArrayList<ShippingGroups> shippingGroups;
    private String shoppingCartId;
    private String shippingAddressId;
    private String coupon;
    private ArrayList<CommerceItems> commerceItems;
    private float discountAmount;
    private float itemsTotal;
    private String totalCommerceItemCount;
    private String paymentCardKey;

    public String getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(String shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public ArrayList<PaymentGroups> getPaymentGroups() {
        return paymentGroups;
    }

    public void setPaymentGroups(ArrayList<PaymentGroups> paymentGroups) {
        this.paymentGroups = paymentGroups;
    }

    public ArrayList<CommerceItems> getCommerceItems() {
        return commerceItems;
    }

    public void setCommerceItems(ArrayList<CommerceItems> commerceItems) {
        this.commerceItems = commerceItems;
    }

    public ArrayList<ShippingGroups> getShippingGroups() {
        return shippingGroups;
    }

    public void setShippingGroups(ArrayList<ShippingGroups> shippingGroups) {
        this.shippingGroups = shippingGroups;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(String shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public float getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(float discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTotalCommerceItemCount() {
        return totalCommerceItemCount;
    }

    public void setTotalCommerceItemCount(String totalCommerceItemCount) {
        this.totalCommerceItemCount = totalCommerceItemCount;
    }

    public float getItemsTotal() {
        return itemsTotal;
    }

    public void setItemsTotal(float itemsTotal) {
        this.itemsTotal = itemsTotal;
    }

    public String getPaymentCardKey() {
        return paymentCardKey;
    }

    public void setPaymentCardKey(String paymentCardKey) {
        this.paymentCardKey = paymentCardKey;
    }
}

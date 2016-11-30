package com.petmeds1800.model.entities;

import com.petmeds1800.model.shoppingcart.response.ShippingAddress;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sdixit on 29-09-2016.
 */

public class Order implements Serializable {

    private float orderSubTotal;

    private float orderTotal;

    private ArrayList<Item> items;

    private float taxTotal;

    private float shippingTotal;

    private ShippingReviewMethod shippingMethod;

    private ShippingAddress shippingAddress;

    private PaymentMethod paymentMethod;

    private String orderId;

    private float discount;

    public float getOrderSubTotal() {
        return orderSubTotal;
    }

    public void setOrderSubTotal(float orderSubTotal) {
        this.orderSubTotal = orderSubTotal;
    }

    public float getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(float orderTotal) {
        this.orderTotal = orderTotal;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public float getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(float taxTotal) {
        this.taxTotal = taxTotal;
    }

    public float getShippingTotal() {
        return shippingTotal;
    }

    public void setShippingTotal(float shippingTotal) {
        this.shippingTotal = shippingTotal;
    }

    public ShippingReviewMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingReviewMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }
}

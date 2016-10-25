package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 8/12/2016.
 */
public class OrderList implements Serializable{
    private double total;
    private String status;
    private String displayOrderId;
    private String submittedDate;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsCancellable() {
        return isCancellable;
    }

    public void setIsCancellable(String isCancellable) {
        this.isCancellable = isCancellable;
    }

    private String isCancellable;

    public ArrayList<CommerceItems> getCommerceItems() {
        return commerceItems;
    }

    public void setCommerceItems(ArrayList<CommerceItems> commerceItems) {
        this.commerceItems = commerceItems;
    }

    private ArrayList<CommerceItems> commerceItems;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisplayOrderId() {
        return displayOrderId;
    }

    public void setDisplayOrderId(String displayOrderId) {
        this.displayOrderId = displayOrderId;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getShipTo() {
        return shipTo;
    }

    public void setShipTo(String shipTo) {
        this.shipTo = shipTo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private String shipTo;
    private String orderId;
    private ArrayList<ShippingGroup> shippingGroups;

    public ArrayList<PaymentGroup> getPaymentGroups() {
        return paymentGroups;
    }

    public void setPaymentGroups(ArrayList<PaymentGroup> paymentGroups) {
        this.paymentGroups = paymentGroups;
    }

    public ArrayList<ShippingGroup> getShippingGroups() {
        return shippingGroups;
    }

    public void setShippingGroups(ArrayList<ShippingGroup> shippingGroups) {
        this.shippingGroups = shippingGroups;
    }

    private ArrayList<PaymentGroup> paymentGroups;


}

package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sdixit on 10-10-2016.
 */

public class CommitOrder implements Serializable {

    private double orderSubTotal;
    private double orderTotal;
    private Boolean guestUserPassword;
    private List<Item> items;
    private String email;
    private double taxTotal;
    private double shippingTotal;
    private String shippingMethod;
    private String firstName;
    private String orderId;
    private int discount;

    public double getOrderSubTotal() {
        return orderSubTotal;
    }

    public void setOrderSubTotal(double orderSubTotal) {
        this.orderSubTotal = orderSubTotal;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Boolean getGuestUserPassword() {
        return guestUserPassword;
    }

    public void setGuestUserPassword(Boolean guestUserPassword) {
        this.guestUserPassword = guestUserPassword;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public double getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(double taxTotal) {
        this.taxTotal = taxTotal;
    }
    public double getShippingTotal() {
        return shippingTotal;
    }
    public void setShippingTotal(double shippingTotal) {
        this.shippingTotal = shippingTotal;
    }
    public String getShippingMethod() {
        return shippingMethod;
    }
    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public int getDiscount() {
        return discount;
    }
    public void setDiscount(int discount) {
        this.discount = discount;
    }
}

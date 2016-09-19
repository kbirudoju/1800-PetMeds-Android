package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 9/13/2016.
 */
public class BrowsingHistory  implements Serializable{
    private ArrayList<Sku> skus;

    public String getWidgetTitle() {
        return widgetTitle;
    }

    public void setWidgetTitle(String widgetTitle) {
        this.widgetTitle = widgetTitle;
    }

    private String widgetTitle;

    public ArrayList<Products> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Products> products) {
        this.products = products;
    }

    public ArrayList<Sku> getSkus() {
        return skus;
    }

    public void setSkus(ArrayList<Sku> skus) {
        this.skus = skus;
    }

    private ArrayList<Products> products;
}

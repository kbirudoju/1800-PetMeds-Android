package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 9/13/2016.
 */
public class RecommendedCategory implements Serializable{
    private ArrayList<Products> productList;
    private String widgetTitle;
    private String id;
    private Category category;

    public ArrayList<Products> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Products> productList) {
        this.productList = productList;
    }

    public String getWidgetTitle() {
        return widgetTitle;
    }

    public void setWidgetTitle(String widgetTitle) {
        this.widgetTitle = widgetTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}

package com.petmeds1800.model;

import com.petmeds1800.model.entities.Status;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Abhinav on 29/9/16.
 */
public class ProductCategoryListResponse {

    private Status status;
    private ArrayList<ProductCategory> categoryLinks;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<ProductCategory> getCategoryLinks() {
        return categoryLinks;
    }

    public void setCategoryLinks(ArrayList<ProductCategory> categoryLinks) {
        this.categoryLinks = categoryLinks;
    }
}

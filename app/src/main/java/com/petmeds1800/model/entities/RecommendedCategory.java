package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 9/13/2016.
 */
public class RecommendedCategory implements Serializable{
    private ArrayList<RecommendedProducts> productList;
    private String widgetTitle;
    private String id;
    private Category category;
    private String doctorPictureUrl;
    private String doctorQuote;
    private String petName;
    private String petImageUrl;
    private String doctorName;

    public String getDoctorPictureUrl() {
        return doctorPictureUrl;
    }

    public void setDoctorPictureUrl(String doctorPictureUrl) {
        this.doctorPictureUrl = doctorPictureUrl;
    }

    public String getDoctorQuote() {
        return doctorQuote;
    }

    public void setDoctorQuote(String doctorQuote) {
        this.doctorQuote = doctorQuote;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetImageUrl() {
        return petImageUrl;
    }

    public void setPetImageUrl(String petImageUrl) {
        this.petImageUrl = petImageUrl;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public ArrayList<RecommendedProducts> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<RecommendedProducts> productList) {
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

package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 9/13/2016.
 */
public class Category implements Serializable{
    private String seoDisplayName;
    private String categoryIntro;
    private String id;
    private String categoryPageUrl;
    private String bannerImagePath;
    private String displayName;

    public String getSeoDisplayName() {
        return seoDisplayName;
    }

    public void setSeoDisplayName(String seoDisplayName) {
        this.seoDisplayName = seoDisplayName;
    }

    public String getCategoryIntro() {
        return categoryIntro;
    }

    public void setCategoryIntro(String categoryIntro) {
        this.categoryIntro = categoryIntro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryPageUrl() {
        return categoryPageUrl;
    }

    public void setCategoryPageUrl(String categoryPageUrl) {
        this.categoryPageUrl = categoryPageUrl;
    }

    public String getBannerImagePath() {
        return bannerImagePath;
    }

    public void setBannerImagePath(String bannerImagePath) {
        this.bannerImagePath = bannerImagePath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

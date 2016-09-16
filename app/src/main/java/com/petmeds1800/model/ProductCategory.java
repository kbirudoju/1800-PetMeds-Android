package com.petmeds1800.model;

/**
 * Created by Abhinav on 16/9/16.
 */
public class ProductCategory {

    private String name;
    private String url;

    public ProductCategory(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

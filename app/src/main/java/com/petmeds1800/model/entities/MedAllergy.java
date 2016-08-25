package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 8/23/2016.
 */
public class MedAllergy implements Serializable{
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

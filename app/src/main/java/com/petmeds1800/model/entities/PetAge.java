package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 9/12/2016.
 */
public class PetAge implements Serializable {
    private String name;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

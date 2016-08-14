package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 8/17/2016.
 */
public class OrderFilterList implements Serializable{
    private String name;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

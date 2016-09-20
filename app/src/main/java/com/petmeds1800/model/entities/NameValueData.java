package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by Sdixit on 16-09-2016.
 */

public class NameValueData implements Serializable{

    String name, value;

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

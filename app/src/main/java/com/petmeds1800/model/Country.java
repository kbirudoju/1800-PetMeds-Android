package com.petmeds1800.model;

import java.io.Serializable;

/**
 * Created by Abhinav on 26/8/16.
 */
public class Country implements Serializable{

    private String code;
    private String displayName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

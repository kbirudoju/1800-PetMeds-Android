package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 8/12/2016.
 */
public class Status implements Serializable {

    private ArrayList<String> errorMessages;

    private String code;

    public ArrayList<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(ArrayList<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

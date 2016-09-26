package com.petmeds1800.model.shoppingcart;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sarthak on 9/23/2016.
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

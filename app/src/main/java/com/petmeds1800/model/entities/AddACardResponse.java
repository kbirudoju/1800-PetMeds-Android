package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by Abhinav on 21/8/16.
 */
public class AddACardResponse implements Serializable {

    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

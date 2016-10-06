package com.petmeds1800.model;

import com.petmeds1800.model.entities.Status;

import java.io.Serializable;

/**
 * Created by pooja on 10/5/2016.
 */
public class ReOrderResponse implements Serializable {
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

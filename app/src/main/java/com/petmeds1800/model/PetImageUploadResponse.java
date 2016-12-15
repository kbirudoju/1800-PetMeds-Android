package com.petmeds1800.model;

import com.petmeds1800.model.entities.Status;

/**
 * Created by Sdixit on 15-12-2016.
 */

public class PetImageUploadResponse {
    private Status status;
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}

package com.petmeds1800.model;

import com.petmeds1800.model.entities.Status;
import com.petmeds1800.model.entities.Vet;

/**
 * Created by pooja on 10/3/2016.
 */
public class AddVetResponse {
    private Status status;
    private Vet vet;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Vet getVet() {
        return vet;
    }

    public void setVet(Vet vet) {
        this.vet = vet;
    }
}

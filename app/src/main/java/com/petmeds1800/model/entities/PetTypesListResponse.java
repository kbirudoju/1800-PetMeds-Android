package com.petmeds1800.model.entities;

import java.util.ArrayList;

/**
 * Created by Sdixit on 16-09-2016.
 */

public class PetTypesListResponse {
    Status status;
    ArrayList<NameValueData> petTypes;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<NameValueData> getPetTypes() {
        return petTypes;
    }

    public void setPetTypes(ArrayList<NameValueData> petTypes) {
        this.petTypes = petTypes;
    }
}

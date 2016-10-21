package com.petmeds1800.model.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sdixit on 16-09-2016.
 */

public class PetMedicalConditionResponse {

    private Status status;

    private List<NameValueData> medicalConditions;

    public List<NameValueData> getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(ArrayList<NameValueData> medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

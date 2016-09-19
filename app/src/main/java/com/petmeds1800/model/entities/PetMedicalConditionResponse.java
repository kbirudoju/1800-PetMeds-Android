package com.petmeds1800.model.entities;

import java.util.ArrayList;

/**
 * Created by Sdixit on 16-09-2016.
 */

public class PetMedicalConditionResponse {

    private Status status;

    private ArrayList<NameValueData> medicalConditions;

    public ArrayList<NameValueData> getMedicalConditions() {
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

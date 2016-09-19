package com.petmeds1800.model.entities;

import java.util.ArrayList;

/**
 * Created by Sdixit on 15-09-2016.
 */

public class PetMedicationResponse {

    private Status status;

    private ArrayList<MedAllergy> medications;

    public ArrayList<MedAllergy> getMedications() {
        return medications;
    }

    public void setMedications(ArrayList<MedAllergy> medications) {
        this.medications = medications;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        status = status;
    }
}

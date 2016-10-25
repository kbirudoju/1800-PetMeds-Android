package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Sdixit on 14-10-2016.
 */


public class AddMedicationReminderResponse implements Serializable {

    private Status status;

    private ArrayList<MedicationReminderItem> medicationReminder;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<MedicationReminderItem> getMedicationReminder() {
        return medicationReminder;
    }

    public void setMedicationReminder(ArrayList<MedicationReminderItem> medicationReminder) {
        this.medicationReminder = medicationReminder;
    }


}

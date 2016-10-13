package com.petmeds1800.model.entities;

import java.util.ArrayList;

/**
 * Created by Sdixit on 13-10-2016.
 */

public class MedicationReminderListResponse  {
    private Integer count;
    private ArrayList<MedicationReminderItem> medicationReminderList;
    private Status status;
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ArrayList<MedicationReminderItem> getMedicationReminderList() {
        return medicationReminderList;
    }
    public void setMedicationReminderList(ArrayList<MedicationReminderItem> medicationReminderList) {
        this.medicationReminderList = medicationReminderList;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

}

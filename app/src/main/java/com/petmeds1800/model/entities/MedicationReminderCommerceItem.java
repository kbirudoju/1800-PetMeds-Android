package com.petmeds1800.model.entities;

/**
 * Created by Sdixit on 20-10-2016.
 */

public class MedicationReminderCommerceItem extends CommerceItems {

    String description, submittedDate;

    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

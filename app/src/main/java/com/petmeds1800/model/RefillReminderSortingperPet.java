package com.petmeds1800.model;

import com.petmeds1800.model.refillreminder.response.EasyRefillReminder;
import com.petmeds1800.model.refillreminder.response.OrderItems;

import java.util.ArrayList;

/**
 * Created by Sarthak on 21-Oct-16.
 */

public class RefillReminderSortingperPet {

    private String petId;
    private String petName;
    private String petImageURL;
    private ArrayList<RefillReminderSortingPetcompOrder> refillReminderSortingPetcompOrderArraylist;

    public RefillReminderSortingperPet(String petId, String petName, String petImageURL, ArrayList<RefillReminderSortingPetcompOrder> refillReminderSortingPetcompOrderArraylist) {
        this.petId = petId;
        this.petName = petName;
        this.petImageURL = petImageURL;
        this.refillReminderSortingPetcompOrderArraylist = refillReminderSortingPetcompOrderArraylist;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetImageURL() {
        return petImageURL;
    }

    public void setPetImageURL(String petImageURL) {
        this.petImageURL = petImageURL;
    }

    public ArrayList<RefillReminderSortingPetcompOrder> getRefillReminderSortingPetcompOrderArraylist() {
        return refillReminderSortingPetcompOrderArraylist;
    }

    public void setRefillReminderSortingPetcompOrderArraylist(ArrayList<RefillReminderSortingPetcompOrder> refillReminderSortingPetcompOrderArraylist) {
        this.refillReminderSortingPetcompOrderArraylist = refillReminderSortingPetcompOrderArraylist;
    }
}

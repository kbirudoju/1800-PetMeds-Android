package com.petmeds1800.model;

import com.petmeds1800.model.entities.Pets;

import java.io.Serializable;

/**
 * Created by Sarthak on 10/18/2016.
 */

public class PetNameSelectionList implements Serializable {

    private Pets petDetails;
    private boolean isSelected;

    public PetNameSelectionList(Pets petDetails, boolean isSelected) {
        this.petDetails = petDetails;
        this.isSelected = isSelected;
    }

    public Pets getPetDetails() {
        return petDetails;
    }

    public void setPetDetails(Pets petDetails) {
        this.petDetails = petDetails;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

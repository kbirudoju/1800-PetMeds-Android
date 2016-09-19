package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 9/13/2016.
 */
public class RefillItem implements Serializable {
    private String petId;
    private String petImageUrl;

    private String petName;
    private ArrayList<PetItemList> petItemList;

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getPetImageUrl() {
        return petImageUrl;
    }

    public void setPetImageUrl(String petImageUrl) {
        this.petImageUrl = petImageUrl;
    }


    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public ArrayList<PetItemList> getPetItemList() {
        return petItemList;
    }

    public void setPetItemList(ArrayList<PetItemList> petItemList) {
        this.petItemList = petItemList;
    }
}

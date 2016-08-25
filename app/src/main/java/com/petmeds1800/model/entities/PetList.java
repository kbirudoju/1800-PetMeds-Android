package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 8/23/2016.
 */
public class PetList implements Serializable {
    private String petListCount;
    private Status status;
    private ArrayList<Pets> petList;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Pets> getPetList() {
        return petList;
    }

    public void setPetList(ArrayList<Pets> petList) {
        this.petList = petList;
    }

    public String getPetListCount() {

        return petListCount;
    }

    public void setPetListCount(String petListCount) {
        this.petListCount = petListCount;
    }
}

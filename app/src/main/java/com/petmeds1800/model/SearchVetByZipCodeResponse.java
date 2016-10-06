package com.petmeds1800.model;

import com.petmeds1800.model.entities.Status;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 10/4/2016.
 */
public class SearchVetByZipCodeResponse implements Serializable {
    private String vetListCount;
    private Status status;
    private ArrayList<VetList> vetList;

    public String getVetListCount() {
        return vetListCount;
    }

    public void setVetListCount(String vetListCount) {
        this.vetListCount = vetListCount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<VetList> getVetList() {
        return vetList;
    }

    public void setVetList(ArrayList<VetList> vetList) {
        this.vetList = vetList;
    }
}

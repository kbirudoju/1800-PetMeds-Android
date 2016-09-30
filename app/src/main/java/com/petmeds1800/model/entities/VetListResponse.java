package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 9/30/2016.
 */
public class VetListResponse implements Serializable {
    private int vetListCount;
    private Status status;
    private ArrayList<Vet> vetList;

    public int getVetListCount() {
        return vetListCount;
    }

    public void setVetListCount(int vetListCount) {
        this.vetListCount = vetListCount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Vet> getVetList() {
        return vetList;
    }

    public void setVetList(ArrayList<Vet> vetList) {
        this.vetList = vetList;
    }
}

package com.petmeds1800.model;

import com.petmeds1800.model.entities.Status;

import java.util.ArrayList;

/**
 * Created by Abhinav on 26/8/16.
 */
public class StatesListResponse {

    private Status status;
    private ArrayList<UsaState> stateList;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<UsaState> getStateList() {
        return stateList;
    }

    public void setStateList(ArrayList<UsaState> stateList) {
        this.stateList = stateList;
    }
}

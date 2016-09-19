package com.petmeds1800.model.entities;

import java.util.ArrayList;

/**
 * Created by Sdixit on 16-09-2016.
 */

public class AgeListResponse {

    Status status;
    ArrayList<NameValueData> ageList;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<NameValueData> getAgeList() {
        return ageList;
    }

    public void setAgeList(ArrayList<NameValueData> ageList) {
        this.ageList = ageList;
    }
}

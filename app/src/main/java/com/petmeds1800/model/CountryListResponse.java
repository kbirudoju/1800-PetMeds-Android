package com.petmeds1800.model;

import com.petmeds1800.model.entities.Status;

import java.util.ArrayList;

/**
 * Created by Abhinav on 26/8/16.
 */
public class CountryListResponse {

    private Status status;
    private ArrayList<Country> countryList;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(ArrayList<Country> countryList) {
        this.countryList = countryList;
    }
}

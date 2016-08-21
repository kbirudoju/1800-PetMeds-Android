package com.petmeds1800.model.entities;

import com.petmeds1800.model.Address;
import com.petmeds1800.model.Card;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Abhinav on 17/8/16.
 */
public class MySavedAddress implements Serializable {

    private Status status;
    private ArrayList<Address> profileAddresses;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Address> getProfileAddresses() {
        return profileAddresses;
    }

    public void setProfileAddresses(ArrayList<Address> profileAddresses) {
        this.profileAddresses = profileAddresses;
    }
}

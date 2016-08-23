package com.petmeds1800.model.entities;

import com.petmeds1800.model.Address;

/**
 * Created by Abhinav on 22/8/16.
 */
public class AddAddressResponse {

    private Status status;
    private Address profileAddress;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Address getProfileAddress() {
        return profileAddress;
    }

    public void setProfileAddress(Address profileAddress) {
        this.profileAddress = profileAddress;
    }
}

package com.petmeds1800.model.entities;

import java.util.ArrayList;

/**
 * Created by Sdixit on 16-09-2016.
 */

public class PetBreedTypeListResponse {

    Status status;
    ArrayList<BreedItem> breeds;
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<BreedItem> getBreeds() {
        return breeds;
    }

    public void setBreeds(ArrayList<BreedItem> breeds) {
        this.breeds = breeds;
    }
}

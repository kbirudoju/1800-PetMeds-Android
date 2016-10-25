package com.petmeds1800.model.entities;

import java.util.List;

/**
 * Created by Digvijay on 10/25/2016.
 */

public class PetEducationCategoriesResponse {

    private Status status;

    private List<PetEducationCategory> categories;

    public Status getStatus() {
        return status;
    }

    public List<PetEducationCategory> getCategories() {
        return categories;
    }
}

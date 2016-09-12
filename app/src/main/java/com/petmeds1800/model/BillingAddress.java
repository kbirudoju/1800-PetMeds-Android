package com.petmeds1800.model;

import java.io.Serializable;

/**
 * Created by Abhinav on 10/9/16.
 */
public class BillingAddress implements Serializable {
    private String repositoryId;

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }
}

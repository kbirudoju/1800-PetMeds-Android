package com.petmeds1800.model.entities;

/**
 * Created by Siddharth on 4/4/2017.
 */

public class CheckResetPasswordTokenRequest {
    private String ptk;

    public CheckResetPasswordTokenRequest(String ptk) {
        this.ptk = ptk;
    }

    public String getPtk() {
        return ptk;
    }
    public void setPtk(String ptk) {
        this.ptk = ptk;
    }

}

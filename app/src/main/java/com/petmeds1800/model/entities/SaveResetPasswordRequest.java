package com.petmeds1800.model.entities;

/**
 * Created by Siddharth on 4/4/2017.
 */

public class SaveResetPasswordRequest {
    private String ptk;
    private String matchedProfileId;
    private String newPassword;
    private String confirmNewPassword;
    private String _dynSessConf;

    public String getPtk() {
        return ptk;
    }

    public void setPtk(String ptk) {
        this.ptk = ptk;
    }

    public String getMatchedProfileId() {
        return matchedProfileId;
    }

    public void setMatchedProfileId(String matchedProfileId) {
        this.matchedProfileId = matchedProfileId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getDynSessConf() {
        return _dynSessConf;
    }

    public void setDynSessConf(String dynSessConf) {
        this._dynSessConf = dynSessConf;
    }

}

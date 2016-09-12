package com.petmeds1800.model.entities;

/**
 * Created by pooja on 9/9/2016.
 */
public class RemovePetRequest {
    private String petId;
    private String option;

    public RemovePetRequest(String petId, String option, String _dynSessConf) {
        this.petId = petId;
        this.option = option;
        this._dynSessConf = _dynSessConf;
    }

    private String _dynSessConf;

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }
}

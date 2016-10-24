package com.petmeds1800.model;

/**
 * Created by pooja on 10/19/2016.
 */
public class UpdateVetRequest {
    private String vetId;
    private String clinic;
    private String phoneNumber;
    private String name;
    private String _dynSessConf;

    public UpdateVetRequest(String vetId, String clinic, String phoneNumber, String name, String _dynSessConf) {
        this.vetId = vetId;
        this.clinic = clinic;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this._dynSessConf = _dynSessConf;
    }

    public UpdateVetRequest(String vetId,  String phoneNumber, String name, String _dynSessConf){
        this.vetId = vetId;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this._dynSessConf = _dynSessConf;
    }

    public String getVetId() {
        return vetId;
    }

    public void setVetId(String vetId) {
        this.vetId = vetId;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }
}

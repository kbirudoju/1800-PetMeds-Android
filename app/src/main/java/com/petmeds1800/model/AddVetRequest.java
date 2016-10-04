package com.petmeds1800.model;

/**
 * Created by pooja on 10/3/2016.
 */
public class AddVetRequest {
    private String name;
    private String clinic;
    private String phoneNumber;
    private String _dynSessConf;

    public AddVetRequest(String name, String clinic, String phoneNumber, String _dynSessConf) {
        this.name = name;
        this.clinic = clinic;
        this.phoneNumber = phoneNumber;
        this._dynSessConf = _dynSessConf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }
}

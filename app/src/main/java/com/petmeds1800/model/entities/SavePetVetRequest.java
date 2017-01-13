package com.petmeds1800.model.entities;

import java.util.ArrayList;

/**
 * Created by pooja on 9/30/2016.
 */
public class SavePetVetRequest {
    private boolean skipPetWeightCheck;
    private String mailInPrescriptionFlag;
    private ArrayList<String> commerceItemIds;
    private ArrayList<String> petIds;
    private ArrayList<String> vetIds;

    public boolean isSkipPetSpeciesCheck() {
        return skipPetSpeciesCheck;
    }

    public void setSkipPetSpeciesCheck(boolean skipPetSpeciesCheck) {
        this.skipPetSpeciesCheck = skipPetSpeciesCheck;
    }

    private boolean skipPetSpeciesCheck;


    public SavePetVetRequest(boolean skipPetSpeciesCheck,boolean skipPetWeightCheck, String mailInPrescriptionFlag, ArrayList<String> commerceItemIds, ArrayList<String> petIds, ArrayList<String> vetIds, String _dynSessConf) {
        this.skipPetWeightCheck = skipPetWeightCheck;
        this.mailInPrescriptionFlag = mailInPrescriptionFlag;
        this.commerceItemIds = commerceItemIds;
        this.petIds = petIds;
        this.vetIds = vetIds;
        this._dynSessConf = _dynSessConf;
        this.skipPetSpeciesCheck=skipPetSpeciesCheck;
    }

    public boolean isSkipPetWeightCheck() {
        return skipPetWeightCheck;
    }

    public void setSkipPetWeightCheck(boolean skipPetWeightCheck) {
        this.skipPetWeightCheck = skipPetWeightCheck;
    }

    public String getMailInPrescriptionFlag() {
        return mailInPrescriptionFlag;
    }

    public void setMailInPrescriptionFlag(String mailInPrescriptionFlag) {
        this.mailInPrescriptionFlag = mailInPrescriptionFlag;
    }

    public ArrayList<String> getCommerceItemIds() {
        return commerceItemIds;
    }

    public void setCommerceItemIds(ArrayList<String> commerceItemIds) {
        this.commerceItemIds = commerceItemIds;
    }

    public ArrayList<String> getPetIds() {
        return petIds;
    }

    public void setPetIds(ArrayList<String> petIds) {
        this.petIds = petIds;
    }

    public ArrayList<String> getVetIds() {
        return vetIds;
    }

    public void setVetIds(ArrayList<String> vetIds) {
        this.vetIds = vetIds;
    }

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }

    private String _dynSessConf;


}

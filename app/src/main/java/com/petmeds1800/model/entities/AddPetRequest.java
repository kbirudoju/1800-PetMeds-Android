package com.petmeds1800.model.entities;

import java.util.ArrayList;

/**
 * Created by pooja on 8/26/2016.
 */
public class AddPetRequest {
    private String petName;
    private String ownerName;
    private String petType;
    private String breedType;
    private String gender;
    private String weight;
    private String petAge;
    private String birthday;

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    private String petId;

    public AddPetRequest(String petName, String ownerName, String petType, String breedType, String gender, String weight, String petAge, String birthday, String allergy, String allergyInfo, String medAllergy1, String medAllergy2, String medAllergy3, String medAllergy4, ArrayList<Integer> medConditionIds, String medicalInfo, String medication1, String medication2, String medication3, String medication4, String _dynSessConf) {
        this.petName = petName;
        this.ownerName = ownerName;
        this.petType = petType;
        this.breedType = breedType;
        this.gender = gender;
        this.weight = weight;
        this.petAge = petAge;
        this.birthday = birthday;
        this.allergy = allergy;
        this.allergyInfo = allergyInfo;
        this.medAllergy1 = medAllergy1;
        this.medAllergy2 = medAllergy2;
        this.medAllergy3 = medAllergy3;
        this.medAllergy4 = medAllergy4;
        this.medConditionIds = medConditionIds;
        this.medicalInfo = medicalInfo;
        this.medication1 = medication1;
        this.medication2 = medication2;
        this.medication3 = medication3;
        this.medication4 = medication4;
        this._dynSessConf = _dynSessConf;
    }
    //this constructor will be used to edit pet where petId is required
    public AddPetRequest(String petId,String petName, String ownerName, String petType, String breedType, String gender, String weight, String petAge, String birthday, String allergy, String allergyInfo, String medAllergy1, String medAllergy2, String medAllergy3, String medAllergy4, ArrayList<Integer> medConditionIds, String medicalInfo, String medication1, String medication2, String medication3, String medication4, String _dynSessConf) {
        this.petId=petId;
        this.petName = petName;
        this.ownerName = ownerName;
        this.petType = petType;
        this.breedType = breedType;
        this.gender = gender;
        this.weight = weight;
        this.petAge = petAge;
        this.birthday = birthday;
        this.allergy = allergy;
        this.allergyInfo = allergyInfo;
        this.medAllergy1 = medAllergy1;
        this.medAllergy2 = medAllergy2;
        this.medAllergy3 = medAllergy3;
        this.medAllergy4 = medAllergy4;
        this.medConditionIds = medConditionIds;
        this.medicalInfo = medicalInfo;
        this.medication1 = medication1;
        this.medication2 = medication2;
        this.medication3 = medication3;
        this.medication4 = medication4;
        this._dynSessConf = _dynSessConf;
    }
    private String allergy;
    private String allergyInfo;
    private String medAllergy1;
    private String medAllergy2;
    private String medAllergy3;
    private String medAllergy4;
    private ArrayList<Integer> medConditionIds;
    private String medicalInfo;
    private String medication1;
    private String medication2;
    private String medication3;
    private String medication4;
    private String _dynSessConf;

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getBreedType() {
        return breedType;
    }

    public void setBreedType(String breedType) {
        this.breedType = breedType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPetAge() {
        return petAge;
    }

    public void setPetAge(String petAge) {
        this.petAge = petAge;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getAllergyInfo() {
        return allergyInfo;
    }

    public void setAllergyInfo(String allergyInfo) {
        this.allergyInfo = allergyInfo;
    }

    public String getMedAllergy1() {
        return medAllergy1;
    }

    public void setMedAllergy1(String medAllergy1) {
        this.medAllergy1 = medAllergy1;
    }

    public String getMedAllergy2() {
        return medAllergy2;
    }

    public void setMedAllergy2(String medAllergy2) {
        this.medAllergy2 = medAllergy2;
    }

    public String getMedAllergy3() {
        return medAllergy3;
    }

    public void setMedAllergy3(String medAllergy3) {
        this.medAllergy3 = medAllergy3;
    }

    public String getMedAllergy4() {
        return medAllergy4;
    }

    public void setMedAllergy4(String medAllergy4) {
        this.medAllergy4 = medAllergy4;
    }

    public ArrayList<Integer> getMedConditionIds() {
        return medConditionIds;
    }

    public void setMedConditionIds(ArrayList<Integer> medConditionIds) {
        this.medConditionIds = medConditionIds;
    }

    public String getMedicalInfo() {
        return medicalInfo;
    }

    public void setMedicalInfo(String medicalInfo) {
        this.medicalInfo = medicalInfo;
    }

    public String getMedication1() {
        return medication1;
    }

    public void setMedication1(String medication1) {
        this.medication1 = medication1;
    }

    public String getMedication2() {
        return medication2;
    }

    public void setMedication2(String medication2) {
        this.medication2 = medication2;
    }

    public String getMedication3() {
        return medication3;
    }

    public void setMedication3(String medication3) {
        this.medication3 = medication3;
    }

    public String getMedication4() {
        return medication4;
    }

    public void setMedication4(String medication4) {
        this.medication4 = medication4;
    }

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }
}

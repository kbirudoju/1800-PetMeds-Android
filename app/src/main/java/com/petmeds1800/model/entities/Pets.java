package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 8/23/2016.
 */
public class Pets implements Serializable{
    private String petId;
    private String birthday;
    private String ownerName;
    private String weight;
    private ArrayList<NameValueData> medications;
    private String breedType;
    private String petName;
    private String allergyInfo;
    private String medicalInfo;

    public PetAge getPetAge() {
        return petAge;
    }

    public void setPetAge(PetAge petAge) {
        this.petAge = petAge;
    }

    private PetAge petAge;

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public ArrayList<NameValueData> getMedications() {
        return medications;
    }

    public void setMedications(ArrayList<NameValueData> medications) {
        this.medications = medications;
    }

    public String getBreedType() {
        return breedType;
    }

    public void setBreedType(String breedType) {
        this.breedType = breedType;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getAllergyInfo() {
        return allergyInfo;
    }

    public void setAllergyInfo(String allergyInfo) {
        this.allergyInfo = allergyInfo;
    }

    public String getMedicalInfo() {
        return medicalInfo;
    }

    public void setMedicalInfo(String medicalInfo) {
        this.medicalInfo = medicalInfo;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public Vet getVet() {
        return vet;
    }

    public void setVet(Vet vet) {
        this.vet = vet;
    }

    public ArrayList<NameValueData> getMedAllergies() {
        return medAllergies;
    }

    public void setMedAllergies(ArrayList<NameValueData> medAllergies) {
        this.medAllergies = medAllergies;
    }

    public ArrayList<NameValueData> getMedConditions() {
        return medConditions;
    }

    public void setMedConditions(ArrayList<NameValueData> medConditions) {
        this.medConditions = medConditions;
    }

    private String petType;
    private String userId;
    private String gender;
    private String pictureURL;
    private Vet vet;
    private ArrayList<NameValueData> medAllergies;
    private ArrayList<NameValueData> medConditions;


}

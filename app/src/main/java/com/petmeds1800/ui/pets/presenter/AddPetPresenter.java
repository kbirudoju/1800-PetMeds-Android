package com.petmeds1800.ui.pets.presenter;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.AddPetRequest;
import com.petmeds1800.model.entities.AddPetResponse;
import com.petmeds1800.model.entities.AgeListResponse;
import com.petmeds1800.model.entities.PetBreedTypeListResponse;
import com.petmeds1800.model.entities.PetMedicalConditionResponse;
import com.petmeds1800.model.entities.PetMedicationResponse;
import com.petmeds1800.model.entities.PetTypesListResponse;
import com.petmeds1800.model.entities.RemovePetRequest;
import com.petmeds1800.model.entities.RemovePetResponse;
import com.petmeds1800.model.shoppingcart.response.Status;
import com.petmeds1800.ui.pets.support.AddPetContract;

import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 8/26/2016.
 */
public class AddPetPresenter implements AddPetContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    private AddPetContract.View mView;

    public AddPetPresenter(@NonNull AddPetContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);

    }


    @Override
    public void addPetData(AddPetRequest addPetRequest) {
        mPetMedsApiService.addPet(addPetRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddPetResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(AddPetResponse s) {
                        Log.d("Addpetresponse", s.toString());
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onPetAddSuccess(s.getPet());
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });


    }


    @Override
    public void updatePetData(AddPetRequest addPetRequest) {
        mPetMedsApiService.updatePet(addPetRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddPetResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(AddPetResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onUpdateSuccess(s.getPet());
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }

    @Override
    public void removePet(RemovePetRequest request) {
        mPetMedsApiService.removePet(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RemovePetResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(RemovePetResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onPetRemoved();
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }

    @Override
    public void populatePetMedicationsList() {
        mPetMedsApiService.getPetMedicationList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PetMedicationResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(PetMedicationResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.populateData(s);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }

    @Override
    public void populatePetAgeList() {
        mPetMedsApiService.getPetAgeList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AgeListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(AgeListResponse response) {
                        if (response.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.populatePetAgeData(response);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(response.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }

    @Override
    public void populatePetTypeList() {
        mPetMedsApiService.getPetTypesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PetTypesListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(PetTypesListResponse response) {
                        if (response.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.populatePetTypeData(response);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(response.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }

    @Override
    public void pouplatePetBreedTypeList() {
        mPetMedsApiService.getPetBreedList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PetBreedTypeListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(PetBreedTypeListResponse response) {
                        if (response.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.populatePetBreedTypeData(response);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(response.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }

    @Override
    public void populatePetMedicalconditionsList() {
        mPetMedsApiService.getPetPetMedicalConditions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PetMedicalConditionResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(PetMedicalConditionResponse response) {
                        if (response.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.populatePetMedicalconditionsData(response);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(response.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }

    @Override
    public void uploadPetImage(RequestBody petId, MultipartBody.Part imageBody) {
        mPetMedsApiService.uploadPetImage(petId, imageBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(Status status) {
                        if (status != null) {
                            if (status.getCode() != null && status.getCode().equals(API_SUCCESS_CODE)) {
                                if (mView.isActive()) {
                                    mView.onImageUplaodSuccess();
                                }
                            } else {
                                if (mView.isActive()) {
                                    mView.onImgaeUploadError();
                                }
                            }

                        } else {
                            if (mView.isActive()) {
                                mView.onImgaeUploadError();
                            }
                        }
                    }
                });
    }


    @Override
    public void start() {

    }

}

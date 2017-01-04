package com.petmeds1800.ui.pets.presenter;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.PetImageUploadResponse;
import com.petmeds1800.model.entities.AddPetRequest;
import com.petmeds1800.model.entities.AddPetResponse;
import com.petmeds1800.model.entities.AgeListResponse;
import com.petmeds1800.model.entities.PetBreedTypeListResponse;
import com.petmeds1800.model.entities.PetMedicalConditionResponse;
import com.petmeds1800.model.entities.PetMedicationResponse;
import com.petmeds1800.model.entities.PetTypesListResponse;
import com.petmeds1800.model.entities.RemovePetRequest;
import com.petmeds1800.model.entities.RemovePetResponse;
import com.petmeds1800.ui.pets.support.AddPetContract;
import com.petmeds1800.util.RetrofitErrorHandler;

import android.content.Context;
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
    private Context mContext;

    public AddPetPresenter(@NonNull AddPetContract.View view,Context context) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
        this.mContext=context;

    }


    @Override
    public void addPetData(final AddPetRequest addPetRequest) {
        mPetMedsApiService.addPet(addPetRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddPetResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SecurityException) {
                            Log.d("updatePetData", "retrying after session renew");

                            addPetData(addPetRequest);

                            return;

                        }
                        else if(RetrofitErrorHandler.getErrorMessage(e) == R.string.noInternetConnection ) {
                            mView.onError(mContext.getString(R.string.no_internet_caps));
                        }else {
                            mView.onError(e.getLocalizedMessage());
                        }

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
    public void updatePetData(final AddPetRequest addPetRequest) {
        mPetMedsApiService.updatePet(addPetRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddPetResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        //check if we need to retry as a consequence of 409 conflict
                        if (e instanceof SecurityException) {
                            Log.d("updatePetData", "retrying after session renew");

                            updatePetData(addPetRequest);

                            return;

                        } else if (RetrofitErrorHandler.getErrorMessage(e) == R.string.noInternetConnection) {
                            mView.onError(mContext.getString(R.string.no_internet_caps));
                        } else {
                            mView.onError(e.getLocalizedMessage());
                        }

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
    public void removePet(RemovePetRequest request, final int key) {
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
                                mView.onPetRemoved(key);
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
                .subscribe(new Subscriber<PetImageUploadResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(PetImageUploadResponse petImageUploadResponse) {
                        if (petImageUploadResponse.getStatus() != null) {
                            if (petImageUploadResponse.getStatus().getCode() != null && petImageUploadResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                                if (mView.isActive()) {
                                    mView.onImageUploadSuccess();
                                }
                            } else {
                                if (mView.isActive()) {
                                    mView.onImageUploadError(
                                            petImageUploadResponse.getStatus().getErrorMessages().size() > 0 ? petImageUploadResponse.getStatus().getErrorMessages()
                                                    .get(0) : null);
                                }
                            }

                        } else {
                            if (mView.isActive()) {
                                mView.onImageUploadError(petImageUploadResponse.getStatus().getErrorMessages().size() > 0 ? petImageUploadResponse.getStatus().getErrorMessages()
                                        .get(0) : null);
                            }
                        }
                    }
                });
    }


    @Override
    public void start() {

    }

}

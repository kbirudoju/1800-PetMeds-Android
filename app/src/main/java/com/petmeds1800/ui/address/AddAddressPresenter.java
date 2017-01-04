package com.petmeds1800.ui.address;

import android.support.v4.app.Fragment;
import com.petmeds1800.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.Country;
import com.petmeds1800.model.CountryListResponse;
import com.petmeds1800.model.RemoveAddressRequest;
import com.petmeds1800.model.StatesListResponse;
import com.petmeds1800.model.UsaState;
import com.petmeds1800.model.entities.AddAddressResponse;
import com.petmeds1800.model.entities.AddressRequest;
import com.petmeds1800.util.RetrofitErrorHandler;

import java.util.LinkedHashMap;
import java.util.TreeMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.petmeds1800.util.Constants.REQUEST_ADD_ADDRESS;
import static com.petmeds1800.util.Constants.REQUEST_REMOVE_ADDRESS;
import static com.petmeds1800.util.Constants.REQUEST_UPDATE_ADDRESS;

/**
 * Created by Abhinav on 13/8/16.
 */
public class AddAddressPresenter implements AddEditAddressContract.Presenter {

    private static final String LOGGED_IN = "logged in";

    @Inject
    PetMedsApiService mPetMedsApiService;

    private final AddEditAddressContract.View mView;
    private TreeMap<String , String> usaStatedHashMap;
    private LinkedHashMap<String, String> countryHashMap;

    public AddAddressPresenter(AddEditAddressContract.View view){
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void saveAddress(AddressRequest addressRequest) {
        //show the progress
        mPetMedsApiService.addAddress(addressRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddAddressResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        Log.e("AddACard", e.getMessage());
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        if(errorId != 0){
                            if(mView.isActive()){
                                mView.showErrorCrouton(((Fragment)mView).getString(errorId), false);
                            }
                        }
                    }

                    @Override
                    public void onNext(AddAddressResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.addressAdded();
                            }
                        } else {
                            Log.d("AddACard", s.getStatus().getErrorMessages().get(0));
                            if (mView.isActive()) {
                                mView.showErrorCrouton(s.getStatus().getErrorMessages().get(0), false);
                                if (s.getStatus().getErrorMessages().get(0).contains(LOGGED_IN)){
                                    mView.openFingerprintAuthenticationDialog(REQUEST_ADD_ADDRESS);
                                }
                            }
                        }

                    }
                });

    }

    @Override
    public void getUsaStatesList() {
        mPetMedsApiService.getUsaStatesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StatesListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        Log.e("GetStatesList",e.getMessage());
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        if(errorId != 0) { //internet connection error. Unknownhost or SocketTimeout exception
                            if(mView.isActive()){
                                mView.showErrorCrouton(((Fragment)mView).getString(errorId), false);
                            }
                        }
                        else {
                            if(mView.isActive()){
                                mView.showErrorCrouton(e.getLocalizedMessage(), false);
                            }
                        }
                    }

                    @Override
                    public void onNext(StatesListResponse s) {
                        if(s.getStatus().getCode().equals(API_SUCCESS_CODE)){
                            if(mView.isActive()){
                                //StateName would be treated as key while stateCode would be treated as a key so that we could get the code as user selects one name
                                usaStatedHashMap = new TreeMap<String, String>();
                                for(UsaState eachUsaState : s.getStateList()) {
                                    usaStatedHashMap.put(eachUsaState.getDisplayName(),eachUsaState.getCode());
                                }
                                mView.usaStatesListReceived(usaStatedHashMap.keySet().toArray(new String[usaStatedHashMap.size()]));
                            }
                        }
                        else{
                            Log.d("AddACard",s.getStatus().getErrorMessages().get(0));
                            if(mView.isActive()){
                                mView.showErrorCrouton(s.getStatus().getErrorMessages().get(0), false);
                            }
                        }

                    }
                });
    }

    @Override
    public String getUsaStateCode(String usaStateName) {
        if(usaStatedHashMap != null) {
            return usaStatedHashMap.get(usaStateName);
        }
        return null;
    }

    @Override
    public void getCountryList() {
        mPetMedsApiService.getCountryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CountryListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        Log.e("GetCountryList",e.getMessage());
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        if(errorId != 0) { //internet connection error. Unknownhost or SocketTimeout exception
                            if(mView.isActive()){
                                mView.showErrorCrouton(((Fragment)mView).getString(errorId), false);
                            }
                        }
                        else {
                            if(mView.isActive()){
                                mView.showErrorCrouton(e.getLocalizedMessage(), false);
                            }
                        }
                    }

                    @Override
                    public void onNext(CountryListResponse s) {
                        if(s.getStatus().getCode().equals(API_SUCCESS_CODE)){
                            if(mView.isActive()){
                                //StateName would be treated as key while stateCode would be treated as a key so that we could get the code as user selects one name
                                TreeMap treeMap = new TreeMap<String, String>();
                                for(Country eachCountry : s.getCountryList()) {
                                    treeMap.put(eachCountry.getDisplayName(), eachCountry.getCode());
                                }

                                //unfortunaltely we need to show the "United States Of America" on top of the list.
                                //so, first we need to remove it
                                treeMap.remove("United States Of America");

                                //then create a new has map and insert it at the first position
                                countryHashMap = new LinkedHashMap<String, String>();
                                countryHashMap.put("United States Of America" , "USA");
                                countryHashMap.putAll(treeMap);

                                String[] countryArray = countryHashMap.keySet()
                                        .toArray(new String[countryHashMap.size()]);

                                mView.countryListReceived(
                                        countryArray);
                            }
                        }
                        else{
                            Log.d("AddACard",s.getStatus().getErrorMessages().get(0));
                            if(mView.isActive()){
                                mView.showErrorCrouton(s.getStatus().getErrorMessages().get(0), false);
                            }
                        }

                    }
                });
    }

    @Override
    public String getCountryCode(String countryName) {
        if(countryHashMap != null) {
            return countryHashMap.get(countryName);
        }
        return null;
    }

    @Override
    public void updateAddress(AddressRequest addressRequest) {
        //show the progress
        mPetMedsApiService.updateAddress(addressRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddAddressResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        Log.e("EditACard", e.getMessage());
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        if(errorId != 0) { //internet connection error. Unknownhost or SocketTimeout exception
                            if(mView.isActive()){
                                mView.showErrorCrouton(((Fragment)mView).getString(errorId), false);
                            }
                        }
                    }

                    @Override
                    public void onNext(AddAddressResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.addressUpdated();
                            }
                        } else {
                            Log.d("AddACard", s.getStatus().getErrorMessages().get(0));
                            if (mView.isActive()) {
                                mView.showErrorCrouton(s.getStatus().getErrorMessages().get(0), false);
                                if (s.getStatus().getErrorMessages().get(0).contains(LOGGED_IN)) {
                                    mView.openFingerprintAuthenticationDialog(REQUEST_UPDATE_ADDRESS);
                                }
                            }
                        }

                    }
                });
    }

    @Override
    public void removeAddress(RemoveAddressRequest removeAddressRequest) {
        //show the progress
        mPetMedsApiService.removeAddress(removeAddressRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddAddressResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        Log.e("RemoveACard", e.getMessage());
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        if(errorId != 0) { //internet connection error. Unknownhost or SocketTimeout exception
                            if(mView.isActive()){
                                mView.showErrorCrouton(((Fragment)mView).getString(errorId), false);
                            }
                        }
                    }

                    @Override
                    public void onNext(AddAddressResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.addressRemoved();
                            }
                        } else {
                            Log.d("AddACard", s.getStatus().getErrorMessages().get(0));
                            if (mView.isActive()) {
                                mView.showErrorCrouton(s.getStatus().getErrorMessages().get(0), false);
                                if (s.getStatus().getErrorMessages().get(0).contains(LOGGED_IN)) {
                                    mView.openFingerprintAuthenticationDialog(REQUEST_REMOVE_ADDRESS);
                                }
                            }
                        }

                    }
                });
    }

    @Override
    public void start() {

    }
}

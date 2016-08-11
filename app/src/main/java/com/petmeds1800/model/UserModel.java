package com.petmeds1800.model;

import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.dagger.AppScope;

import rx.Observable;

import javax.inject.Inject;

/**
 * Created by Abhinav on 8/8/16.
 */

@AppScope
public class UserModel {

    private PetMedsApiService mService;

    @Inject
    public UserModel(PetMedsApiService service) {
        mService = service;
    }

    public Observable<String> updateAccountSettings(String username, String password){

        return null;
    }

}

package com.petmeds1800.ui.fragments;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.util.GeneralPreferencesHelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

/**
 * Created by pooja on 8/4/2016.
 */
public class AccountRootFragment extends AbstractFragment {

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_container, container, false);
        PetMedsApplication.getAppComponent().inject(this);
        replaceAppropriateFragment();
        return view;
    }

    public void replaceAppropriateFragment(){
        replaceAccountFragment(mPreferencesHelper.getIsUserLoggedIn() ? new AccountFragment() : new SignOutFragment());
    }
}

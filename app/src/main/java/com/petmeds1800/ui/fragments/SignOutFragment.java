package com.petmeds1800.ui.fragments;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.util.GeneralPreferencesHelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

/**
 * Created by Sdixit on 08-09-2016.
 */

public class SignOutFragment extends HomeFragment {
    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout, container, false);
        PetMedsApplication.getAppComponent().inject(this);
        ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.title_account));
        mPreferencesHelper.clearGeneralPreferencesData();
        return view;
    }

}

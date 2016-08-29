package com.petmeds1800.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.R;
import com.petmeds1800.ui.AbstractActivity;

/**
 * Created by pooja on 8/2/2016.
 */
public class HomeFragment extends AbstractFragment{

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AbstractActivity) getActivity()).disableBackButton();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        Log.d("Visible Fragment","HomeFragment");
        (( AbstractActivity) getActivity()).setToolBarTitle("Home");
        return view;
    }
}

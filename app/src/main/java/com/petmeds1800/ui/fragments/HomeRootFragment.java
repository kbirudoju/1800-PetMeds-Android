package com.petmeds1800.ui.fragments;

import com.petmeds1800.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pooja on 8/4/2016.
 */
public class HomeRootFragment extends AbstractFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_container, container, false);
        addHomeFragment();
        return view;
    }

    public void addHomeFragment() {
        replaceHomeFragment(new HomeFragment(), HomeFragment.class.getSimpleName());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}

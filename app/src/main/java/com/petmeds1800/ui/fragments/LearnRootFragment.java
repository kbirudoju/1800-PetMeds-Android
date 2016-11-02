package com.petmeds1800.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.R;

/**
 * Created by Digvijay on 10/19/2016.
 */

public class LearnRootFragment extends AbstractFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn_container, container, false);
        replaceFragment(new LearnFragment(), LearnFragment.class.getSimpleName(), R.id.container_fragment_learn);
        return view;
    }

}

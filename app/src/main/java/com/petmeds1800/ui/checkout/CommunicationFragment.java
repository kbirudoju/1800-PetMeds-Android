package com.petmeds1800.ui.checkout;

import com.petmeds1800.R;
import com.petmeds1800.ui.fragments.AbstractFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sdixit on 22-09-2016.
 */

public class CommunicationFragment extends AbstractFragment {

    public static final String  REQUEST_CODE_KEY = "code";
    public static final int  REQUEST_CODE_VALUE = 1;
    @Override
    public void replaceAccountAndAddToBackStack(Fragment fragment, String tag) {
        super.replaceAccountAndAddToBackStack(fragment, tag);
    }

    public static CommunicationFragment newInstance(int requestCode) {

        Bundle args = new Bundle();
        args.putInt(CommunicationFragment.REQUEST_CODE_KEY, requestCode);
        CommunicationFragment fragment = new CommunicationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communication, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

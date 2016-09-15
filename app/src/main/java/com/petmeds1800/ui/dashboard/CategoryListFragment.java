package com.petmeds1800.ui.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.R;
import com.petmeds1800.ui.fragments.AbstractFragment;

/**
 * Created by pooja on 9/13/2016.
 */
public class CategoryListFragment extends AbstractFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_category_list,container,false);
        return view;
    }
}

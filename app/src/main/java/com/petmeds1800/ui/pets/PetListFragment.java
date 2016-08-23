package com.petmeds1800.ui.pets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.R;
import com.petmeds1800.ui.fragments.AbstractFragment;

import butterknife.BindView;

/**
 * Created by pooja on 8/22/2016.
 */
public class PetListFragment extends AbstractFragment {
    @BindView(R.id.pet_list_view)
    RecyclerView petRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

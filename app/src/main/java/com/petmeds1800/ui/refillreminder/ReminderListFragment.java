package com.petmeds1800.ui.refillreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.R;
import com.petmeds1800.service.RefillReminderService;
import com.petmeds1800.ui.fragments.AbstractFragment;

/**
 * Created by pooja on 9/7/2016.
 */
public class ReminderListFragment extends AbstractFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_list,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().startService(new Intent(getActivity(),RefillReminderService.class));
        super.onViewCreated(view, savedInstanceState);
    }
}

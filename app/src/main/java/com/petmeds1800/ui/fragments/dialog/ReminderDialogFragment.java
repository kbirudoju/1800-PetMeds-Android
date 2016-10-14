package com.petmeds1800.ui.fragments.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.petmeds1800.R;
import com.petmeds1800.ui.fragments.reminder.DailyReminderDialogSubFragment;
import com.petmeds1800.ui.fragments.reminder.WeeklyReminderDialogSubFragment;

/**
 * Created by Sarthak on 10/12/2016.
 */

public class ReminderDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private Spinner mReminderPicker;
    private FrameLayout mReminderMAinContentContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_reminder_dropdown_menu, container, false);
        mReminderPicker = (Spinner) rootView.findViewById(R.id.picker_reminder_options);
        mReminderMAinContentContainer = (FrameLayout) rootView.findViewById(R.id.reminder_main_content_root_fragment);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mReminderPicker.setOnItemSelectedListener(this);
        getChildFragmentManager().beginTransaction().add(R.id.reminder_main_content_root_fragment, new WeeklyReminderDialogSubFragment()).commit();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

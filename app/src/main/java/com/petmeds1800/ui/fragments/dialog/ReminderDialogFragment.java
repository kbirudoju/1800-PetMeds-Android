package com.petmeds1800.ui.fragments.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.petmeds1800.R;
import com.petmeds1800.model.ReminderDialogData;
import com.petmeds1800.ui.fragments.reminder.DailyReminderDialogSubFragment;
import com.petmeds1800.ui.fragments.reminder.MonthlyReminderDialogSubFragment;
import com.petmeds1800.ui.fragments.reminder.WeeklyReminderDialogSubFragment;
import com.petmeds1800.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sarthak on 10/12/2016.
 */

public class ReminderDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    private Spinner mReminderPicker;
    private FrameLayout mReminderMAinContentContainer;
    private Button mOKButton;
    private ReminderDialogData reminderDialogState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_reminder_dropdown_menu, container, false);
        mReminderPicker = (Spinner) rootView.findViewById(R.id.picker_reminder_options);
        mReminderMAinContentContainer = (FrameLayout) rootView.findViewById(R.id.reminder_main_content_root_fragment);
        mOKButton = (Button) rootView.findViewById(R.id.done_button);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mReminderPicker.setOnItemSelectedListener(this);
        mOKButton.setOnClickListener(this);

        getChildFragmentManager().beginTransaction().add(R.id.reminder_main_content_root_fragment, new DailyReminderDialogSubFragment()).commit();

        if (getArguments() != null && getArguments().containsKey(Constants.DIALOG_DATA_TOKEN)){
            if (((ReminderDialogData)(getArguments().getSerializable(Constants.DIALOG_DATA_TOKEN))).getRepeatFrequency().equals(Constants.RepeatFrequency.REPEAT_DAILY)){
                getChildFragmentManager().beginTransaction().add(R.id.reminder_main_content_root_fragment, new DailyReminderDialogSubFragment()).commit();
            } else
            if (((ReminderDialogData)(getArguments().getSerializable(Constants.DIALOG_DATA_TOKEN))).getRepeatFrequency().equals(Constants.RepeatFrequency.REPEAT_WEEKLY)){
                getChildFragmentManager().beginTransaction().add(R.id.reminder_main_content_root_fragment, new WeeklyReminderDialogSubFragment()).commit();
            } else
            if (((ReminderDialogData)(getArguments().getSerializable(Constants.DIALOG_DATA_TOKEN))).getRepeatFrequency().equals(Constants.RepeatFrequency.REPEAT_MONTHLY)){
                getChildFragmentManager().beginTransaction().add(R.id.reminder_main_content_root_fragment, new MonthlyReminderDialogSubFragment()).commit();
            }
        } else {
            reminderDialogState = new ReminderDialogData(true,1,new Date(),Constants.RepeatFrequency.REPEAT_DAILY);
        }
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

        if (position == 0 && !reminderDialogState.getRepeatFrequency().equals(Constants.RepeatFrequency.REPEAT_DAILY)){
            getChildFragmentManager().beginTransaction().replace(R.id.reminder_main_content_root_fragment, new DailyReminderDialogSubFragment()).commit();
            reminderDialogState.setRepeatFrequency(Constants.RepeatFrequency.REPEAT_DAILY);
        } else
        if (position == 1 && !reminderDialogState.getRepeatFrequency().equals(Constants.RepeatFrequency.REPEAT_WEEKLY)){
            getChildFragmentManager().beginTransaction().replace(R.id.reminder_main_content_root_fragment, new WeeklyReminderDialogSubFragment()).commit();
            reminderDialogState.setRepeatFrequency(Constants.RepeatFrequency.REPEAT_WEEKLY);
        } else
        if (position == 2 && !reminderDialogState.getRepeatFrequency().equals(Constants.RepeatFrequency.REPEAT_MONTHLY)){
            getChildFragmentManager().beginTransaction().replace(R.id.reminder_main_content_root_fragment, new MonthlyReminderDialogSubFragment()).commit();
            reminderDialogState.setRepeatFrequency(Constants.RepeatFrequency.REPEAT_MONTHLY);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra(Constants.DIALOG_DATA_TOKEN,reminderDialogState);
        getTargetFragment().onActivityResult(Constants.DIALOG_REMINDER_REQUEST, Activity.RESULT_OK,intent);
        dismiss();
    }
}

package com.petmeds1800.ui.fragments.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
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
import com.petmeds1800.util.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sarthak on 10/12/2016.
 */

public class ReminderDialogFragment extends DialogFragment
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    @BindView(R.id.picker_reminder_options)
    Spinner mReminderPicker;

    @BindView(R.id.switch_compat2)
    SwitchCompat mSwitchCompat2;

    @BindView(R.id.reminder_main_content_root_fragment)
    FrameLayout mReminderMAinContentContainer;

    @BindView(R.id.done_button)
    Button mOKButton;

    private boolean isFirstTime = true;

    private ReminderDialogData reminderDialogState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_reminder_dropdown_menu, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public static ReminderDialogFragment newInstance(ReminderDialogData reminderDialogData) {
        ReminderDialogFragment f = new ReminderDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DIALOG_DATA_TOKEN, reminderDialogData);
        f.setArguments(args);
        return f;
    }

    public void setSpinner(final int selectedPosition) {
        mReminderPicker.post(new Runnable() {
            @Override
            public void run() {
                mReminderPicker.setSelection(selectedPosition);
                mReminderPicker.setOnItemSelectedListener(ReminderDialogFragment.this);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mOKButton.setOnClickListener(this);
        if (getArguments() != null && getArguments().containsKey(Constants.DIALOG_DATA_TOKEN)) {
            reminderDialogState = ((ReminderDialogData) (getArguments().getSerializable(Constants.DIALOG_DATA_TOKEN)));
            if (reminderDialogState
                    .getRepeatFrequency().equals(Constants.RepeatFrequency.REPEAT_DAILY)) {
                getChildFragmentManager().beginTransaction()
                        .add(R.id.reminder_main_content_root_fragment, DailyReminderDialogSubFragment.newInstance(
                                reminderDialogState))
                        .commit();
                setSpinner(Constants.RepeatFrequency.REPEAT_DAILY.ordinal());

            } else if (reminderDialogState
                    .getRepeatFrequency().equals(Constants.RepeatFrequency.REPEAT_WEEKLY)) {
                getChildFragmentManager().beginTransaction()
                        .add(R.id.reminder_main_content_root_fragment, WeeklyReminderDialogSubFragment.newInstance(
                                reminderDialogState))
                        .commit();
                setSpinner(Constants.RepeatFrequency.REPEAT_WEEKLY.ordinal());
            } else if (reminderDialogState
                    .getRepeatFrequency().equals(Constants.RepeatFrequency.REPEAT_MONTHLY)) {
                getChildFragmentManager().beginTransaction()
                        .add(R.id.reminder_main_content_root_fragment, MonthlyReminderDialogSubFragment.newInstance(
                                reminderDialogState))
                        .commit();
                setSpinner(Constants.RepeatFrequency.REPEAT_MONTHLY.ordinal());
            }
            mSwitchCompat2.setChecked(reminderDialogState.isActive());

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

        if (position == Constants.RepeatFrequency.REPEAT_DAILY.ordinal() && !reminderDialogState.getRepeatFrequency()
                .equals(Constants.RepeatFrequency.REPEAT_DAILY)) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.reminder_main_content_root_fragment, DailyReminderDialogSubFragment.newInstance(
                            reminderDialogState))
                    .commit();
            reminderDialogState.setRepeatFrequency(Constants.RepeatFrequency.REPEAT_DAILY);

        } else if (position == Constants.RepeatFrequency.REPEAT_WEEKLY.ordinal() && !reminderDialogState
                .getRepeatFrequency()
                .equals(Constants.RepeatFrequency.REPEAT_WEEKLY)) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.reminder_main_content_root_fragment, WeeklyReminderDialogSubFragment.newInstance(
                            reminderDialogState))
                    .commit();
            reminderDialogState.setRepeatFrequency(Constants.RepeatFrequency.REPEAT_WEEKLY);
        } else if (position == Constants.RepeatFrequency.REPEAT_MONTHLY.ordinal() && !reminderDialogState
                .getRepeatFrequency()
                .equals(Constants.RepeatFrequency.REPEAT_MONTHLY)) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.reminder_main_content_root_fragment, MonthlyReminderDialogSubFragment.newInstance(
                            reminderDialogState))
                    .commit();
            reminderDialogState.setRepeatFrequency(Constants.RepeatFrequency.REPEAT_MONTHLY);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Fragment childFragment = getChildFragmentManager().findFragmentById(R.id.reminder_main_content_root_fragment);
        String qtyValue = "";
        ArrayList<String> daysOfWeek = new ArrayList<String>();
        if (childFragment instanceof DailyReminderDialogSubFragment) {
            qtyValue = ((DailyReminderDialogSubFragment) childFragment).getRepeatQuantity();
            if (qtyValue.isEmpty()) {
                Utils.displayCrouton(getActivity(), getString(R.string.error_empty_day_reminder));
                return;
            }
        } else if (childFragment instanceof WeeklyReminderDialogSubFragment) {
            qtyValue = ((WeeklyReminderDialogSubFragment) childFragment).getRepeatQuantity();
            if (qtyValue.isEmpty()) {
                Utils.displayCrouton(getActivity(), getString(R.string.error_empty_week_reminder));
                return;
            }
            daysOfWeek = ((WeeklyReminderDialogSubFragment) childFragment).getWeekDays();
            reminderDialogState
                    .setToggleValues(((WeeklyReminderDialogSubFragment) childFragment).getWeekDaysToggleValue());
        } else if (childFragment instanceof MonthlyReminderDialogSubFragment) {
            qtyValue = ((MonthlyReminderDialogSubFragment) childFragment).getRepeatQuantity();
            if (qtyValue.isEmpty()) {
                Utils.displayCrouton(getActivity(), getString(R.string.error_empty_month_reminder));
                return;
            }
            reminderDialogState.setmStartDate(
                    ((MonthlyReminderDialogSubFragment) childFragment).getStartDate());

        }

        reminderDialogState.setDayOfWeeks(daysOfWeek);
        reminderDialogState.setmRepeatValue(
                Integer.parseInt(qtyValue));
        reminderDialogState.setActive(mSwitchCompat2.isChecked());
        Intent intent = new Intent();
        intent.putExtra(Constants.DIALOG_DATA_TOKEN, reminderDialogState);
        getTargetFragment().onActivityResult(Constants.DIALOG_REMINDER_REQUEST, Activity.RESULT_OK, intent);
        dismiss();
    }
}

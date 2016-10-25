package com.petmeds1800.ui.fragments.reminder;

import com.petmeds1800.R;
import com.petmeds1800.model.ReminderDialogData;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.InputTypeFilterMinMax;
import com.petmeds1800.util.Utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sarthak on 10/14/2016.
 */

public class WeeklyReminderDialogSubFragment extends Fragment {

    @BindView(R.id.repeat_every_amount)
    EditText mRepeatQuantity;

    @BindView(R.id.day_sunday)
    TextView mDaySunday;

    @BindView(R.id.day_monday)
    TextView mDayMonday;

    @BindView(R.id.day_tuesday)
    TextView mDayTuesday;

    @BindView(R.id.day_wednesday)
    TextView mDayWednesday;

    @BindView(R.id.day_thursday)
    TextView mDayThursday;

    @BindView(R.id.day_friday)
    TextView mDayFriday;

    @BindView(R.id.day_saturday)
    TextView mDaySaturday;

    @BindView(R.id.main_container)
    LinearLayout mMainContainer;


    private final int SIZE_SEVEN = 7;

    private boolean weekDaysToggleValue[];



    private ReminderDialogData reminderDialogData;

    private final String one_value = "1";

    public static WeeklyReminderDialogSubFragment newInstance(ReminderDialogData reminderDialogData) {
        WeeklyReminderDialogSubFragment f = new WeeklyReminderDialogSubFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DIALOG_DATA_TOKEN, reminderDialogData);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dailog_weekly_reminder_layout, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            reminderDialogData = (ReminderDialogData) getArguments().getSerializable(Constants.DIALOG_DATA_TOKEN);
        }
        populateData();
        mRepeatQuantity.setFilters(new InputFilter[]{new InputTypeFilterMinMax("1", "12")});
        return view;
    }

    private void setBackgroundOnCircles(TextView textView, boolean isSelected) {
        if (isSelected) {
            textView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.check_out_blue_circle_drawable));
            textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        } else {
            textView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circular_background));
            textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        }
    }

    private void populateData() {
        TextView textViewIds[] = new TextView[]{mDaySunday, mDayMonday, mDayTuesday, mDayWednesday, mDayThursday,
                mDayFriday, mDaySaturday};
        if (reminderDialogData != null) {
            weekDaysToggleValue = reminderDialogData.getToggleValues() != null ? reminderDialogData.getToggleValues()
                    : new boolean[SIZE_SEVEN];
            mRepeatQuantity.setText(String.valueOf(
                    reminderDialogData.getmRepeatValue() == 0 ? one_value : reminderDialogData.getmRepeatValue()));
            for (int i = 0;
                    reminderDialogData.getToggleValues() != null && i < reminderDialogData.getToggleValues().length;
                    i++) {
                setBackgroundOnCircles(textViewIds[i], weekDaysToggleValue[i]);
            }

        }


    }

    public String getRepeatQuantity() {
        return mRepeatQuantity.getText().toString();
    }

    public boolean[] getWeekDaysToggleValue() {
        return weekDaysToggleValue;
    }

    public ArrayList<String> getWeekDays() {
        ArrayList<String> weekdaysList = new ArrayList<String>();
        for (int i = 0; i < weekDaysToggleValue.length; i++) {
            if (weekDaysToggleValue[i]) {
                weekdaysList.add(Utils.WEEKDAYS_NAMES[i]);
            }
        }
        return weekdaysList;
    }


    @OnClick({R.id.day_sunday, R.id.day_monday, R.id.day_tuesday, R.id.day_wednesday, R.id.day_thursday,
            R.id.day_friday, R.id.day_saturday})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.day_sunday:
                weekDaysToggleValue[Constants.WeekDays.SUNDAY.ordinal()]
                        = !weekDaysToggleValue[Constants.WeekDays.SUNDAY.ordinal()];
                setBackgroundOnCircles(mDaySunday, weekDaysToggleValue[Constants.WeekDays.SUNDAY.ordinal()]);
                break;
            case R.id.day_monday:
                weekDaysToggleValue[Constants.WeekDays.MONDAY.ordinal()]
                        = !weekDaysToggleValue[Constants.WeekDays.MONDAY.ordinal()];
                setBackgroundOnCircles(mDayMonday, weekDaysToggleValue[Constants.WeekDays.MONDAY.ordinal()]);
                break;
            case R.id.day_tuesday:
                weekDaysToggleValue[Constants.WeekDays.TUESDAY.ordinal()]
                        = !weekDaysToggleValue[Constants.WeekDays.TUESDAY.ordinal()];
                setBackgroundOnCircles(mDayTuesday, weekDaysToggleValue[Constants.WeekDays.TUESDAY.ordinal()]);
                break;
            case R.id.day_wednesday:
                weekDaysToggleValue[Constants.WeekDays.WEDNESDAY.ordinal()]
                        = !weekDaysToggleValue[Constants.WeekDays.WEDNESDAY.ordinal()];
                setBackgroundOnCircles(mDayWednesday, weekDaysToggleValue[Constants.WeekDays.WEDNESDAY.ordinal()]);
                break;
            case R.id.day_thursday:
                weekDaysToggleValue[Constants.WeekDays.THURUSDAY.ordinal()]
                        = !weekDaysToggleValue[Constants.WeekDays.THURUSDAY.ordinal()];
                setBackgroundOnCircles(mDayThursday, weekDaysToggleValue[Constants.WeekDays.THURUSDAY.ordinal()]);
                break;
            case R.id.day_friday:
                weekDaysToggleValue[Constants.WeekDays.FRIDAY.ordinal()]
                        = !weekDaysToggleValue[Constants.WeekDays.FRIDAY.ordinal()];
                setBackgroundOnCircles(mDayFriday, weekDaysToggleValue[Constants.WeekDays.FRIDAY.ordinal()]);
                break;
            case R.id.day_saturday:
                weekDaysToggleValue[Constants.WeekDays.SATURDAY.ordinal()]
                        = !weekDaysToggleValue[Constants.WeekDays.SATURDAY.ordinal()];
                setBackgroundOnCircles(mDaySaturday, weekDaysToggleValue[Constants.WeekDays.SATURDAY.ordinal()]);
                break;
        }
    }
}

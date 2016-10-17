package com.petmeds1800.ui.fragments.reminder;

import com.petmeds1800.R;
import com.petmeds1800.model.ReminderDialogData;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.InputTypeFilterMinMax;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sarthak on 10/14/2016.
 */

public class MonthlyReminderDialogSubFragment extends Fragment {

    Calendar myCalendar = Calendar.getInstance();

    @BindView(R.id.repeat_every_amount)
    EditText mRepeatQuantity;

    @BindView(R.id.start_days_Value)
    EditText mRepeatStartDate;

    @BindView(R.id.main_container)
    LinearLayout mMainContainer;

    private ReminderDialogData reminderDialogData;

    private static final String one_value = "1";

    private static final String MM_DD_YY_FORMAT = "MM/dd/yy";

    public static MonthlyReminderDialogSubFragment newInstance(ReminderDialogData reminderDialogData) {
        MonthlyReminderDialogSubFragment f = new MonthlyReminderDialogSubFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DIALOG_DATA_TOKEN, reminderDialogData);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_monthly_reminder_layout, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            reminderDialogData = (ReminderDialogData) getArguments().getSerializable(Constants.DIALOG_DATA_TOKEN);
        }
        populateData();

        mRepeatQuantity.setFilters(new InputFilter[]{new InputTypeFilterMinMax("1", "12")});
        mRepeatStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = MM_DD_YY_FORMAT;
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        mRepeatStartDate.setText(sdf.format(myCalendar.getTime()));
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
        return view;
    }

    private void populateData() {
        if (reminderDialogData != null) {
            mRepeatQuantity.setText(String.valueOf(
                    reminderDialogData.getmRepeatValue() == 0 ? one_value : reminderDialogData.getmRepeatValue()));
            mRepeatStartDate
                    .setText((new SimpleDateFormat(MM_DD_YY_FORMAT).format(reminderDialogData.getmStartDate())));
            myCalendar.setTime(reminderDialogData.getmStartDate());
        } else {
            mRepeatStartDate.setText((new SimpleDateFormat(MM_DD_YY_FORMAT)).format(new Date()));
        }
    }

    public String getRepeatQuantity() {
        return mRepeatQuantity.getText().toString();

    }

    public Date getStartDate() {
        return myCalendar.getTime();

    }
}


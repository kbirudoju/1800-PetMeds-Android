package com.petmeds1800.ui.fragments.reminder;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.petmeds1800.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sarthak on 10/13/2016.
 */

public class DailyReminderDialogSubFragment extends Fragment {

    Calendar myCalendar = Calendar.getInstance();
    EditText mRepeatQuantity;
    EditText mRepeatStartDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_daily_reminder_layout, container, false);

        mRepeatQuantity = (EditText) v.findViewById(R.id.repeat_every_amount);
        mRepeatStartDate = (EditText) v.findViewById(R.id.start_days_Value);
        mRepeatStartDate.setText((new SimpleDateFormat("MM/dd/yy")).format(new Date()));

        mRepeatStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "MM/dd/yy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        mRepeatStartDate.setText(sdf.format(myCalendar.getTime()));;
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return v;
    }
}

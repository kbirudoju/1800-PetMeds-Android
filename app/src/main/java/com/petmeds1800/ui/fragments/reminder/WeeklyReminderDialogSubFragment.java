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
 * Created by Sarthak on 10/14/2016.
 */

public class WeeklyReminderDialogSubFragment extends Fragment {

    EditText mRepeatQuantity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dailog_weekly_reminder_layout, container, false);

        mRepeatQuantity = (EditText) v.findViewById(R.id.repeat_every_amount);
        return v;
    }
}

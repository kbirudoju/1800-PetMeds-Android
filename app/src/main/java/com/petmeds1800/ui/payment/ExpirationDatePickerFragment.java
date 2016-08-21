package com.petmeds1800.ui.payment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.petmeds1800.R;

import java.util.Calendar;

/**
 * Created by Abhinav on 21/8/16.
 */
public class ExpirationDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

//    private final DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        (((LinearLayout)datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(1)).setVisibility(View.GONE);
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
    }

}

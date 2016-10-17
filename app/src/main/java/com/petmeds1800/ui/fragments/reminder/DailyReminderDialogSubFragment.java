package com.petmeds1800.ui.fragments.reminder;

import com.petmeds1800.R;
import com.petmeds1800.model.ReminderDialogData;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.InputTypeFilterMinMax;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sarthak on 10/13/2016.
 */

public class DailyReminderDialogSubFragment extends Fragment {

    ReminderDialogData mReminderDialogData;

    @BindView(R.id.repeat_every_amount)
    EditText mRepeatQuantity;

    private ReminderDialogData reminderDialogData;

    private final String one_value = "1";

    public static DailyReminderDialogSubFragment newInstance(ReminderDialogData reminderDialogData) {
        DailyReminderDialogSubFragment f = new DailyReminderDialogSubFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DIALOG_DATA_TOKEN, reminderDialogData);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_daily_reminder_layout, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            reminderDialogData = (ReminderDialogData) getArguments().getSerializable(Constants.DIALOG_DATA_TOKEN);
        }
        populateData();
        mRepeatQuantity.setFilters(new InputFilter[]{new InputTypeFilterMinMax("1", "12")});
        return view;
    }

    private void populateData() {
        if (reminderDialogData != null) {
            mRepeatQuantity.setText(String.valueOf(
                    reminderDialogData.getmRepeatValue() == 0 ? one_value : reminderDialogData.getmRepeatValue()));
        }
    }

    public String getRepeatQuantity() {
        return mRepeatQuantity.getText().toString();

    }
}

package com.petmeds1800.ui.medicationreminders;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.ReminderDialogData;
import com.petmeds1800.model.entities.AddMedicationReminderRequest;
import com.petmeds1800.model.entities.AddMedicationReminderResponse;
import com.petmeds1800.model.entities.MedicationReminderItem;
import com.petmeds1800.model.entities.MedicationReminderTest;
import com.petmeds1800.model.entities.NameValueData;
import com.petmeds1800.model.entities.RemoveMedicationReminderRequest;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.dialog.ReminderDialogFragment;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;
import com.petmeds1800.util.alarm.MedicationAlarmReceiver;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.petmeds1800.util.Constants.MEDICATION_REMINDER_INFO;

/**
 * Created by Sdixit on 13-10-2016.
 */

public class AddEditMedicationReminders extends AbstractFragment
        implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, AddEditMedicationRemindersContract.View {

    @BindView(R.id.item_edit)
    EditText mItemEdit;

    @BindView(R.id.itemInputLayout)
    TextInputLayout mItemInputLayout;

    @BindView(R.id.pet_name_edit)
    EditText mPetNameEdit;

    @BindView(R.id.petNameInputLayout)
    TextInputLayout mPetNameInputLayout;

    @BindView(R.id.time_edit)
    EditText mTimeEdit;

    @BindView(R.id.timeInputLayout)
    TextInputLayout mTimeInputLayout;

    @BindView(R.id.repeat_edit)
    EditText mRepeatEdit;

    @BindView(R.id.repeatInputLayout)
    TextInputLayout mRepeatInputLayout;

    @BindView(R.id.remove_pet_button)
    Button mRemovePetButton;

    @BindView(R.id.containerLayout)
    LinearLayout mContainerLayout;

    private boolean isEditable;

    private TimePickerDialog mTimePickerDialog;

    private final int ZERO_INDEX = 0;


    private final int SIZE_SEVEN = 7;

    AddEditMedicationRemindersContract.Presenter mPresenter;

    private ReminderDialogData mReminderDialogData;

    private final String reminderTypeArray[] = new String[]{"daily", "weekly", "monthly"};

    private ArrayList<String> mDayOfWeeks;

    private final String MMM_DD_DATE_FORMAT = "MMM dd";

    private final String TIME_FORMAT_AMPM = "h:mm a";

    private String reminderId;

    private String reminderName;

    private ArrayList<String> weeksList;

    private final int INCREMENT_FACTOR = 10;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    public static AddEditMedicationReminders newInstance(boolean isEditable, MedicationReminderItem item) {
        AddEditMedicationReminders addEditMedicationReminders = new AddEditMedicationReminders();
        Bundle args = new Bundle();
        args.putBoolean(Constants.IS_EDITABLE, isEditable);
        args.putSerializable(MEDICATION_REMINDER_INFO, item);
        addEditMedicationReminders.setArguments(args);
        return addEditMedicationReminders;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_medication_reminders, container, false);
        mDayOfWeeks = new ArrayList<String>();
        PetMedsApplication.getAppComponent().inject(this);
        mPresenter = new AddEditMedicationRemindersPresentor(this);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        isEditable = bundle.getBoolean(Constants.IS_EDITABLE);
        if (isEditable) {

            populateMedicalReminderData(
                    (MedicationReminderItem) bundle.getSerializable(Constants.MEDICATION_REMINDER_INFO));
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.DIALOG_REMINDER_REQUEST && resultCode == Activity.RESULT_OK && data != null && data
                .hasExtra(Constants.DIALOG_DATA_TOKEN)) {
            mReminderDialogData = (ReminderDialogData) data.getExtras()
                    .getSerializable(Constants.DIALOG_DATA_TOKEN);
            mRepeatEdit.setText(reminderTypeArray[mReminderDialogData.getRepeatFrequency().ordinal()]);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            ((AbstractActivity) getActivity())
                    .setToolBarTitle(isEditable ? getString(R.string.edit_reminder) : getString(R.string.add_reminder));
            ((AbstractActivity) getActivity()).enableBackButton();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

    }

    private ArrayList<String> populateDaysOfWeeks(ArrayList<NameValueData> nameValueDatas) {
        ArrayList<String> weeksList = new ArrayList<String>();
        if (nameValueDatas != null) {
            for (NameValueData value : nameValueDatas) {
                weeksList.add(value.getValue());
            }
        }

        return weeksList;

    }

    private boolean[] setToggleValuse(ArrayList<String> weeksList) {
        boolean toggleValues[] = new boolean[SIZE_SEVEN];
        if (weeksList.size() > ZERO_INDEX) {
            int count = ZERO_INDEX;
            for (String weeksName : Utils.WEEKDAYS_NAMES) {
                toggleValues[count] = weeksList.contains(weeksName);
                count++;
            }
        }
        return toggleValues;
    }

    private Constants.RepeatFrequency getReminderTypeValue(String value) {
        for (int i = 0; i < reminderTypeArray.length; i++) {
            if (reminderTypeArray[i].equalsIgnoreCase(value)) {
                switch (i) {
                    case 0:
                        return Constants.RepeatFrequency.REPEAT_DAILY;
                    case 1:
                        return Constants.RepeatFrequency.REPEAT_WEEKLY;
                    case 2:
                        return Constants.RepeatFrequency.REPEAT_MONTHLY;
                }
            }
        }
        return Constants.RepeatFrequency.REPEAT_DAILY;
    }

    private void populateMedicalReminderData(MedicationReminderItem item) {
        mRemovePetButton.setVisibility(View.VISIBLE);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        try {
            date = (item.getStartDate() == null) ? Utils
                    .getReminderDate(
                            new SimpleDateFormat(MMM_DD_DATE_FORMAT, Locale.getDefault()).format(new Date()) + " "
                                    + item.getTimeHourMin())
                    : Utils.getReminderDate(item.getStartDate() + " " + item.getTimeHourMin());
            calendar.setTime(date);
            calendar.set(Calendar.YEAR, year);
            date = calendar.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        weeksList = populateDaysOfWeeks(item.getDaysOfWeek());
        reminderId = item.getReminderId();
        reminderName = item.getReminderName();
        mReminderDialogData = new ReminderDialogData(!item.isDisableReminder(),
                Integer.parseInt(item.getRepeatInterval()),
                date,
                getReminderTypeValue(item.getReminderType()),
                weeksList);
        mReminderDialogData.setToggleValues(setToggleValuse(weeksList));
        mItemEdit.setText(item.getReminderName());
        mPetNameEdit.setText(item.getPetName());
        mTimeEdit.setText(item.getTimeHourMin());
        mRepeatEdit.setText(item.getReminderType());
    }

    private void submitMedicationReminderDetails() {
        AddMedicationReminderRequest addMedicationReminderRequest = new AddMedicationReminderRequest();
        String startDate = (mReminderDialogData.getRepeatFrequency().ordinal() == 2) ? Utils
                .getDateInMM_DD_YYYY_Format(mReminderDialogData.getmStartDate()) : "";
        addMedicationReminderRequest.setDaysOfWeek(mReminderDialogData.getDayOfWeeks());
        addMedicationReminderRequest.setDisableReminder(!mReminderDialogData.isActive());
        addMedicationReminderRequest.setPetName(mPetNameEdit.getText().toString());
        addMedicationReminderRequest.setReminderName(mItemEdit.getText().toString());
        addMedicationReminderRequest
                .setDynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        addMedicationReminderRequest
                .setReminderType(reminderTypeArray[mReminderDialogData.getRepeatFrequency().ordinal()]);
        addMedicationReminderRequest
                .setStartDate(startDate);
        addMedicationReminderRequest.setTimeHourMin(mTimeEdit.getText().toString());
        addMedicationReminderRequest.setRepeatInterval(String.valueOf(mReminderDialogData.getmRepeatValue()));
        addMedicationReminderRequest.setShortDescription("6pk Dog 10.1-20lbs");
        if (isEditable) {
            addMedicationReminderRequest.setReminderId(reminderId);
            mPresenter.updateReminders(addMedicationReminderRequest);
        } else {
            mPresenter.saveReminders(addMedicationReminderRequest);
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_a_card, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_done:
                submitMedicationReminderDetails();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openTimePickerDailog() {
        int hour;
        int minute;
        Calendar calendar = Calendar.getInstance();
        if (!mTimeEdit.getText().toString().isEmpty()) {
            try {
                calendar.setTime(Utils.getDate(mTimeEdit.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        mTimePickerDialog = new TimePickerDialog(getContext(), R.style.TimePickerDialogTheme, this, hour, minute,
                false);//Yes 24 hour time
        mTimePickerDialog.show();
    }

    public void openReminderDailog(ReminderDialogData reminderDialogData) {
        DialogFragment fragment = ReminderDialogFragment.newInstance(reminderDialogData);
        fragment.setTargetFragment(this, Constants.DIALOG_REMINDER_REQUEST);
        fragment.show(getFragmentManager(), ReminderDialogFragment.class.getSimpleName());
    }

    @OnClick({R.id.item_edit, R.id.pet_name_edit, R.id.time_edit, R.id.repeat_edit, R.id.remove_pet_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_edit:
                break;
            case R.id.pet_name_edit:
                break;
            case R.id.time_edit:
                openTimePickerDailog();
                break;
            case R.id.repeat_edit:
                if (mRepeatEdit.getText().toString().isEmpty()) {
                    openReminderDailog(new ReminderDialogData(false, ZERO_INDEX, new Date(),
                            Constants.RepeatFrequency.REPEAT_DAILY, mDayOfWeeks));
                } else {
                    for (String aReminderTypeArray : reminderTypeArray) {
                        if (mRepeatEdit.getText().toString().equals(aReminderTypeArray)) {
                            openReminderDailog(mReminderDialogData);
                            break;
                        }
                    }
                }
                break;
            case R.id.remove_pet_button:
                mPresenter.removeMedicationReminders(new RemoveMedicationReminderRequest(
                        mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber(),
                        reminderId));
                break;
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT_AMPM, Locale.getDefault());
        mTimeEdit.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onAddEditSuccess(AddMedicationReminderResponse response) {
        MedicationReminderItem medicationReminderItem = null;
        ArrayList<String> weeksList;
        if (response != null && response.getMedicationReminder() != null
                && response.getMedicationReminder().size() > 0) {
            medicationReminderItem = response
                    .getMedicationReminder().get(ZERO_INDEX);
        }
        if (isEditable) {
            Snackbar.make(mContainerLayout, R.string.medication_reminder_updated_message, Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(mContainerLayout, R.string.medication_reminder_added_message, Snackbar.LENGTH_LONG).show();
        }

        if (medicationReminderItem != null) {
            if (!(medicationReminderItem.isDisableReminder())) {
                weeksList = populateDaysOfWeeks(medicationReminderItem.getDaysOfWeek());
                ArrayList<MedicationReminderTest> medicationReminderTestArrayList
                        = new ArrayList<MedicationReminderTest>();

                ;
                medicationReminderTestArrayList.add(new MedicationReminderTest(medicationReminderItem.getReminderName(),
                        Integer.valueOf(medicationReminderItem.getRepeatInterval()),
                        getReminderTypeValue(medicationReminderItem.getReminderType()),
                        Integer.valueOf(medicationReminderItem.getReminderId()), weeksList,
                        medicationReminderItem.getTimeHourMin(), medicationReminderItem.getStartDate()));
                new MedicationAlarmReceiver().addMultipleAlarms(getContext(), medicationReminderTestArrayList);
                Snackbar.make(mContainerLayout, R.string.medication_reminder_status, Snackbar.LENGTH_LONG).show();
            } else {
                if (isEditable) {
                    new MedicationAlarmReceiver()
                            .cancelAlarm(getContext(), Integer.valueOf(medicationReminderItem.getReminderId()),
                                    medicationReminderItem.getReminderName());
                }
            }

        }
        getFragmentManager().popBackStack();
    }

    @Override
    public void onRemoveSuccess() {
        Snackbar.make(mContainerLayout, R.string.medication_reminder_removed_message, Snackbar.LENGTH_LONG).show();
        if (weeksList != null && weeksList.size() > 0) {
            int counter = 1;
            for (String weeks : weeksList) {
                new MedicationAlarmReceiver()
                        .cancelAlarm(getContext(), Integer.valueOf(reminderId) * INCREMENT_FACTOR + counter,
                                reminderName);
                counter++;
            }
        } else {
            new MedicationAlarmReceiver()
                    .cancelAlarm(getContext(), Integer.valueOf(reminderId), reminderName);
        }

        getFragmentManager().popBackStack();

    }

    @Override
    public void onError(String errorMessage) {
        Utils.displayCrouton(getActivity(), errorMessage, mContainerLayout);
    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        Utils.displayCrouton(getActivity(), message.toString(), mContainerLayout);
    }

    @Override
    public void setPresenter(AddEditMedicationRemindersContract.Presenter presenter) {

    }

}

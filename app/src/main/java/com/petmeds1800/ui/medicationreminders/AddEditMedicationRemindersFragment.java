package com.petmeds1800.ui.medicationreminders;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.ReminderDialogData;
import com.petmeds1800.model.entities.AddMedicationReminderRequest;
import com.petmeds1800.model.entities.AddMedicationReminderResponse;
import com.petmeds1800.model.entities.MedicationReminderItem;
import com.petmeds1800.model.entities.MedicationRemindersAlarmData;
import com.petmeds1800.model.entities.RemoveMedicationReminderRequest;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.dialog.FingerprintAuthenticationDialog;
import com.petmeds1800.ui.fragments.dialog.PetNameDialogFragment;
import com.petmeds1800.ui.fragments.dialog.ReminderDialogFragment;
import com.petmeds1800.ui.pets.AddPetFragment;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;
import com.petmeds1800.util.alarm.MedicationAlarmReceiver;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import static com.petmeds1800.util.Utils.getReminderTypeValue;
import static com.petmeds1800.util.Utils.populateDaysOfWeeks;
import static com.petmeds1800.util.Utils.reminderTypeArray;

/**
 * Created by Sdixit on 13-10-2016.
 */

public class AddEditMedicationRemindersFragment extends AbstractFragment
        implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, AddEditMedicationRemindersContract.View,
        AddPetNameListener, DialogInterface.OnClickListener {

    public static final String FROM_PUSH = "fromPush";

    private static final String IS_EDITABLE = "isEditable";

    private static final String REMINDER_ID = "reminderId";

    private static final String FINGERPRINT_AUTHENTICATION_DIALOG = "FingerprintAuthenticationDialog";

    private static final String LOGGED_IN = "logged in";

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

    private final int ZERO_INDEX = 0;

    private String mProductName;

    private String mItemDescription;


    AddEditMedicationRemindersContract.Presenter mPresenter;

    private ReminderDialogData mReminderDialogData;

    private FingerprintAuthenticationDialog mAuthenticationDialog;

    private ArrayList<String> mDayOfWeeks;

    private final String MMM_DD_DATE_FORMAT = "MMM dd";

    private final String TIME_FORMAT_AMPM = "h:mm a";

    private String reminderId;

    private String reminderName;

    private ArrayList<String> weeksList;

    private final int INCREMENT_FACTOR = 10;


    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    public static AddEditMedicationRemindersFragment newInstance(boolean isEditable, MedicationReminderItem item) {
        AddEditMedicationRemindersFragment addEditMedicationRemindersFragment
                = new AddEditMedicationRemindersFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.IS_EDITABLE, isEditable);
        args.putSerializable(MEDICATION_REMINDER_INFO, item);
        addEditMedicationRemindersFragment.setArguments(args);
        return addEditMedicationRemindersFragment;
    }

    public static AddEditMedicationRemindersFragment newInstance(boolean isEditable, String reminderId) {
        AddEditMedicationRemindersFragment addEditMedicationRemindersFragment
                = new AddEditMedicationRemindersFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.IS_EDITABLE, isEditable);
        args.putString(REMINDER_ID, reminderId);
        addEditMedicationRemindersFragment.setArguments(args);
        return addEditMedicationRemindersFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerIntent(new IntentFilter(Constants.KEY_AUTHENTICATION_SUCCESS), getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        String reminderIdFromPush = null;
        View view = inflater.inflate(R.layout.fragment_add_edit_medication_reminders, container, false);
        mDayOfWeeks = new ArrayList<String>();
        PetMedsApplication.getAppComponent().inject(this);
        mPresenter = new AddEditMedicationRemindersPresentor(this);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        isEditable = bundle.getBoolean(Constants.IS_EDITABLE);
        reminderIdFromPush = bundle.getString(REMINDER_ID);
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
        if (requestCode == Constants.DIALOG_REMINDER_PET_NAME_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data
                .hasExtra(Constants.PET_NAME)) {
            String petName = data
                    .getStringExtra(Constants.PET_NAME);
            if (petName != null) {
                mPetNameEdit.setText(petName);
            }

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


    private boolean[] setToggleValuse(ArrayList<String> weeksList) {
        int SIZE_SEVEN = 7;
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


    private void populateMedicalReminderData(MedicationReminderItem item) {
        mRemovePetButton.setVisibility(View.VISIBLE);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        try {
            date = (item.getStartDate() == null) ? Utils
                    .getReminderDate(
                            new SimpleDateFormat(MMM_DD_DATE_FORMAT, Locale.ENGLISH).format(new Date()) + " "
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

    public boolean checkAndShowError(EditText auditEditText, TextInputLayout auditTextInputLayout, int errorStringId) {
        if (auditEditText.getText().toString().isEmpty()) {
            auditTextInputLayout.setError(getContext().getString(errorStringId));
            return true;
        } else {
            auditTextInputLayout.setError(null);
            auditTextInputLayout.setErrorEnabled(false);
            return false;
        }
    }

    public boolean validateFields() {
        boolean invalidItem;
        boolean invalidPetName;
        boolean invalidTime;
        boolean invalidRepeatTime;
        //return in-case of any error
        invalidItem = checkAndShowError(mItemEdit, mItemInputLayout, R.string.errorItemRequired);
        invalidPetName = checkAndShowError(mPetNameEdit, mPetNameInputLayout, R.string.errorPetNameRequired);
        invalidTime = checkAndShowError(mTimeEdit, mTimeInputLayout,
                R.string.errorInvalidTimeRequired);
        invalidRepeatTime = checkAndShowError(mRepeatEdit, mRepeatInputLayout, R.string.errorRepeatTimeRequired);
        if (invalidItem ||
                invalidPetName ||
                invalidTime ||
                invalidRepeatTime) {
            return false;
        }
        return true;
    }

    private void submitMedicationReminderDetails() {
        if (!validateFields()) {
            return;
        }
        AddMedicationReminderRequest addMedicationReminderRequest = new AddMedicationReminderRequest();
        String startDate = (mReminderDialogData.getRepeatFrequency().ordinal() == 2) ? Utils
                .getDateInMM_DD_YYYY_Format(mReminderDialogData.getmStartDate()) : "";
        addMedicationReminderRequest.setDaysOfWeek(mReminderDialogData.getDayOfWeeks());
        addMedicationReminderRequest.setDisableReminder(!mReminderDialogData.isActive());
        addMedicationReminderRequest.setPetName(mPetNameEdit.getText().toString());
        addMedicationReminderRequest.setReminderName(mProductName);
        addMedicationReminderRequest
                .setDynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        addMedicationReminderRequest
                .setReminderType(reminderTypeArray[mReminderDialogData.getRepeatFrequency().ordinal()]);
        addMedicationReminderRequest
                .setStartDate(startDate);
        addMedicationReminderRequest.setTimeHourMin(
                mTimeEdit.getText().toString().trim().contains(".") ? mTimeEdit.getText().toString().trim()
                        .replace(".", "").trim() : mTimeEdit.getText().toString().trim());
        addMedicationReminderRequest.setRepeatInterval(String.valueOf(mReminderDialogData.getmRepeatValue()));
        addMedicationReminderRequest.setShortDescription((mItemDescription != null) ? mItemDescription : "");
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.TimePickerDialogTheme, this,
                hour, minute,
                false);
        timePickerDialog.show();
    }

    public void openReminderDailog(ReminderDialogData reminderDialogData) {
        DialogFragment fragment = ReminderDialogFragment.newInstance(reminderDialogData);
        fragment.setTargetFragment(this, Constants.DIALOG_REMINDER_REQUEST);
        fragment.show(getFragmentManager(), ReminderDialogFragment.class.getSimpleName());
    }


    public void openPetNameDailog(String petName) {
        DialogFragment fragment = PetNameDialogFragment.newInstance(petName);
        fragment.setTargetFragment(this, Constants.DIALOG_REMINDER_PET_NAME_REQUEST);
        ((PetNameDialogFragment) fragment).setAddPetNameListener(this);
        fragment.show(getFragmentManager(), PetNameDialogFragment.class.getSimpleName());
    }

    @OnClick({R.id.item_edit, R.id.pet_name_edit, R.id.time_edit, R.id.repeat_edit, R.id.remove_pet_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_edit:
                Fragment fragment = MedicationReminderItemsListFragment.newInstance();
                replaceAccountAndAddToBackStack(fragment,
                        MedicationReminderItemsListFragment.class.getName());
                break;
            case R.id.pet_name_edit:
                openPetNameDailog(null);
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
                AlertDialog alertDialog = Utils.showAlertDailog(getActivity(), getString(R.string.areYouSure)
                        , getString(R.string.reminder_removal_alert_msg)
                        , R.style.StyleForNotification)
                        .setPositiveButton(getString(R.string.dialog_ok_button).toUpperCase(), this)
                        .setNegativeButton(getString(R.string.dialog_cancel_button).toUpperCase(), this)
                        .create();
                alertDialog.show();
                break;
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT_AMPM, Locale.ENGLISH);
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
                ArrayList<MedicationRemindersAlarmData> medicationRemindersAlarmDataArrayList
                        = new ArrayList<MedicationRemindersAlarmData>();
                medicationRemindersAlarmDataArrayList
                        .add(new MedicationRemindersAlarmData(medicationReminderItem.getReminderName(),
                                Integer.valueOf(medicationReminderItem.getRepeatInterval()),
                                getReminderTypeValue(medicationReminderItem.getReminderType()),
                                Integer.valueOf(medicationReminderItem.getReminderId()), weeksList,
                                medicationReminderItem.getTimeHourMin(), medicationReminderItem.getStartDate()));
                new MedicationAlarmReceiver().addMultipleAlarms(getContext(), medicationRemindersAlarmDataArrayList);
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

    @Override
    public void openAddPetScreen() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_EDITABLE, false);
        AddPetFragment addPetFragment = new AddPetFragment();
        addPetFragment.setAddPetNameListener(this);
        replaceAccountFragmentWithBundle(addPetFragment, bundle);
    }

    @Override
    public void openAddPetDailog(final String petName) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openPetNameDailog(petName);
            }
        }, 500);

    }

    public void displayItemText(String productName, String decription) {
        mProductName = productName;
        mItemDescription = decription;
        mItemEdit.setText(
                (decription != null && !decription.equals("")) ? productName + "\n" + decription : productName);
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                mPresenter.removeMedicationReminders(new RemoveMedicationReminderRequest(
                        mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber(),
                        reminderId));
                break;
            case DialogInterface.BUTTON_NEGATIVE:

                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}

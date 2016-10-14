package com.petmeds1800.ui.medicationreminders;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.AddMedicationReminderRequest;
import com.petmeds1800.model.entities.AddMedicationReminderResponse;
import com.petmeds1800.model.entities.MedicationReminderItem;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

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
    ScrollView mContainerLayout;

    private boolean isEditable;

    private TimePickerDialog mTimePickerDialog;

    private final String AM = "AM";

    private final String PM = "PM";

    private final String HOUR_12 = "12";

    AddEditMedicationRemindersContract.Presenter mPresenter;

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

    private void populateMedicalReminderData(MedicationReminderItem item) {
        mItemEdit.setText(item.getReminderName());
        mPetNameEdit.setText(item.getPetName());
        mTimeEdit.setText(item.getTimeHourMin());
        mRepeatEdit.setText(item.getReminderType());
    }

    private void submitMedicationReminderDetails() {

        AddMedicationReminderRequest addMedicationReminderRequest = new AddMedicationReminderRequest();
        addMedicationReminderRequest.setDaysOfWeek(new ArrayList<String>());
        addMedicationReminderRequest.setDisableReminder(true);
        addMedicationReminderRequest.setPetName("Rody");
        addMedicationReminderRequest.setReminderName("Treeeee Lorem Ipsum Dolor");
        addMedicationReminderRequest
                .setDynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        addMedicationReminderRequest.setReminderType("daily");
        addMedicationReminderRequest.setStartDate("");
        addMedicationReminderRequest.setTimeHourMin("12:30 AM");
        addMedicationReminderRequest.setRepeatInterval("2");
        addMedicationReminderRequest.setShortDescription("6pk Dog 10.1-20lbs");
        mPresenter.saveReminders(addMedicationReminderRequest);

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
        if (isEditable) {
            try {
                calendar.setTime(Utils.getDate(mTimeEdit.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        mTimePickerDialog = new TimePickerDialog(getContext(), this, hour, minute, false);//Yes 24 hour time
        mTimePickerDialog.show();
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
                break;
            case R.id.remove_pet_button:
                break;
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String am_pm = "";
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        if ((int) calendar.get(Calendar.AM_PM) == Calendar.AM) {
            am_pm = AM;
        } else if ((int) calendar.get(Calendar.AM_PM) == Calendar.PM) {
            am_pm = PM;
        }
        String strHrsToShow = ((int) calendar.get(Calendar.HOUR) == 0) ? HOUR_12 : calendar.get(Calendar.HOUR) + "";
        mTimeEdit.setText(strHrsToShow + ":" + calendar.get(Calendar.MINUTE) + " " + am_pm);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSuccess(AddMedicationReminderResponse response) {
        getFragmentManager().popBackStack();

    }

    @Override
    public void onError(String errorMessage) {
        Utils.displayCrouton(getActivity(), errorMessage.toString(), mContainerLayout);
    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        Utils.displayCrouton(getActivity(), message.toString(), mContainerLayout);
    }

    @Override
    public void setPresenter(AddEditMedicationRemindersContract.Presenter presenter) {

    }
}

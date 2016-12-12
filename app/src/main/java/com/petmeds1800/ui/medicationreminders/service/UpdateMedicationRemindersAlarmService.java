package com.petmeds1800.ui.medicationreminders.service;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.LoginRequest;
import com.petmeds1800.model.entities.LoginResponse;
import com.petmeds1800.model.entities.MedicationReminderItem;
import com.petmeds1800.model.entities.MedicationReminderListResponse;
import com.petmeds1800.model.entities.MedicationRemindersAlarmData;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.alarm.MedicationAlarmReceiver;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.petmeds1800.mvp.BasePresenter.API_SUCCESS_CODE;
import static com.petmeds1800.util.Constants.IS_CANCEL_ALARM;
import static com.petmeds1800.util.Constants.MEDICATION_RESULT_RECEIVER;
import static com.petmeds1800.util.Constants.RESULT_VALUE;
import static com.petmeds1800.util.Constants.SUCCESS;
import static com.petmeds1800.util.Utils.getReminderTypeValue;
import static com.petmeds1800.util.Utils.populateDaysOfWeeks;

/**
 * Created by Sdixit on 21-10-2016.
 */

public class UpdateMedicationRemindersAlarmService extends Service {

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isCancelAlarm;

    private ResultReceiver mMedicationReminderResultReceiver;

    private final int INCREMENT_FACTOR = 10;

    private final int RETRY_TIME = 5;

    NotificationManager mNotificationManager;

    String code;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PetMedsApplication.getAppComponent().inject(this);
        isCancelAlarm = intent.getBooleanExtra(IS_CANCEL_ALARM, false);
        mMedicationReminderResultReceiver = intent.getParcelableExtra(MEDICATION_RESULT_RECEIVER);
        getMedicationList();
        return START_REDELIVER_INTENT;
    }

    private void getMedicationList() {
        mPetMedsApiService.getMedicationReminderList()
                .subscribeOn(Schedulers.io())
                .retry(RETRY_TIME)
                .flatMap(new Func1<MedicationReminderListResponse, Observable<LoginResponse>>() {
                    @Override
                    public Observable<LoginResponse> call(
                            MedicationReminderListResponse medicationReminderListResponse) {
                        if (medicationReminderListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            addOrRemoveAlarms(medicationReminderListResponse);
                            if (mMedicationReminderResultReceiver != null) {
                                setResult(SUCCESS);
                            }
                            stopSelf();
                            return null;
                        } else {

                            if (medicationReminderListResponse.getStatus().getErrorMessages().get(0)
                                    .contains("logged in")) {
                                //TODO Input the email and password from shared preference
                                return mPetMedsApiService.login(new LoginRequest(mPreferencesHelper.getLoginEmail(),
                                        mPreferencesHelper.getLoginPassword(),
                                        mPreferencesHelper.getSessionConfirmationResponse()
                                                .getSessionConfirmationNumber()))
                                        .subscribeOn(Schedulers.io());
                            } else {
                                return null;
                            }


                        }
                    }
                })
                .flatMap(new Func1<LoginResponse, Observable<MedicationReminderListResponse>>() {
                    @Override
                    public Observable<MedicationReminderListResponse> call(LoginResponse loginResponse) {
                        if (loginResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            return mPetMedsApiService.getMedicationReminderList()
                                    .subscribeOn(Schedulers.io());


                        } else {
                            if (mMedicationReminderResultReceiver != null) {
                                setResult(loginResponse.getStatus().getErrorMessages().get(0));
                            }
                            stopSelf();
                            return null;
                        }
                    }
                })
                .subscribe(new Subscriber<MedicationReminderListResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        if (e.getMessage().contains("Conflict")) { //TODO need to change it
                            renewSessionConfirmationNumber();
                        }
                    }

                    @Override
                    public void onNext(MedicationReminderListResponse medicationReminderListResponse) {

                        if (medicationReminderListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            addOrRemoveAlarms(medicationReminderListResponse);
                        }
                        if (mMedicationReminderResultReceiver != null) {
                            setResult(SUCCESS);
                        }
                        stopSelf();

                    }
                });
    }

    private void renewSessionConfirmationNumber() {
        mPetMedsApiService.getSessionConfirmationNumber()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SessionConfNumberResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(SessionConfNumberResponse sessionConfNumberResponse) {
                        String sessionConfNumber = sessionConfNumberResponse.getSessionConfirmationNumber();
                        if (sessionConfNumber != null) {
                            mPreferencesHelper.saveSessionConfirmationResponse(sessionConfNumberResponse);
                            getMedicationList();
                        }

                    }
                });
    }

    private void addOrRemoveAlarms(MedicationReminderListResponse medicationReminderListResponse) {
        ArrayList<MedicationRemindersAlarmData> medicationRemindersAlarmDataArrayList
                = new ArrayList<MedicationRemindersAlarmData>();
        ArrayList<MedicationReminderItem> medicationReminderItemList = null;
        if (medicationReminderListResponse.getMedicationReminderList() != null
                && medicationReminderListResponse.getMedicationReminderList().size() > 0) {
            medicationReminderItemList
                    = medicationReminderListResponse.getMedicationReminderList();
            for (MedicationReminderItem medicationReminderItem : medicationReminderItemList) {
                if (!(medicationReminderItem.isDisableReminder())) {
                    ArrayList<String> weeksList = populateDaysOfWeeks(
                            medicationReminderItem.getDaysOfWeek());

                    medicationRemindersAlarmDataArrayList.add(new MedicationRemindersAlarmData(
                            medicationReminderItem.getReminderName(),
                            Integer.valueOf(medicationReminderItem.getRepeatInterval()),
                            getReminderTypeValue(medicationReminderItem.getReminderType()),
                            Integer.valueOf(medicationReminderItem.getReminderId()), weeksList,
                            medicationReminderItem.getTimeHourMin(),
                            medicationReminderItem.getStartDate()));
                    if (isCancelAlarm) {
                        cancelAlarm(weeksList,
                                Integer.valueOf(medicationReminderItem.getReminderId()),
                                medicationReminderItem.getReminderName());
                    }
                }
            }

            if (medicationRemindersAlarmDataArrayList.size() > 0 && !isCancelAlarm) {
                addAlarms(medicationRemindersAlarmDataArrayList);
            }

        }
    }

    private void addAlarms(ArrayList<MedicationRemindersAlarmData> medicationRemindersAlarmDataArrayList) {
        new MedicationAlarmReceiver()
                .addMultipleAlarms(getBaseContext().getApplicationContext(),
                        medicationRemindersAlarmDataArrayList);
    }

    private void cancelAlarm(ArrayList<String> weeksList, int reminderId, String reminderName) {
        if (weeksList != null && weeksList.size() > 0) {
            int counter = 1;
            for (String weeks : weeksList) {
                new MedicationAlarmReceiver()
                        .cancelAlarm(getBaseContext().getApplicationContext(),
                                Integer.valueOf(reminderId) * INCREMENT_FACTOR + counter,
                                reminderName, String.valueOf(reminderId));
                counter++;
            }
        } else {
            new MedicationAlarmReceiver()
                    .cancelAlarm(getBaseContext().getApplicationContext(), Integer.valueOf(reminderId), reminderName,
                            String.valueOf(reminderId));
        }
    }

    public void setResult(String resultValue) {
        // To send a message to the Activity, create a pass a Bundle
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_VALUE, resultValue);
        mMedicationReminderResultReceiver.send(Activity.RESULT_OK, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

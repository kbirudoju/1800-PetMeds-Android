package com.petmeds1800.intent;

import com.petmeds1800.ui.medicationreminders.service.UpdateMedicationRemindersAlarmService;

import android.content.Context;
import android.content.Intent;

import static com.petmeds1800.util.Constants.IS_CANCEL_ALARM;

/**
 * Created by Sdixit on 21-10-2016.
 */

public class AddUpdateMedicationRemindersIntent extends Intent {

    public AddUpdateMedicationRemindersIntent(Context mContext, boolean isCancel) {
        super(mContext, UpdateMedicationRemindersAlarmService.class);
        putExtra(IS_CANCEL_ALARM, isCancel);
    }
}

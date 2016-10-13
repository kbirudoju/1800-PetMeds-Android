package com.petmeds1800.intent;

import com.petmeds1800.util.alarm.MedicationAlarmReceiver;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Sdixit on 12-10-2016.
 */

public class AlarmIntent extends Intent {


    public AlarmIntent(Context context, Class<MedicationAlarmReceiver> medicationAlarmReceiverClass) {
        super(context,medicationAlarmReceiverClass);
    }

    @Override
    public boolean filterEquals(Intent other) {
        boolean flag = super.filterEquals(other);
        return flag;
    }
}

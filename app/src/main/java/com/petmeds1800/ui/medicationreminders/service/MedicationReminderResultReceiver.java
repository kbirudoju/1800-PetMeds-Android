package com.petmeds1800.ui.medicationreminders.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Sdixit on 22-10-2016.
 */

public class MedicationReminderResultReceiver extends ResultReceiver {

    Receiver mMedicationReminderResultReceiver;

    /**
     * Create a new ResultReceive to receive results.  Your {@link #onReceiveResult} method will be called from the
     * thread running <var>handler</var> if given, or from an arbitrary thread if null.
     */
    public MedicationReminderResultReceiver(Handler handler) {
        super(handler);
    }

    // Delegate method which passes the result to the receiver if the receiver has been assigned
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mMedicationReminderResultReceiver != null) {
            mMedicationReminderResultReceiver.onReceiveResult(resultCode, resultData);
        }
    }

    // Setter for assigning the receiver
    public void setReceiver(Receiver receiver) {
        this.mMedicationReminderResultReceiver = receiver;
    }

    // Defines our event interface for communication
    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

}

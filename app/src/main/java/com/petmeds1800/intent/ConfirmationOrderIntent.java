package com.petmeds1800.intent;

import com.petmeds1800.ui.ConfirmationReceiptActivity;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Sdixit on 04-10-2016.
 */

public class ConfirmationOrderIntent extends Intent {

    public ConfirmationOrderIntent(final Context context) {
        super(context, ConfirmationReceiptActivity.class);
    }
}

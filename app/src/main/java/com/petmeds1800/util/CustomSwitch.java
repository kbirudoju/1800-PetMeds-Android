package com.petmeds1800.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Switch;

/**
 * Created by Sdixit on 20-12-2016.
 */

public class CustomSwitch extends Switch {


    private OnCheckedChangeListener mListener;

    public CustomSwitch(Context context) {
        super(context);
    }

    public CustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        // Do not call supper method
        mListener = listener;
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (mListener != null) {
            mListener.onCheckedChanged(this, checked);
        }
    }

    public void setCheckedProgrammatically(boolean checked) {
        // You can call super method
        super.setChecked(checked);
    }

}

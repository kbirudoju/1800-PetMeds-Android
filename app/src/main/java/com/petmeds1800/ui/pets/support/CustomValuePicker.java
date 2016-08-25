package com.petmeds1800.ui.pets.support;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.NumberPicker;

/**
 * Created by pooja on 8/24/2016.
 */
public class CustomValuePicker extends NumberPicker {
    public CustomValuePicker(Context context) {
        super(context);
        init();
    }
    public CustomValuePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomValuePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }
    public void setValues(String[] values) {
        setMinValue(0);
        setMaxValue(values.length - 1);
        setDisplayedValues(values);
        setWrapSelectorWheel(false);
    }

    @Override
    public void setDividerDrawable(Drawable divider) {
        super.setDividerDrawable(divider);
    }
}

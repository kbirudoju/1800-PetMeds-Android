package com.petmeds1800.ui.pets.support;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.petmeds1800.R;

import java.lang.reflect.Field;

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
        setDividerColor(getResources().getColor(R.color.hint_color));
    }

    @Override
    public void setValue(int value) {
        super.setValue(value);
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

    public void setDividerColor(@ColorInt int color) {
        try {
            Field fDividerDrawable = NumberPicker.class.getDeclaredField("mSelectionDivider");
            Field selectorWheelPaintField = NumberPicker.class.getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);

            fDividerDrawable.setAccessible(true);
            Drawable d = (Drawable) fDividerDrawable.get(this);
            d.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            d.setAlpha(46);
            d.invalidateSelf();
            postInvalidate(); // Drawable is dirty
        } catch (Exception e) {

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((widthMeasureSpec - getResources().getDimensionPixelSize(R.dimen.dp_30)), getResources().getDimensionPixelSize(R.dimen.dp_170));
    }
}

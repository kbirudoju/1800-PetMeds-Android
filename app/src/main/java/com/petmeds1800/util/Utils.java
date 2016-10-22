package com.petmeds1800.util;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.NameValueData;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by pooja on 8/27/2016.
 */
public class Utils {
    public static final String TIME_OUT = "timeout";

    private static final String[] PICKER_DISPLAY_MONTHS_NAMES = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct",
            "Nov", "Dec"};
    public  static final String [] WEEKDAYS_NAMES = new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    public  static final String reminderTypeArray[] = new String[]{"daily", "weekly", "monthly"};
    public static String changeDateFormat(long millisecond, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String dateString = formatter.format(new Date(millisecond));
        return dateString;
    }

    public static AlertDialog.Builder showAlertDailog(Activity activity, String title, String msg, int theme) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, theme);
        builder.setTitle(title);
        if (msg != null) {
            builder.setMessage(msg);
        }
        return builder;
    }

    public static AlertDialog.Builder showAlertDailogListView(Activity activity, View view,
            int theme) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, theme);
        builder.setView(view);
        return builder;

    }
    public static boolean checkConfirmFields(EditText enteredText,EditText confirmedText){
        return TextUtils.equals(enteredText.getText().toString(),confirmedText.getText().toString());
    }
    public static void displayCrouton(Activity activity, Spanned messageString, ViewGroup attachToView) {
        Crouton.makeText(activity,
                messageString,
                new Style.Builder()
                        .setBackgroundColor(R.color.color_snackbar)
                        .setTextAppearance(R.style.StyleCrouton)
                        .setTextColor(R.color.color_snackbar_text)
                        .setHeight(activity.getResources().getDimensionPixelSize(R.dimen.height_snackbar))
                        .setGravity(Gravity.CENTER)
                        .setTextColor(android.R.color.white)
                        .build(),
                attachToView).show();
    }

    public static void displayCrouton(Activity activity, String messageString, ViewGroup attachToView) {
        Crouton.makeText(activity,
                messageString,
                new Style.Builder()
                        .setBackgroundColor(R.color.color_snackbar)
                        .setTextAppearance(R.style.StyleCrouton)
                        .setTextColor(R.color.color_snackbar_text)
                        .setHeight(activity.getResources().getDimensionPixelSize(R.dimen.height_snackbar))
                        .setGravity(Gravity.CENTER)
                        .setTextColor(android.R.color.white)
                        .build(),
                attachToView).show();
    }


    public static String getShortMonthName(int month) {
        if (month < 1 && month > 12) {
            return "ERR";
        }

        return PICKER_DISPLAY_MONTHS_NAMES[month - 1];
    }
    public static Date getDate(String dateStr) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("h:mm a");
        return formatter.parse(dateStr);
    }
    public static Date getReminderDate(String dateStr) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("MMM dd h:mm a");
        return formatter.parse(dateStr);
    }

    public static String getDateInMM_DD_YYYY_Format(Date date){
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(date);
    }
    public static Constants.RepeatFrequency getReminderTypeValue(String value) {
        for (int i = 0; i < reminderTypeArray.length; i++) {
            if (reminderTypeArray[i].equalsIgnoreCase(value)) {
                switch (i) {
                    case 0:
                        return Constants.RepeatFrequency.REPEAT_DAILY;
                    case 1:
                        return Constants.RepeatFrequency.REPEAT_WEEKLY;
                    case 2:
                        return Constants.RepeatFrequency.REPEAT_MONTHLY;
                }
            }
        }
        return Constants.RepeatFrequency.REPEAT_DAILY;
    }

    public static ArrayList<String> populateDaysOfWeeks(ArrayList<NameValueData> nameValueDatas) {
        ArrayList<String> weeksList = new ArrayList<String>();
        if (nameValueDatas != null) {
            for (NameValueData value : nameValueDatas) {
                weeksList.add(value.getValue());
            }
        }

        return weeksList;

    }
}

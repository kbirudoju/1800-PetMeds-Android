package com.petmeds1800.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.NameValueData;
import com.petmeds1800.ui.AbstractActivity;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import okhttp3.Cookie;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by pooja on 8/27/2016.
 */
public class Utils {
    public static final String TIME_OUT = "timeout";

    private static final String[] PICKER_DISPLAY_MONTHS_NAMES = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct",
            "Nov", "Dec"};

    private static final int CREDIT_CARD_DIGITS_RULE_1 = 16;

    private static final int CREDIT_CARD_DIGITS_RULE_2 = 15;

    private static final int CVV_DIGITS_RULE_1 = 3;

    private static final int CVV_DIGITS_RULE_2 = 4;

    public static final String[] WEEKDAYS_NAMES = new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    public static final String reminderTypeArray[] = new String[]{"daily", "weekly", "monthly"};

    public static String changeDateFormat(long millisecond, String dateFormat) {
         if(millisecond == 0){
             return "";
         }
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
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

    public static boolean checkConfirmFields(EditText enteredText, EditText confirmedText) {
        return TextUtils.equals(enteredText.getText().toString(), confirmedText.getText().toString());
    }

    public static void displayCrouton(Activity activity, Spanned messageString, ViewGroup attachToView) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.view_crouton, null);
        TextView textView = (TextView) customView.findViewById(R.id.txv_crouton);
        textView.setText(messageString);
        final Configuration configuration = new Configuration.Builder().setDuration(Configuration.DURATION_LONG).build();
        final Crouton crouton = Crouton.make(activity, customView, attachToView.getId(), configuration);
        crouton.show();
    }

    public static void displayCrouton(Activity activity, String messageString, ViewGroup attachToView) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.view_crouton, null);
        TextView textView = (TextView) customView.findViewById(R.id.txv_crouton);
        textView.setText(messageString);
        final Configuration configuration = new Configuration.Builder().setDuration(Configuration.DURATION_LONG).build();
        final Crouton crouton = Crouton.make(activity, customView, attachToView.getId(), configuration);
        crouton.show();
    }

    public static String getShortMonthName(int month) {
        if (month < 1 && month > 12) {
            return "ERR";
        }

        return PICKER_DISPLAY_MONTHS_NAMES[month - 1];
    }


    public static boolean isCreditCardNumberValid(String creditCardNumber) {
        if (creditCardNumber.length() == CREDIT_CARD_DIGITS_RULE_1
                || creditCardNumber.length() == CREDIT_CARD_DIGITS_RULE_2) {
            return true;
        }
        return false;
    }

    public static boolean isExpirationDateValid(int expirationMonth, int expirationYear) {
        if (expirationMonth <= 0 && expirationYear <= 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isCvvValid(String cvv) {
        if (cvv.length() == CVV_DIGITS_RULE_1 || cvv.length() == CVV_DIGITS_RULE_2) {
            return true;
        }
        return false;
    }

    public static Date getDate(String dateStr) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("h:mm a",Locale.ENGLISH);
        return formatter.parse(dateStr);
    }

    public static Date getReminderDate(String dateStr) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("MMM dd h:mm a",Locale.ENGLISH);
        return formatter.parse(dateStr);
    }

    public static String getDateInMM_DD_YYYY_Format(Date date) {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy",Locale.ENGLISH);
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


    public static void toggleGIFAnimantionVisibility(boolean showVisible, Activity activity) {
        if (showVisible) {
            try {
                ((AbstractActivity) activity).startLoadingGif(activity);
            } catch (Exception e) {
                try {
                    ((AbstractActivity) activity).stopLoadingGif(activity);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            try {
                ((AbstractActivity) activity).stopLoadingGif(activity);
            } catch (Exception e) {
                try {
                    ((AbstractActivity) activity).stopLoadingGif(activity);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void toggleProgressDialogVisibility(boolean showVisisble, View mProgressBar) {
        if (showVisisble) {
            if (mProgressBar != null && mProgressBar.getVisibility() == View.GONE) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        } else {
            if (mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    public static void shareFile(Context context, File attachmentFile, String attachmentName) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        if (attachmentFile != null) {
            Uri uri = Uri.fromFile(new File(attachmentFile, attachmentName + ".pdf"));
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sendIntent.setType("application/pdf");
        }
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email applications installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendEmail(Context context, String receiverEmail, String subject, String body) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        if (receiverEmail != null && !receiverEmail.isEmpty()) {
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{receiverEmail});
        }
        if (subject != null && !subject.isEmpty()) {
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (body != null && !body.isEmpty()) {
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        }
        emailIntent.setType("message/rfc822");
        try {
            context.startActivity(Intent.createChooser(emailIntent, context.getResources().getString(R.string.choose_email_client)));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public static void getAndUpdateNewCookie(Throwable e , SetCookieCache cookieCache) {
        if (e instanceof HttpException) {
            Map<String, List<String>> map = ((HttpException) e).response().headers().toMultimap();

            if (!map.containsKey("set-cookie")){
                return;
            }
            List<String> cookiesList = map.get("set-cookie");

            String cookieName;
            String cookieValue = null;

            for (String eachCookie : cookiesList) {
                if (eachCookie.contains("JSESSIONID")) {
                    String[] rawCookieParams = eachCookie.split(";");
                    String[] rawCookieNameAndValue = rawCookieParams[0].split("=");
                    if (rawCookieNameAndValue.length != 2) {
                        Log.d("errorRetreivingCookie", "missing name and value");
                    }

                    cookieName = rawCookieNameAndValue[0].trim();
                    cookieValue = rawCookieNameAndValue[1].trim();

                    break;
                }
            }

            //remove the old JSESSIONID
            ArrayList<Cookie> updatedCookieCache = new ArrayList<Cookie>();

            for (Iterator<Cookie> iterator = cookieCache.iterator(); iterator.hasNext(); ) {
                Cookie cookie = iterator.next();
                if ( ! cookie.name().equals("JSESSIONID")) {
                    updatedCookieCache.add(cookie);
                }
            }

            //insert the valid JsessionID
            Cookie.Builder builder = new Cookie.Builder();
            builder.name("JSESSIONID");
            builder.value(cookieValue);
            builder.domain("dev.1800petmeds.com"); //TODO update the domain
            Cookie jsessionCookie = builder.build();
            updatedCookieCache.add(jsessionCookie);
            cookieCache.clear();
            cookieCache.addAll(updatedCookieCache);
        }
    }
}

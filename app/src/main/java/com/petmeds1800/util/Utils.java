package com.petmeds1800.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pooja on 8/27/2016.
 */
public class Utils {

    public static String changeDateFormat(long millisecond, String dateFormat){
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String dateString = formatter.format(new Date(millisecond));
        return dateString;
    }

}

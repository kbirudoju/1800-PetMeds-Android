package com.petmeds1800.util;

import com.petmeds1800.R;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Digvijay on 9/5/2016.
 */
public class RetrofitErrorHandler {

    //TODO: logic improvement
    public static int getErrorMessage(Throwable e) {
        if (e instanceof UnknownHostException) {
            return R.string.noInternetConnection;
        } else if(e instanceof SocketTimeoutException) {
            return R.string.connectionTimeout;
        } else {
            return 0;
        }
    }
}

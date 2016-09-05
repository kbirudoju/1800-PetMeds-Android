package com.petmeds1800.util;

import com.petmeds1800.R;

import retrofit.RetrofitError;

/**
 * Created by Digvijay on 9/5/2016.
 */
public class RetrofitErrorHandler {

    public static int handleError(Throwable e) {

        if (e instanceof RetrofitError) {
            final RetrofitError error = (RetrofitError) e;
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                return R.string.networkError;
            } else if (error.getKind() == RetrofitError.Kind.HTTP) {
                return R.string.httpError;
            } else {
                return R.string.unexpectedError;
            }
        } else {
            return R.string.unexpectedError;
        }
    }
}

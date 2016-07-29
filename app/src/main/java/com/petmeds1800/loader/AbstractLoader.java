package com.petmeds1800.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

abstract class AbstractLoader<T> extends AsyncTaskLoader<T> {

    private T mResult;

    public AbstractLoader(final Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mResult == null) {
            forceLoad();
        } else {
            deliverResult(mResult);
        }
    }

    @Override
    public void deliverResult(final T result) {
        super.deliverResult(result);
        mResult = result;
    }
}

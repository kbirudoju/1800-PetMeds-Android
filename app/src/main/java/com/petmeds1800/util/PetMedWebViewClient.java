package com.petmeds1800.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.ui.HomeActivity;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sarthak on 07-Nov-16.
 */

public class PetMedWebViewClient extends WebViewClient {

    private Activity mContext;

    @Inject
    OkHttpClient okHttpClient;

    public PetMedWebViewClient(Activity mContext) {
        this.mContext = mContext;
        PetMedsApplication.getAppComponent().inject(this);
    }

    private void finalyClose(){
        Log.w("OverrideUrlLoading", "finalyClose Enter");
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContext.onBackPressed();
            }
        });
        Log.w("OverrideUrlLoading", "finalyClose Enter");
    }

    private void onSuccessResponse(final WebView view, final String url){
        Log.w("OverrideUrlLoading", "onSuccessResponse Enter");

        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ((HomeActivity)mContext).updateCartMenuItemCount();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        Log.w("OverrideUrlLoading", "onSuccessResponse Exit");
    }

    private void onFailureResponse(final WebView view, final String url, final String responseCode){
        Log.w("OverrideUrlLoading", "FailureResponse Enter");

        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext,responseCode, Toast.LENGTH_LONG).show();
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(Constants.KEY_HOME_ROOT_SESSION_CONFIRMATION));
            }
        });
        Log.w("OverrideUrlLoading", "FailureResponse Exit");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d("url is", url);

        if (url.contains("Add+To+Cart")){
            Log.w("OverrideUrlLoading", "Contains Cart");
            syncCallWebViewResponse(view,url);
            return true;
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private void syncCallWebViewResponse(final WebView view, final String url){
        Log.w("OverrideUrlLoading", "Thread_URL_Call_Cart Enter");

        Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                try {
                    Response response = okHttpClient.newCall(new Request.Builder().url(url).build()).execute();
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                    if (!response.isSuccessful()) subscriber.onError(new Exception("error"));
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response>() {

                    @Override
                    public void onCompleted() {
                        finalyClose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onFailureResponse(view,url,(e.getMessage()));
                    }

                    @Override
                    public void onNext(Response response) {
                        onSuccessResponse(view,url);
                    }
                });
        Log.w("OverrideUrlLoading", "Thread_URL_Call_Cart Exit");
    }
}

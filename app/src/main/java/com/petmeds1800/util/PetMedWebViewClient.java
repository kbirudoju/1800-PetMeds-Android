package com.petmeds1800.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.SessionConfNumberResponse;

import java.io.IOException;
import java.util.Iterator;

import javax.inject.Inject;

import okhttp3.Cookie;
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
    private WebView mWebView;
    private String mUrl;
    private boolean shouldloaddata = false;
    private ProgressBar mProgressBar;

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @Inject
    SetCookieCache mCookieCache;

    @Inject
    OkHttpClient okHttpClient;

    public PetMedWebViewClient(Activity mContext, WebView mWebView, String mUrl, boolean shouldloaddata,ProgressBar mProgressBar) {
        this.mContext = mContext;
        this.mWebView = mWebView;
        this.mUrl = mUrl;
        this.shouldloaddata = shouldloaddata;
        this.mProgressBar = mProgressBar;
        PetMedsApplication.getAppComponent().inject(this);
    }

    private void finalyClose(){
        Log.w("OverrideUrlLoading", "finalyClose Enter");

//        Hide Progress Dialog
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressBar != null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });


        if (((AppCompatActivity)mContext).getSupportFragmentManager().getBackStackEntryCount()<=0){
            if (shouldloaddata){
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadData(mUrl, "text/html", "UTF-8");
                    }
                });
            } else {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadUrl(mUrl);
                    }
                });
            }
        } else {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mContext.onBackPressed();
                }
            });
        }
        Log.w("OverrideUrlLoading", "finalyClose Enter");
    }

    private void onSuccessResponse(final WebView view, String url){
        Log.w("OverrideUrlLoading", "onSuccessResponse Enter");

        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(Constants.KEY_CART_FRAGMENT_INTENT_FILTER));
            }
        });
        Log.w("OverrideUrlLoading", "onSuccessResponse Exit");
    }

    private void onFailureResponse(final WebView view, String url, final String responseCode){
        Log.w("OverrideUrlLoading", "FailureResponse Enter");

        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext,responseCode, Toast.LENGTH_LONG).show();
                if (responseCode.contains("Conflict")) {
                    Toast.makeText(mContext,responseCode,Toast.LENGTH_SHORT).show();
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(Constants.KEY_HOME_ROOT_SESSION_CONFIRMATION));
                }
            }
        });
        Log.w("OverrideUrlLoading", "FailureResponse Exit");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d("url is", url);

        if (CookieManager.getInstance().getCookie(url)!= null && !CookieManager.getInstance().getCookie(url).toString().contains("JSESSIONID")) {

            final int currentApiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentApiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                //removeSessionCookies seems the only working option.removeAllCookie didn't clear all the cookies in this case.
                CookieManager.getInstance().removeSessionCookies(null);
                CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
            } else {
                CookieManager.getInstance().removeSessionCookie();
                CookieManager.getInstance().setAcceptCookie(true);
            }

            String cookieString = "";
            for (Iterator<Cookie> iterator = mCookieCache.iterator(); iterator.hasNext(); ) {
                Cookie cookie = iterator.next();
                if (cookie.name().equals("JSESSIONID")) {
                    cookieString = cookieString + "JSESSIONID=" + cookie.value() + "; ";
                } else if (cookie.name().equals("SITESERVER")) {
                    cookieString = cookieString + "SITESERVER=" + cookie.value() + "; ";
                }
            }
            cookieString = cookieString + "app=true; ";
            CookieManager.getInstance().setCookie(url, cookieString);

            if (currentApiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                CookieManager.getInstance().flush();
            } else {
                CookieSyncManager.getInstance().sync();
            }
        }

        if (url.contains("Add+To+Cart")){
            Log.w("OverrideUrlLoading", "Contains Cart");
            Log.w("URL HANDLING PRIOR", url);

            syncCallWebViewResponse(view,url,false);
            return true;
        } else {
            mWebView.loadUrl(url);
            return true;
        }
    }

    private void syncCallWebViewResponse(final WebView view,final String url,final boolean isRepeat){
        Log.w("OverrideUrlLoading", "Thread_URL_Call_Cart Enter");

        Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                try {

//                    Show Progress Dialog
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mProgressBar != null){
                                mProgressBar.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    Log.w("URL HANDLING", url);
                    Response response;
                    if (url.toString().contains("jsessionid")) {
                        Log.w("URL HANDLING", "in new okhttpclient");
                         response = new OkHttpClient().newCall(new Request.Builder().url(url).build()).execute();
                    }
                    else {
                        Log.w("URL HANDLING", "in mokhttpclient");
                         response = okHttpClient.newCall(new Request.Builder().url(url).build()).execute();
                    }
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
                        Log.w("URL HANDLING Response", response.toString());
                        if (response.toString().contains("Conflict") && !isRepeat){
                            renewSessionConfirmationNumber(view,url,true);

                        } else if (response.toString().contains("Conflict")){
                            Log.w("URL HANDLING Response","Conflict Still Remains : " + response.toString());
                        } else {
                            onSuccessResponse(view,url);
                        }
                    }
                });
        Log.w("OverrideUrlLoading", "Thread_URL_Call_Cart Exit");
    }

    /**
     * Renew Session Confirmation number in case it has expired
     * This is intrinsic call and does not effect overall cart fragment APIs
     * @param
     */
    private void renewSessionConfirmationNumber(final WebView view, final String url, boolean repeat) {
        Log.w("ShoppingCartListPresntr", "renewSessionConfirmationNumber Enter");

        mPetMedsApiService.getSessionConfirmationNumber().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<SessionConfNumberResponse>() {

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                Toast.makeText(mContext,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onNext(SessionConfNumberResponse sessionConfNumberResponse) {
                syncCallWebViewResponse(view,url,true);
            }
        });
        Log.w("ShoppingCartListPresntr", "renewSessionConfirmationNumber Exit");
    }
}
package com.petmeds1800.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
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
import com.petmeds1800.ui.HomeActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Cookie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.petmeds1800.util.Constants.KEY_APP_ID;
import static com.petmeds1800.util.Constants.KEY_JESESSION_ID;
import static com.petmeds1800.util.Constants.KEY_SITE_SERVER;

/**
 * Created by Sarthak on 07-Nov-16.
 */

public class PetMedWebViewClient extends WebViewClient {

    private Activity mContext;
    private WebView mWebView;
    private String mUrl;
    private boolean shouldloaddata = false;
    private ProgressBar mProgressBar;
    private boolean isSuccess = false;

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

        if (isSuccess) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(Constants.KEY_CART_FRAGMENT_INTENT_FILTER));
                }
            });
        }


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

        //        Hide Progress Dialog
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressBar != null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        if (isSuccess){
            isSuccess = false;
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                Move to Shopping Cart on Successful Item Add
                    try {
                        ((HomeActivity)mContext).scrollViewPager(1);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

        Log.w("OverrideUrlLoading", "finalyClose Enter");
    }

    private void onSuccessResponse(final WebView view, String url){
        Log.w("OverrideUrlLoading", "onSuccessResponse Enter");
        isSuccess = true;
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
    public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
        Log.d("url is", url);

        if (CookieManager.getInstance().getCookie(url)== null || !CookieManager.getInstance().getCookie(url).toString().contains(KEY_JESESSION_ID) || !CookieManager.getInstance().getCookie(url).toString().contains(KEY_SITE_SERVER)) {

            CookieManager.getInstance().removeAllCookie();
            Log.w("PetMedWebViewClient", "shouldOverrideUrlLoading remove All Cookie Called");
            SystemClock.sleep(1000);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
            } else {
                CookieManager.getInstance().setAcceptCookie(true);
            }
            Log.w("PetMedWebViewClient", "shouldOverrideUrlLoading set accept third party cookies Called");

            Map cookieMap = new HashMap();
            for (Iterator<Cookie> iterator = mCookieCache.iterator(); iterator.hasNext(); ) {
                Cookie cookie = iterator.next();
                if (cookie.name().equals(KEY_JESESSION_ID)) {
                    cookieMap.put(KEY_JESESSION_ID,KEY_JESESSION_ID + "=" + cookie.value() + "; ");
                } else if (cookie.name().equals(KEY_SITE_SERVER)) {
                    cookieMap.put(KEY_SITE_SERVER,KEY_SITE_SERVER + "=" + cookie.value() + "; ");
                }
            }
            cookieMap.put(KEY_APP_ID,KEY_APP_ID + "=true; ");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                Log.d("setUpWebView", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
                CookieManager.getInstance().setCookie(url, (String) cookieMap.get("JSESSIONID"));
                CookieManager.getInstance().setCookie(url, (String) cookieMap.get("SITESERVER"));
                CookieManager.getInstance().setCookie(url, (String) cookieMap.get("app"));
                CookieManager.getInstance().flush();
            } else {
                Log.d("setUpWebView", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
                CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(mWebView.getContext());
                cookieSyncMngr.startSync();
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setCookie(url, (String) cookieMap.get("JSESSIONID"));
                cookieManager.setCookie(url, (String) cookieMap.get("SITESERVER"));
                cookieManager.setCookie(url, (String) cookieMap.get("app"));
                cookieSyncMngr.stopSync();
                cookieSyncMngr.sync();
            }
            SystemClock.sleep(1000);
        }

        {
            if (url.contains("Add+To+Cart")){
                Log.w("OverrideUrlLoading", "Contains Cart");
                Log.w("URL HANDLING PRIOR", url);

                Log.w("shouldOverrideUrl", "Add To Cart Cookies : " + CookieManager.getInstance().getCookie(url));
                syncCallWebViewResponse(view,url,false);
            } else {

                Log.w("shouldOverrideUrl", "NON Add To Cart Cookies : " + CookieManager.getInstance().getCookie(url));
                mWebView.loadUrl(url);
            }
        }
        return true;
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
                    Log.w("URL HANDLING", "in mokhttpclient");
                    Log.w("URL HANDLING", " Cookies : " + CookieManager.getInstance().getCookie(url));
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
                        Log.w("URL HANDLING Response", response.toString());
                        if (response.toString().contains("Conflict") && !isRepeat){
                            renewSessionConfirmationNumber(view,url,true);

                        } else if (response.toString().contains("Conflict")){
                            Log.w("URL HANDLING Response","Conflict Still Remains : " + response.toString());
                            onFailureResponse(view,url,("Cannot Add Item"));
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
                if (!sessionConfNumberResponse.getSessionConfirmationNumber().isEmpty()) {
                    mPreferencesHelper.saveSessionConfirmationResponse(sessionConfNumberResponse);
                }
                syncCallWebViewResponse(view,url,true);
            }
        });
        Log.w("ShoppingCartListPresntr", "renewSessionConfirmationNumber Exit");
    }

    @Override
    public void onPageFinished(WebView view, String url) {

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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }

        super.onPageFinished(view, url);
    }
}
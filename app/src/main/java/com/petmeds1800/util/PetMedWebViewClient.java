package com.petmeds1800.util;

import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.ui.HomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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

import java.io.IOException;
import java.net.URISyntaxException;
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

    private void finalyClose() throws URISyntaxException {
        if (isSuccess) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "Item Added Successfully", Toast.LENGTH_SHORT).show();

                }
            });
        }
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(Constants.KEY_CART_FRAGMENT_INTENT_FILTER));
        reInitializeCookieManager();
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
                        ((HomeActivity)mContext).scrollViewPager(1,false);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void onSuccessResponse(final WebView view, String url){
        isSuccess = true;
    }

    private void onFailureResponse(final WebView view, String url, final String responseCode){
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
    }

    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
        Log.e("url is", url);

        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressBar != null){
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        
        new Thread(new Runnable() {
            @Override
            public void run() {

                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                cookieManager.removeSessionCookie();
                Log.e("CommonWebviewFragment", "onCreate remove All Cookie Called");

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
                    Log.e("setUpWebView", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
                    CookieManager.getInstance().setCookie((url), (String) cookieMap.get(KEY_JESESSION_ID));
                    CookieManager.getInstance().setCookie((url), (String) cookieMap.get(KEY_SITE_SERVER));
                    CookieManager.getInstance().setCookie((url), (String) cookieMap.get(KEY_APP_ID));
                    CookieManager.getInstance().flush();
                } else {
                    Log.e("setUpWebView", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
                    CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(mWebView.getContext());
                    cookieSyncMngr.startSync();
                    cookieManager.setCookie((url), (String) cookieMap.get(KEY_JESESSION_ID));
                    cookieManager.setCookie((url), (String) cookieMap.get(KEY_SITE_SERVER));
                    cookieManager.setCookie((url), (String) cookieMap.get(KEY_APP_ID));
                    cookieSyncMngr.stopSync();
                    cookieSyncMngr.sync();
                }

                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressBar != null){
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
                
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (url.contains("Add+To+Cart")){
                            syncCallWebViewResponse(view,url);
                        } else {
                            mWebView.loadUrl(url);
                        }
                    }
                });
            }
        }).start();
        
        return true;
    }

    private void syncCallWebViewResponse(final WebView view,final String url){

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
                try {
                    finalyClose();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                onFailureResponse(view,url,(e.getMessage()));
            }

            @Override
            public void onNext(Response response) {
                Log.e("URL HANDLING Response", response.toString());
                 if (response.toString().contains("Conflict")){
                    Log.e("URL HANDLING Response","Conflict Still Remains : " + response.toString());
                    onFailureResponse(view,url,("Cannot Add Item"));
                } else {
                    onSuccessResponse(view,url);
                }
            }
        });
    }

    /**
     * Renew Session Confirmation number in case it has expired
     * This is intrinsic call and does not effect overall cart fragment APIs
     * @param
     */
    private void reInitializeCookieManager() throws URISyntaxException {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
        Log.e("CommonWebviewFragment", "onCreate remove All Cookie Called");

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
            Log.e("setUpWebView", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().setCookie(Utils.getDomainName(mUrl), (String) cookieMap.get(KEY_JESESSION_ID));
            CookieManager.getInstance().setCookie(Utils.getDomainName(mUrl), (String) cookieMap.get(KEY_SITE_SERVER));
            CookieManager.getInstance().setCookie(Utils.getDomainName(mUrl), (String) cookieMap.get(KEY_APP_ID));
            CookieManager.getInstance().flush();
        } else {
            Log.e("setUpWebView", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(mWebView.getContext());
            cookieSyncMngr.startSync();
            cookieManager.setCookie(Utils.getDomainName(mUrl), (String) cookieMap.get(KEY_JESESSION_ID));
            cookieManager.setCookie(Utils.getDomainName(mUrl), (String) cookieMap.get(KEY_SITE_SERVER));
            cookieManager.setCookie(Utils.getDomainName(mUrl), (String) cookieMap.get(KEY_APP_ID));
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }
}
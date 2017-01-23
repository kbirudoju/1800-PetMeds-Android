package com.petmeds1800.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.ui.HomeActivity;

import java.io.IOException;
import java.net.URISyntaxException;
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

import static com.petmeds1800.util.Constants.KEY_APP_ID;

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
                Toast.makeText(mContext, responseCode, Toast.LENGTH_LONG).show();
                if (responseCode.contains("Conflict")) {
                    Toast.makeText(mContext, responseCode, Toast.LENGTH_SHORT).show();
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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    Log.e("setUpWebView", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));

                    for (Iterator<Cookie> iterator = mCookieCache.iterator(); iterator.hasNext(); ) {
                        Cookie cookie = iterator.next();
                        cookieManager.setCookie((url), cookie.name() + "=" + cookie.value() + ";");
                    }

                    cookieManager.setCookie((url),KEY_APP_ID + "=true;");
                    cookieManager.flush();
                } else {
                    Log.e("setUpWebView", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));

                    CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(mWebView.getContext());
                    cookieSyncMngr.startSync();

                    for (Iterator<Cookie> iterator = mCookieCache.iterator(); iterator.hasNext(); ) {
                        Cookie cookie = iterator.next();
                        cookieManager.setCookie((url), cookie.name() + "=" + cookie.value() + ";");
                    }
                    cookieManager.setCookie((url),KEY_APP_ID + "=true;");
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
                        Log.e("shouldOverrideUrl", "Outside Cookies after setcookie: " + CookieManager.getInstance().getCookie(url));
                        if (url.contains("Add+To+Cart")){
                            syncCallWebViewResponse(view,url);
                        } else if((url.contains("tel:")))
                        {
                            try {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse(url));
                                mContext.startActivity(intent);
                            }catch (android.content.ActivityNotFoundException ex) {
                                ex.printStackTrace();
                            }
                        }else if(url.contains("mailto") ){
                            String mail = url.replaceFirst("mailto:", "");
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                            //need this to prompts email client only
                            emailIntent.setType("message/rfc822");
                            try {
                                mContext.startActivity(Intent.createChooser(emailIntent, mContext.getResources().getString(R.string.choose_email_client)));
                            } catch (android.content.ActivityNotFoundException ex) {

                            }
                        }
                        else {
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
}
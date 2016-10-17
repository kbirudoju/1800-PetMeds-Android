package com.petmeds1800.ui.fragments;

import com.petmeds1800.R;
import com.petmeds1800.ui.AbstractActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.ui.AbstractActivity;


import java.util.Iterator;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

/**
 * Created by pooja on 8/25/2016.
 */
public class CommonWebviewFragment extends AbstractFragment {

    public static final String URL_KEY = "url";

    public static final String TITLE_KEY = "title";

    public static final String HTML_DATA = "html_data";

    @BindView(R.id.webViewContainer)
    WebView mWebView;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @Inject
    SetCookieCache mCookieCache;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common_webview, container, false);
        ButterKnife.bind(this, rootView);
        ((AbstractActivity) getActivity()).enableBackButton();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String url = getArguments().getString(URL_KEY);
        String title = getArguments().getString(TITLE_KEY);
        String htmlData = getArguments().getString(HTML_DATA);

        if (title != null && !title.isEmpty()) {
            ((AbstractActivity) getActivity()).setToolBarTitle(title);
        }
        if(htmlData != null){
            loadFromHtmlData(htmlData);
        }else{
            setUpWebView(url);
        }

        ((AbstractActivity)getActivity()).getToolbar().getMenu().clear();
        ((AbstractActivity)getActivity()).getToolbar().setLogo(null);
    }

    private void setUpWebView(String url) {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
        } else {
            CookieManager.getInstance().removeAllCookie();
        }
        CookieManager.getInstance().setAcceptCookie(true);

        String cookieString = null;
        for (Iterator<Cookie> iterator = mCookieCache.iterator() ; iterator.hasNext();) {
            Cookie cookie = iterator.next();
            if(cookie.name().equals("JSESSIONID")) {
               cookieString = "JSESSIONID=" + cookie.value() + ";";
            }
            else if(cookie.name().equals("SITESERVER")) {
                cookieString = cookieString + "SITESERVER=" + cookie.value() + ";";
            }
        }
        cookieString = cookieString + "app=true;";
        CookieManager.getInstance().setCookie(url, cookieString);
        CookieSyncManager.getInstance().sync();

        mWebView.loadUrl(url);
        Log.d("URL", url + ">>>>>");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        WebChromeClient client = new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    mProgressBar.setVisibility(View.GONE);

                }
            }
        };

        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.setWebViewClient(new Callback());
        mWebView.setWebChromeClient(client);
    }

    private void loadFromHtmlData(String htmlData){
        mWebView.loadData(htmlData, "text/html", "UTF-8");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));

        WebChromeClient client = new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    mProgressBar.setVisibility(View.GONE);

                }
            }
        };

        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.setWebViewClient(new Callback());
        mWebView.setWebChromeClient(client);
    }

    private class Callback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("when you click on any interlink on webview that time you got url :-" + url);

            if (url.contains("Add+To+Cart")){
                getActivity().onBackPressed();
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}


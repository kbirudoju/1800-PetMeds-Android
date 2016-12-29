package com.petmeds1800.ui.fragments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.checkout.CheckOutActivity;
import com.petmeds1800.util.PetMedWebViewClient;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Cookie;

import static com.petmeds1800.util.Constants.KEY_APP_ID;
import static com.petmeds1800.util.Constants.KEY_INITIALIZE_COOKIES;
import static com.petmeds1800.util.Constants.KEY_JESESSION_ID;
import static com.petmeds1800.util.Constants.KEY_SITE_SERVER;

/**
 * Created by pooja on 8/25/2016.
 */
public class CommonWebviewFragment extends AbstractFragment {

    public static final String URL_KEY = "url";

    public static final String TITLE_KEY = "title";

    public static final String HTML_DATA = "html_data";

    public static final String PAYPAL_DATA = "paypal_data";

    public static final String ISCHECKOUT = "ischeckout";

    public static final String STEPNAME = "stepname";

    public static final String DISABLE_BACK_BUTTON = "disableBackButton";

    @BindView(R.id.webViewContainer)
    WebView mWebView;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @Inject
    SetCookieCache mCookieCache;

    @Inject
    PetMedsApiService mPetMedsApiService;

    private boolean mDisableBackButton;

    private OnPaymentCompletedListener onPaymnetSelectedListener;

    private boolean isCheckout = false;

    private String mStepName;

    public static CommonWebviewFragment newInstance(boolean disableBackButton) {
        Bundle args = new Bundle();
        args.putBoolean(DISABLE_BACK_BUTTON, disableBackButton);
        CommonWebviewFragment fragment = new CommonWebviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented the callback interface. If not, it throws an exception
        if (activity instanceof HomeActivity || activity instanceof CheckOutActivity) {
            try {
                onPaymnetSelectedListener = (OnPaymentCompletedListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement onPaymnetSelectedListener");
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetMedsApplication.getAppComponent().inject(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mDisableBackButton = bundle.getBoolean(DISABLE_BACK_BUTTON);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common_webview, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
//        } else {
//            CookieManager.getInstance().setAcceptCookie(true);
//        }

        Log.d("setUpWebView", "Using clearCookies code for cookies");
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDisableBackButton) {
            ((AbstractActivity) getActivity()).disableBackButton();
        } else {
            ((AbstractActivity) getActivity()).enableBackButton();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String url = getArguments().getString(URL_KEY);
        String title = getArguments().getString(TITLE_KEY);
        String htmlData = getArguments().getString(HTML_DATA);
        String paypalData = getArguments().getString(PAYPAL_DATA);
        isCheckout = getArguments().getBoolean(ISCHECKOUT);
        mStepName = getArguments().getString(STEPNAME);

        if (title != null && !title.isEmpty()) {
            ((AbstractActivity) getActivity()).setToolBarTitle(title);
        }

        if (htmlData != null) {
            loadFromHtmlData(htmlData);
        } else if (paypalData != null) {
            try {
                loadHtmlWithPostRequest(paypalData);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            try {
                new Thread(new CookieChecker(url, messageHandler)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ((AbstractActivity) getActivity()).getToolbar().getMenu().clear();
        if (title != null && !title.equals(getString(R.string.label_q_and_a_directory))) {
            ((AbstractActivity) getActivity()).getToolbar().setLogo(null);
        }
    }

    private void setUpWebView(final String url) throws URISyntaxException {

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
        mWebView.setWebViewClient(new PetMedWebViewClient(getActivity(), mWebView, url, false, mProgressBar));
        mWebView.setWebChromeClient(client);
        mWebView.loadUrl(url);
    }

    private void loadFromHtmlData(String htmlData) {

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));

        WebChromeClient client = new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, progress);
            }
        };

        mWebView.setWebViewClient(new PetMedWebViewClient(getActivity(), mWebView, htmlData, true, mProgressBar));
        mWebView.setWebChromeClient(client);
        mWebView.loadData(htmlData, "text/html", "UTF-8");
    }

    private void loadHtmlWithPostRequest(String postData) throws URISyntaxException {

        Log.d("URL", postData + ">>>>>");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));

        WebChromeClient client = new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, progress);
            }
        };

        mWebView.setWebViewClient(new Callback());
        mWebView.setWebChromeClient(client);
        mWebView.addJavascriptInterface(new MyJavaScriptInterface(getActivity()), "HtmlViewer");
        mWebView.loadUrl(postData);
    }

    @Override
    public void onDestroyView() {
        deregisterIntent(getContext());
        super.onDestroyView();
    }

    /*This Webview client will be used to capture the paypal response*/
    private class Callback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //this is controversial - see comments and other answers
            String cookieString = null;
            for (Iterator<Cookie> iterator = mCookieCache.iterator(); iterator.hasNext(); ) {
                Cookie cookie = iterator.next();
                if (cookie.name().equals("JSESSIONID")) {
                    cookieString = "JSESSIONID=" + cookie.value() + "; ";
                } else if (cookie.name().equals("SITESERVER")) {
                    cookieString = cookieString + "SITESERVER=" + cookie.value() + "; ";
                }
            }
            //   cookieString = cookieString + "app=true; ";
            CookieManager.getInstance().setCookie(url, cookieString);
            CookieManager.getInstance().getCookie(url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.contains("applyPayPalPaymentMethod")) {
                mWebView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                mWebView.loadUrl("javascript:HtmlViewer.showHTML"
                        + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }

        }
    }

    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            String jsonString = (html.substring(html.indexOf("{"), html.lastIndexOf("}") + 1));
            Gson gson = new GsonBuilder().create();
            ShoppingCartListResponse paypalResponse = gson.fromJson(jsonString, ShoppingCartListResponse.class);
            if (isCheckout) {
                onPaymnetSelectedListener.onCheckoutPaymentCompleted(paypalResponse, mStepName);
            } else {
                onPaymnetSelectedListener.onPaymentCompleted(paypalResponse);
            }
            removeFragment();
        }
    }

    public void removeFragment() {
        Log.d("Remove Fragment", "CommonWebViewFragment");
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();
    }

    public interface OnPaymentCompletedListener {

        void onPaymentCompleted(ShoppingCartListResponse paypalResponse);

        void onCheckoutPaymentCompleted(ShoppingCartListResponse paypalResponse, String stepName);
    }

    class CookieChecker implements Runnable {

        private String url;

        private Handler handler;

        public CookieChecker(String url, Handler handler) {
            this.url = url;
            this.handler = handler;
        }

        @Override
        public void run() {
            Log.e("shouldOverrideUrl", "Cookies before clearing: " + CookieManager.getInstance().getCookie(url));
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            Log.e("shouldOverrideUrl", "Cookies before setcookie: " + CookieManager.getInstance().getCookie(url));

            Map cookieMap = new HashMap();
            for (Iterator<Cookie> iterator = mCookieCache.iterator(); iterator.hasNext(); ) {
                Cookie cookie = iterator.next();
                if (cookie.name().equals(KEY_JESESSION_ID)) {
                    cookieMap.put(KEY_JESESSION_ID, KEY_JESESSION_ID + "=" + cookie.value() + "; ");
                } else if (cookie.name().equals(KEY_SITE_SERVER)) {
                    cookieMap.put(KEY_SITE_SERVER, KEY_SITE_SERVER + "=" + cookie.value() + "; ");
                }
            }
            cookieMap.put(KEY_APP_ID, KEY_APP_ID + "=true; ");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                Log.d("setUpWebView",
                        "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
                try {
                    CookieManager.getInstance().setCookie((url), (String) cookieMap.get(KEY_JESESSION_ID));
                    CookieManager.getInstance().setCookie((url), (String) cookieMap.get(KEY_SITE_SERVER));
                    CookieManager.getInstance().setCookie((url), (String) cookieMap.get(KEY_APP_ID));
                    CookieManager.getInstance().flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.d("setUpWebView",
                        "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
                try {
                    CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(mWebView.getContext());
                    cookieSyncMngr.startSync();
                    CookieManager cManager = CookieManager.getInstance();
                    cManager.setCookie((url), (String) cookieMap.get(KEY_JESESSION_ID));
                    cManager.setCookie((url), (String) cookieMap.get(KEY_SITE_SERVER));
                    cManager.setCookie((url), (String) cookieMap.get(KEY_APP_ID));
                    cookieSyncMngr.stopSync();
                    cookieSyncMngr.sync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.e("shouldOverrideUrl", "Cookies after setcookie: " + CookieManager.getInstance().getCookie(url));

            Message message = Message.obtain(null, KEY_INITIALIZE_COOKIES);
            Bundle b = new Bundle();
            b.putString(URL_KEY, url);
            message.setData(b);
            handler.sendMessage(message);
        }
    }

    private final Handler messageHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            if (msg.what == KEY_INITIALIZE_COOKIES) {
                try {
                    setUpWebView(msg.getData().getString(URL_KEY));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
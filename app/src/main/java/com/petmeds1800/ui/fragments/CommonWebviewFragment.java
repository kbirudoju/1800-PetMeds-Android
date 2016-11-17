package com.petmeds1800.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.checkout.CheckOutActivity;
import com.petmeds1800.util.PetMedWebViewClient;

import java.util.Iterator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Cookie;

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

        //check if back button needs to be enabled
        if (mDisableBackButton) {
            ((AbstractActivity) getActivity()).disableBackButton();
        } else {
            ((AbstractActivity) getActivity()).enableBackButton();
        }

        setHasOptionsMenu(true);

        return rootView;
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
            loadHtmlWithPostRequest(paypalData);
        } else {
            setUpWebView(url);
        }

        ((AbstractActivity) getActivity()).getToolbar().getMenu().clear();
        ((AbstractActivity) getActivity()).getToolbar().setLogo(null);
    }


    private void setUpWebView(final String url) {

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
        mWebView.setWebViewClient(new PetMedWebViewClient(getActivity(),mWebView,url,false));
        mWebView.setWebChromeClient(client);
        mWebView.loadUrl(url);

    }

    private void loadFromHtmlData(String htmlData) {

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
        mWebView.setWebViewClient(new PetMedWebViewClient(getActivity(),mWebView,htmlData,true));
        mWebView.setWebChromeClient(client);
    }


    private void loadHtmlWithPostRequest(String postData) {

        final int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
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
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.contains("applyPayPalPaymentMethod")) {
                mWebView.loadUrl("javascript:HtmlViewer.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

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

    private void removeFragment() {
        Log.d("Remove Fragment","CommonWebViewFragment");
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        if (activity instanceof HomeActivity || activity instanceof CheckOutActivity) {
            try {
                onPaymnetSelectedListener = (OnPaymentCompletedListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement onPaymnetSelectedListener");
            }
        }
    }

    public interface OnPaymentCompletedListener {

        void onPaymentCompleted(ShoppingCartListResponse paypalResponse);

        void onCheckoutPaymentCompleted(ShoppingCartListResponse paypalResponse, String stepName);
    }
}


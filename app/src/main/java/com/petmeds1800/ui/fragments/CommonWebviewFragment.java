package com.petmeds1800.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.petmeds1800.R;
import com.petmeds1800.ui.AbstractActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/25/2016.
 */
public class CommonWebviewFragment extends AbstractFragment {

    public static final String URL_KEY = "url";

    public static final String TITLE_KEY = "title";

    @BindView(R.id.webViewContainer)
    WebView mWebView;
    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    private String mUrl;
    private String mTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common_webview, container, false);
        ButterKnife.bind(this, rootView);
        ((AbstractActivity)getActivity()).enableBackButton();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUrl=getArguments().getString(URL_KEY);
        mTitle=getArguments().getString(TITLE_KEY);
        if(mTitle!=null && !mTitle.isEmpty()){
            ((AbstractActivity)getActivity()).setToolBarTitle(mTitle);
        }
        setUpWebView(mUrl);
       // ((AbstractActivity)getActivity()).enableBackButton();

    }

    private void setUpWebView(String url){
        mWebView.loadUrl(url);
        Log.d("URL", url + ">>>>>");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setBackgroundColor(getResources().getColor(R.color.white));
        WebChromeClient client = new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if(progress == 100) {
                    mProgressBar.setVisibility(View.GONE);

                }
            }
        };
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(client);
    }
}

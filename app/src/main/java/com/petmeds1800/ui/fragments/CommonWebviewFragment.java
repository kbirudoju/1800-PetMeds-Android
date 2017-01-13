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
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Log;
import com.petmeds1800.util.PetMedWebViewClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Cookie;

import static android.app.Activity.RESULT_OK;
import static com.petmeds1800.util.Constants.KEY_APP_ID;
import static com.petmeds1800.util.Constants.KEY_INITIALIZE_COOKIES;
import static com.petmeds1800.util.Constants.KEY_JESESSION_ID;
import static com.petmeds1800.util.Constants.KEY_SITE_SERVER;
import static com.urbanairship.UAirship.getApplicationContext;
import static com.urbanairship.UAirship.getPackageManager;

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

    private static final int INPUT_FILE_REQUEST_CODE = 1;

    private static final String TAG = CommonWebviewFragment.class.getSimpleName();

    private static final String PHOTO_PATH = "PhotoPath";

    private static final String FILE_PATH = "file:";

    private static final String MIME_TYPE = "image/*";

    private static final String IMAGE_CHOOSER = "Image Chooser";

    private static final String PET_MEDS_PICTURES = "PetMedsPictures";

    private static final String JPEG_FORMAT = ".jpg";

    private static final String IMG_STRING = "IMG_";

    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";

    private ValueCallback<Uri[]> mFilePathCallback;

    private Uri mCapturedImageURI = null;

    private String mCameraPhotoPath;

    @BindView(R.id.webViewContainer)
    WebView mWebView;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @Inject
    SetCookieCache mCookieCache;

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private boolean mDisableBackButton;

    private OnPaymentCompletedListener onPaymnetSelectedListener;

    private boolean isCheckout = false;

    private String mStepName;

    private String url;

    private String htmlData;

    private String paypalData;

    private final static int FILECHOOSER_RESULTCODE = 1;

    private ValueCallback<Uri> mUploadMessage;

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

        //start listening for security status lock
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.SECURITY_STATUS_RECEIVED);
        registerIntent(intentFilter, getContext());

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

        url = getArguments().getString(URL_KEY);
        String title = getArguments().getString(TITLE_KEY);
        htmlData = getArguments().getString(HTML_DATA);
        paypalData = getArguments().getString(PAYPAL_DATA);
        isCheckout = getArguments().getBoolean(ISCHECKOUT);
        mStepName = getArguments().getString(STEPNAME);

        if (title != null && !title.isEmpty()) {
            ((AbstractActivity) getActivity()).setToolBarTitle(title);
        }

        //if its homeActivity instance then we should check if we have a securityStatus lock
        if (getActivity() instanceof HomeActivity) {
            if (!mPreferencesHelper.shouldWaitForSecurityStatus()) {
                startLoading(url, htmlData, paypalData);
            }
        } else {
            //loading should get started as in-case of all other activities as here we dont have to wait and check for a securitylock
            //here no security status broadcast would be fired up
            startLoading(url, htmlData, paypalData);
        }

        ((AbstractActivity) getActivity()).getToolbar().getMenu().clear();
        if (title != null && !title.equals(getString(R.string.label_q_and_a_directory))) {
            ((AbstractActivity) getActivity()).getToolbar().setLogo(null);
        }
    }

    private void startLoading(String url, String htmlData, String paypalData) {
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }
                Uri result = null;
                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }

        return;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,
                JPEG_FORMAT,
                storageDir
        );
        return imageFile;
    }

    private void setUpWebView(final String url) throws URISyntaxException {
        mWebView.getSettings().setJavaScriptEnabled(true);
        //For webView goback
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    WebView webView = (WebView) v;
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (webView.canGoBack()) {
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }
                return false;
            }
        });
        mWebView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        WebChromeClient client = new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            // For Android 5.0
            @Override
            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                // Double check that we don't have any existing callbacks
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePath;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra(PHOTO_PATH, mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = FILE_PATH + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType(MIME_TYPE);
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }
                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, IMAGE_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
                return true;
            }

            // openFileChooser for Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                // Create AndroidExampleFolder at sdcard
                File imageStorageDir = new File(
                        Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES)
                        , PET_MEDS_PICTURES);
                if (!imageStorageDir.exists()) {
                    // Create AndroidExampleFolder at sdcard
                    imageStorageDir.mkdirs();
                }
                // Create camera captured image file path and name
                File file = new File(
                        imageStorageDir + File.separator + IMG_STRING
                                + String.valueOf(System.currentTimeMillis())
                                + JPEG_FORMAT);
                Log.d("File", "File: " + file);
                mCapturedImageURI = Uri.fromFile(file);
                // Camera capture image intent
                final Intent captureIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType(MIME_TYPE);
                // Create file chooser intent
                Intent chooserIntent = Intent.createChooser(i, IMAGE_CHOOSER);
                // Set camera intent to file chooser
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                        , new Parcelable[]{captureIntent});
                // On select image call onActivityResult method of activity
                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
            }

            // openFileChooser for Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }

            //openFileChooser for other Android versions
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg, acceptType);
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

    @Override
    protected void onReceivedBroadcast(Context context, Intent intent) {
        checkAndSetHasOptionsMenu(intent, LearnRootFragment.class.getName());
        //add the webview if we have released the security status lock
        if (intent.getAction().equals(Constants.SECURITY_STATUS_RECEIVED)) {
            startLoading(url, htmlData, paypalData);
        }
        super.onReceivedBroadcast(context, intent);
    }
}
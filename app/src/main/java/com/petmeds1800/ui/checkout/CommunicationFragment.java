package com.petmeds1800.ui.checkout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.petmeds1800.R;
import com.petmeds1800.ui.CommonWebViewActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;
import com.petmeds1800.util.Utils;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sdixit on 22-09-2016.
 */

public class CommunicationFragment extends AbstractFragment {

    public static final String REQUEST_CODE_KEY = "code";
    public static final int REQUEST_CODE_VALUE = 1;

    @Override
    public void replaceAccountAndAddToBackStack(Fragment fragment, String tag) {
        super.replaceAccountAndAddToBackStack(fragment, tag);
    }

    public static CommunicationFragment newInstance(int requestCode) {

        Bundle args = new Bundle();
        args.putInt(CommunicationFragment.REQUEST_CODE_KEY, requestCode);
        CommunicationFragment fragment = new CommunicationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communication, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.btn_email_us)
    public void emailUs() {
        Utils.sendEmail(getActivity(), getString(R.string.email_contact_us), null, null);
    }

    @OnClick(R.id.btn_call_us)
    public void callUs() {
        initCallPhonePermissionsWrapper();
    }

    @OnClick(R.id.btn_live_chat)
    public void liveChat() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.title_live_chat));
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_live_chat));
        startActivity(new Intent(getActivity(), CommonWebViewActivity.class).putExtras(bundle));
    }

    private void initCallPhonePermissionsWrapper() {
        int hasCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        if (hasCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            checkRequiredPermission(new String[]{Manifest.permission.CALL_PHONE}, new PermissionRequested() {
                @Override
                public void onPermissionGranted() {
                    makePhoneCall();
                }

                @Override
                public void onPermissionDenied(HashMap<String, Boolean> deniedPermissions) {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Phone call permission denied", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            makePhoneCall();
        }
    }

    private void makePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + getString(R.string.number_phone_toll_free)));
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}

package com.petmeds1800.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.util.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Digvijay on 11/4/2016.
 */

public class ContactUsFragment extends AbstractFragment {

    @BindView(R.id.email_button)
    Button mEmailButton;

    @BindView(R.id.call_button)
    Button mCallButton;

    @BindView(R.id.imv_map)
    ImageView mMapImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AbstractActivity) getActivity()).setToolBarTitle(getString(R.string.title_contact_us_caps));
        ((AbstractActivity) getActivity()).enableBackButton();
        mEmailButton.setVisibility(View.GONE);
        mCallButton.setVisibility(View.GONE);

        String latVet = String.valueOf(26.211818);
        String lngVet = String.valueOf(-80.162765);
        String url = "http://maps.google.com/maps/api/staticmap?center=" + latVet + "," + lngVet + "&zoom=10&size=200x200&markers=color:blue%7Clabel:S%7C11211%7C11206%7C11222&sensor=false";

        Glide.with(this).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(mMapImageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);

                mMapImageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @OnClick(R.id.chat_button)
    public void showChatWindow() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.title_live_chat));
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_live_chat));
        replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.account_root_fragment_container);
    }

    @OnClick(R.id.feedback_button)
    public void shareFeedback() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_share_your_feedback));
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_share_your_feedback));
        replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.account_root_fragment_container);
    }

    @OnClick(R.id.imv_facebook)
    public void facebookPage() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_facebook));
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_join_network_facebook));
        replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.account_root_fragment_container);
    }

    @OnClick(R.id.imv_twitter)
    public void twitterPage() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_twitter));
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_join_network_twitter));
        replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.account_root_fragment_container);
    }

    @OnClick(R.id.imv_google)
    public void googlePage() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_google));
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_join_network_google));
        replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.account_root_fragment_container);
    }

    @OnClick(R.id.imv_linkedin)
    public void linkedinPage() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_linkedin));
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_join_network_linkedin));
        replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.account_root_fragment_container);
    }

    @OnClick(R.id.imv_instagram)
    public void instagramPage() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_instagram));
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_join_network_instagram));
        replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.account_root_fragment_container);
    }

    @OnClick(R.id.imv_youtube)
    public void youtubePage() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_youtube));
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_join_network_youtube));
        replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.account_root_fragment_container);
    }

    @OnClick(R.id.imv_map)
    public void openMapView() {
        double latitude = 26.211818;
        double longitude = -80.162765;
        String label = "Pet Med Express Inc";
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.view_email_address)
    public void sendEmail() {
        Utils.sendEmail(getActivity(), getString(R.string.email_contact_us), null, null);
    }

    @OnClick(R.id.view_call_toll_free)
    public void callPhoneTollFree() {
        initCallPhonePermissionsWrapper(getString(R.string.phone_toll_free_number));
    }

    @OnClick(R.id.view_call_main)
    public void callPhoneMain() {
        initCallPhonePermissionsWrapper(getString(R.string.phone_main));
    }

    @OnClick(R.id.view_call_pharmacy)
    public void callPhonePharmacy() {
        initCallPhonePermissionsWrapper(getString(R.string.phone_pharmacy_number));
    }

    @OnClick(R.id.pharmacy_call_main)
    public void faxPharmacy() {
        initCallPhonePermissionsWrapper(getString(R.string.fax_pharmacy));
    }

    private void initCallPhonePermissionsWrapper(final String phoneNumber) {
        int hasCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        if (hasCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            checkRequiredPermission(new String[]{Manifest.permission.CALL_PHONE}, new PermissionRequested() {
                @Override
                public void onPermissionGranted() {
                    makePhoneCall(phoneNumber);
                }

                @Override
                public void onPermissionDenied(HashMap<String, Boolean> deniedPermissions) {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Phone call permission denied", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            makePhoneCall(phoneNumber);
        }
    }

    private void makePhoneCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}

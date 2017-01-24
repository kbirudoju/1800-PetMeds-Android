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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.model.ContactUs;
import com.petmeds1800.model.SocialInfo;
import com.petmeds1800.mvp.contactustask.ContactUsContract;
import com.petmeds1800.mvp.contactustask.ContactUsPresenter;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.util.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Digvijay on 11/4/2016.
 */

public class ContactUsFragment extends AbstractFragment implements ContactUsContract.View{

    @BindView(R.id.email_button)
    Button mEmailButton;

    @BindView(R.id.call_button)
    Button mCallButton;

    @BindView(R.id.imv_map)
    ImageView mMapImageView;

    @BindView(R.id.toll_free_txt)
    TextView tollFreeTxt;

    @BindView(R.id.main_phone_txt)
    TextView mainPhoneTxt;

    @BindView(R.id.pharmacy_phone_txt)
    TextView pharmacyPhoneTxt;

    @BindView(R.id.pharmacy_fax_txt)
    TextView pharmacyFaxTxt;

    @BindView(R.id.email_txt)
    TextView emailIdTxt;

    @BindView(R.id.email_description)
    TextView emialDescription;

    @BindView(R.id.mailing_address_detail)
    TextView mailingAddress;

    @BindView(R.id.contact_us_container)
    LinearLayout mContactUsContainer;

    private ContactUsContract.Presenter mPresenter;

    private SocialInfo mSocialInfo;

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
        mEmailButton.setVisibility(View.GONE);
        mCallButton.setVisibility(View.GONE);

        mPresenter=new ContactUsPresenter(this);
        ((HomeActivity)getActivity()).showProgress();
        mPresenter.start();

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

    @Override
    public void onResume() {
        super.onResume();
        ((AbstractActivity) getActivity()).enableBackButton();
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
        if(mSocialInfo!=null) {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_facebook));
            bundle.putString(CommonWebviewFragment.URL_KEY, mSocialInfo.getFacebook());
            replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.account_root_fragment_container);
        }
    }

    @OnClick(R.id.imv_twitter)
    public void twitterPage() {
        if (mSocialInfo != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_twitter));
            bundle.putString(CommonWebviewFragment.URL_KEY, mSocialInfo.getTwitter());
            replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.account_root_fragment_container);
        }
    }
    @OnClick(R.id.imv_google)
    public void googlePage() {
        if (mSocialInfo != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_google_plus));
            bundle.putString(CommonWebviewFragment.URL_KEY, mSocialInfo.getGooglePlus());
            replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.account_root_fragment_container);
        }
    }

    @OnClick(R.id.imv_linkedin)
    public void linkedinPage() {
        if (mSocialInfo != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_linkedin));
            bundle.putString(CommonWebviewFragment.URL_KEY, mSocialInfo.getLinkedIn());
            replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.account_root_fragment_container);
        }
    }

    @OnClick(R.id.imv_instagram)
    public void instagramPage() {
        if (mSocialInfo != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_instagram));
            bundle.putString(CommonWebviewFragment.URL_KEY, mSocialInfo.getInstagram());
            replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.account_root_fragment_container);
        }
    }

    @OnClick(R.id.imv_youtube)
    public void youtubePage() {
        if (mSocialInfo != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_youtube));
            bundle.putString(CommonWebviewFragment.URL_KEY, mSocialInfo.getYoutube());
            replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.account_root_fragment_container);
        }
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
        initCallPhonePermissionsWrapper(getString(R.string.number_phone_toll_free));
    }

    @OnClick(R.id.view_call_main)
    public void callPhoneMain() {
        initCallPhonePermissionsWrapper(getString(R.string.number_phone_main));
    }

    @OnClick(R.id.view_call_pharmacy)
    public void callPhonePharmacy() {
        initCallPhonePermissionsWrapper(getString(R.string.number_phone_pharmacy));
    }

   /* @OnClick(R.id.pharmacy_call_main)
    public void faxPharmacy() {
        initCallPhonePermissionsWrapper(getString(R.string.label_fax_pharmacy));
    }*/

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
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setContactData(ContactUs contactUs) {
        ((HomeActivity)getActivity()).hideProgress();
        if(contactUs!=null){
            mainPhoneTxt.setText(contactUs.getMainPhoneNumber());
            tollFreeTxt.setText(contactUs.getTollFreeNumber());
            pharmacyFaxTxt.setText(contactUs.getPharmacyFaxNumber());
            pharmacyPhoneTxt.setText(contactUs.getPharmacyPhoneNumber());
            emailIdTxt.setText(contactUs.getEmailAddress());
            emialDescription.setText(contactUs.getEmailMessage());
            mailingAddress.setText(contactUs.getMailingAddress());
            mSocialInfo=contactUs.getSocial();
        }
    }

    @Override
    public void onError(String errorMessage) {
        ((HomeActivity)getActivity()).hideProgress();
        Utils.displayCrouton(getActivity(), errorMessage, mContactUsContainer);

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(ContactUsContract.Presenter presenter) {
        mPresenter = presenter;
    }
}

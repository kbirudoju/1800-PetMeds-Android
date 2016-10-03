package com.petmeds1800.ui.fragments;

import com.petmeds1800.R;
import com.petmeds1800.ui.ConfirmationReceiptActivity;
import com.petmeds1800.util.LayoutPrintingUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Digvijay on 9/28/2016.
 */
public class ConfirmationReceiptFragment extends AbstractFragment {

    OnShareButtonClickListener mCallback;

    @BindView(R.id.root_view)
    ScrollView mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_confirmation_receipt, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnShareButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    ConfirmationReceiptActivity.class.getSimpleName() + " must implement OnShareButtonClickListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_confirmation_receipt, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            mCallback.onShareButtonClick();
        }
        return super.onOptionsItemSelected(item);
    }

    public File generatePdf() {
        LayoutPrintingUtils layoutPrintingUtils = new LayoutPrintingUtils();
        Bitmap bitmap = layoutPrintingUtils.getBitmapFromView(mRootView);
        if (bitmap != null) {
            File pdfFile = layoutPrintingUtils.printViewToPdf("receipt", bitmap);
            if (pdfFile != null) {
                return pdfFile;
            }
        }
        return null;
    }

    public void shareFile(File pdfFile, String pdfName) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, "receiver_email_address");
        email.putExtra(Intent.EXTRA_SUBJECT, "subject");
        email.putExtra(Intent.EXTRA_TEXT, "email body");
        Uri uri = Uri.fromFile(new File(pdfFile, pdfName + ".pdf"));
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("application/pdf");
        email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(email);
    }

    public interface OnShareButtonClickListener {

        void onShareButtonClick();
    }
}

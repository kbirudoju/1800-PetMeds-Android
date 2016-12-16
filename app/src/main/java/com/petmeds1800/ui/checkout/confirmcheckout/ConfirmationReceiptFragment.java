package com.petmeds1800.ui.checkout.confirmcheckout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.petmeds1800.R;
import com.petmeds1800.intent.ForgotPasswordIntent;
import com.petmeds1800.model.entities.CommitOrder;
import com.petmeds1800.model.entities.CommitOrderResponse;
import com.petmeds1800.model.entities.Item;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.checkout.stepfive.StepFiveRootFragment;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.util.AnalyticsUtil;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.LayoutPrintingUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Digvijay on 9/28/2016.
 */
public class ConfirmationReceiptFragment extends AbstractFragment {

    @BindView(R.id.root_view)
    ScrollView mRootView;

    @BindView(R.id.txv_first_name)
    TextView mFirstName;

    @BindView(R.id.txv_order_id)
    TextView mOrderId;

    @BindView(R.id.txv_email)
    TextView mEmail;

    @BindView(R.id.txv_shipping_method)
    TextView mShippingMethod;

    @BindView(R.id.txv_subtotal)
    TextView mSubtotal;

    @BindView(R.id.txv_total)
    TextView mTotal;

    @BindView(R.id.discount)
    TextView mDiscount;

    @BindView(R.id.txv_shipping)
    TextView mShippingTotal;

    @BindView(R.id.txv_taxes)
    TextView mTaxes;

    @BindView(R.id.txv_label_subtotal)
    TextView mSubtotalLabel;

    @BindView(R.id.recycler_items)
    RecyclerView mRecyclerReceiptItems;

    @BindView(R.id.view_error)
    View mErrorView;

    private ReceiptItemsListAdapter mListAdapter;

    protected List<Item> mReceiptItemList = new ArrayList<>();


    public static ConfirmationReceiptFragment newInstance(CommitOrderResponse commitOrderResponse) {

        Bundle args = new Bundle();

        ConfirmationReceiptFragment fragment = new ConfirmationReceiptFragment();
        args.putSerializable(Constants.CONFIRMATION_ORDER_RESPONSE, commitOrderResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_confirmation_receipt, container, false);
        ButterKnife.bind(this, view);
        ((HomeActivity) getActivity()).updateCartTabItemCount();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_confirmation_receipt, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            initStoragePermissionsWrapper();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerReceiptItems.setLayoutManager(layoutManager);
        mListAdapter = new ReceiptItemsListAdapter(mReceiptItemList, getContext());
        mRecyclerReceiptItems.setAdapter(mListAdapter);
        populateReceiptData();
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

    private void initStoragePermissionsWrapper() {
        int hasStorageAccessPermission = ContextCompat
                .checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasStorageAccessPermission != PackageManager.PERMISSION_GRANTED) {
            checkRequiredPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    new PermissionRequested() {
                        @Override
                        public void onPermissionGranted() {
                            showShareOptions();
                        }

                        @Override
                        public void onPermissionDenied(HashMap<String, Boolean> deniedPermissions) {
                            // Permission Denied
                            Toast.makeText(getActivity(), "Storage access denied", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            showShareOptions();
        }
    }

    private void showShareOptions() {
        hideErrorView();
        File pdfFile = generatePdf();
        if (pdfFile != null) {
            shareFile(pdfFile, "receipt");
        }
    }

    private void sendAnalyticsData(CommitOrder commitOrder) {
        new AnalyticsUtil()
                .trackScreenForProductTransaction(getString(R.string.label_order_receipt_analytics_title), commitOrder,
                        getActivity().getApplicationContext());
    }

    private void populateReceiptData() {

        CommitOrderResponse commitOrderResponse = (CommitOrderResponse) getArguments()
                .getSerializable(Constants.CONFIRMATION_ORDER_RESPONSE);

        CommitOrder order = null;
        if (commitOrderResponse != null) {
            String errorCode = commitOrderResponse.getStatus().getCode();
            order = commitOrderResponse.getOrder();
            mFirstName.setText(order.getFirstName());
            mOrderId.setText(order.getMpOrderNumber());
            mEmail.setText(order.getEmail());
            mShippingMethod.setText(order.getShippingMethod());
            Locale locale = getResources().getConfiguration().locale;
            mSubtotal.setText(StepFiveRootFragment.DOLLAR_SIGN + String.format(locale, "%.2f", order.getOrderSubTotal()));
            mDiscount.setText(StepFiveRootFragment.MINUS_SIGN + StepFiveRootFragment.DOLLAR_SIGN + String.format(locale, "%.2f", order.getDiscount()));
            mShippingTotal.setText(StepFiveRootFragment.DOLLAR_SIGN + String.format(locale, "%.2f", order.getShippingTotal()));
            mTaxes.setText(StepFiveRootFragment.DOLLAR_SIGN + String.format(locale, "%.2f", order.getTaxTotal()));
            mTotal.setText(StepFiveRootFragment.DOLLAR_SIGN + String.format(locale, "%.2f", order.getOrderTotal()));
            mSubtotalLabel.setText(getString(R.string.items_formatter, order.getItems().size()));
            sendAnalyticsData(order);
            updateRecyclerView(order.getItems());
            if (errorCode.equals(BasePresenter.API_WARNING_CODE)) {
                showErrorView(Html.fromHtml(commitOrderResponse.getStatus().getErrorMessages().get(0)));
            }
        }
    }

    private void updateRecyclerView(List<Item> itemList) {
        if (itemList != null) {
            mReceiptItemList.clear();
            mReceiptItemList.addAll(itemList);
            mListAdapter.notifyDataSetChanged();
        }
    }

    private void showErrorView(CharSequence message) {
        mErrorView.setVisibility(View.VISIBLE);
        TextView title = (TextView) mErrorView.findViewById(R.id.txv_error_title);
        TextView description = (TextView) mErrorView.findViewById(R.id.txv_error_message);
        title.setText(getString(R.string.label_unable_to_update_password));
        description.setText(message);
        View resetPasswordButton = mErrorView.findViewById(R.id.view_reset_password);
        resetPasswordButton.setVisibility(View.VISIBLE);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new ForgotPasswordIntent(getActivity()));
            }
        });
        description.setText(message);
    }

    private void hideErrorView() {
        mErrorView.setVisibility(View.GONE);
        mErrorView.findViewById(R.id.view_reset_password).setVisibility(View.GONE);
    }
}

package com.petmeds1800.ui.orders;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.AddToCartRequest;
import com.petmeds1800.model.ReOrderRequest;
import com.petmeds1800.model.entities.CommerceItems;
import com.petmeds1800.model.entities.OrderDetailHeader;
import com.petmeds1800.model.entities.OrderList;
import com.petmeds1800.model.entities.PaymentGroup;
import com.petmeds1800.model.entities.ShippingGroup;
import com.petmeds1800.model.entities.WebViewHeader;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;
import com.petmeds1800.ui.fragments.dialog.BaseDialogFragment;
import com.petmeds1800.ui.fragments.dialog.OkCancelDialogFragment;
import com.petmeds1800.ui.orders.presenter.OrderDetailPresenter;
import com.petmeds1800.ui.orders.support.CustomOrderDetailRecyclerAdapter;
import com.petmeds1800.ui.orders.support.OrderDetailAdapter;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.LayoutPrintingUtils;
import com.petmeds1800.util.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/19/2016.
 */
public class OrderDetailFragment extends AbstractFragment implements OrderDetailContract.View {

    @BindView(R.id.scroll_view)
    ScrollView mScrollView;

    @BindView(R.id.linear_container)
    LinearLayout mPrintViewsLinearLayout;

    @BindView(R.id.order_detail_recycler_view)
    RecyclerView mOrderDetailRecyclerView;

    private OrderDetailAdapter mOrderDetailAdapter;

    private OrderDetailContract.Presenter mPresenter;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private OrderList orderList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new OrderDetailPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            orderList = (OrderList) bundle.getSerializable("orderlist");

        }
        mOrderDetailAdapter = new OrderDetailAdapter(getActivity(), orderList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("view id is", v.toString());
                int position = mOrderDetailRecyclerView.getChildAdapterPosition(v);
                WebViewHeader webviewRow = (WebViewHeader) mOrderDetailAdapter.getItemAt(position);
                switch (webviewRow.getId()) {
                    case CustomOrderDetailRecyclerAdapter.REVIEW_ROW_ID:
                        String skuId = webviewRow.getItemId();
                        String productId = webviewRow.getProductId();
                        Bundle bundle = new Bundle();
                        bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.title_my_orders));
                        bundle.putString(CommonWebviewFragment.URL_KEY, getActivity().getString(R.string.server_endpoint) + "/product.jsp?id=" + productId + "&sku=" + skuId + "&review=write");
                        replaceAccountFragmentWithBundle(new CommonWebviewFragment(), bundle);
                        break;
                    case CustomOrderDetailRecyclerAdapter.TRACK_ROW_ID:
                        String trackingId = orderList.getShippingGroups().get(0).getTrackingNumber();
                        String vendorName = orderList.getShippingGroups().get(0).getCompanyName();
                        Bundle shippingBundle = new Bundle();
                        shippingBundle.putString("vendorName", vendorName);
                        shippingBundle.putString("trackingId", trackingId);
                        replaceAccountFragmentWithBundle(new TrackShipmentFragment(), shippingBundle);
                        break;
                    case CustomOrderDetailRecyclerAdapter.REORDER_ENTIRE_ORDER_ROW_ID:
                        ReOrderRequest reOrderRequest = new ReOrderRequest(orderList.getOrderId(), mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
                        try {
                            ((AbstractActivity) getActivity()).startLoadingGif(getActivity());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mPresenter.reOrder(reOrderRequest);
                        break;
                    case CustomOrderDetailRecyclerAdapter.CANCEL_ORDER_ROW_ID:
                        if (orderList.getIsCancellable().equals("true")) {

                            final OkCancelDialogFragment okCancelDialogFragment = new OkCancelDialogFragment().newInstance(getString(R.string.cancel_order_msg) + orderList.getOrderId(), getString(R.string.cancel_order_title), getString(R.string.dialog_ok_button), getString(R.string.dialog_cancel_button));
                            okCancelDialogFragment.show(((AbstractActivity) getActivity()).getSupportFragmentManager());
                            okCancelDialogFragment.setPositiveListener(new BaseDialogFragment.DialogButtonsListener() {
                                @Override
                                public void onDialogButtonClick(DialogFragment dialog, String buttonName) {
                                    ReOrderRequest cancelOrderRequest = new ReOrderRequest(orderList.getOrderId(), mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
                                    try {
                                        ((AbstractActivity) getActivity()).startLoadingGif(getActivity());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    mPresenter.cancelOrder(cancelOrderRequest);

                                }
                            });
                            okCancelDialogFragment.setNegativeListener(new BaseDialogFragment.DialogButtonsListener() {
                                @Override
                                public void onDialogButtonClick(DialogFragment dialog, String buttonName) {
                                    okCancelDialogFragment.dismiss();
                                }
                            });

                        } else {
                            Snackbar.make(mOrderDetailRecyclerView, getString(R.string.cancel_order_error_msg), Snackbar.LENGTH_LONG).show();
                        }
                        break;
                    case CustomOrderDetailRecyclerAdapter.REORDER_ITEM_ROW_ID:
                        try {
                            ((AbstractActivity) getActivity()).startLoadingGif(getActivity());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String reOrderProductId = webviewRow.getProductId();
                        String reOrderSkuId = webviewRow.getItemId();
                        int quantity = webviewRow.getQuantity();
                        AddToCartRequest addToCartRequest = new AddToCartRequest(reOrderSkuId, reOrderProductId, quantity, mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
                        mPresenter.addToCart(addToCartRequest);
                        break;
                }

            }
        });
        setRecyclerView();
        setTitle();
        List<Object> mData = mOrderDetailAdapter.setData(orderList);
        prepareListViewContentForPrinting(mData);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ViewTreeObserver viewTreeObserver = mPrintViewsLinearLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this);
                mScrollView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            initStoragePermissionsWrapper();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTitle() {
        if (orderList != null)
            ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.order_txt) + " #" + orderList.getOrderId());
    }

    private void setRecyclerView() {
        mOrderDetailRecyclerView.setAdapter(mOrderDetailAdapter);
        mOrderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderDetailRecyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onError(String errorMessage) {
        Snackbar.make(mOrderDetailRecyclerView, errorMessage, Snackbar.LENGTH_LONG).show();
        try {
            ((AbstractActivity) getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccess() {
        try {
            ((AbstractActivity) getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((HomeActivity) getActivity()).getViewPager().setCurrentItem(1);
    }

    @Override
    public void addToCartSuccess() {
        try {
            ((AbstractActivity) getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelSuccess() {
        try {
            ((AbstractActivity) getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Snackbar.make(mOrderDetailRecyclerView, R.string.order_cancelled_success_msg, Snackbar.LENGTH_LONG).show();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();
    }

    @Override
    public void setPresenter(OrderDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void initStoragePermissionsWrapper() {
        int hasStorageAccessPermission = ContextCompat
                .checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasStorageAccessPermission != PackageManager.PERMISSION_GRANTED) {
            checkRequiredPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionRequested() {
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
        File pdfFile = generatePdf();
        if (pdfFile != null) {
            shareFile(pdfFile, "receipt");
        }
    }

    public File generatePdf() {
        LayoutPrintingUtils layoutPrintingUtils = new LayoutPrintingUtils();
        Bitmap bitmap = layoutPrintingUtils.getBitmapFromView(mScrollView);
        if (bitmap != null) {
            File pdfFile = layoutPrintingUtils.printViewToPdf("receipt", bitmap);
            if (pdfFile != null) {
                return pdfFile;
            }
        }
        return null;
    }

    public void shareFile(File pdfFile, String pdfName) {
        Utils.sendEmail(getActivity(), null, null, null, pdfFile, pdfName);
    }

    private LinearLayout prepareListViewContentForPrinting(List<Object> data) {

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < data.size(); i++) {
            int viewType = mOrderDetailAdapter.getItemViewType(i);
            switch (viewType) {
                case OrderDetailAdapter.VIEW_TYPE_HEADER:
                    LinearLayout headerView = (LinearLayout) layoutInflater.inflate(R.layout.view_order_detail_header_row, null);
                    OrderDetailHeader header = (OrderDetailHeader) data.get(i);
                    ((TextView) headerView.getChildAt(0)).setText(header.getHeader());
                    mPrintViewsLinearLayout.addView(headerView);
                    break;

                case OrderDetailAdapter.VIEW_TYPE_PRODUCT:
                    LinearLayout productView = (LinearLayout) layoutInflater.inflate(R.layout.view_order_detail_product_row, null);
                    CommerceItems commerceItem = (CommerceItems) data.get(i);
                    ((TextView) productView.findViewById(R.id.product_price_label)).setText("$" + commerceItem.getAmount());
                    ((TextView) productView.findViewById(R.id.product_name_label)).setText(commerceItem.getProductName());
                    ((TextView) productView.findViewById(R.id.quantity_label)).setText(getString(R.string.quantity_txt) + commerceItem.getQuantity());
                    ((TextView) productView.findViewById(R.id.item_description)).setText(commerceItem.getSkuName());
                    final ImageView productImage = (ImageView) productView.findViewById(R.id.product_image);
                    Glide.with(this).load(getString(R.string.server_endpoint) + commerceItem.getSkuImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(productImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            productImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    TextView petNameLabel = ((TextView) productView.findViewById(R.id.pet_name_label));
                    if (commerceItem.getPetName() != null && !commerceItem.getPetName().isEmpty()) {
                        petNameLabel.setText(getString(R.string.pet_txt) + commerceItem.getPetName());
                    } else {
                        petNameLabel.setVisibility(View.GONE);
                    }

                    TextView vetNameLabel = ((TextView) productView.findViewById(R.id.vet_name_label));
                    if (commerceItem.getVetName() != null && !commerceItem.getVetName().isEmpty()) {
                        vetNameLabel.setText(getString(R.string.vet_txt) + commerceItem.getVetName());
                    } else {
                        vetNameLabel.setVisibility(View.GONE);
                    }
                    mPrintViewsLinearLayout.addView(productView);
                    break;

                case OrderDetailAdapter.VIEW_TYPE_SHIPPING:
                    LinearLayout shippingView = (LinearLayout) layoutInflater.inflate(R.layout.view_order_detail_shipping_row, null);
                    ShippingGroup shippingDetail = (ShippingGroup) data.get(i);
                    ((TextView) shippingView.findViewById(R.id.shipping_method_label)).setText(shippingDetail.getShippingMethod());

                    TextView shippingAddressLabel = ((TextView) shippingView.findViewById(R.id.shipping_address_label));
                    if (shippingDetail.getAddress2() != null && !shippingDetail.getAddress2().isEmpty()) {
                        shippingAddressLabel.setText(shippingDetail.getAddress1() + "," + shippingDetail.getAddress2());
                    } else {
                        shippingAddressLabel.setText(shippingDetail.getAddress1());
                    }
                    mPrintViewsLinearLayout.addView(shippingView);
                    break;

                case OrderDetailAdapter.VIEW_TYPE_FIXED:
                    LinearLayout fixedView = (LinearLayout) layoutInflater.inflate(R.layout.view_order_detail_webview_row, null);
                    WebViewHeader webViewHeader = (WebViewHeader) data.get(i);
                    ((TextView) fixedView.findViewById(R.id.webview_header_label)).setText(webViewHeader.getWebviewHeader());
                    mPrintViewsLinearLayout.addView(fixedView);
                    break;

                case OrderDetailAdapter.VIEW_TYPE_PAYMENT:
                    LinearLayout paymentView = (LinearLayout) layoutInflater.inflate(R.layout.view_order_detail_payment_row, null);
                    PaymentGroup paymentInfo = (PaymentGroup) data.get(i);
                    TextView billingAddressLabel = ((TextView) paymentView.findViewById(R.id.billing_address_label));
                    if (paymentInfo.getAddress2() != null && !paymentInfo.getAddress2().isEmpty()) {
                        billingAddressLabel.setText(paymentInfo.getAddress1() + "," + paymentInfo.getAddress2());
                    } else {
                        billingAddressLabel.setText(paymentInfo.getAddress1());
                    }
                    ((TextView) paymentView.findViewById(R.id.payment_method_label)).setText(paymentInfo.getPaymentMethod());
                    mPrintViewsLinearLayout.addView(paymentView);
                    break;

                case OrderDetailAdapter.VIEW_TYPE_INFO:
                    LinearLayout infoView = (LinearLayout) layoutInflater.inflate(R.layout.view_order_detail_info_row, null);
                    OrderList orderInfo = (OrderList) data.get(i);
                    ((TextView) infoView.findViewById(R.id.order_no_label)).setText(orderInfo.getOrderId());
                    ((TextView) infoView.findViewById(R.id.order_date_label)).setText(orderInfo.getSubmittedDate());
                    ((TextView) infoView.findViewById(R.id.ship_to_label)).setText(orderInfo.getShipTo());
                    ((TextView) infoView.findViewById(R.id.order_total_label)).setText("$" + String.valueOf(orderInfo.getTotal()));
                    TextView orderStatusLabel = ((TextView) infoView.findViewById(R.id.status_label));
                    orderStatusLabel.setText(orderInfo.getStatus());

                    //temporary hardcoded value to check layout, it will be changed after confirmation from backend
                    if (orderInfo.getStatus().equalsIgnoreCase("PROCESSING")) {
                        ((TextView) infoView.findViewById(R.id.status_label)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_shipping, 0, 0, 0);
                        orderStatusLabel.setBackgroundResource(R.drawable.yellow_rounded_button);
                    } else if (orderInfo.getStatus().equalsIgnoreCase("Cancelled")) {
                        orderStatusLabel.setBackgroundResource(R.drawable.red_rounded_button);
                        orderStatusLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_cancelled, 0, 0, 0);
                    } else {
                        orderStatusLabel.setBackgroundResource(R.drawable.green_rounded_button);
                    }
                    mPrintViewsLinearLayout.addView(infoView);
                    break;
            }
        }

        //Add some padding below before printing
        mPrintViewsLinearLayout.setPadding(0, 0, 0, 20);

        return mPrintViewsLinearLayout;
    }
}
package com.petmeds1800.ui.dashboard.support;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.BrowsingHistory;
import com.petmeds1800.model.entities.Category;
import com.petmeds1800.model.entities.PetItemList;
import com.petmeds1800.model.entities.Products;
import com.petmeds1800.model.entities.RecommendedCategory;
import com.petmeds1800.model.entities.RecommendedProducts;
import com.petmeds1800.model.entities.RefillItem;
import com.petmeds1800.model.entities.SalePitch;
import com.petmeds1800.model.entities.WhatsNextCategory;
import com.petmeds1800.model.entities.WidgetData;
import com.petmeds1800.model.entities.WidgetFooter;
import com.petmeds1800.ui.dashboard.WidgetListFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 9/15/2016.
 */
public class WidgetListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> mData;
    public final static int BANNER_VIEW_TYPE = 1;
    public final static int REFILL_HEADER_VIEW_TYPE = 2;
    public final static int REFILL_PRODUCT_VIEW_TYPE = 3;
    public final static int RECOMENDATION_HEADER_VIEW_TYPE = 4;
    public final static int TIP_VIEW_TYPE = 5;
    public final static int WHATS_NEXT_VIEW_TYPE = 6;
    public final static int SALES_PITCH_VIEW_TYPE = 7;
    public final static int BROWSE_HISTORY_HEADER_VIEW_TYPE = 8;
    public final static int BROWSE_HISTORY_VIEW_TYPE = 9;
    public final static int RECOMMENDATION_PRODUCT_DETAIL = 10;
    public final static int RECOMMENDATION_VIEW_MORE_PRODUCT = 11;
    public final static int VIEW_FOOTER = 12;
    private WidgetListFragment mContext;
    private View.OnClickListener listener;

    public WidgetListAdapter(WidgetListFragment context, View.OnClickListener listener) {
        this.mContext = context;
        this.listener = listener;
    }

    public void setData(List<Object> widgetListData) {
        this.mData = widgetListData;
        notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == BANNER_VIEW_TYPE) {
            int resource = R.layout.view_banner;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new BannerViewHolder(v);
        } else if (viewType == REFILL_HEADER_VIEW_TYPE) {
            int resource = R.layout.view_refill_header;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new RefillHeaderViewHolder(v);
        } else if (viewType == REFILL_PRODUCT_VIEW_TYPE) {
            int resource = R.layout.view_refill_product;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            Button addCartBtn = (Button) v.findViewById(R.id.add_cart_button);
            addCartBtn.setOnClickListener(listener);
            viewHolder = new RefillViewHolder(v);
        } else if (viewType == RECOMENDATION_HEADER_VIEW_TYPE) {
            int resource = R.layout.view_recommendation;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new RecommendationViewHolder(v);
        } else if (viewType == RECOMMENDATION_PRODUCT_DETAIL) {
            int resource = R.layout.view_recommendation_product_detail;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new RecommendationProductViewHolder(v);
            RelativeLayout recommendationView=(RelativeLayout)v.findViewById(R.id.recommendationView);
            recommendationView.setOnClickListener(listener);
        } else if (viewType == RECOMMENDATION_VIEW_MORE_PRODUCT) {
            int resource = R.layout.view_more_recommendation;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new RecommendationMoreViewHolder(v);
            TextView moreRecommendationLabel=(TextView)v.findViewById(R.id.see_product_label);
            moreRecommendationLabel.setOnClickListener(listener);
        } else if (viewType == TIP_VIEW_TYPE) {
            int resource = R.layout.view_tip;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new TipViewHolder(v);
            RelativeLayout learnMoreLayout=(RelativeLayout)v.findViewById(R.id.learnMoreView);
            learnMoreLayout.setOnClickListener(listener);
        } else if (viewType == WHATS_NEXT_VIEW_TYPE) {
            int resource = R.layout.view_whats_next;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            RelativeLayout exploreMoreLayout=(RelativeLayout)v.findViewById(R.id.exploreMoreView);
            exploreMoreLayout.setOnClickListener(listener);
            viewHolder = new WhatsNextViewHolder(v);
        } else if (viewType == SALES_PITCH_VIEW_TYPE) {
            int resource = R.layout.view_sales_pitch;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            RelativeLayout salesPitchView=(RelativeLayout)v.findViewById(R.id.sales_pitch_layout);
            salesPitchView.setOnClickListener(listener);
            viewHolder = new SalesPitchViewHolder(v);
        } else if (viewType == BROWSE_HISTORY_VIEW_TYPE) {
            int resource = R.layout.view_shopping_history;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new BrowseHistoryViewHolder(v);
            RelativeLayout shoppingHistoryLayout=(RelativeLayout) v.findViewById(R.id.shoppingHistorylayout);
            shoppingHistoryLayout.setOnClickListener(listener);
        } else if (viewType == BROWSE_HISTORY_HEADER_VIEW_TYPE) {
            int resource = R.layout.view_shopping_history_header;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new BrowseHistoryHeaderViewHolder(v);
        } else if (viewType == VIEW_FOOTER) {
            int resource = R.layout.view_widget_footer;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            ((Button) v.findViewById(R.id.email_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mContext.getResources().getString(R.string.customer_care_email_id)});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                    //need this to prompts email client only
                    emailIntent.setType("message/rfc822");
                    try {
                        mContext.startActivity(Intent.createChooser(emailIntent, mContext.getResources().getString(R.string.choose_email_client)));
                    } catch (android.content.ActivityNotFoundException ex) {

                    }
                }
            });

            ((Button) v.findViewById(R.id.call_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mContext.getString(R.string.number_phone_toll_free)));
                    try {
                        mContext.startActivity(intent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            (v.findViewById(R.id.feedback_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonWebviewFragment.TITLE_KEY, mContext.getString(R.string.label_share_your_feedback));
                    bundle.putString(CommonWebviewFragment.URL_KEY, mContext.getString(R.string.url_share_your_feedback));
                    mContext.startWebView(bundle);
                }
            });

            (v.findViewById(R.id.chat_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonWebviewFragment.TITLE_KEY, mContext.getString(R.string.title_live_chat));
                    bundle.putString(CommonWebviewFragment.URL_KEY, mContext.getString(R.string.url_live_chat));
                    mContext.startWebView(bundle);
                }
            });

            (v.findViewById(R.id.imv_facebook)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonWebviewFragment.TITLE_KEY, mContext.getString(R.string.label_facebook));
                    bundle.putString(CommonWebviewFragment.URL_KEY, mContext.getString(R.string.url_join_network_facebook));
                    mContext.startWebView(bundle);
                }
            });

            (v.findViewById(R.id.imv_twitter)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonWebviewFragment.TITLE_KEY, mContext.getString(R.string.label_twitter));
                    bundle.putString(CommonWebviewFragment.URL_KEY, mContext.getString(R.string.url_join_network_twitter));
                    mContext.startWebView(bundle);
                }
            });

            (v.findViewById(R.id.imv_google)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonWebviewFragment.TITLE_KEY, mContext.getString(R.string.label_google));
                    bundle.putString(CommonWebviewFragment.URL_KEY, mContext.getString(R.string.url_join_network_google));
                    mContext.startWebView(bundle);
                }
            });

            (v.findViewById(R.id.imv_linkedin)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonWebviewFragment.TITLE_KEY, mContext.getString(R.string.label_linkedin));
                    bundle.putString(CommonWebviewFragment.URL_KEY, mContext.getString(R.string.url_join_network_linkedin));
                    mContext.startWebView(bundle);
                }
            });

            (v.findViewById(R.id.imv_instagram)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonWebviewFragment.TITLE_KEY, mContext.getString(R.string.label_instagram));
                    bundle.putString(CommonWebviewFragment.URL_KEY, mContext.getString(R.string.url_join_network_instagram));
                    mContext.startWebView(bundle);
                }
            });

            (v.findViewById(R.id.imv_youtube)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonWebviewFragment.TITLE_KEY, mContext.getString(R.string.label_youtube));
                    bundle.putString(CommonWebviewFragment.URL_KEY, mContext.getString(R.string.url_join_network_youtube));
                    mContext.startWebView(bundle);
                }
            });
            viewHolder = new FooterViewHolder(v);
        }


        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case BANNER_VIEW_TYPE:
                BannerViewHolder bannerHolder = (BannerViewHolder) holder;
                String url = (String) getItemAt(position);
                bannerHolder.bannerWebView.loadUrl(url);
                bannerHolder.bannerWebView.getSettings().setJavaScriptEnabled(true);
                bannerHolder.bannerWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                bannerHolder.bannerWebView.setWebViewClient(new WebViewClient());
                break;
            case REFILL_HEADER_VIEW_TYPE:
                final RefillHeaderViewHolder refillHeaderHolder = (RefillHeaderViewHolder) holder;
                RefillItem refillItem = (RefillItem) getItemAt(position);
                refillHeaderHolder.refillPetName.setText(refillItem.getPetName());
                refillHeaderHolder.refillTitleLabel.setText(refillItem.getWidgetTitle());
                Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + refillItem.getPetImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(refillHeaderHolder.refillPetImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        refillHeaderHolder.refillPetImage.setImageDrawable(circularBitmapDrawable);
                    }
                });

                break;
            case REFILL_PRODUCT_VIEW_TYPE:
                final RefillViewHolder refillHolder = (RefillViewHolder) holder;
                PetItemList petItem = (PetItemList) getItemAt(position);
                if(petItem.getSku().getPriceInfo().getPromoText()!=null && !petItem.getSku().getPriceInfo().getPromoText().isEmpty()){
                   refillHolder.row_coupons_layout.setVisibility(View.VISIBLE);
                    refillHolder.noCouponRefillLayout.setVisibility(View.GONE);
                    refillHolder.refillProductCouponsLabel.setText(petItem.getSku().getDisplayName());
                    refillHolder.refillDateCouponLabel.setText(mContext.getString(R.string.due_on_txt) + " " + petItem.getDueDate());
                    refillHolder.refillStartCouponLabel.setPaintFlags(refillHolder.refillStartCouponLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    refillHolder.refillOriginalPriceCouponLabel.setText(" $" + petItem.getSku().getPriceInfo().getSellingPrice());
                    refillHolder.refillStartCouponLabel.setText(" $" + petItem.getSku().getPriceInfo().getListPrice());
                    refillHolder.refillPromoLabel.setText(petItem.getSku().getPriceInfo().getPromoText());
                    Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + petItem.getSku().getParentProduct().getProductImage()).asBitmap().centerCrop().into(new BitmapImageViewTarget(refillHolder.refillCouponProductImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            refillHolder.refillCouponProductImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }else {
                    refillHolder.row_coupons_layout.setVisibility(View.GONE);
                    refillHolder.noCouponRefillLayout.setVisibility(View.VISIBLE);
                    refillHolder.refillProductTitleLabel.setText(petItem.getSku().getDisplayName());
                    refillHolder.refillOriginalPriceLabel.setText(" $" + petItem.getSku().getPriceInfo().getListPrice());
                    refillHolder.refillOriginalPriceLabel.setPaintFlags(refillHolder.refillOriginalPriceLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    refillHolder.refillSellingpriceLabel.setText(" $" + petItem.getSku().getPriceInfo().getSellingPrice());
                    refillHolder.refillDateLabel.setText(mContext.getString(R.string.due_on_txt) + " " + petItem.getDueDate());
                    Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + petItem.getSku().getParentProduct().getProductImage()).asBitmap().centerCrop().into(new BitmapImageViewTarget(refillHolder.refillProductImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            refillHolder.refillProductImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
                refillHolder.refillAddCartButton.setTag(petItem);
                break;
            case RECOMENDATION_HEADER_VIEW_TYPE:
                final RecommendationViewHolder recommendationViewHolder = (RecommendationViewHolder) holder;
                RecommendedCategory recommendedCategory = (RecommendedCategory) getItemAt(position);
                recommendationViewHolder.doctorNameLabel.setText(recommendedCategory.getDoctorName());

                if(recommendedCategory.getCategory().getCategoryIntro() != null) {
                    recommendationViewHolder.quoteLabel.setText(
                            "[img src=ic_quote_left/]" + " " + recommendedCategory.getCategory().getCategoryIntro() + "  "
                                    + "[img src=ic_quote_right/]");

                }
                else {
                    recommendationViewHolder.quoteLabel.setText(
                            "[img src=ic_quote_left/]" + " " + recommendedCategory.getDoctorQuote() + "  "
                                    + "[img src=ic_quote_right/]");
                }

                recommendationViewHolder.recommendatipnTitleLabel.setText(recommendedCategory.getWidgetTitle());
                recommendationViewHolder.recommendationPetNameLabel.setText(recommendedCategory.getPetName());
                Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + recommendedCategory.getDoctorPictureUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(recommendationViewHolder.doctorImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        recommendationViewHolder.doctorImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
                Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + recommendedCategory.getPetImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(recommendationViewHolder.recommendationPetImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        recommendationViewHolder.recommendationPetImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
                break;
            case RECOMMENDATION_PRODUCT_DETAIL:
                final RecommendationProductViewHolder recommendationProductViewHolder = (RecommendationProductViewHolder) holder;
                RecommendedProducts recommendedProducts = (RecommendedProducts) getItemAt(position);
                Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + recommendedProducts.getProductImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(recommendationProductViewHolder.recommendationProductImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        recommendationProductViewHolder.recommendationProductImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
                recommendationProductViewHolder.recommendationProductLabel.setText(recommendedProducts.getDisplayName());
                //show layout as per promotext
                if(recommendedProducts.getPromoText()!=null && !recommendedProducts.getPromoText().isEmpty()){
                    recommendationProductViewHolder.promoViewlayout.setVisibility(View.VISIBLE);
                    recommendationProductViewHolder.noPromoViewLayout.setVisibility(View.GONE);
                    recommendationProductViewHolder.promoTextLabel.setText(recommendedProducts.getPromoText());
                    recommendationProductViewHolder.listPriceLabel.setText(" $" + recommendedProducts.getListPrice());
                    recommendationProductViewHolder.listPriceLabel.setPaintFlags(recommendationProductViewHolder.recommendationPriceLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    recommendationProductViewHolder.startsAtLabel.setText(recommendedProducts.getPriceLabel());
                    recommendationProductViewHolder.sellingPriceLabel.setText(" $" +recommendedProducts.getSellingPrice());
                }else {
                    recommendationProductViewHolder.promoViewlayout.setVisibility(View.GONE);
                    recommendationProductViewHolder.noPromoViewLayout.setVisibility(View.VISIBLE);
                    recommendationProductViewHolder.recommendationPriceLabel.setText(" $" + recommendedProducts.getListPrice());
                    recommendationProductViewHolder.recommendationPriceLabel.setPaintFlags(recommendationProductViewHolder.recommendationPriceLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    recommendationProductViewHolder.recommendationSellingPriceLabel.setText(" $" + recommendedProducts.getSellingPrice());
                }
               recommendationProductViewHolder.recommendationView.setTag(recommendedProducts);
                break;
            case RECOMMENDATION_VIEW_MORE_PRODUCT:
                RecommendationMoreViewHolder moreViewHolder = (RecommendationMoreViewHolder) holder;
                Category category = (Category) getItemAt(position);
                moreViewHolder.seeMoreLabel.setText(mContext.getString(R.string.see_all_txt) + category.getDisplayName() + " " + mContext.getString(R.string.products_txt));
                moreViewHolder.seeMoreLabel.setTag(category);
                break;
            case TIP_VIEW_TYPE:
                final TipViewHolder tipViewHolder = (TipViewHolder) holder;
                WidgetData widgetData = (WidgetData) getItemAt(position);
                tipViewHolder.contentLabel.setText(Html.fromHtml(widgetData.getContent()));
                tipViewHolder.tipTitleLabel.setText(widgetData.getWidgetTitle());
                tipViewHolder.tipDoctorLabel.setText(widgetData.getDoctorName());
                Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + widgetData.getDoctorPictureUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(tipViewHolder.tipDoctorImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        tipViewHolder.tipDoctorImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
                tipViewHolder.learnViewLayout.setTag(widgetData);

                break;
            case SALES_PITCH_VIEW_TYPE:
                final SalesPitchViewHolder salesPitchViewHolder = (SalesPitchViewHolder) holder;
                SalePitch salesPitch = (SalePitch) getItemAt(position);
                Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + salesPitch.getImage().getUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(salesPitchViewHolder.salesPitchImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        salesPitchViewHolder.salesPitchImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
                salesPitchViewHolder.salesPitchlayout.setTag(salesPitch);
                break;
            case WHATS_NEXT_VIEW_TYPE:
                final WhatsNextViewHolder whatsNextViewHolder = (WhatsNextViewHolder) holder;
                WhatsNextCategory whatsNextCategory = (WhatsNextCategory) getItemAt(position);
                whatsNextViewHolder.whatsNextTitle.setText(whatsNextCategory.getDisplayName());
                whatsNextViewHolder.subtitleWhatsNext.setText(mContext.getString(R.string.explore_txt) + " " + whatsNextCategory.getDisplayName() + mContext.getString(R.string.products_txt));

                if (whatsNextCategory.getBannerImagePath() != null) {
                    Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + whatsNextCategory.getBannerImagePath()).asBitmap().centerCrop().into(new BitmapImageViewTarget(whatsNextViewHolder.whatsNextImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            whatsNextViewHolder.whatsNextImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
                whatsNextViewHolder.exploreMoreLayout.setTag(whatsNextCategory);
                break;
            case BROWSE_HISTORY_VIEW_TYPE:
                final BrowseHistoryViewHolder browseHistoryViewHolder = (BrowseHistoryViewHolder) holder;
                Products shoppingProducts = (Products) getItemAt(position);
                browseHistoryViewHolder.productTitlelabel.setText(shoppingProducts.getDisplayName());
                if(shoppingProducts.getPromoText()!=null && !shoppingProducts.getPromoText().isEmpty()){
                    browseHistoryViewHolder.promoViewlayout.setVisibility(View.VISIBLE);
                    browseHistoryViewHolder.noPromoViewLayout.setVisibility(View.GONE);
                    browseHistoryViewHolder.promoTextLabel.setText(shoppingProducts.getPromoText());
                    browseHistoryViewHolder.listPriceLabel.setText(" $" + shoppingProducts.getListPrice());
                    browseHistoryViewHolder.listPriceLabel.setPaintFlags(browseHistoryViewHolder.listPriceLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    browseHistoryViewHolder.startsAtLabel.setText(shoppingProducts.getPriceLabel());
                    browseHistoryViewHolder.sellingPriceLabel.setText(" $" +shoppingProducts.getSellingPrice());
                } else {
                    browseHistoryViewHolder.promoViewlayout.setVisibility(View.GONE);
                    browseHistoryViewHolder.noPromoViewLayout.setVisibility(View.VISIBLE);
                    browseHistoryViewHolder.productPriceLabel.setText(" $" + shoppingProducts.getListPrice());
                    browseHistoryViewHolder.productPriceLabel.setPaintFlags(browseHistoryViewHolder.productPriceLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    browseHistoryViewHolder.productSellingPriceLabel.setText(" $" + shoppingProducts.getSellingPrice());


                }

                Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + shoppingProducts.getProductImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(browseHistoryViewHolder.productImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        browseHistoryViewHolder.productImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
browseHistoryViewHolder.shoppingHistoryLayout.setTag(shoppingProducts);
                break;
            case BROWSE_HISTORY_HEADER_VIEW_TYPE:
                final BrowseHistoryHeaderViewHolder browseHistoryHeaderViewHolder = (BrowseHistoryHeaderViewHolder) holder;
                BrowsingHistory browseHistory = (BrowsingHistory) getItemAt(position);
                browseHistoryHeaderViewHolder.shoppingHistoryTitleLabel.setText(browseHistory.getWidgetTitle());
                break;
            case VIEW_FOOTER:

                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        } else {
            return mData.size();

        }
    }


    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.banner_webview)
        WebView bannerWebView;

        public BannerViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public static class BrowseHistoryHeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_shopping_history_label)
        TextView shoppingHistoryTitleLabel;

        public BrowseHistoryHeaderViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public static class RefillViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.row_pet_product)
        RelativeLayout noCouponRefillLayout;

        @BindView(R.id.refill_view_product_image)
        ImageView refillProductImage;
        @BindView(R.id.product_detail_layout)
        LinearLayout productDetailView;
        @BindView(R.id.refill_view_product_label)
        TextView refillProductTitleLabel;
        @BindView(R.id.refill_view_date_label)
        TextView refillDateLabel;
        @BindView(R.id.refill_view_original_price_label)
        TextView refillOriginalPriceLabel;
        @BindView(R.id.refill_view_sell_price_label)
        TextView refillSellingpriceLabel;
        @BindView(R.id.add_cart_button)
        Button refillAddCartButton;

        @BindView(R.id.row_pet_product_coupons)
        RelativeLayout row_coupons_layout;
        @BindView(R.id.refill_view_product_label_coupons)
        TextView refillProductCouponsLabel;
        @BindView(R.id.refill_view_date_label_coupons)
        TextView refillDateCouponLabel;
        @BindView(R.id.refill_view_start_label_coupons)
        TextView refillStartCouponLabel;
        @BindView(R.id.refill_view_original_price_label_coupons)
        TextView  refillOriginalPriceCouponLabel;
        @BindView(R.id.promotext)
        TextView refillPromoLabel;
        @BindView(R.id.refill_view_product_image_coupons)
        ImageView refillCouponProductImage;




        public RefillViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public static class RefillHeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_pet_image)
        RelativeLayout petImageRow;
        @BindView(R.id.refill_view_pet_name)
        TextView refillPetName;
        @BindView(R.id.refill_view_pet_image)
        ImageView refillPetImage;
        @BindView(R.id.refill_view_title_label)
        TextView refillTitleLabel;

        public RefillHeaderViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public static class RecommendationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recommendation_pet_image)
        ImageView recommendationPetImage;

        @BindView(R.id.recommendation_title_label)
        TextView recommendatipnTitleLabel;

        @BindView(R.id.pet_name_label)
        TextView recommendationPetNameLabel;

        @BindView(R.id.quote_label)
        TextView quoteLabel;
        @BindView(R.id.doctor_name_label)
        TextView doctorNameLabel;

        @BindView(R.id.doctor_image)
        ImageView doctorImage;


        public RecommendationViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public static class RecommendationProductViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.product_image)
        ImageView recommendationProductImage;

        @BindView(R.id.product_title_label)
        TextView recommendationProductLabel;

        @BindView(R.id.product_price_label)
        TextView recommendationPriceLabel;

        @BindView(R.id.product_selling_price_label)
        TextView recommendationSellingPriceLabel;

        @BindView(R.id.starts_label)
        TextView startsAtLabel;

        @BindView(R.id.promotext_view)
        LinearLayout promoViewlayout;

        @BindView(R.id.noPromo_view)
        LinearLayout noPromoViewLayout;

        @BindView(R.id.promotext)
        TextView promoTextLabel;

        @BindView(R.id.selling_price)
        TextView sellingPriceLabel;

        @BindView(R.id.list_price_label)
        TextView listPriceLabel;

        @BindView(R.id.recommendationView)
        RelativeLayout recommendationView;



        public RecommendationProductViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }

    }

    public static class RecommendationMoreViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.see_product_label)
        TextView seeMoreLabel;


        public RecommendationMoreViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public static class TipViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.learn_more_label)
        TextView learnMoreLabel;

        @BindView(R.id.content_label)
        TextView contentLabel;

        @BindView(R.id.tip_doctor_name_label)
        TextView tipDoctorLabel;

        @BindView(R.id.tip_title_label)
        TextView tipTitleLabel;

        @BindView(R.id.tip_doctor_image)
        ImageView tipDoctorImage;

        @BindView(R.id.learnMoreView)
        RelativeLayout learnViewLayout;

        public TipViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public static class WhatsNextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_whats_next)
        TextView whatsNextTitle;
        @BindView(R.id.whats_next_image)
        ImageView whatsNextImage;
        @BindView(R.id.subtitle_whats_next)
        TextView subtitleWhatsNext;
        @BindView(R.id.exploreMoreView)
        RelativeLayout exploreMoreLayout;

        public WhatsNextViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public static class SalesPitchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sales_pitch_image)
        ImageView salesPitchImage;

        @BindView(R.id.sales_pitch_layout)
        RelativeLayout salesPitchlayout;

      /*  @BindView(R.id.title_sales_pitch_label)
        TextView salesPitchTitle;*/

   /*     @BindView(R.id.price_sales_pitch_label)
        TextView alesPitchPriceLabel;*/

        public SalesPitchViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public static class BrowseHistoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.product_selling_price_label)
        TextView productSellingPriceLabel;
        @BindView(R.id.product_price_label)
        TextView productPriceLabel;
        @BindView(R.id.product_title_label)
        TextView productTitlelabel;
        @BindView(R.id.product_image)
        ImageView productImage;

        @BindView(R.id.starts_label)
        TextView startsAtLabel;

        @BindView(R.id.promotext_view)
        LinearLayout promoViewlayout;

        @BindView(R.id.noPromo_view)
        LinearLayout noPromoViewLayout;

        @BindView(R.id.promotext)
        TextView promoTextLabel;

        @BindView(R.id.selling_price)
        TextView sellingPriceLabel;

        @BindView(R.id.list_price_label)
        TextView listPriceLabel;

        @BindView(R.id.shoppingHistorylayout)
        RelativeLayout shoppingHistoryLayout;

        public BrowseHistoryViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (getItemAt(position) instanceof RefillItem)
            return REFILL_HEADER_VIEW_TYPE;
        if (getItemAt(position) instanceof PetItemList)
            return REFILL_PRODUCT_VIEW_TYPE;
        if (getItemAt(position) instanceof SalePitch)
            return SALES_PITCH_VIEW_TYPE;
        if (getItemAt(position) instanceof WhatsNextCategory)
            return WHATS_NEXT_VIEW_TYPE;
        if (getItemAt(position) instanceof BrowsingHistory)
            return BROWSE_HISTORY_HEADER_VIEW_TYPE;
        if (getItemAt(position) instanceof Products)
            return BROWSE_HISTORY_VIEW_TYPE;
        if (getItemAt(position) instanceof RecommendedCategory)
            return RECOMENDATION_HEADER_VIEW_TYPE;
        if (getItemAt(position) instanceof RecommendedProducts)
            return RECOMMENDATION_PRODUCT_DETAIL;
        if (getItemAt(position) instanceof Category)
            return RECOMMENDATION_VIEW_MORE_PRODUCT;
        if (getItemAt(position) instanceof WidgetData)
            return TIP_VIEW_TYPE;
        if (getItemAt(position) instanceof String)
            return BANNER_VIEW_TYPE;
        if (getItemAt(position) instanceof WidgetFooter)
            return VIEW_FOOTER;

        return -1;

    }

    public Object getItemAt(int position) {
        return mData.get(position);
    }
}

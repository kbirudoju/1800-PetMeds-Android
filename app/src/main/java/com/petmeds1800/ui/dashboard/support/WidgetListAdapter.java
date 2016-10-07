package com.petmeds1800.ui.dashboard.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
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
    public final static int RECOMMENDATION_PRODUCT_DETAIL=10;
    public final static int RECOMMENDATION_VIEW_MORE_PRODUCT=11;
    public final static int VIEW_FOOTER=12;
    private Context mContext;
    private View.OnClickListener listener;
    public WidgetListAdapter(Context context,View.OnClickListener listener){
        this.mContext=context;
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
        } else if(viewType == REFILL_HEADER_VIEW_TYPE){
            int resource = R.layout.view_refill_header;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new RefillHeaderViewHolder(v);
        }else if(viewType == REFILL_PRODUCT_VIEW_TYPE){
            int resource = R.layout.view_refill_product;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            Button addCartBtn= (Button)v.findViewById(R.id.add_cart_button);
            addCartBtn.setOnClickListener(listener);
            viewHolder = new RefillViewHolder(v);
        }else if(viewType == RECOMENDATION_HEADER_VIEW_TYPE){
            int resource = R.layout.view_recommendation;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new RecommendationViewHolder(v);
        }else if(viewType == RECOMMENDATION_PRODUCT_DETAIL){
            int resource = R.layout.view_recommendation_product_detail;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new RecommendationProductViewHolder(v);
        }else if(viewType == RECOMMENDATION_VIEW_MORE_PRODUCT){
            int resource = R.layout.view_more_recommendation;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new RecommendationMoreViewHolder(v);
        }else if(viewType == TIP_VIEW_TYPE){
            int resource = R.layout.view_tip;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new TipViewHolder(v);
        }else if(viewType == WHATS_NEXT_VIEW_TYPE){
            int resource = R.layout.view_whats_next;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new WhatsNextViewHolder(v);
        }else if(viewType == SALES_PITCH_VIEW_TYPE){
            int resource = R.layout.view_sales_pitch;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new SalesPitchViewHolder(v);
        }else if(viewType == BROWSE_HISTORY_VIEW_TYPE){
            int resource = R.layout.view_shopping_history;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new BrowseHistoryViewHolder(v);
        }else if(viewType == BROWSE_HISTORY_HEADER_VIEW_TYPE){
            int resource = R.layout.view_shopping_history_header;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new BrowseHistoryHeaderViewHolder(v);
        }else if(viewType == VIEW_FOOTER){
            int resource = R.layout.view_widget_footer;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
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
                String url= (String)getItemAt(position);
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
                Glide.with(mContext).load(mContext.getString(R.string.server_endpoint)+refillItem.getPetImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(refillHeaderHolder.refillPetImage) {
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
                refillHolder.refillProductTitleLabel.setText(petItem.getSku().getDisplayName());
                refillHolder.refillOriginalPriceLabel.setText(" $"+petItem.getSku().getPriceInfo().getListPrice());
                refillHolder.refillOriginalPriceLabel.setPaintFlags(refillHolder.refillOriginalPriceLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                refillHolder.refillSellingpriceLabel.setText(" $" + petItem.getSku().getPriceInfo().getSellingPrice());
                refillHolder.refillDateLabel.setText(mContext.getString(R.string.due_on_txt)+" "+petItem.getDueDate());
                Glide.with(mContext).load(mContext.getString(R.string.server_endpoint)+petItem.getSku().getParentProduct().getProductImage()).asBitmap().centerCrop().into(new BitmapImageViewTarget(refillHolder.refillProductImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        refillHolder.refillProductImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
                refillHolder.refillAddCartButton.setTag(petItem);
                break;
            case RECOMENDATION_HEADER_VIEW_TYPE:
                final RecommendationViewHolder recommendationViewHolder = (RecommendationViewHolder) holder;
                RecommendedCategory recommendedCategory =(RecommendedCategory)getItemAt(position);
                recommendationViewHolder.doctorNameLabel.setText(recommendedCategory.getDoctorName());
                recommendationViewHolder.quoteLabel.setText("[img src=ic_quote_left/]"+" "+recommendedCategory.getDoctorQuote()+"  "+"[img src=ic_quote_right/]");
                recommendationViewHolder.recommendatipnTitleLabel.setText(recommendedCategory.getWidgetTitle());
                recommendationViewHolder.recommendationPetNameLabel.setText(recommendedCategory.getPetName());
                Glide.with(mContext).load(mContext.getString(R.string.server_endpoint)+recommendedCategory.getDoctorPictureUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(recommendationViewHolder.doctorImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        recommendationViewHolder.doctorImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
                Glide.with(mContext).load(mContext.getString(R.string.server_endpoint)+recommendedCategory.getPetImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(recommendationViewHolder.recommendationPetImage) {
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
                final RecommendationProductViewHolder recommendationProductViewHolder=(RecommendationProductViewHolder)holder;
                RecommendedProducts recommendedProducts= (RecommendedProducts) getItemAt(position);
                Glide.with(mContext).load(mContext.getString(R.string.server_endpoint)+recommendedProducts.getProductImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(recommendationProductViewHolder.recommendationProductImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        recommendationProductViewHolder.recommendationProductImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
                recommendationProductViewHolder.recommendationProductLabel.setText(recommendedProducts.getDisplayName());
                recommendationProductViewHolder.recommendationPriceLabel.setText(" $"+recommendedProducts.getListPrice());
                recommendationProductViewHolder.recommendationPriceLabel.setPaintFlags(recommendationProductViewHolder.recommendationPriceLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                recommendationProductViewHolder.recommendationSellingPriceLabel.setText(" $"+recommendedProducts.getSellingPrice());
                break;
            case RECOMMENDATION_VIEW_MORE_PRODUCT:
                RecommendationMoreViewHolder moreViewHolder =(RecommendationMoreViewHolder)holder;
                Category category =(Category)getItemAt(position);
                moreViewHolder.seeMoreLabel.setText(mContext.getString(R.string.see_all_txt) + category.getDisplayName() +" " +mContext.getString(R.string.products_txt));
                break;
            case TIP_VIEW_TYPE:
                final  TipViewHolder tipViewHolder = (TipViewHolder) holder;
                WidgetData widgetData= (WidgetData)getItemAt(position);
                tipViewHolder.contentLabel.setText(Html.fromHtml(widgetData.getContent()));
                tipViewHolder.tipTitleLabel.setText(widgetData.getWidgetTitle());
                tipViewHolder.tipDoctorLabel.setText(widgetData.getDoctorName());
                Glide.with(mContext).load(mContext.getString(R.string.server_endpoint)+widgetData.getDoctorPictureUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(tipViewHolder.tipDoctorImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        tipViewHolder.tipDoctorImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
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


                break;
            case WHATS_NEXT_VIEW_TYPE:
                final WhatsNextViewHolder whatsNextViewHolder = (WhatsNextViewHolder) holder;
                WhatsNextCategory whatsNextCategory = (WhatsNextCategory) getItemAt(position);
                whatsNextViewHolder.whatsNextTitle.setText(whatsNextCategory.getDisplayName());
                whatsNextViewHolder.subtitleWhatsNext.setText(mContext.getString(R.string.explore_txt)+" "+whatsNextCategory.getDisplayName()+mContext.getString(R.string.products_txt));

                if(whatsNextCategory.getBannerImagePath()!=null) {
                    Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + whatsNextCategory.getBannerImagePath()).asBitmap().centerCrop().into(new BitmapImageViewTarget(whatsNextViewHolder.whatsNextImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            whatsNextViewHolder.whatsNextImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
                break;
            case BROWSE_HISTORY_VIEW_TYPE:
                final BrowseHistoryViewHolder browseHistoryViewHolder = (BrowseHistoryViewHolder) holder;
                Products shoppingProducts= (Products)getItemAt(position);
                browseHistoryViewHolder.productPriceLabel.setText(" $"+shoppingProducts.getListPrice());
                browseHistoryViewHolder.productPriceLabel.setPaintFlags(browseHistoryViewHolder.productPriceLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                browseHistoryViewHolder.productSellingPriceLabel.setText(" $"+shoppingProducts.getSellingPrice());
                browseHistoryViewHolder.productTitlelabel.setText(shoppingProducts.getDisplayName());
                Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + shoppingProducts.getProductImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(browseHistoryViewHolder.productImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        browseHistoryViewHolder.productImage.setImageDrawable(circularBitmapDrawable);
                    }
                });

                break;
            case BROWSE_HISTORY_HEADER_VIEW_TYPE:
                final BrowseHistoryHeaderViewHolder browseHistoryHeaderViewHolder = (BrowseHistoryHeaderViewHolder) holder;
                BrowsingHistory browseHistory=(BrowsingHistory)getItemAt(position);
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
        if(mData==null){
            return 0;
        }else{
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
        RelativeLayout petProductRow;

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
        public WhatsNextViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }
    public static class SalesPitchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sales_pitch_image)
        ImageView salesPitchImage;

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
        TextView  productPriceLabel;
        @BindView(R.id.product_title_label)
        TextView productTitlelabel;
        @BindView(R.id.product_image)
        ImageView productImage;

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
        if(getItemAt(position) instanceof RecommendedCategory)
            return RECOMENDATION_HEADER_VIEW_TYPE;
        if(getItemAt(position) instanceof RecommendedProducts)
            return RECOMMENDATION_PRODUCT_DETAIL;
        if(getItemAt(position) instanceof Category)
            return RECOMMENDATION_VIEW_MORE_PRODUCT;
        if(getItemAt(position) instanceof WidgetData)
            return TIP_VIEW_TYPE;
        if(getItemAt(position) instanceof String)
            return BANNER_VIEW_TYPE;
        if(getItemAt(position) instanceof WidgetFooter)
            return VIEW_FOOTER;

        return -1;

    }

    public Object getItemAt(int position) {
        return mData.get(position);
    }
}

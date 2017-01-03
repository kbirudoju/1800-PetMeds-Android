package com.petmeds1800.ui.dashboard.presenter;

import android.support.annotation.NonNull;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.AddToCartRequest;
import com.petmeds1800.model.entities.BrowsingHistory;
import com.petmeds1800.model.entities.PetItemList;
import com.petmeds1800.model.entities.Products;
import com.petmeds1800.model.entities.RecentlyOrdered;
import com.petmeds1800.model.entities.RecentlyOrderedTitle;
import com.petmeds1800.model.entities.RecommendedCategory;
import com.petmeds1800.model.entities.RecommendedProducts;
import com.petmeds1800.model.entities.RefillItem;
import com.petmeds1800.model.entities.SalePitch;
import com.petmeds1800.model.entities.WhatsNextCategory;
import com.petmeds1800.model.entities.Widget;
import com.petmeds1800.model.entities.WidgetFooter;
import com.petmeds1800.model.entities.WidgetListResponse;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.RetrofitErrorHandler;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 9/15/2016.
 */
public class WidgetPresenter implements WidgetContract.Presenter{
    private WidgetContract.View mView;
    private List<Object> mData;

    @Inject
    PetMedsApiService mApiService;

    public WidgetPresenter(@NonNull WidgetContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);

    }

    @Override
    public void getWidgetListData() {
        mView.showProgress();
        mApiService.getWidgetData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WidgetListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgress();
                        if(mView.isActive()){
                            int errorId = RetrofitErrorHandler.getErrorMessage(e);
                            if (errorId == R.string.noInternetConnection || errorId == R.string.connectionTimeout) {
                                mView.showRetryView();
                            }else{
                                //error handling would be implemented once we get the details from backend team
                                mView.showErrorCrouton(e.getLocalizedMessage(), true);
                            }
                        }
                    }

                    @Override
                    public void onNext(WidgetListResponse s) {
                        mView.hideProgress();
                        if(mView.isActive()){
                            if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                                mView.onSuccess(formatWidgetData(s.getWidgets()));
                            } else {
                                mView.showErrorCrouton(s.getStatus().getErrorMessages().get(0), true);
                            }
                        }
                    }
                });


    }

    @Override
    public void addToCart(AddToCartRequest addToCartRequest) {
        mView.showProgress();
        mApiService.addToCart(addToCartRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShoppingCartListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgress();
                        //error handling would be implemented once we get the details from backend team
                        mView.onAddCartError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(ShoppingCartListResponse s) {
                        mView.hideProgress();
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.addToCartSuccess();
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onAddCartError(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });


    }

    @Override
    public void start() {
        mData= new ArrayList<>();
        getWidgetListData();
    }

    private List<Object> formatWidgetData(ArrayList<Widget> widgetData) {
        for (int widgetCount = 0; widgetCount < widgetData.size(); widgetCount++) {
            if (widgetData.get(widgetCount).getWidgetType().equalsIgnoreCase(Constants.VIEW_TYPE_REFILL)) {
                ArrayList<RefillItem> refillItem = widgetData.get(widgetCount).getData().getRefillItems();
                for (int refillCount = 0; refillCount < refillItem.size(); refillCount++) {
                    //Add Refill Header
                    RefillItem refillListItem=refillItem.get(refillCount);
                    refillListItem.setWidgetTitle(widgetData.get(widgetCount).getData().getWidgetTitle());
                    mData.add(refillListItem);
                    ArrayList<PetItemList> petItemList = refillItem.get(refillCount).getPetItemList();
                    for (int petCount = 0; petCount < petItemList.size(); petCount++) {
                        PetItemList petItem = petItemList.get(petCount);
                        //Add Refill Pet Product
                        mData.add(petItem);
                    }

                }
            }
            //Add recently Ordered data
            if(widgetData.get(widgetCount).getWidgetType().equalsIgnoreCase(Constants.VIEW_TYPE_RECENTLY_ORDERED)){
                ArrayList<RecentlyOrdered> recentlyOrderedItem=widgetData.get(widgetCount).getData().getRecentlyOrderedItems();
                RecentlyOrderedTitle recentlyOrderedTitle=new RecentlyOrderedTitle(widgetData.get(widgetCount).getData().getWidgetTitle());
                mData.add(recentlyOrderedTitle);
              for(int recentOrderCount =0;recentOrderCount<recentlyOrderedItem.size();recentOrderCount++){
                  //Add RecentlyOrdred data
                 RecentlyOrdered recentlyOrdered= recentlyOrderedItem.get(recentOrderCount);
                  mData.add(recentlyOrdered);

              }
            }
            //Add salespitch data
            if (widgetData.get(widgetCount).getWidgetType().equalsIgnoreCase(Constants.VIEW_TYPE_SALES_PITCH)) {
                SalePitch salesPitchData = widgetData.get(widgetCount).getData().getSalesPitch();
                mData.add(salesPitchData);
            }
            if (widgetData.get(widgetCount).getWidgetType().equalsIgnoreCase(Constants.VIEW_TYPE_WHATS_NEXT)) {
                WhatsNextCategory whatsNextData = widgetData.get(widgetCount).getData().getWhatsNextCategory();
                mData.add(whatsNextData);
            }

           if(widgetData.get(widgetCount).getWidgetType().equalsIgnoreCase(Constants.VIEW_TYPE_BROWSE_HISTORY)){
                BrowsingHistory browseHistory=widgetData.get(widgetCount).getData().getBrowsingHistory();
               browseHistory.setWidgetTitle(widgetData.get(widgetCount).getData().getWidgetTitle());
                mData.add(browseHistory);
                ArrayList<Products> products=browseHistory.getProducts();
                for(int productCount=0;productCount<products.size();productCount++){
                    Products shoppingItem =products.get(productCount);
                    mData.add(shoppingItem);
                }
            }
            //Add recommendation data
            if(widgetData.get(widgetCount).getWidgetType().equalsIgnoreCase(Constants.VIEW_TYPE_RECOMMENDATIONS)){
                RecommendedCategory recommendedCategory=widgetData.get(widgetCount).getData().getRecommendedCategory();
                recommendedCategory.setRecommendationTitle(widgetData.get(widgetCount).getData().getWidgetTitle());
                mData.add(recommendedCategory);
                ArrayList<RecommendedProducts> recommendedProductsList=recommendedCategory.getProductList();
                for(int recommendedProductsCount=0;recommendedProductsCount<recommendedProductsList.size();recommendedProductsCount++){
                    RecommendedProducts recommendedProducts= recommendedProductsList.get(recommendedProductsCount);
                    mData.add(recommendedProducts);
                }
                //Add see more product Object
                mData.add(recommendedCategory.getCategory());

            }
//Add Tip Data
            if(widgetData.get(widgetCount).getWidgetType().equalsIgnoreCase(Constants.VIEW_TYPE_TIP)){
               mData.add(widgetData.get(widgetCount).getData());
            }

            if(widgetData.get(widgetCount).getWidgetType().equalsIgnoreCase(Constants.VIEW_TYPE_BANNER)){
                mData.add(widgetData.get(widgetCount).getData().getBannerUrl());
            }

            }
        mData.add(new WidgetFooter());
        return mData;
    }
}

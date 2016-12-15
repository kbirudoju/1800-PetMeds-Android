package com.petmeds1800.model.entities;

import java.util.ArrayList;

/**
 * Created by pooja on 9/13/2016.
 */
public class WidgetData {
    //Banner
    private String widgetTitle;
    private String altText;
    private String BannerUrl;
    // Tip Data
    private String id;
    private String content;
    private String learnMoreUrl;
    private String name;
    private String doctorPictureUrl;
    private String doctorQuote;
    private String doctorName;
    //Browsinghistory
    private BrowsingHistory browsingHistory;
    //Recommendations
    private RecommendedCategory recommendedCategory;
    //Refill
    private ArrayList<RefillItem> refillItems;
    //what's Next
    private WhatsNextCategory whatsNextCategory;
    //Sales Pitch
    private SalePitch SalesPitch;

    public ArrayList<RecentlyOrdered> getRecentlyOrderedItems() {
        return recentlyOrderedItems;
    }

    public void setRecentlyOrderedItems(ArrayList<RecentlyOrdered> recentlyOrderedItems) {
        this.recentlyOrderedItems = recentlyOrderedItems;
    }

    //Recently Ordered
    private ArrayList<RecentlyOrdered> recentlyOrderedItems;
    public RecentlyOrderedTitle getRecentlyOrderedWidgetTitle() {
        return recentlyOrderedWidgetTitle;
    }

    public void setRecentlyOrderedWidgetTitle(RecentlyOrderedTitle recentlyOrderedWidgetTitle) {
        this.recentlyOrderedWidgetTitle = recentlyOrderedWidgetTitle;
    }

    private RecentlyOrderedTitle recentlyOrderedWidgetTitle;

    public BrowsingHistory getBrowsingHistory() {
        return browsingHistory;
    }

    public void setBrowsingHistory(BrowsingHistory browsingHistory) {
        this.browsingHistory = browsingHistory;
    }

    public String getDoctorPictureUrl() {
        return doctorPictureUrl;
    }

    public void setDoctorPictureUrl(String doctorPictureUrl) {
        this.doctorPictureUrl = doctorPictureUrl;
    }

    public String getDoctorQuote() {
        return doctorQuote;
    }

    public void setDoctorQuote(String doctorQuote) {
        this.doctorQuote = doctorQuote;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getWidgetTitle() {
        return widgetTitle;
    }

    public void setWidgetTitle(String widgetTitle) {
        this.widgetTitle = widgetTitle;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getBannerUrl() {
        return BannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        BannerUrl = bannerUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLearnMoreUrl() {
        return learnMoreUrl;
    }

    public void setLearnMoreUrl(String learnMoreUrl) {
        this.learnMoreUrl = learnMoreUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public RecommendedCategory getRecommendedCategory() {
        return recommendedCategory;
    }

    public void setRecommendedCategory(RecommendedCategory recommendedCategory) {
        this.recommendedCategory = recommendedCategory;
    }

    public ArrayList<RefillItem> getRefillItems() {
        return refillItems;
    }

    public void setRefillItems(ArrayList<RefillItem> refillItems) {
        this.refillItems = refillItems;
    }

    public WhatsNextCategory getWhatsNextCategory() {
        return whatsNextCategory;
    }

    public void setWhatsNextCategory(WhatsNextCategory whatsNextCategory) {
        this.whatsNextCategory = whatsNextCategory;
    }

    public SalePitch getSalesPitch() {
        return SalesPitch;
    }

    public void setSalesPitch(SalePitch salesPitch) {
        SalesPitch = salesPitch;
    }
}

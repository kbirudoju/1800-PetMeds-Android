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
    //Browsinghistory
    private ArrayList<BrowsingHistory> browsingHistory;
    //Recommendations
    private RecommendedCategory recommendedCategory;
    //Refill
    private ArrayList<RefillItem> refillItems;
    //what's Next
    private WhatsNextCategory whatsNextCategory;
    //Sales Pitch
    private SalePitch SalesPitch;

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

    public ArrayList<BrowsingHistory> getBrowsingHistory() {
        return browsingHistory;
    }

    public void setBrowsingHistory(ArrayList<BrowsingHistory> browsingHistory) {
        this.browsingHistory = browsingHistory;
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

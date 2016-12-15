package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 12/15/2016.
 */
public class RecentlyOrderedTitle implements Serializable{
    private String recentlyOrderedWidgetTitle;

    public RecentlyOrderedTitle(String recentlyOrderedWidgetTitle) {
        this.recentlyOrderedWidgetTitle = recentlyOrderedWidgetTitle;
    }

    public String getRecentlyOrderedWidgetTitle() {
        return recentlyOrderedWidgetTitle;
    }

    public void setRecentlyOrderedWidgetTitle(String recentlyOrderedWidgetTitle) {
        this.recentlyOrderedWidgetTitle = recentlyOrderedWidgetTitle;
    }
}

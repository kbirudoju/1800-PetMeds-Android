package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 9/13/2016.
 */
public class Widget implements Serializable {
    private String index;
    private String widgetType;
    private WidgetData data;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(String widgetType) {
        this.widgetType = widgetType;
    }

    public WidgetData getData() {
        return data;
    }

    public void setData(WidgetData data) {
        this.data = data;
    }
}

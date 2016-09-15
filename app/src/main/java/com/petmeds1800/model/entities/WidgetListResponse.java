package com.petmeds1800.model.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 9/13/2016.
 */
public class WidgetListResponse implements Serializable {
    private ArrayList<Widget> widgets;
    private String count;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private Status status;

    public WidgetListResponse() {
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList<Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(ArrayList<Widget> widgets) {
        this.widgets = widgets;
    }
}

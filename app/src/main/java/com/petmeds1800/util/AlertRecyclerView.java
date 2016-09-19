package com.petmeds1800.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Sdixit on 14-09-2016.
 */

public class AlertRecyclerView extends RecyclerView {
    Context context;
    public AlertRecyclerView(Context context) {
        super(context);
    }

    public AlertRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AlertRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public  void setAlertRecyclerView(AlertRecyclerViewAdapter adapter) {
        setAdapter(adapter);
        setLayoutManager(new LinearLayoutManager(context));  // 2 represts number of column
        setHasFixedSize(true);

    }
}

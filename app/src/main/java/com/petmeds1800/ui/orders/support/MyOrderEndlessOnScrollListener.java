package com.petmeds1800.ui.orders.support;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by user on 8/4/2016.
 */
public abstract class MyOrderEndlessOnScrollListener extends
        RecyclerView.OnScrollListener {

    private long previousTotal = 0;
    private boolean loading = true;
    private long visibleThreshold = 5;
    long firstVisibleItem, visibleItemCount, totalItemCount;
    private long totalRecordsOnServer = -1;

    private long current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public MyOrderEndlessOnScrollListener(
            LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public long getTotalRecordsOnServer() {
        return totalRecordsOnServer;
    }

    public void setTotalRecordsOnServer(long totalRecordsOnServer) {
        this.totalRecordsOnServer = totalRecordsOnServer;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if(totalRecordsOnServer != -1l && totalItemCount>=totalRecordsOnServer){
            previousTotal = totalItemCount;
            loading = false;
            return;
        }

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading
                && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            // Do something
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
    }

    public abstract void onLoadMore(long current_page);
}
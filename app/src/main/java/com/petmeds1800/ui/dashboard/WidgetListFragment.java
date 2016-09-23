package com.petmeds1800.ui.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.R;
import com.petmeds1800.ui.dashboard.presenter.WidgetContract;
import com.petmeds1800.ui.dashboard.presenter.WidgetPresenter;
import com.petmeds1800.ui.dashboard.support.WidgetListAdapter;
import com.petmeds1800.ui.fragments.AbstractFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 9/13/2016.
 */
public class WidgetListFragment extends AbstractFragment implements WidgetContract.View {
    @BindView(R.id.widget_recycler_view)
    RecyclerView mWidgetRecyclerView;

    private WidgetListAdapter mWidgetListAdapter;
    private WidgetContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_widget_list,container,false);
        ButterKnife.bind(this,view);
        mWidgetListAdapter=new WidgetListAdapter(getActivity());
        mPresenter=new WidgetPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpWidgetList();
        mPresenter.start();
    }

    private void setUpWidgetList() {
        mWidgetRecyclerView.setAdapter(mWidgetListAdapter);
        mWidgetRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mWidgetRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void updateWidgetData(List<Object> widgetData) {
        mWidgetListAdapter.setData(widgetData);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSuccess(List<Object> widgetData) {
        updateWidgetData(widgetData);
    }

    @Override
    public void onError(String errorMessage) {

    }

    @Override
    public void setPresenter(WidgetContract.Presenter presenter) {

    }
}

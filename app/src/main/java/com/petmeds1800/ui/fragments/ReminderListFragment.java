package com.petmeds1800.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.petmeds1800.R;
import com.petmeds1800.model.RefillReminderSortingPetcompOrder;
import com.petmeds1800.model.RefillReminderSortingperPet;
import com.petmeds1800.model.refillreminder.response.EasyRefillReminder;
import com.petmeds1800.model.refillreminder.response.OrderItems;
import com.petmeds1800.model.refillreminder.response.RefillReminderListResponse;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.refillreminder.ReminderListContract;
import com.petmeds1800.ui.refillreminder.presenter.ReminderListPresenter;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.ReminderListRecyclerViewAdapter;

import java.util.ArrayList;

import static com.petmeds1800.util.Constants.HIDE_PROGRESSBAR_OR_ANIMATION;
import static com.petmeds1800.util.Constants.SHOW_PROGRESSBAR_OR_ANIMATION;
import static com.petmeds1800.util.Utils.toggleGIFAnimantionVisibility;
import static com.petmeds1800.util.Utils.toggleProgressDialogVisibility;

/**
 * Created by pooja on 9/7/2016.
 */
public class ReminderListFragment extends AbstractFragment implements ReminderListContract.View {

    private RecyclerView mReminderMainList;
    private ReminderListContract.Presenter mPresenter;
    private ProgressBar mProgressBar;
    private ReminderListRecyclerViewAdapter mReminderListRecyclerViewAdapter;
    private LinearLayout mReminderListContainer;
    private LinearLayout mNoReminderListContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_list,null);
        mReminderMainList = (RecyclerView) view.findViewById(R.id.reminder_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mReminderListContainer = (LinearLayout) view.findViewById(R.id.reminer_list_container);
        mNoReminderListContainer = (LinearLayout) view.findViewById(R.id.no_reminder_container);
        mReminderMainList.setLayoutManager(mLayoutManager);
        mReminderMainList.setItemAnimator(new DefaultItemAnimator());
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((Button)mNoReminderListContainer.findViewById(R.id.shop_now_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).scrollViewPager(0);
            }
        });
        callReminderAPI(null);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public boolean postGeneralPopulateRefillReminderList(RefillReminderListResponse refillReminderListResponse) {
        if (null == refillReminderListResponse || null == refillReminderListResponse.getEasyRefillReminder() || refillReminderListResponse.getEasyRefillReminder().isEmpty()){
            mNoReminderListContainer.setVisibility(View.VISIBLE);
            mReminderListContainer.setVisibility(View.GONE);
        } else {
            mNoReminderListContainer.setVisibility(View.GONE);
            mReminderListContainer.setVisibility(View.VISIBLE);
        }
        toggleProgressDialogVisibility(HIDE_PROGRESSBAR_OR_ANIMATION,mProgressBar);
        toggleGIFAnimantionVisibility(HIDE_PROGRESSBAR_OR_ANIMATION,getActivity());
        new Thread(new ReminderListSorterRunnable(refillReminderListResponse.getEasyRefillReminder(),RefillListMessageHandler)).start();
        return false;
    }

    @Override
    public boolean onError(String errorMessage) {
        toggleProgressDialogVisibility(HIDE_PROGRESSBAR_OR_ANIMATION,mProgressBar);
        toggleGIFAnimantionVisibility(HIDE_PROGRESSBAR_OR_ANIMATION,getActivity());
        return false;
    }

    @Override
    public void setPresenter(ReminderListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void callReminderAPI(Object object){
        if (mPresenter == null){
            mPresenter = new ReminderListPresenter(this);
        }
        if (object == null)
        {
            toggleProgressDialogVisibility(SHOW_PROGRESSBAR_OR_ANIMATION,mProgressBar);
            mPresenter.getGeneralPopulateRefillReminderList();
        }
    }

    private final Handler RefillListMessageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constants.REFILL_LIST_PER_PET_SORT_COMPLETE) {
                mReminderListRecyclerViewAdapter = new ReminderListRecyclerViewAdapter(getActivity(),RefillListMessageHandler, (ArrayList<RefillReminderSortingperPet>) msg.getData().getSerializable(Constants.REFILL_LIST_PER_PET_));
                mReminderMainList.setAdapter(mReminderListRecyclerViewAdapter);
            } else
            if (msg.what == Constants.REFILL_LIST_PER_PET_EDIT){
                Fragment EditReFragment = new EditReminderFragment();
                EditReFragment.setArguments(msg.getData());
                replaceAccountAndAddToBackStack(EditReFragment,EditReFragment.getClass().getSimpleName());
            }
        }
    };

    private class ReminderListSorterRunnable implements Runnable {
        private ArrayList<EasyRefillReminder> easyRefillReminders;
        private Handler postbackHandler;
        private ArrayList<RefillReminderSortingperPet> refillReminderSortingperPets ;

        public ReminderListSorterRunnable(ArrayList<EasyRefillReminder> easyRefillReminders, Handler postbackHandler) {
            this.easyRefillReminders = easyRefillReminders;
            this.postbackHandler = postbackHandler;
        }

        private int getindex(ArrayList<RefillReminderSortingperPet> local_refillReminderSortingperPets, String petId){
            int retValue = -1;

            for (int l = 0 ; l < local_refillReminderSortingperPets.size() ; l++){
                if (petId.equalsIgnoreCase(local_refillReminderSortingperPets.get(l).getPetId())){
                    retValue = l;
                    break;
                }
            }
            return retValue;
        }

        @Override
        public void run() {
            refillReminderSortingperPets = new ArrayList<>();
            for (int i = 0 ; i < easyRefillReminders.size() ; i++){
                ArrayList<OrderItems> orderItems = easyRefillReminders.get(i).getOrderItems();
                for (int j = 0 ; j < orderItems.size(); j++){

                    if (null == orderItems.get(j).getPetId()){
                        orderItems.get(j).setPetId("");
                    }
                    if (null == orderItems.get(j).getPetName()){
                        orderItems.get(j).setPetName("");
                    }

                    int index = getindex(refillReminderSortingperPets,orderItems.get(j).getPetId());
                    if (index == -1){
                        ArrayList<RefillReminderSortingPetcompOrder> local_refillReminderSortingPetcompOrderArraylist = new ArrayList<>();
                        local_refillReminderSortingPetcompOrderArraylist.add(new RefillReminderSortingPetcompOrder(orderItems.get(j),easyRefillReminders.get(i)));
                        refillReminderSortingperPets.add(new RefillReminderSortingperPet(orderItems.get(j).getPetId(),orderItems.get(j).getPetName(),orderItems.get(j).getPetImageUrl(),local_refillReminderSortingPetcompOrderArraylist));
                    } else{
                        RefillReminderSortingperPet local_refillReminderSortingperPet = new RefillReminderSortingperPet(refillReminderSortingperPets.get(index).getPetId(),refillReminderSortingperPets.get(index).getPetName(),refillReminderSortingperPets.get(index).getPetImageURL(),refillReminderSortingperPets.get(index).getRefillReminderSortingPetcompOrderArraylist());
                        refillReminderSortingperPets.remove(index);
                        local_refillReminderSortingperPet.getRefillReminderSortingPetcompOrderArraylist().add(new RefillReminderSortingPetcompOrder(orderItems.get(j),easyRefillReminders.get(i)));
                        refillReminderSortingperPets.add(local_refillReminderSortingperPet);
                    }
                }
            }

            Message msg = Message.obtain(null, Constants.REFILL_LIST_PER_PET_SORT_COMPLETE);
            Bundle b = new Bundle();
            b.putSerializable(Constants.REFILL_LIST_PER_PET_,refillReminderSortingperPets);
            msg.setData(b);

            try {
                postbackHandler.sendMessage(msg);
            } catch (ClassCastException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
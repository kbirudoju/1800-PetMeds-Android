package com.petmeds1800.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.petmeds1800.util.Utils;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
            sortReminders(refillReminderListResponse.getEasyRefillReminder());
        }
        toggleProgressDialogVisibility(HIDE_PROGRESSBAR_OR_ANIMATION,mProgressBar);
        toggleGIFAnimantionVisibility(HIDE_PROGRESSBAR_OR_ANIMATION,getActivity());

        return false;
    }

    @Override
    public boolean onError(String errorMessage) {
        Utils.displayCrouton(getActivity(), (String) errorMessage, mReminderMainList);

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

    //****************************************************Callable Implelemtation for sorting Reminders***********

    public void sortReminders(final ArrayList<EasyRefillReminder> easyRefillReminders) {

        Observable.create(new Observable.OnSubscribe<ArrayList<RefillReminderSortingperPet>>() {

            private int getIndex(ArrayList<RefillReminderSortingperPet> local_refillReminderSortingperPets, String petId){
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
            public void call(Subscriber<? super ArrayList<RefillReminderSortingperPet>> subscriber) {

                try{
                    if (easyRefillReminders == null || easyRefillReminders.size() == 0 || easyRefillReminders.isEmpty()) {
                        subscriber.onError(new Exception("Empty ReminderList"));
                    }
                    else {
                        ArrayList<RefillReminderSortingperPet> refillReminderSortingperPets = new ArrayList<>();

                        for (int i = 0 ; i < easyRefillReminders.size() ; i++){
                            ArrayList<OrderItems> orderItems = easyRefillReminders.get(i).getOrderItems();
                            for (int j = 0 ; j < orderItems.size(); j++){

                                if (null == orderItems.get(j).getPetId()){
                                    orderItems.get(j).setPetId("");
                                }
                                if (null == orderItems.get(j).getPetName()){
                                    orderItems.get(j).setPetName("");
                                }

                                int index = getIndex(refillReminderSortingperPets,orderItems.get(j).getPetId());
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
                        subscriber.onNext(refillReminderSortingperPets);
                    }
                } catch (Exception e){
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ArrayList<RefillReminderSortingperPet>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("sortReminders", e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ArrayList<RefillReminderSortingperPet> refillReminderSortingperPets) {
                        Message msg = Message.obtain(null, Constants.REFILL_LIST_PER_PET_SORT_COMPLETE);
                        Bundle b = new Bundle();
                        b.putSerializable(Constants.REFILL_LIST_PER_PET_,refillReminderSortingperPets);
                        msg.setData(b);

                        try {
                            RefillListMessageHandler.sendMessage(msg);
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //**************************************************************************************
}
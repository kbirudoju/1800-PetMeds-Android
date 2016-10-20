package com.petmeds1800.ui.vet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.model.VetList;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.vet.presenter.FindVetPresenter;
import com.petmeds1800.ui.vet.support.SearchVetAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 10/18/2016.
 */
public class FindAVetFragment extends AbstractFragment implements FindVetContract.View {
    @BindView(R.id.vet_recycler_view)
    RecyclerView mVetListRecyclerView;


    private SearchVetAdapter mVetListAdapter;
    private FindVetContract.Presenter mPresenter;
    @BindView(R.id.zipCodeEdit)
    EditText mZipCodeEdit;
    private ArrayList<VetList>mVetList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_vet, container, false);
        ButterKnife.bind(this, view);
        mPresenter=new FindVetPresenter(this);
        setHasOptionsMenu(true);

        mVetListAdapter=new SearchVetAdapter(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mVetListRecyclerView.getChildAdapterPosition(v);
                Bundle bundle =new Bundle();
                bundle.putSerializable("vet_detail",mVetList.get(position));
                addAccountFragmentWithBundle(new VetDetailFragment(), bundle);
            }
        });

        mZipCodeEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideSoftKeyBoard();
                    if(mZipCodeEdit.getText().toString()!=null && !mZipCodeEdit.getText().toString().isEmpty())
                        try {
                            ((AbstractActivity) getActivity()).startLoadingGif(getActivity());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        performSearch(mZipCodeEdit.getText().toString());
                    return true;
                }
                return false;
            }
        });
        return  view;
    }

    private void performSearch(String zipCode){
        mPresenter.getVetList(zipCode);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpVetList();


    }

    private void setUpVetList() {
        mVetListRecyclerView.setAdapter(mVetListAdapter);
        mVetListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mVetListRecyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSuccess(ArrayList<VetList> vetList) {
        mVetList=new ArrayList<>();
        mVetList=vetList;
        try {
            ((AbstractActivity)getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
     mVetListAdapter.setData(vetList);
    }

    @Override
    public void onError(String errorMessage) {

        try {
            ((AbstractActivity)getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void setPresenter(FindVetContract.Presenter presenter) {

    }
}

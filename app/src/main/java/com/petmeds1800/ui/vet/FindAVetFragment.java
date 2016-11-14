package com.petmeds1800.ui.vet;

import com.petmeds1800.R;
import com.petmeds1800.intent.ShowVetOnMapIntent;
import com.petmeds1800.model.VetList;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.vet.presenter.FindVetPresenter;
import com.petmeds1800.ui.vet.support.LocationRelatedStuff;
import com.petmeds1800.ui.vet.support.SearchVetAdapter;
import com.petmeds1800.util.AnalyticsUtil;
import com.petmeds1800.util.Constants;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.petmeds1800.R;
import com.petmeds1800.intent.ShowVetOnMapIntent;
import com.petmeds1800.model.VetList;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.vet.presenter.FindVetPresenter;
import com.petmeds1800.ui.vet.support.LocationRelatedStuff;
import com.petmeds1800.ui.vet.support.SearchVetAdapter;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 10/18/2016.
 */
public class FindAVetFragment extends AbstractFragment
        implements FindVetContract.View, LocationRelatedStuff.ZipCodeListener {

    @BindView(R.id.vet_recycler_view)
    RecyclerView mVetListRecyclerView;

    private SearchVetAdapter mVetListAdapter;

    private FindVetContract.Presenter mPresenter;

    @BindView(R.id.zipCodeEdit)
    EditText mZipCodeEdit;

    private ArrayList<VetList> mVetList;

    private String mZipCode;

    @BindView(R.id.location_icon)
    ImageView mLocationImage;
    @BindView(R.id.top_layout)
    LinearLayout mContainer;
    private LocationRelatedStuff locationRelatedStuff;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_vet, container, false);
        new AnalyticsUtil().trackScreen(getString(R.string.label_locate_vet_analytics_title));
        ButterKnife.bind(this, view);
        mPresenter = new FindVetPresenter(this);
        setHasOptionsMenu(true);
        ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.title_my_vets));
        ((AbstractActivity) getActivity()).enableBackButton();

        locationRelatedStuff = new LocationRelatedStuff(getActivity());
        locationRelatedStuff.setZipCodeListener(this);

        mVetListAdapter = new SearchVetAdapter(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mVetListRecyclerView.getChildAdapterPosition(v);
                Bundle bundle = new Bundle();
                bundle.putSerializable("vet_detail", mVetList.get(position));
                replaceAccountFragmentWithBundle(new VetDetailFragment(), bundle);
            }
        });

        mZipCodeEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideSoftKeyBoard();
                    if (mZipCodeEdit.getText().toString() != null && !mZipCodeEdit.getText().toString().isEmpty()) {
                        try {
                            ((AbstractActivity) getActivity()).startLoadingGif(getActivity());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mZipCode = mZipCodeEdit.getText().toString();
                        performSearch(mZipCodeEdit.getText().toString());

                    }
                    return true;
                }
                return false;
            }
        });

        mLocationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //locationRelatedStuff = new LocationRelatedStuff(ShowVetOnMapActivity.this);
                locationRelatedStuff.initLocation();
                locationRelatedStuff.connectGoogleApiClient();
            }
        });

        return view;
    }


    private void performSearch(String zipCode) {
        mPresenter.getVetList(zipCode);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpVetList();
        Log.d("mZipCode", mZipCode + ">>>>");
        if (mZipCode != null && !mZipCode.isEmpty()) {
            Log.d("zipcode is", mZipCode);
            try {
                ((AbstractActivity) getActivity()).startLoadingGif(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            performSearch(mZipCode);
        }
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
        mVetList = new ArrayList<>();
        mVetList = vetList;
        if (mVetList.size() > 0) {
            new AnalyticsUtil().trackScreen(getString(R.string.label_vet_search_results_analytics_title));
        }
        try {
            ((AbstractActivity) getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mZipCodeEdit.setText(mZipCode);
        mVetListAdapter.setData(vetList);

    }

    @Override
    public void onError(String errorMessage) {
       // Snackbar.make(mVetListRecyclerView, , Snackbar.LENGTH_LONG).show();
        Utils.displayCrouton(getActivity(), errorMessage, mContainer);


        try {
            ((AbstractActivity) getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_find_vet, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_map) {
            ShowVetOnMapIntent showVetOnMapIntent = new ShowVetOnMapIntent(getActivity(), mVetList, mZipCode);
            startActivityForResult(showVetOnMapIntent, Constants.REFRESH_VET_DATA);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(FindVetContract.Presenter presenter) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove(this);
            trans.commit();
            manager.popBackStack();
        } else if (requestCode == Constants.REFRESH_VET_DATA) {
            //Reload data of vet
            if (data != null) {
                Log.d("zipCode", mZipCode + ">>");
                Bundle b = data.getExtras();
                String currentZipCode = b.getString("zipCode");
                if (currentZipCode != null && !currentZipCode.isEmpty()) {
                    mZipCode = currentZipCode;
                    mZipCodeEdit.setText(mZipCode);
                    ArrayList<VetList> vetList = (ArrayList<VetList>) b.getSerializable("vetList");
                    onSuccess(vetList);
                }
            }
        } else if (requestCode == LocationRelatedStuff.REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    locationRelatedStuff.checkMarshMallowPermissions();
                    Log.i("TAG", "User agreed to make required location settings changes.");
                    break;
                case Activity.RESULT_CANCELED:
                    Log.i("TAG", "User chose not to make required location settings changes.");
                    break;
            }
        }
    }

    @Override
    public void onZipCodeFetched(String zipCode) {
        if (zipCode != null) {
           /* try {
                ((AbstractActivity) getActivity()).startLoadingGif(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            mZipCode = zipCode;
            performSearch(mZipCode);
        }
    }

    @Override
    public void onZipCodeError(String error) {
        Log.d("onError", error);
        try {
            ((AbstractActivity) getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Utils.displayCrouton(getActivity(), error, mContainer);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LocationRelatedStuff.LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationRelatedStuff.getLocationUpdate();
                } else {
                    Toast.makeText(getActivity(), "Permission Denied, You cannot access location data.",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationRelatedStuff.disconnectGoogleApiClient();
    }
}

package com.petmeds1800.ui.vet;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.petmeds1800.R;
import com.petmeds1800.intent.ShowVetOnMapIntent;
import com.petmeds1800.intent.VetDetailIntent;
import com.petmeds1800.model.VetList;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.vet.presenter.FindVetPresenter;
import com.petmeds1800.ui.vet.support.LocationRelatedStuff;
import com.petmeds1800.util.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 10/20/2016.
 */
public class ShowVetOnMapActivity extends AbstractActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener,View.OnClickListener, FindVetContract.View ,LocationRelatedStuff.ZipCodeListener{
    private ExtendedMapFragment extendedMapFragment = null;
    private ArrayList<VetList> mVetList;
    private LinkedHashMap<Marker, VetList> vetDetailHashMap
            = new LinkedHashMap<Marker, VetList>();
    private GoogleMap mMap;

    @BindView(R.id.addressLine2_label)
    TextView address_second_label;
    @BindView(R.id.clinicNameLabel)
    TextView clinic_name_label;
    @BindView(R.id.addressLine1_label)
    TextView address_first_label;
    @BindView(R.id.distanceLabel)
    TextView distancelabel;
    @BindView(R.id.vet_deatil_layout)
    RelativeLayout mVetDetailLayout;
    private VetList mSelectedVet;
    Marker prevMarker;
    @BindView(R.id.zipCodeEdit)
    EditText mZipCodeEdit;
    private FindVetContract.Presenter mPresenter;
    private String mZipCode;
    @BindView(R.id.location_icon)
    ImageView mLocationImage;
    private  LocationRelatedStuff locationRelatedStuff;
    @BindView(R.id.arrow_image)
    ImageView mArrowImage;
    @BindView(R.id.loaction_image)
    ImageView mLocationIcon;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        enableBackButton();
        mPresenter = new FindVetPresenter(this);
        locationRelatedStuff = new LocationRelatedStuff(ShowVetOnMapActivity.this);
        locationRelatedStuff.setZipCodeListener(this);

        setToolBarTitle(getString(R.string.title_find_a_pet));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        if (fragmentManager.findFragmentByTag("mapTag") == null) {
            extendedMapFragment = new ExtendedMapFragment();
            fragmentTransaction.add(R.id.map_container, extendedMapFragment, "mapTag");
            fragmentTransaction.commit();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initMap();
            }
        }, 500);

        mVetDetailLayout.setOnClickListener(this);

        mZipCodeEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //hideSoftKeyBoard();
                    if (mZipCodeEdit.getText().toString() != null && !mZipCodeEdit.getText().toString().isEmpty()) {
                        try {
                            startLoadingGif(ShowVetOnMapActivity.this);
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


    }

    private void performSearch(String zipCode) {
        mPresenter.getVetList(zipCode);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_show_vet_map;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationRelatedStuff.disconnectGoogleApiClient();
    }

    private void initVetData(){

        if(getIntent().getSerializableExtra(ShowVetOnMapIntent.REQUEST_CODE)!=null){
            mVetList=(ArrayList<VetList>)getIntent().getSerializableExtra(ShowVetOnMapIntent.REQUEST_CODE);
            mZipCode=getIntent().getStringExtra(ShowVetOnMapIntent.ZIP_CODE);
            Log.d("mVetList Size",mVetList.size()+">>>");
            addVetPointsListToMap(mVetList);

        }
    }

    private void addVetPointsListToMap(ArrayList<VetList> mVetList) {
        /**
         * following code will be adding only new data
         */
        vetDetailHashMap=new LinkedHashMap<>();
        //put a marker as key and a vet detail as the value in hasmap
        int markerposition=0;

        for(VetList vetList:mVetList){
            //add the marker on the map
            Marker marker = addVetPointsMarker(vetList, markerposition);

            //add this marker to the hashmap for easy mapping between vet and its marker
            vetDetailHashMap.put(marker, vetList);
            markerposition++;
        }
    }

    private void initMap(){
        if(mMap==null){
            final SupportMapFragment mapFragment
                    = ((SupportMapFragment) getSupportFragmentManager().findFragmentByTag("mapTag"));
            mapFragment.getMapAsync(this);

        }
    }


    private Marker addVetPointsMarker(VetList vetData,int markerPosition) {

        Marker marker = null;
        if (mMap != null && vetData != null) {
            if (markerPosition == 0) {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(vetData.getLatitude(),
                                vetData.getLongitude()))
                        .title(vetData.getClinic())
                        .icon(BitmapDescriptorFactory.fromResource(
                                R.drawable.ic_marker_selected)));
                mSelectedVet=vetData;
                prevMarker = marker;
            } else {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(vetData.getLatitude(),
                                vetData.getLongitude()))
                        .title(vetData.getClinic())
                        .icon(BitmapDescriptorFactory.fromResource(
                                R.drawable.ic_marker_unselected)));
            }

        }


        return marker;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
        initVetData();
        zoomToIncludeAllMarkers();
        if(mSelectedVet!=null)
            setVetailLayout(mSelectedVet);
    }

    public void setUpMap(){
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setOnMarkerClickListener(this);
    }



    @Override
    public boolean onMarkerClick(Marker marker) {
        mSelectedVet=vetDetailHashMap.get(marker);
        setVetailLayout(mSelectedVet);
        if (prevMarker != null) {
            //Set prevMarker back to default color
            prevMarker.setIcon(BitmapDescriptorFactory.fromResource(
                    R.drawable.ic_marker_unselected));
        }

        //leave Marker default color if re-click current Marker
        if (!marker.equals(prevMarker)) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(
                    R.drawable.ic_marker_selected));
            prevMarker = marker;
        }
        prevMarker = marker;
        return false;
    }

    private void setVetailLayout(VetList mSelectedVet){
        mLocationIcon.setVisibility(View.VISIBLE);
        mArrowImage.setVisibility(View.VISIBLE);
        clinic_name_label.setText(mSelectedVet.getClinic());
        address_first_label.setText(mSelectedVet.getAddress());
        address_second_label.setText(mSelectedVet.getCity() + "," + mSelectedVet.getState());
        distancelabel.setText(mSelectedVet.getDistanceFromZip() + " " + getString(R.string.miles_txt) + " " + getString(R.string.from_txt) + " " + mSelectedVet.getZip());

    }

    private void zoomToIncludeAllMarkers() {

        if (vetDetailHashMap != null && vetDetailHashMap.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            Iterator it = vetDetailHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();

                Marker marker = (Marker) pairs.getKey();
                builder.include(marker.getPosition());
            }


            LatLngBounds bounds = builder.build();

            int padding = 80; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            if (mMap != null) {
                mMap.animateCamera(cu);
            }

        }


    }

    @Override
    public void onClick(View v) {
        if(mSelectedVet!=null) {
            VetDetailIntent vetDetailActivtiy = new VetDetailIntent(ShowVetOnMapActivity.this, mSelectedVet);
            startActivity(vetDetailActivtiy);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_find_vet, menu);//Menu Resource, Menu
        MenuItem listIcon= menu.findItem(R.id.action_list);
        MenuItem mapIcon= menu.findItem(R.id.action_map);
        listIcon.setVisible(true);
        mapIcon.setVisible(false);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            setResult(Constants.SHOW_VET_ON_MAP);
            finish();
        }
        if(id == R.id.action_list){
            Intent intent=new Intent();
            Bundle bundle =new Bundle();
            bundle.putSerializable("vetList",mVetList);
            bundle.putString("zipCode", mZipCode);
            intent.putExtras(bundle);
            setResult(Constants.REFRESH_VET_DATA, intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void onSuccess(ArrayList<VetList> vetList) {
        mVetList=vetList;
        try {
            stopLoadingGif(ShowVetOnMapActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mZipCodeEdit.setText(mZipCode);
        clearPreviousMarker();
        addVetPointsListToMap(vetList);
        zoomToIncludeAllMarkers();
        if(mSelectedVet!=null)
            setVetailLayout(mSelectedVet);
    }

    @Override
    public void onError(String errorMessage) {
        Snackbar.make(mZipCodeEdit, errorMessage, Snackbar.LENGTH_LONG).show();
        try {
            stopLoadingGif(ShowVetOnMapActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPresenter(FindVetContract.Presenter presenter) {

    }
    //this method will remove all the previously added marker and then will the new marker
    private void clearPreviousMarker(){
        if (vetDetailHashMap != null) {
            Iterator it = vetDetailHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();

                Marker marker = (Marker) pairs.getKey();
                marker.remove();
                it.remove(); // avoids a ConcurrentModificationException
            }

            vetDetailHashMap.clear();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case LocationRelatedStuff.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        locationRelatedStuff.checkMarshMallowPermissions();
                        Log.i("TAG", "User agreed to make required location settings changes.");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("TAG", "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LocationRelatedStuff.LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationRelatedStuff.getLocationUpdate();
                } else {

                    Toast.makeText(this, "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    @Override
    public void onZipCodeFetched(String zipCode) {
        if (zipCode != null) {
            try {
                startLoadingGif(ShowVetOnMapActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mZipCode = zipCode;
            performSearch(mZipCode);
        }
    }
}
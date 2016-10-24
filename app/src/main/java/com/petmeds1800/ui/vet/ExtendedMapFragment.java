package com.petmeds1800.ui.vet;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by user on 11/5/2014.
 */
public class ExtendedMapFragment extends SupportMapFragment {

    private MapListener mMapListner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            this.onViewStateRestored(savedInstanceState);
    }

    public void setmMapListner(MapListener mMapListner) {
        this.mMapListner = mMapListner;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }


}

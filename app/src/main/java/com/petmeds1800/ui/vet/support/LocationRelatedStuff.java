package com.petmeds1800.ui.vet.support;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.petmeds1800.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by pooja on 10/22/2016.
 */
public class LocationRelatedStuff  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,ResultCallback<LocationSettingsResult> {


    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double lat, lon;
    public static final int LOCATION_REQUEST=1340;
    public static final int REQUEST_CHECK_SETTINGS=1350;
    protected LocationSettingsRequest mLocationSettingsRequest;

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private Context mContext;
    private ZipCodeListener mZipCodeListener;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9001;


    public LocationRelatedStuff(Context mContext) {
        this.mContext = mContext;
    }






    public void initLocation() {
        if (!checkPlayServices()) {
           return;
        }
        buildGoogleApiClient();
    }



    @Override
    public void onConnected(Bundle bundle) {


        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100); // Update location every second

        buildLocationSettingsRequest();

    }


    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest).setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
        checkLocationSettings();
    }
    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = (location.getLatitude());
        lon = (location.getLongitude());
        // getZipCode();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        try {
            // Start an Activity that tries to resolve the error
            connectionResult.startResolutionForResult(
                    (Activity) mContext,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
        } catch (IntentSender.SendIntentException e) {
            // Log the error

            e.printStackTrace();

        }

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        (Activity) mContext,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();

            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */

        }



        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        (Activity) mContext,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */

        }

        //  buildGoogleApiClient();
    }

    synchronized void buildGoogleApiClient() {


        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }

    public void connectGoogleApiClient() {
        if(mGoogleApiClient!=null){
            mGoogleApiClient.connect();
        }

    }

    public void disconnectGoogleApiClient() {
        if(mGoogleApiClient!=null){
            mGoogleApiClient.disconnect();
        }

    }



    private void getZipCode()  {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
           if(addresses!=null){
               String postalCode = addresses.get(0).getPostalCode();
               Log.d("zipcode", postalCode + ">>>>");
               if(postalCode!=null){
                   mZipCodeListener.onZipCodeFetched(postalCode);
               }
           }
            else{
                mZipCodeListener.onZipCodeError(mContext.getString(R.string.location_not_found_error));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void checkMarshMallowPermissions(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST);
            } else {
                // permission has been granted, continue as usual
                getLocationUpdate();
            }
        }
        else {//in case device does not support marshmallow


            getLocationUpdate();
        }


       /* if (mLastLocation != null) {
            lat = (mLastLocation.getLatitude());
            lon = (mLastLocation.getLongitude());

        }
        updateUI();*/
    }


    public void getLocationUpdate(){
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if(mLastLocation!=null){
                lat = (mLastLocation.getLatitude());
                lon = (mLastLocation.getLongitude());
            }
            Log.d("latitude and longitude",lat+">>>>"+lon);
            getZipCode();
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                checkMarshMallowPermissions();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i("TAG", "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    //

                    //move to step 6 in onActivityResult to check what action user has taken on settings dialog
                    status.startResolutionForResult((Activity)mContext, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i("TAG", "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i("TAG", "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }
    //create an interface that will listen when zipcode has been fetched
    public interface ZipCodeListener {
        void onZipCodeFetched(String zipCode);
        void onZipCodeError(String error);
    }

    public void setZipCodeListener(ZipCodeListener zipCodeListener) {
        this.mZipCodeListener = zipCodeListener;
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog((Activity)mContext, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("TAG", "This device is not supported.");
            }
            return false;
        }
        return true;
    }
}
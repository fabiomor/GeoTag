package com.fabio.gis.geotag;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback {


    private static final String TAG = MapsFragment.class.getSimpleName();

    private Context mContext;
    private GoogleMap mMap;
    private LatLng latLng;
    private Marker currLocationMarker;
    private int markerCount;
    private OnLocationChangedListener onLocationChangedListener;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLocationChangedListener) {
            onLocationChangedListener = (OnLocationChangedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet MapsFragment.OnLocationChangedListener");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        markerCount = 0;
    }

    @Nullable


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        mContext = getActivity();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG,"onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        registerLocationListeners();
        Log.i(TAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"onStop");
        stopLocationUpdates();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG,"onDestroyView");
        if(locationManager != null){ locationManager = null;}
        if(onLocationChangedListener != null){ onLocationChangedListener = null;}
        if(locationListener != null) { locationListener = null;}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }

    @Override
    public void onDetach() {
        Log.i(TAG,"onDetach");
        super.onDetach();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG,"WARN: permission not granted");
            return;
        }
        else {

            mMap.setMyLocationEnabled(true);

            mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

                @Override
                public void onMapLongClick(LatLng arg0) {
                    latLng = arg0;
                    if(latLng != null) placeMarker();
                }
            });
            registerLocationListeners();

            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                promptGps();
            }


            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
            //buildGoogleApiClient();
            //mGoogleApiClient.connect();
        }
    }

    private void registerLocationListeners() {
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (locationGps != null && locationNetwork != null) {
            Location location = locationGps.getTime() > locationNetwork.getTime() ? locationGps : locationNetwork;
            latLng = new LatLng(location.getLatitude(),location.getLongitude());
        } else if (locationGps != null) {
            latLng = new LatLng(locationGps.getLatitude(),locationGps.getLongitude());
        } else if (locationNetwork != null) {
            latLng = new LatLng(locationNetwork.getLatitude(),locationNetwork.getLongitude());
        }

        locationListener = new DeviceLocationListener();

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }



        /*
            compile 'com.google.android.gms:play-services-location:10.2.0'
            compile 'com.google.android.gms:play-services-maps:10.2.0'
            compile 'com.google.android.gms:play-services-ads:10.2.0'
         */
    }

    public String placeMarker(){
        String msg;
        if(mMap != null && latLng != null) {
            markerCount++;
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(getString(R.string.marker_title) + " " + markerCount);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            removeMarker();
            currLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Settings.getZoomLevel()));
            msg = getString(R.string.marker_created);
        } else if (mMap == null) {
            msg = getString(R.string.map_not_loaded);
        } else {
            msg = getString(R.string.pos_not_loaded);
        }
        return msg;
    }



    public void removeMarker(){
        if(currLocationMarker != null) {
            currLocationMarker.remove();
        }
    }


    public interface OnLocationChangedListener {
        public void onLocationChanged(LatLng latLng);
    }


    private void promptGps(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.gps_alert_dialog_text)
                .setTitle(R.string.gps_alert_dialog_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(gpsOptionsIntent);
                        }})
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void stopLocationUpdates() {
        locationManager.removeUpdates(locationListener);
    }

    private class DeviceLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            latLng = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Settings.getZoomLevel()));
            if(onLocationChangedListener != null) {
                onLocationChangedListener.onLocationChanged(latLng);
            }
            /*
            if(currLocationMarker != null){
               currLocationMarker.remove();
            }
            latLng = new LatLng(location.getLatitude(),location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
            */
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.i(TAG,"Status Changed");
            registerLocationListeners();
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.i(TAG,"Povider Enabled");
            registerLocationListeners();
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.i(TAG,"Provider Disabled");

        }
    }
}

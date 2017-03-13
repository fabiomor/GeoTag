package com.fabio.gis.geotag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.FloatingActionButton;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements MapsFragment.OnMapLongPressListener,
        View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;

    private FloatingActionButton fabSecondary, fabPrimary;

    // fragments
    private MapsFragment mapsFragment;
    private TomapFragment tomapFragment;
    private ChartsFragment chartsFragment;
    private FragmentManager fragmentManager;

    // layouts
    private NavigationView navigationView;
    private View coordinatorLayout;

    //
    LatLng latLng;
    MapMarker mapMarker;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(Constants.IS_LOGIN_REQUIRED) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        mapsFragment = new MapsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.maincontainer,mapsFragment,Constants.MAP_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();

        //TODO: adding fragment to backstack?
        
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferenceChangeListener = new OnSharedPreferenceChangeListener();

        fabPrimary = (FloatingActionButton) findViewById(R.id.primary_action);
        fabPrimary.setOnClickListener(this);
        fabSecondary = (FloatingActionButton) findViewById(R.id.secondary_action);
        fabSecondary.setOnClickListener(this);



        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout_app_bar_main);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // checking permissions
        if(!PermissionManager.checkPermissions(this.getApplicationContext(), Arrays.asList(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION))){
            PermissionManager.requestPermissions(this,Constants.APPLICATION_PERMISSIONS_REQUEST);
        }

        // updating settings
        for(String key : sharedPreferences.getAll().keySet()) {
            updateSettingsFromPreferences(key);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        super.onStop();
        if (sharedPreferenceChangeListener != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMapLongPress(MapMarker mapMarker) {
        this.mapMarker = mapMarker;
    }

    @Override
    public void onClick(View v) {
        Snackbar snackbar;
        switch (v.getId()){
            case R.id.secondary_action:
                mapMarker = mapsFragment.placeMarker();
                if(mapMarker == null) {
                    snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.map_not_loaded), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.retry), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mapsFragment.placeMarker();
                                    //Snackbar snackbarUndo = Snackbar.make(coordinatorLayout, getString(R.string.marker_deleted), Snackbar.LENGTH_SHORT);
                                    //snackbarUndo.show();
                                }
                            });
                } else if(mapMarker.getPositon() == null){
                    snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.pos_not_loaded), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.retry), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mapsFragment.placeMarker();
                                    //Snackbar snackbarUndo = Snackbar.make(coordinatorLayout, getString(R.string.marker_deleted), Snackbar.LENGTH_SHORT);
                                    //snackbarUndo.show();
                                }
                            });
                }
                else {
                    snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.marker_created), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.undo), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mapsFragment.removeMarker();
                                    //Snackbar snackbarUndo = Snackbar.make(coordinatorLayout, getString(R.string.marker_deleted), Snackbar.LENGTH_SHORT);
                                    //snackbarUndo.show();
                                }
                            });
                }
               snackbar.show();
                break;
            case R.id.primary_action:
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.maincontainer);
                String tag = currentFragment.getTag();
                switch (tag) {
                    case Constants.MAP_FRAGMENT_TAG:
                        fabSecondary.setVisibility(View.INVISIBLE);
                        Bundle bundle = new Bundle();
                        bundle.putDouble("latitude", mapMarker.getPositon().latitude);
                        bundle.putDouble("longitude", mapMarker.getPositon().longitude);
                        tomapFragment = new TomapFragment();
                        tomapFragment.setArguments(bundle);
                        fragmentManager.beginTransaction()
                                .replace(R.id.maincontainer, tomapFragment, Constants.TOMAP_FRAGMENT_TAG)
                                .addToBackStack(Constants.MAP_FRAGMENT_TAG)
                                .commit();
                        break;
                    case Constants.TOMAP_FRAGMENT_TAG:
                        tomapFragment.buildJson();
                        break;
                    default:
                        Log.e(TAG, "Unhandled FAB fragment tag " + tag);
                        Snackbar.make(coordinatorLayout, "Not sure what to do...my bad", Snackbar.LENGTH_SHORT).show();
                        break;
                }
        }
    }



    // menu

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // drawer

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Snackbar snackbar;
        Fragment fragment = null;
        switch(item.getItemId()){
            case R.id.nav_manage:
                snackbar = Snackbar
                        .make(coordinatorLayout, "manage", Snackbar.LENGTH_LONG);
                snackbar.show();
                break;
            case R.id.nav_share:
                snackbar = Snackbar
                        .make(coordinatorLayout, "share", Snackbar.LENGTH_LONG);
                snackbar.show();
                break;
            case R.id.nav_send:
                snackbar = Snackbar
                        .make(coordinatorLayout, "send", Snackbar.LENGTH_LONG);
                snackbar.show();
                fragment = new ChartsFragment();
                break;
            case R.id.nav_view:
                snackbar = Snackbar
                        .make(coordinatorLayout, "view", Snackbar.LENGTH_LONG);
                snackbar.show();
                break;
        }
        if(fragment != null){
            fragmentManager.beginTransaction()
                    .replace(R.id.maincontainer, new ChartsFragment())
                    .commit();
        }
        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        //setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // settings

    private void updateSettingsFromPreferences(String key) {
        switch (key) {
            case Constants.USERNAME_KEY:
                Settings.setUsername(sharedPreferences.getString(key, "username"));
                break;

            case Constants.PASSWORD_KEY:
                Settings.setPassword(sharedPreferences.getString(key, "password"));
                break;

            case Constants.SERVER_PROTOCOL_KEY:
                Settings.setServerProtocol(sharedPreferences.getString(key, "http"));
                break;

            case Constants.SERVER_ADDRESS_KEY:
                Settings.setServerAddress(sharedPreferences.getString(key, "localhost"));
                break;

            case Constants.SERVER_PORT_KEY:
                Settings.setServerPort(Integer.parseInt(sharedPreferences.getString(key, "8080")));
                break;

            case Constants.ZOOM_LEVEL:
                Settings.setZoomLevel(Float.parseFloat(sharedPreferences.getString(key, "16")));
                break;

            default:
        }
    }
    
    //listeners
    private class OnSharedPreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updateSettingsFromPreferences(key);
        }
    }
}

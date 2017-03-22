package com.fabio.gis.geotag.ui;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import com.fabio.gis.geotag.control.RestManager;
import com.fabio.gis.geotag.model.helper.PermissionManager;
import com.fabio.gis.geotag.R;
import com.fabio.gis.geotag.model.helper.ServerManager;
import com.fabio.gis.geotag.model.data.DataModel;
import com.fabio.gis.geotag.model.data.MapMarker;
import com.fabio.gis.geotag.model.database.DatabaseManager;
import com.fabio.gis.geotag.model.helper.Constants;
import com.fabio.gis.geotag.model.helper.JsonHandler;
import com.fabio.gis.geotag.model.data.Settings;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements MapsFragment.OnMapLongPressListener,
        TomapFragment.OnTomapSampleSentListener,
        View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseManager databaseManager;
    private RestManager mManager;

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
    private ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constants.CONFIG.IS_LOGIN_REQUIRED) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent,Constants.CONFIG.LOGIN_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.CONFIG.LOGIN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                init();
            }
        }
    }
    private void init() {
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

        mapsFragment = new MapsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.maincontainer,mapsFragment,Constants.FRAGMENT_TAG.MAP_FRAGMENT_TAG)
                //.addToBackStack(Constants.MAP_FRAGMENT_TAG)
                .commit();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferenceChangeListener = new OnSharedPreferenceChangeListener();

        fabPrimary = (FloatingActionButton) findViewById(R.id.primary_action);
        fabPrimary.setOnClickListener(this);
        fabSecondary = (FloatingActionButton) findViewById(R.id.secondary_action);
        fabSecondary.setOnClickListener(this);

        coordinatorLayout = findViewById(R.id.coordinator_layout_app_bar_main);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        databaseManager = DatabaseManager.getInstance(this);
        mManager = new RestManager();
        getFeed();
        //new DownloadDataTask().execute(Constants.TOMAP.HTTP.BASE_URL + "/sample");

    }



    @Override
    protected void onStart() {
        super.onStart();
        // checking permissions
        if(!PermissionManager.checkPermissions(this.getApplicationContext(), Arrays.asList(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION))){
            PermissionManager.requestPermissions(this,Constants.CONFIG.APPLICATION_PERMISSIONS_REQUEST);
        }

        // updating settings
        if(sharedPreferences != null) {
            for (String key : sharedPreferences.getAll().keySet()) {
                updateSettingsFromPreferences(key);
            }
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
                    case Constants.FRAGMENT_TAG.MAP_FRAGMENT_TAG:
                        fabSecondary.setVisibility(View.INVISIBLE);
                        Bundle bundle = new Bundle();
                        bundle.putDouble("latitude", mapMarker.getPositon().latitude);
                        bundle.putDouble("longitude", mapMarker.getPositon().longitude);
                        tomapFragment = new TomapFragment();
                        tomapFragment.setArguments(bundle);
                        /*
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .hide(mapsFragment)
                                .commit();
                        */
                        fragmentManager.beginTransaction()
                                .replace(R.id.maincontainer, tomapFragment, Constants.FRAGMENT_TAG.TOMAP_FRAGMENT_TAG)
                                .addToBackStack(Constants.FRAGMENT_TAG.TOMAP_FRAGMENT_TAG)
                                .commit();
                        break;
                    case Constants.FRAGMENT_TAG.TOMAP_FRAGMENT_TAG:
                        tomapFragment.buildJson();
                        break;
                    default:
                        Log.e(TAG, "Unhandled FAB fragment tag ");
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
        } else if (fragmentManager.getBackStackEntryCount() > 0){
            //fragmentManager.popBackStack(Constants.TOMAP_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.popBackStack();
            String tag = fragmentManager.findFragmentById(R.id.maincontainer).getTag();
            switch (tag) {
                case Constants.FRAGMENT_TAG.TOMAP_FRAGMENT_TAG:
                    fabSecondary.setVisibility(View.VISIBLE);
            }
        }
        else {
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
            case Constants.PREFERENCES.USERNAME_KEY:
                Settings.setUsername(sharedPreferences.getString(key, "username"));
                break;

            case Constants.PREFERENCES.PASSWORD_KEY:
                Settings.setPassword(sharedPreferences.getString(key, "password"));
                break;

            case Constants.PREFERENCES.SERVER_PROTOCOL_KEY:
                Settings.setServerProtocol(sharedPreferences.getString(key, "http"));
                break;

            case Constants.PREFERENCES.SERVER_ADDRESS_KEY:
                Settings.setServerAddress(sharedPreferences.getString(key, "localhost"));
                break;

            case Constants.PREFERENCES.SERVER_PORT_KEY:
                Settings.setServerPort(Integer.parseInt(sharedPreferences.getString(key, "8080")));
                break;

            case Constants.PREFERENCES.ZOOM_LEVEL:
                Settings.setZoomLevel(Float.parseFloat(sharedPreferences.getString(key, "16")));
                break;

            default:
        }
    }

    public void getFeed() {
        mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Loading DB Data Feed...");
        mDialog.setCancelable(true);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setIndeterminate(true);


        Call<List<DataModel.TomapSample>> listCall = mManager.getTomapService().getTomapSamples();
        listCall.enqueue(new Callback<List<DataModel.TomapSample>>() {

            @Override
            public void onResponse(Call<List<DataModel.TomapSample>> call, Response<List<DataModel.TomapSample>> response) {

                if (response.isSuccessful()) {
                    List<DataModel.TomapSample> tomapSampleList = response.body();

                    for (int i = 0; i < tomapSampleList.size(); i++) {
                        DataModel.TomapSample sample = tomapSampleList.get(i);
                        databaseManager.insertTomapSample(sample);
                    }
                } else {
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                    }
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<DataModel.TomapSample>> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    //listeners
    @Override
    public void onMapLongPress(MapMarker mapMarker) {
        this.mapMarker = mapMarker;
    }

    @Override
    public void onTomapSampleSent() {
        Snackbar  snackbar = Snackbar
                .make(findViewById(R.id.coordinator_layout_app_bar_main), getString(R.string.tomap_sample_inserted), Snackbar.LENGTH_LONG);
        snackbar.show();
        int count = fragmentManager.getBackStackEntryCount();
        /*
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .show(mapsFragment)
                .commit();
        */
        fragmentManager.popBackStack();
        fabSecondary.setVisibility(View.VISIBLE);
    }


    private class OnSharedPreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updateSettingsFromPreferences(key);
        }
    }


    // ASYNC TASKS
    class DownloadDataTask  extends AsyncTask<String, Void, Integer> {


        private static final int RESULT_OK = 0;
        private static final int RESULT_FAILED = 1;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.downloading_data_dialog));
            progressDialog.show();
            databaseManager = DatabaseManager.getInstance(getApplicationContext());
        }

        protected Integer doInBackground(String... urls) {
            try {
                HttpURLConnection connection = ServerManager.httpGetConnection(urls[0]);
                DataModel.TomapSample tomapSample = null;
                BufferedReader br = null;
                String line;
                StringBuilder json = new StringBuilder();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        json.append(line);
                    }
                    Gson gson = JsonHandler.getInstance()
                            .getGsonBuilder()
                            .create();
                    Type collectionType = new TypeToken<Collection<DataModel.TomapSample>>() {}.getType();
                    Collection<DataModel.TomapSample> enums = gson.fromJson(json.toString(), collectionType);
                    Iterator<DataModel.TomapSample> it = enums.iterator();
                    while (it.hasNext()){
                        databaseManager.getWritableDatabase()
                                .replace(Constants.TOMAP.DATABASE.TABLE_NAME_RILIEVI, null, insertTomapSampleValues(it.next()));
                    }
                    return RESULT_OK;
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return RESULT_FAILED;
        }

        protected void onPostExecute(Integer resultCode) {
            switch (resultCode) {
                case RESULT_OK:
                    progressDialog.dismiss();
                    // onLoginSuccess();
                    break;
                case RESULT_FAILED:
                    progressDialog.dismiss();
                    //onLoginFailed();
                    break;
                default:
                    // Do nothing..
            }
        }

        private ContentValues insertTomapSampleValues(DataModel.TomapSample tomapSample) {
            ContentValues values = new ContentValues();
            values.put("id_rilievi", tomapSample.getGid());
            values.put("terrasystem", tomapSample.getTerrasystem());
            values.put("id_squadra", tomapSample.getId_squadra());
            values.put("id_utente_rilevatore", tomapSample.getId_utente_rilevatore());
            values.put("id_gruppo_rilevatori", tomapSample.getId_gruppo_rilevatori());
            values.put("azienda_rilevata", tomapSample.getAzienda_rilevata());
            values.put("data_ora_rilievo", tomapSample.getData_ora_rilievo() != null ? tomapSample.getData_ora_rilievo().toString() : "");
            values.put("data_ora_invio", tomapSample.getData_ora_invio() != null ? tomapSample.getData_ora_invio().toString() : "");
            values.put("cod_punto", tomapSample.getCod_punto());
            values.put("cod_uso", tomapSample.getCod_uso());
            values.put("nome_varieta", tomapSample.getNome_varieta());
            values.put("pacciamato", tomapSample.getPacciamato());
            values.put("lato_strada", tomapSample.getLato_strada());
            values.put("visibile", tomapSample.getVisibile());
            values.put("note_coltura", tomapSample.getNote_coltura());
            values.put("data_trapianto", tomapSample.getData_trapianto() != null ? tomapSample.getData_trapianto().toString() : "");
            values.put("id_classe_tempo_trapianto", tomapSample.getId_classe_tempo_trapianto());
            values.put("id_stadio_accresc", tomapSample.getId_stadio_accresc());
            values.put("resa", tomapSample.getResa());
            values.put("note_rilievo", tomapSample.getNote_rilievo());
            values.put("note_importazione", tomapSample.getNote_importazione());
            values.put("lat", tomapSample.getLat());
            values.put("lon", tomapSample.getLon());
            values.put("mappa", tomapSample.getMappa());
            values.put("x_stima", tomapSample.getX_stima());
            values.put("id_uso", tomapSample.getId_uso());
            values.put("icon", tomapSample.getIcon());
            values.put("id_avversita", tomapSample.getId_avversita());
            values.put("grado_avversita", tomapSample.getGrado_avversita());
            values.put("id_elemento_qualitativo", tomapSample.getId_elemento_qualitativo());
            values.put("grado_elemento_qualitativo", tomapSample.getGrado_elemento_qualitativo());
            values.put("tipo_pomodoro", tomapSample.getTipo_pomodoro());
            values.put("data_raccolta", tomapSample.getData_raccolta() != null ? tomapSample.getData_raccolta().toString() : "");
            values.put("data_trapianto", tomapSample.getData_trapianto() != null ? tomapSample.getData_trapianto().toString() : "");
            values.put("data_trapianto_certezza", tomapSample.getData_trapianto_certezza());
            return values;
        }
    }
}

package com.fabio.gis.geotag.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.fabio.gis.geotag.R;
import com.fabio.gis.geotag.model.helper.ServerManager;
import com.fabio.gis.geotag.model.data.DataModel;
import com.fabio.gis.geotag.model.helper.Constants;
import com.fabio.gis.geotag.model.helper.JsonHandler;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pc on 07/03/2017.
 */

public class TomapFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private static final String TAG = TomapFragment.class.getSimpleName();

    private Context mContext;
    View rootView;
    private Calendar calendar;
    private SharedPreferences sharedPreferences;
    private DatePickerDialog.OnDateSetListener measureDateDialogListener, transplantDateDialogListener, pickDateDialogListener;
    private HashMap<Integer,String> adversitySpinnerEntries, qualitativeSpinnerEntries;
    private LatLng latLng;
    private DataModel.TomapSample tomapSample;

    //listeners
    private OnTomapSampleSentListener onTomapSampleSentListener;

    @Bind(R.id.input_latitude) EditText input_latitude;
    @Bind(R.id.input_longitude) EditText input_longitude;
    @Bind(R.id.measure_date) EditText measure_date;
    @Bind(R.id.usage_culture) AppCompatSpinner usage_culture;
    @Bind(R.id.tomato_type) AppCompatSpinner tomato_type;
    @Bind(R.id.pacciamato) CheckBox pacciamato;
    @Bind(R.id.culture_notes) EditText culture_notes;
    @Bind(R.id.transplant_date) EditText transplant_date;
    @Bind(R.id.date_certified) CheckBox date_certified;
    @Bind(R.id.growing_state) AppCompatSpinner growing_state;
    @Bind(R.id.pick_date) EditText pick_date;
    @Bind(R.id.yeld) EditText yeld;
    @Bind(R.id.adversity) AppCompatSpinner adversity;
    @Bind(R.id.adversity_level) AppCompatSpinner adversity_level;
    @Bind(R.id.qualitative_element) AppCompatSpinner qualitative_element;
    @Bind(R.id.qualitative_level) AppCompatSpinner qualitative_level;



    @Override
    public void onAttach(Context context){
        super.onAttach (context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tomap, container, false);
        ButterKnife.bind(this, rootView);
        latLng = new LatLng(getArguments().getDouble("latitude"),getArguments().getDouble("longitude"));
        mContext = getContext();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        tomapSample = new DataModel.TomapSample();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (mContext instanceof OnTomapSampleSentListener) {
            onTomapSampleSentListener = (OnTomapSampleSentListener) mContext;
        } else {
            throw new ClassCastException(mContext.toString()
                    + " must implemenet " + TAG + ".OnMapLongPressListener");
        }
        initWidgets();
        setListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initWidgets(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.usage_culture, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usage_culture.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(mContext,
                R.array.tomato_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tomato_type.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(mContext,
                R.array.growing_state, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        growing_state.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(mContext,
                R.array.adversity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adversity.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(mContext,
                R.array.adversity_level, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adversity_level.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(mContext,
                R.array.qualitative_element, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualitative_element.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(mContext,
                R.array.qualitative_level, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualitative_level.setAdapter(adapter);

        calendar = Calendar.getInstance();
        updateDateLabels();

        input_latitude.setText(String.valueOf(latLng.latitude));
        input_longitude.setText(String.valueOf(latLng.longitude));
    }
    private void setListeners(){
       measureDateDialogListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                measure_date.setText(getDateUpdated());
            }
        };

        transplantDateDialogListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                transplant_date.setText(getDateUpdated());
            }
        };

        pickDateDialogListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                pick_date.setText(getDateUpdated());
            }
        };

        measure_date.setOnClickListener(this);
        transplant_date.setOnClickListener(this);
        pick_date.setOnClickListener(this);

        usage_culture.setOnItemSelectedListener(this);
        tomato_type.setOnItemSelectedListener(this);
        growing_state.setOnItemSelectedListener(this);
        adversity.setOnItemSelectedListener(this);
        adversity_level.setOnItemSelectedListener(this);
        qualitative_element.setOnItemSelectedListener(this);
        qualitative_level.setOnItemSelectedListener(this);
    }

    private String getDateUpdated() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.CONFIG.DATE_FORMAT);
        return sdf.format(calendar.getTime());
    }

    private void updateDateLabels(){
        measure_date.setText(getDateUpdated());
    }

    public void buildJson(){
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.CONFIG.DATE_FORMAT);
        //getSpinnerValues();
        try {
            int s = sharedPreferences.getInt(Constants.TOMAP.PREFERENCES.TOMAP_ID_UTENTE, 0);
            tomapSample.setId_utente_rilevatore(sharedPreferences.getInt(Constants.TOMAP.PREFERENCES.TOMAP_ID_UTENTE, 0));
            tomapSample.setId_gruppo_rilevatori(sharedPreferences.getInt(Constants.TOMAP.PREFERENCES.TOMAP_ID_GRUPPO, 0));
            tomapSample.setCognome_rilevatore(sharedPreferences.getString(Constants.TOMAP.PREFERENCES.TOMAP_GOGNOME, ""));
            tomapSample.setId_squadra(sharedPreferences.getInt(Constants.TOMAP.PREFERENCES.TOMAP_ID_SQUADRA, 0));
            tomapSample.setLat(Double.parseDouble(input_latitude.getText().toString()));
            tomapSample.setLon(Double.parseDouble(input_longitude.getText().toString()));
            tomapSample.setData_ora_rilievo((sdf.parse(measure_date.getText().toString())));
            // TODO: 13/03/2017 aggiungere interfaccia con db per avere il rispettivo dell id
            tomapSample.setId_uso(30);
            tomapSample.setPacciamato(pacciamato.isEnabled());
            tomapSample.setNote_coltura(culture_notes.getText().toString());
            if(!transplant_date.getText().toString().equals("")) tomapSample.setData_trapianto(sdf.parse(transplant_date.getText().toString()));
            tomapSample.setData_trapianto_certezza(date_certified.isEnabled() ? getString(R.string.yes) : getString(R.string.no_date_estimated));
            if(!pick_date.getText().toString().equals("")) tomapSample.setData_raccolta(sdf.parse(pick_date.getText().toString()));
            if(!yeld.getText().toString().equals("")) tomapSample.setResa(Double.parseDouble(yeld.getText().toString()));
            //tomapSample.setGrado_avversita();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Gson gson = JsonHandler.getInstance()
                        .getGsonBuilder()
                        .create();
        String json = gson.toJson(tomapSample);
        new TomapSampleHttpPostTask(json).execute(Constants.TOMAP.HTTP.BASE_URL + "/sample", json);
    }

    // TODO: 13/03/2017 da rimuovere getspinnervalues? 
    /*
    private void getSpinnerValues(){
        int i;
        Resources res = getResources();
        String[] stringArray = res.getStringArray(R.array.adversity_level);
        for (i = 0; i < stringArray.length; i++) {
            adversitySpinnerEntries.put(i,stringArray[i]);
        }
        stringArray = res.getStringArray(R.array.qualitative_level);
        for (i = 0; i < stringArray.length; i++) {
            qualitativeSpinnerEntries.put(i,stringArray[i]);
        }
    }
    */

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.measure_date:
                new DatePickerDialog(mContext, measureDateDialogListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.transplant_date:
                new DatePickerDialog(mContext, transplantDateDialogListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.pick_date:
                new DatePickerDialog(mContext, pickDateDialogListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Resources res = getResources();;
        switch (parent.getId()){
            case R.id.usage_culture:
                tomapSample.setCod_uso(30); //= parent.getItemAtPosition(position).toString();
                break;
            case R.id.tomato_type:
                tomapSample.setTipo_pomodoro(parent.getItemAtPosition(position).toString());
                break;
            case R.id.growing_state:
                // TODO: 13/03/2017 rivedere inserimento entry nel db tomap: inserire stringhe anziche id
                tomapSample.setId_stadio_accresc(position);
                break;
            case R.id.adversity:
                tomapSample.setId_avversita(position);
                break;
            case R.id.adversity_level:
                tomapSample.setGrado_avversita(res.getStringArray(R.array.adversity_level_label)[position]);
                break;
            case R.id.qualitative_element:
                tomapSample.setId_elemento_qualitativo(position);
                break;
            case R.id.qualitative_level:
                tomapSample.setGrado_elemento_qualitativo(res.getStringArray(R.array.qualitative_level_label)[position]);
                break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {    }

    private void onTomapSamplePostSucced() {
        onTomapSampleSentListener.onTomapSampleSent();
        Log.i(TAG, getString(R.string.tomap_sample_inserted));
    }

    private void onTomapSamplePostFail() {

    }

    // interfaces
    public interface OnTomapSampleSentListener{
        public void onTomapSampleSent();
    }

    class TomapSampleHttpPostTask extends AsyncTask<String, Void, Integer> {


        private static final int RESULT_OK = 0;
        private static final int RESULT_FAILED = 1;
        private ProgressDialog progressDialog;
        private String payload;

        public TomapSampleHttpPostTask(String payload) {
            super();
            this.payload = payload;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.posting_sample_dialog));
            progressDialog.show();
        }

        protected Integer doInBackground(String... urls) {
            try {
                HttpURLConnection connection = ServerManager.httpPostJson(urls[0], this.payload);
                DataModel.TomapSample tomapSample = null;
                BufferedReader br = null;
                String line;
                StringBuilder json = new StringBuilder();
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        json.append(line);
                    }
                    Gson gson = JsonHandler.getInstance()
                            .getGsonBuilder()
                            .create();
                    Type collectionType = new TypeToken<Collection<DataModel.TomapSample>>(){}.getType();
                    Collection<DataModel.TomapSample> enums = gson.fromJson(json.toString(), collectionType);
                    Iterator<DataModel.TomapSample> it = enums.iterator();
                    // TODO: 17/03/2017 lettura dati risposta alla post
                    return RESULT_OK;
                }
            } catch (Exception e) {
                Log.e(TAG,e.toString());
            }
            return RESULT_FAILED;
        }

        protected void onPostExecute(Integer resultCode) {
            switch (resultCode) {
                case  RESULT_OK:
                    progressDialog.dismiss();
                    onTomapSamplePostSucced();
                    break;
                case RESULT_FAILED:
                    progressDialog.dismiss();
                    onTomapSamplePostFail();
                    break;
                default:
                    // Do nothing..
            }
        }
    }
}

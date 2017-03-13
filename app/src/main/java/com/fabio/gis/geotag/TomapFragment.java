package com.fabio.gis.geotag;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
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

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private DatePickerDialog.OnDateSetListener measureDateDialogListener, transplantDateDialogListener, pickDateDialogListener;
    private HashMap<Integer,String> adversitySpinnerEntries, qualitativeSpinnerEntries;
    private LatLng latLng;
    private DataModel.TomapSample tomapSample;

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
        mContext = context;
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
        initWidgets();
        setListeners();
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
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        return sdf.format(calendar.getTime());
    }

    private void updateDateLabels(){
        measure_date.setText(getDateUpdated());
    }

    public void buildJson(){
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        //getSpinnerValues();
        try {
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
        Log.i(TAG,json);
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
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

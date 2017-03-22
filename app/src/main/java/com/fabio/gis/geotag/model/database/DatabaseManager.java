package com.fabio.gis.geotag.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fabio.gis.geotag.model.data.DataModel;
import com.fabio.gis.geotag.model.helper.Constants;

/**
 * Created by pc on 20/03/2017.
 */

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String TAG = DatabaseManager.class.getSimpleName();
    private static final String DATABASE_NAME = Constants.TOMAP.DATABASE.DB_NAME;
    private static final int SCHEMA_VERSION=3;
    private static DatabaseManager singleton=null;

    public synchronized static DatabaseManager getInstance(Context ctxt) {
        if (singleton == null) {
            singleton=new DatabaseManager(ctxt.getApplicationContext());
        }

        return(singleton);
    }

    private DatabaseManager(Context ctxt) {
        super(ctxt, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    public void insertTomapSample(DataModel.TomapSample sample) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.replace(Constants.TOMAP.DATABASE.TABLE_NAME_RILIEVI, null, insertTomapSampleValues(sample));
        db.close();
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

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.TOMAP.DATABASE.DB_CREATE_TABLE_RILIEVI);
        db.execSQL(Constants.TOMAP.DATABASE.DB_CREATE_TABLE_USI_SUOLO);
        db.execSQL(Constants.TOMAP.DATABASE.DB_CREATE_TABLE_STADI_ACCRESCIMENTO);
        db.execSQL(Constants.TOMAP.DATABASE.DB_CREATE_TABLE_AVVERSITA);
        db.execSQL(Constants.TOMAP.DATABASE.DB_CREATE_TABLE_ELEMENTI_QUALITATIVI);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG,"Updating db from version: " + oldVersion + " up to version: " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TOMAP.DATABASE.TABLE_NAME_RILIEVI);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TOMAP.DATABASE.TABLE_NAME_USI_SUOLO);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TOMAP.DATABASE.TABLE_NAME_STADI_ACCRESCIMENTO);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TOMAP.DATABASE.TABLE_NAME_AVVERSITA);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TOMAP.DATABASE.TABLE_NAME_ELEMENTI_QUALITATIVI);
        onCreate(db);
    }
}

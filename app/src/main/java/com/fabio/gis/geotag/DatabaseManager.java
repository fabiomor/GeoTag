package com.fabio.gis.geotag;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by pc on 20/03/2017.
 */

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String TAG = DatabaseManager.class.getSimpleName();
    private static final String DATABASE_NAME = Constants.DB_NAME;
    private static final int SCHEMA_VERSION=3;
    private static DatabaseManager singleton=null;

    synchronized static DatabaseManager getInstance(Context ctxt) {
        if (singleton == null) {
            singleton=new DatabaseManager(ctxt.getApplicationContext());
        }

        return(singleton);
    }

    private DatabaseManager(Context ctxt) {
        super(ctxt, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.DB_CREATE_TABLE_RILIEVI);
        db.execSQL(Constants.DB_CREATE_TABLE_USI_SUOLO);
        db.execSQL(Constants.DB_CREATE_TABLE_STADI_ACCRESCIMENTO);
        db.execSQL(Constants.DB_CREATE_TABLE_AVVERSITA);
        db.execSQL(Constants.DB_CREATE_TABLE_ELEMENTI_QUALITATIVI);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG,"Updating db from version: " + oldVersion + " up to version: " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_RILIEVI);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_USI_SUOLO);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_STADI_ACCRESCIMENTO);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_AVVERSITA);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_ELEMENTI_QUALITATIVI);
        onCreate(db);

    }
}

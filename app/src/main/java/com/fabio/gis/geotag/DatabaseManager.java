package com.fabio.gis.geotag;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pc on 20/03/2017.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="empublite.db";
    private static final int SCHEMA_VERSION=1;
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
        db.execSQL("");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

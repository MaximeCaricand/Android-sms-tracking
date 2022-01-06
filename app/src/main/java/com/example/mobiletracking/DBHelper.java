package com.example.mobiletracking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    //paramètres BDD
    private static final String DBNAME = "emargementnfc";
    private static final int VER = 1;

    private static final String STALKER_TABLE="stalker";
    private static final String VICTIM_TABLE="victim";

    //Table Stalker
    private static final String STALKER_ID="id";
    private static final String STALKER_TEL="tel";
    private static final String STALKER_LOCALISATION="localisation";
    private  String createStalker = "CREATE TABLE " + STALKER_TABLE +
            "(" + STALKER_ID + " INTEGER PRIMARY KEY,"
                + STALKER_TEL + " TEXT,"
                + STALKER_LOCALISATION + " TEXT" +
            ")";

    //Table Victim
    private static final String VICTIM_ID="id";
    private static final String VICTIM_NBPAS="nbPas";
    private static final String VICTIM_AVGSPEED="avgSpeed";
    private static final String VICTIM_DISTPARCOURU="distParcouru";
    private static final String VICTIM_DATE="date";
    private static final String VICTIM_HEURE="heure";
    private  String createVictim = "CREATE TABLE " + VICTIM_TABLE +
            "(" + VICTIM_ID + " INTEGER PRIMARY KEY,"
                + VICTIM_NBPAS + " INTEGER,"
                + VICTIM_AVGSPEED + " FLOAT,"
                + VICTIM_DISTPARCOURU + " FLOAT,"
                + VICTIM_DATE + " TEXT,"
                + VICTIM_HEURE + " TEXT" +
            ")";

    public DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createStalker);
        db.execSQL(createVictim);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHandler", "upgrading database");
        db.execSQL("drop table if exists " + STALKER_TABLE + "");
        db.execSQL("drop table if exists " + VICTIM_TABLE + "");
        onCreate(db);
    }

    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists " + STALKER_TABLE + "");
        db.execSQL("drop table if exists " + VICTIM_TABLE + "");
        db.execSQL(createStalker;
        db.execSQL(createVictim);
        db.close();
    }
}

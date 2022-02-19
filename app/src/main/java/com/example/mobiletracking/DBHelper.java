package com.example.mobiletracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    // paramètres BDD
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

    //Fonctions basiques de la base
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
        db.execSQL(createStalker);
        db.execSQL(createVictim);
        db.close();
    }

    //Fonction pour la table Stalker
    //Ajout d'un élément dans la table
    public long addStalker(Stalker stalker){
        long insertId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STALKER_ID, stalker.getId());
        values.put(STALKER_TEL, stalker.getTel());
        values.put(STALKER_LOCALISATION, stalker.getLocalisation());

        // Inserting Row
        insertId = db.insert(STALKER_TABLE, null, values);
        db.close(); // Closing database connection
        return insertId;
    }

    //Fonction pour la table Victim
    //Ajout d'un élément dans la table
    public long addVictim(Victim victim){
        long insertId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VICTIM_ID, victim.getId());
        values.put(VICTIM_NBPAS, victim.getNbPas());
        values.put(VICTIM_AVGSPEED, victim.getAvgSpeed());
        values.put(VICTIM_DISTPARCOURU, victim.getDistParcouru());
        values.put(VICTIM_DATE, victim.getDate());
        values.put(VICTIM_HEURE, victim.getHeure());

        // Inserting Row
        insertId = db.insert(VICTIM_TABLE, null, values);
        db.close(); // Closing database connection
        return insertId;
    }
}

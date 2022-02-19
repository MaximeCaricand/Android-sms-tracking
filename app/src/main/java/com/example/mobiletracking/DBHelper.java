package com.example.mobiletracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    // paramètres BDD
    private static final String DBNAME = "mobileTracking";
    private static final int VER = 1;

    private static final String POSITION_TABLE="position";
    private static final String WALKER_TABLE="walker";

    //Table position
    private static final String POSITION_PHONE_NUMBER="phoneNumber";
    private static final String POSITION_LAT="lat";
    private static final String POSITION_LONG="long";
    private static final String POSITION_DATE ="date";
    private  String createPosition = "CREATE TABLE " + POSITION_TABLE +
            "(" + POSITION_PHONE_NUMBER + " TEXT,"
                + POSITION_LAT + " TEXT,"
                + POSITION_LONG + " TEXT,"
                + POSITION_DATE + " TEXT" +
            ")";

    //Table Walker
    private static final String WALKER_ID="id";
    private static final String WALKER_NUMBERSTEP="numberStep";
    private static final String WALKER_CURRENTTIMESPEED="currentTimeSpeed";
    private static final String WALKER_DISTTRAVELED="distTraveled";
    private static final String WALKER_DATE="date";
    private static final String WALKER_HOUR="hour";
    private  String createWalker = "CREATE TABLE " + WALKER_TABLE +
            "(" + WALKER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WALKER_NUMBERSTEP + " INTEGER,"
                + WALKER_CURRENTTIMESPEED + " FLOAT,"
                + WALKER_DISTTRAVELED + " FLOAT,"
                + WALKER_DATE + " TEXT,"
                + WALKER_HOUR + " TEXT" +
            ")";

    public DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, VER);
    }

    //Fonctions basiques de la base
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createPosition);
        db.execSQL(createWalker);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHandler", "upgrading database");
        clearTable();
    }

    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + POSITION_TABLE + "");
        db.execSQL("delete from " + WALKER_TABLE + "");
        db.close();
    }

    //Fonction pour la table Position
    //Ajout d'un élément dans la table
    public long addPosition(Position position){
        long insertId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(POSITION_PHONE_NUMBER, position.getPhoneNumber());
        values.put(POSITION_LAT, position.getLat());
        values.put(POSITION_LONG, position.getLng());
        values.put(POSITION_DATE, position.getDate());

        // Inserting Row
        insertId = db.insert(POSITION_TABLE, null, values);
        db.close(); // Closing database connection
        return insertId;
    }

    //Fonction pour la table Position
    //Ajout d'un élément dans la table
    public ArrayList<Position> getAllPositions() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(POSITION_TABLE,
                new String[] {POSITION_PHONE_NUMBER, POSITION_LAT, POSITION_LONG, POSITION_DATE},
                null, null, null, null, null, null);

        ArrayList<Position> p = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                p.add(new Position(cursor.getString(0), cursor.getFloat(1), cursor.getFloat(2), cursor.getLong(3)));
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
        return p;
    }

    //Fonction pour la table Victim
    //Ajout d'un élément dans la table
    public long addWalker(Walker walker){
        long insertId = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WALKER_NUMBERSTEP, walker.getNbPas());
        values.put(WALKER_CURRENTTIMESPEED, walker.getAvgSpeed());
        values.put(WALKER_DISTTRAVELED, walker.getDistParcouru());
        values.put(WALKER_DATE, walker.getDate());
        values.put(WALKER_HOUR, walker.getHeure());

        // Inserting Row
        insertId = db.insert(WALKER_TABLE, null, values);
        db.close(); // Closing database connection
        return insertId;
    }
    //recuperer tous les exams
    public ArrayList<Walker> getAllWalkers() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(WALKER_TABLE,
                new String[] {WALKER_NUMBERSTEP, WALKER_CURRENTTIMESPEED, WALKER_DISTTRAVELED, WALKER_DATE, WALKER_HOUR},
                null, null, null, null, null, null);

        ArrayList<Walker> w = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                w.add(new Walker(cursor.getInt(0), cursor.getFloat(1), cursor.getFloat(2), cursor.getString(3), cursor.getString(4)));
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
        return w;
    }
}

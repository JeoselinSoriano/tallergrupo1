package com.example.romy.gpsfinal;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by estudiante on 16/08/16.
 */
public class MyDataBase extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "SQLiteGPS.db";
    private static final int DATABASE_VERSION = 1;
    public static final String GPS_TABLE_NAME = "gpslocation";
    public static final String GPS_COLUMN_ID = "_id";
    public static final String GPS_COLUMN_LAT = "latitud";
    public static final String GPS_COLUMN_LONG = "longitud";
    public static final String GPS_COLUMN_TIME = "tiempo";

    public MyDataBase(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+GPS_TABLE_NAME + "(" +
                GPS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                GPS_COLUMN_LAT +" TEXT, "+
                GPS_COLUMN_LONG+" TEXT, "+
                GPS_COLUMN_TIME+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABL IF EXISTS "+GPS_TABLE_NAME);
        onCreate(db);
    }
    public void insertGPS(JGPS gps) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GPS_COLUMN_LAT, gps.getLatitud());
        contentValues.put(GPS_COLUMN_LONG, gps.getLongitud());
        contentValues.put(GPS_COLUMN_TIME, gps.getHora());
        db.insert(GPS_TABLE_NAME, null, contentValues);
        db.close();
    }
    public List<JGPS> getDatos(){
        List<JGPS> lista = new ArrayList<JGPS>();
        String selectQuery = "Select * from " + GPS_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do{
                JGPS gp = new JGPS();
                gp.setId(Integer.parseInt(cursor.getString(0)));
                gp.setLatitud(cursor.getString(1));
                gp.setLongitud(cursor.getString(2));
                gp.setHora(cursor.getString(3));

                lista.add(gp);
            }while (cursor.moveToNext());

        }
        return lista;

    }
    public void updateGPS(JGPS gps) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GPS_COLUMN_LAT, gps.getLatitud());
        contentValues.put(GPS_COLUMN_LONG, gps.getLongitud());
        contentValues.put(GPS_COLUMN_TIME, gps.getHora());
        db.update(GPS_TABLE_NAME, contentValues, GPS_COLUMN_ID + " = ? ", new String[] { String.valueOf(gps.getId())} );
        db.close();
    }


    public Cursor getGPS(JGPS gps) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + GPS_TABLE_NAME + " WHERE " +
                GPS_COLUMN_ID + "=?", new String[] {  String.valueOf(gps.getId()) } );
        return res;
    }
    public Cursor getAllGPS() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + GPS_TABLE_NAME, null );
        return res;
    }

    public Integer deleteGPS(JGPS gps) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(GPS_TABLE_NAME,
                GPS_COLUMN_ID + " = ? ",
                new String[] {  String.valueOf(gps.getId()) });
    }


}
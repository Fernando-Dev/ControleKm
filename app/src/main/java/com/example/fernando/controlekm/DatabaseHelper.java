package com.example.fernando.controlekm;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Flavia on 30/07/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_DATA = "data";
    public static final String KEY_ITINERARIO = "itinerario";
    public static final String KEY_KM_INICIAL = "kmInicial";
    public static final String KEY_KM_FINAL = "kmFinal";
    public static final String KEY_KM_TOTAL = "kmTotal";
    public static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "ControleKm";
    public static final String DATABASE_TABLE = "kms";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table kms(_id integer primary key autoincrement,"
            + "data date not null, itinerario text not null, kmInicial integer not null, kmFinal integer not null, kmTotal integer not null);";
    private static final String KEY_ID_USERS = "id";
    private static final String KEY_NOME = "nome";
    private static final String DATABASE_TABLE_USERS = "usuarios";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DATABASE_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version" + oldVersion + "to"
                + newVersion + ",wich will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }
}


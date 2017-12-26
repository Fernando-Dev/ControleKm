package com.example.fernando.controlekm.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.NavUtils;
import android.util.Log;

import com.example.fernando.controlekm.DatabaseHelper;
import com.example.fernando.controlekm.dominio.Km;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.start;
import static com.example.fernando.controlekm.DatabaseHelper.DATABASE_TABLE;
import static com.example.fernando.controlekm.DatabaseHelper.KEY_DATA;
import static com.example.fernando.controlekm.DatabaseHelper.KEY_ITINERARIO;
import static com.example.fernando.controlekm.DatabaseHelper.KEY_KM_FINAL;
import static com.example.fernando.controlekm.DatabaseHelper.KEY_KM_INICIAL;
import static com.example.fernando.controlekm.DatabaseHelper.KEY_KM_TOTAL;
import static com.example.fernando.controlekm.DatabaseHelper.KEY_ROWID;


/**
 * Created by Flavia on 22/07/2017.
 */

public class DBAdapter {

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;


    public DBAdapter(Context context) {
        DBHelper = new DatabaseHelper(context);
    }

    //    abrindo banco de dados
    public SQLiteDatabase open() throws SQLException {
        if (db == null) {
            db = DBHelper.getWritableDatabase();
        }
        return db;
    }

    //    fechando o banco de dados
    public void close() {
        DBHelper.close();
    }

    //    INSERINDO CONTATOS NO BANCO DE DADOS
    public long inserirKm(Km km) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseHelper.KEY_DATA, km.getData().getTime());
        initialValues.put(DatabaseHelper.KEY_ITINERARIO, km.getItinerario());
        initialValues.put(DatabaseHelper.KEY_KM_INICIAL, km.getKmInicial());
        initialValues.put(DatabaseHelper.KEY_KM_FINAL, km.getKmFinal());
//        initialValues.put(DatabaseHelper.KEY_KM_TOTAL, km.getKmTotal());
        return open().insert(DatabaseHelper.DATABASE_TABLE, null, initialValues);
    }

    public Cursor getAllKms() {
        return db.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, KEY_DATA, KEY_ITINERARIO, KEY_KM_INICIAL, KEY_KM_FINAL},null,null,null,
                null,null,null);
    }

    //    deletando somente um contato
    public boolean deleteKm(Long rowId) {
        String whereClause = DatabaseHelper.KEY_ROWID + " = ?";
        String[] whereArgs = new String[]{rowId.toString()};
        int removidos = open().delete(DatabaseHelper.DATABASE_TABLE,
                whereClause, whereArgs);
        return removidos > 0;
    }

    //    retornando todos os contatos
    public List<Km> getAllKm() {
        Cursor cursor = open().query(DatabaseHelper.DATABASE_TABLE,
                new String[]{KEY_ROWID, KEY_DATA, KEY_ITINERARIO, KEY_KM_INICIAL, KEY_KM_FINAL/*, KEY_KM_TOTAL*/},
                null, null, null, null, null);
        List<Km> kms = new ArrayList<Km>();
        while (cursor.moveToNext()) {
            Km km = criarKm(cursor);
            kms.add(km);
        }
        cursor.close();
        return kms;
    }

    private Km criarKm(Cursor cursor) {
        Km km = new Km(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_ROWID)),
                new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.KEY_DATA))),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ITINERARIO)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_KM_INICIAL)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_KM_FINAL)));
                /*cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_KM_TOTAL)));*/
        return km;
    }


    //    retornando somente um contato
    public Km getKm(Integer rowId) throws SQLException {
        Cursor cursor = open().query(true, DatabaseHelper.DATABASE_TABLE, new String[]{KEY_ROWID, KEY_DATA,
                        KEY_ITINERARIO, KEY_KM_INICIAL, KEY_KM_FINAL, KEY_KM_TOTAL}, DatabaseHelper.KEY_ROWID + " = ?",
                new String[]{rowId.toString()}, null, null, null, null);
        if (cursor.moveToNext()) {
            Km km = criarKm(cursor);
            cursor.close();
            return km;
        }
        return null;

    }
//    atualizar um contato

    public boolean updateKm(Long rowId, Km km) {
        ContentValues args = new ContentValues();
        args.put(DatabaseHelper.KEY_DATA, km.getData().getTime());
        args.put(DatabaseHelper.KEY_ITINERARIO, km.getItinerario());
        args.put(DatabaseHelper.KEY_KM_INICIAL, km.getKmInicial());
        args.put(DatabaseHelper.KEY_KM_FINAL, km.getKmFinal());
//        args.put(DatabaseHelper.KEY_KM_TOTAL, km.getKmTotal());
        return open().update(DatabaseHelper.DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}

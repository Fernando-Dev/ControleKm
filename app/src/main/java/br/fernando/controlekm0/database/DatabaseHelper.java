package br.fernando.controlekm0.database;

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
    public static final String KEY_QTD_CLIENTE = "qtdCliente";
    public static final String KEY_KM_INICIAL = "kmInicial";
    public static final String KEY_KM_FINAL = "kmFinal";
    public static final String KEY_KM_TOTAL = "kmTotal";
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "ControleKm";
    public static final String DATABASE_TABLE = "kms";
    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_CREATE = "create table kms(_id integer primary key autoincrement,"
            + "data text not null, itinerario text not null, qtdCliente integer not null, kmInicial integer not null, " +
            "kmFinal integer not null, kmTotal integer not null);";
    public static final String KEY_ID_USERS = "id";
    public static final String KEY_NOME = "nome";
    public static final String KEY_UNIDADE = "unidade";
    public static final String KEY_FUNCAO = "funcao";
    public static final String KEY_PLACA = "placa";
    public static final String KEY_GERENCIA = "gerencia";
    public static final String DATABASE_TABLE_USERS = "usuarios";
    private static final String DATABASE_CREATE_USERS = "create table usuarios(id integer primary key autoincrement,"
            + "nome text not null, unidade text not null," +
            "funcao text not null,placa text not null,gerencia text not null);";
    public static final String ID_TROCA_OLEO = "idTroca";
    public static final String KM_TROCA_OLEO = "kmTroca";
    public static final String DATA_TROCA_OLEO = "dataTroca";
    public static final String VIDA_UTIL_OLEO = "vidaUtil";
    public static final String KM_PROXIMA_TROCA = "kmProximaTroca";
    public static final String DATABASE_TABLE_TROCA_OLEO = "trocasDeOleo";
    public static final String DATABASE_CREATE_TROCA_OLEO = "create table trocasDeOleo(idTroca integer primary key autoincrement," +
            "kmTroca integer not null, dataTroca text not null, vidaUtil integer not null,kmProximaTroca integer not null);";
    public static final String ID_MANUTENCAO = "idManutencao";
    public static final String KM_MANUTENCAO = "kmManutencao";
    public static final String DATA_MANUTENCAO = "dataManutencao";
    public static final String DISTANCIA_MANUTENCAO = "distanciaManutencao";
    public static final String KM_PROXIMA_MANUTENCAO = "kmProximaManutencao";
    public static final String DATABASE_TABLE_MANUTENCAO = "manutencoes";
    public static final String DATABASE_CREATE_MANUTENCAO = "create table manutencoes(idManutencao integer primary key autoincrement," +
            "kmManutencao integer not null, dataManutencao text not null,distanciaManutencao integer not null, kmProximaManutencao integer not null);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE_USERS);
            db.execSQL(DATABASE_CREATE_TROCA_OLEO);
            db.execSQL(DATABASE_CREATE_MANUTENCAO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version" + oldVersion + "to"
                + newVersion + ",wich will destroy all old data");
        String sqlUsuarios = "DROP TABLE IF EXISTS usuarios";
        String sqlKms = "DROP TABLE IF EXISTS kms";
        String sqlTrocaOleo = "DROP TABLE IF EXISTS trocasDeOleo";
        String sqlManutencao = "DROP TABLE IF EXISTS manutencoes";
        db.execSQL(sqlUsuarios);
        db.execSQL(sqlKms);
        db.execSQL(sqlTrocaOleo);
        db.execSQL(sqlManutencao);
        onCreate(db);
    }
}


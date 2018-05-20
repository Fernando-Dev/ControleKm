package br.fernando.controlekm.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import br.fernando.controlekm.BD.DatabaseHelper;
import br.fernando.controlekm.dominio.Km;
import br.fernando.controlekm.dominio.Manutencao;
import br.fernando.controlekm.dominio.TrocaOleo;
import br.fernando.controlekm.dominio.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static br.fernando.controlekm.BD.DatabaseHelper.DATABASE_TABLE;
import static br.fernando.controlekm.BD.DatabaseHelper.DATABASE_TABLE_MANUTENCAO;
import static br.fernando.controlekm.BD.DatabaseHelper.DATABASE_TABLE_TROCA_OLEO;
import static br.fernando.controlekm.BD.DatabaseHelper.DATABASE_TABLE_USERS;
import static br.fernando.controlekm.BD.DatabaseHelper.DATA_MANUTENCAO;
import static br.fernando.controlekm.BD.DatabaseHelper.DATA_TROCA_OLEO;
import static br.fernando.controlekm.BD.DatabaseHelper.DISTANCIA_MANUTENCAO;
import static br.fernando.controlekm.BD.DatabaseHelper.ID_MANUTENCAO;
import static br.fernando.controlekm.BD.DatabaseHelper.ID_TROCA_OLEO;
import static br.fernando.controlekm.BD.DatabaseHelper.KEY_DATA;
import static br.fernando.controlekm.BD.DatabaseHelper.KEY_FUNCAO;
import static br.fernando.controlekm.BD.DatabaseHelper.KEY_GERENCIA;
import static br.fernando.controlekm.BD.DatabaseHelper.KEY_ID_USERS;
import static br.fernando.controlekm.BD.DatabaseHelper.KEY_ITINERARIO;
import static br.fernando.controlekm.BD.DatabaseHelper.KEY_KM_FINAL;
import static br.fernando.controlekm.BD.DatabaseHelper.KEY_KM_INICIAL;
import static br.fernando.controlekm.BD.DatabaseHelper.KEY_KM_TOTAL;
import static br.fernando.controlekm.BD.DatabaseHelper.KEY_NOME;
import static br.fernando.controlekm.BD.DatabaseHelper.KEY_PLACA;
import static br.fernando.controlekm.BD.DatabaseHelper.KEY_QTD_CLIENTE;
import static br.fernando.controlekm.BD.DatabaseHelper.KEY_ROWID;
import static br.fernando.controlekm.BD.DatabaseHelper.KEY_UNIDADE;
import static br.fernando.controlekm.BD.DatabaseHelper.KM_MANUTENCAO;
import static br.fernando.controlekm.BD.DatabaseHelper.KM_PROXIMA_MANUTENCAO;
import static br.fernando.controlekm.BD.DatabaseHelper.KM_PROXIMA_TROCA;
import static br.fernando.controlekm.BD.DatabaseHelper.KM_TROCA_OLEO;
import static br.fernando.controlekm.BD.DatabaseHelper.VIDA_UTIL_OLEO;


/**
 * Created by Flavia on 22/07/2017.
 */

public class DBAdapter {

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private List<Map<String, Object>> users;
    private Map<String, Object> usuarios;


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

    public SQLiteDatabase read() throws SQLException {
        if (db == null) {
            db = DBHelper.getReadableDatabase();
        }
        return db;
    }

    //    fechando o banco de dados
    public void close() {
        DBHelper.close();
    }

    //    INSERINDO quilometragem NO BANCO DE DADOS
    public long inserirKm(Km km) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseHelper.KEY_DATA, km.getData());
        initialValues.put(DatabaseHelper.KEY_ITINERARIO, km.getItinerario());
        initialValues.put(DatabaseHelper.KEY_QTD_CLIENTE, km.getQtdCliente());
        initialValues.put(DatabaseHelper.KEY_KM_INICIAL, km.getKmInicial());
        initialValues.put(DatabaseHelper.KEY_KM_FINAL, km.getKmFinal());
        initialValues.put(DatabaseHelper.KEY_KM_TOTAL, km.getKmTotal());
        return open().insert(DatabaseHelper.DATABASE_TABLE, null, initialValues);
    }

    //    INSERINDO USUARIOS NO BANCO DE DADOS
    public long inserirUsuario(Usuario usuario) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseHelper.KEY_NOME, usuario.getNome());
        initialValues.put(DatabaseHelper.KEY_UNIDADE, usuario.getUnidade());
        initialValues.put(DatabaseHelper.KEY_FUNCAO, usuario.getFuncao());
        initialValues.put(DatabaseHelper.KEY_PLACA, usuario.getPlaca());
        initialValues.put(DatabaseHelper.KEY_GERENCIA, usuario.getGerencia());
        return open().insert(DatabaseHelper.DATABASE_TABLE_USERS, null, initialValues);
    }

    //    inseindo dados na tabela troca de oleo
    public long inserirTrocaOleo(TrocaOleo trocaOleo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.KM_TROCA_OLEO, trocaOleo.getKmTroca());
        contentValues.put(DatabaseHelper.DATA_TROCA_OLEO, trocaOleo.getDataTroca());
        contentValues.put(DatabaseHelper.VIDA_UTIL_OLEO, trocaOleo.getVidaUtilOleo());
        contentValues.put(DatabaseHelper.KM_PROXIMA_TROCA, trocaOleo.getProximaTroca());
        return open().insert(DatabaseHelper.DATABASE_TABLE_TROCA_OLEO, null, contentValues);
    }

    //    inserindo dados na tabela de manutencoes
    public long inserirManutencao(Manutencao manutencao) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.KM_MANUTENCAO, manutencao.getKmManutencao());
        contentValues.put(DatabaseHelper.DATA_MANUTENCAO, manutencao.getDataManutencao());
        contentValues.put(DatabaseHelper.DISTANCIA_MANUTENCAO, manutencao.getDistanciaManutencao());
        contentValues.put(DatabaseHelper.KM_PROXIMA_MANUTENCAO, manutencao.getKmProximaManutencao());
        return open().insert(DatabaseHelper.DATABASE_TABLE_MANUTENCAO, null, contentValues);
    }

    // retornando todas as quiloemtragens
    public Cursor getAllKms() {
        return db.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, KEY_DATA, KEY_ITINERARIO, KEY_QTD_CLIENTE, KEY_KM_INICIAL, KEY_KM_FINAL, KEY_KM_TOTAL}, null, null, null,
                null, null, null);
    }

    //    retornando todos as trcoas de oleo
    public Cursor getTrocaOleo() {
        return read().query(true, DATABASE_TABLE_TROCA_OLEO, new String[]{ID_TROCA_OLEO, KM_TROCA_OLEO, DATA_TROCA_OLEO, VIDA_UTIL_OLEO, KM_PROXIMA_TROCA},
                null, null, null, null, null, null);
    }

    //    retorna todas as manutencoes
    public Cursor getmanutencoes() {
        return read().query(true, DATABASE_TABLE_MANUTENCAO, new String[]{ID_MANUTENCAO, KM_MANUTENCAO, DATA_MANUTENCAO, DISTANCIA_MANUTENCAO, KM_PROXIMA_MANUTENCAO},
                null, null, null, null, null, null, null);
    }

    //    retornando todos os usuarios
    public Cursor getAllUsers() {
        return db.query(DATABASE_TABLE_USERS,
                new String[]{KEY_ID_USERS, KEY_NOME, KEY_UNIDADE, KEY_FUNCAO, KEY_PLACA, KEY_GERENCIA}, null, null, null,
                null, null, null);
    }


    //    deletando somente uma quilometragem
    public boolean deleteKm(Integer rowId) {
        String whereClause = DatabaseHelper.KEY_ROWID + " = ?";
        String[] whereArgs = new String[]{rowId.toString()};
        int removidos = open().delete(DatabaseHelper.DATABASE_TABLE,
                whereClause, whereArgs);
        return removidos > 0;
    }

    public boolean deleteUser(Integer rowId) {
        //    DELELTANDO UM USUARIO
        String whereClause = DatabaseHelper.KEY_ID_USERS + " = ?";
        String[] whereArgs = new String[]{rowId.toString()};
        int removidos = open().delete(DatabaseHelper.DATABASE_TABLE_USERS,
                whereClause, whereArgs);
        return removidos > 0;
    }

    //    retornando todas as quilometragens
    public List<Km> getAllKm() {
        Cursor cursor = read().query(DatabaseHelper.DATABASE_TABLE,
                new String[]{KEY_ROWID, KEY_DATA, KEY_ITINERARIO, KEY_QTD_CLIENTE,
                        KEY_KM_INICIAL, KEY_KM_FINAL, KEY_KM_TOTAL},
                null, null, null, null, null);
        List<Km> kms = new ArrayList<Km>();
        while (cursor.moveToNext()) {
            Km km = criarKm(cursor);
            kms.add(km);
        }
        cursor.close();
        return kms;
    }

    //criando arquivo cursor para listar quilometragens
    private Km criarKm(Cursor cursor) {
        Km km = new Km(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_ROWID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATA)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ITINERARIO)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_QTD_CLIENTE)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_KM_INICIAL)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_KM_FINAL)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_KM_TOTAL)));
        return km;
    }
//    retorna todos os usuarios em uma lista

    public List<Usuario> listaUsuario() {
        Cursor cursor = read().query(DatabaseHelper.DATABASE_TABLE_USERS, new String[]{KEY_ID_USERS,
                KEY_NOME, KEY_UNIDADE, KEY_FUNCAO,
                KEY_PLACA, KEY_GERENCIA}, null, null, null, null, null);
        List<Usuario> usuarios = new ArrayList<Usuario>();
        while (cursor.moveToNext()) {
            Usuario usuario = criarUser(cursor);
            usuarios.add(usuario);
        }
        cursor.close();
        return usuarios;


    }


    public Object returnIdUser(Integer id) {
        Cursor cursor = read().query(true, DatabaseHelper.DATABASE_TABLE_USERS, new String[]{KEY_ID_USERS,
                        KEY_NOME, KEY_UNIDADE, KEY_FUNCAO, KEY_PLACA, KEY_GERENCIA},
                DatabaseHelper.KEY_ID_USERS + " = ?",
                new String[]{id.toString()}, null, null, null, null);
        if (cursor.moveToNext()) {
            Usuario usuario = criarUser(cursor);
            cursor.close();
            return usuario.getId();
        }
        return null;
    }

    public Object returnIdKm(Integer id) {
        Cursor cursor = read().query(true, DatabaseHelper.DATABASE_TABLE, new String[]{KEY_ROWID, KEY_DATA,
                        KEY_ITINERARIO, KEY_QTD_CLIENTE, KEY_KM_INICIAL, KEY_KM_FINAL, KEY_KM_TOTAL},
                DatabaseHelper.KEY_ROWID + " = ?",
                new String[]{id.toString()}, null, null, null, null);
        if (cursor.moveToNext()) {
            Km km = criarKm(cursor);
            cursor.close();
            return km.getId();
        }
        return null;
    }

    //criando arquivo cursor para listar usuarios
    private Usuario criarUser(Cursor cursor) {
        Usuario usuario = new Usuario(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_ID_USERS)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_NOME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_UNIDADE)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FUNCAO)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_PLACA)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_GERENCIA)));
        return usuario;
    }


    //    retornando somente uma quilometragem
    public Km getKm(Integer rowId) throws SQLException {
        Cursor cursor = read().query(true, DatabaseHelper.DATABASE_TABLE, new String[]{KEY_ROWID, KEY_DATA,
                        KEY_ITINERARIO, KEY_QTD_CLIENTE, KEY_KM_INICIAL, KEY_KM_FINAL, KEY_KM_TOTAL},
                DatabaseHelper.KEY_ROWID + " = ?",
                new String[]{rowId.toString()}, null, null, null, null);
        if (cursor.moveToNext()) {
            Km km = criarKm(cursor);
            cursor.close();
            return km;
        }
        return null;

    }
//    retornando somente um usuario

    public Usuario getUsuario(Integer id) {
        Cursor cursor = read().query(true, DatabaseHelper.DATABASE_TABLE_USERS, new String[]{KEY_ID_USERS, KEY_NOME,
                        KEY_UNIDADE, KEY_FUNCAO, KEY_PLACA, KEY_GERENCIA},
                DatabaseHelper.KEY_ROWID + " = ?",
                new String[]{id.toString()}, null, null, null, null);
        if (cursor.moveToNext()) {
            Usuario usuario = criarUser(cursor);
            cursor.close();
            return usuario;
        }
        return null;

    }

//    atualizar uma quilometragem

    public long updateKm(Km km) {
        String selecao = DatabaseHelper.KEY_ROWID + " = ? ";
        String[] selecaoArgumento = {"" + km.getId()};
        ContentValues args = new ContentValues();
        args.put(DatabaseHelper.KEY_ROWID, km.getId());
        args.put(DatabaseHelper.KEY_DATA, km.getData());
        args.put(DatabaseHelper.KEY_ITINERARIO, km.getItinerario());
        args.put(DatabaseHelper.KEY_QTD_CLIENTE, km.getQtdCliente());
        args.put(DatabaseHelper.KEY_KM_INICIAL, km.getKmInicial());
        args.put(DatabaseHelper.KEY_KM_FINAL, km.getKmFinal());
        args.put(DatabaseHelper.KEY_KM_TOTAL, km.getKmTotal());
        return open().update(DatabaseHelper.DATABASE_TABLE, args, selecao, selecaoArgumento);
    }

    //    atualizar um usuario
    public long updateUser(Usuario usuario) {
        String selecao = DatabaseHelper.KEY_ID_USERS + " = ? ";
        String[] selecaoArgumento = {"" + usuario.getId()};
        ContentValues args = new ContentValues();
        args.put(DatabaseHelper.KEY_ID_USERS, usuario.getId());
        args.put(DatabaseHelper.KEY_NOME, usuario.getNome());
        args.put(DatabaseHelper.KEY_UNIDADE, usuario.getUnidade());
        args.put(DatabaseHelper.KEY_FUNCAO, usuario.getFuncao());
        args.put(DatabaseHelper.KEY_PLACA, usuario.getPlaca());
        args.put(DatabaseHelper.KEY_GERENCIA, usuario.getGerencia());
        return open().update(DatabaseHelper.DATABASE_TABLE_USERS, args, selecao, selecaoArgumento);
    }
}

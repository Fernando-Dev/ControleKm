package br.fernando.controlekm.ui;


import android.app.Dialog;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;


import android.widget.TextView;

import br.fernando.controlekm.database.DatabaseHelper;
import br.fernando.controlekm.dao.DBAdapter;

import br.fernando.controlekm.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Flavia on 13/01/2018.
 */

public class Utilitario extends AppCompatActivity {
    private TextView ultimaTrocaOleo, ultimoKmTrocaOleo, proximaTrocaOleo, ultimaManutencao, ultimoKmManutencao, proximaManutencao;
    private Button btnTrocaOleo, btnManutencao, btnVoltarUtils;
    private DBAdapter db;
    private SQLiteDatabase database;
    private Integer kmFinal = -1;
    private Integer kmTroca = 0;
    private Integer kmManutencao = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.utilitario);
        db = new DBAdapter(this);

        final Dialog dialog = new Dialog(this, R.style.DialogoSemTitulo);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_alert_dialog_aviso);
        TextView txtMsgem = dialog.findViewById(R.id.mensagemAlertDialogAviso);
        txtMsgem.setText(R.string.aviso_utilitario);
        Button btnOK = dialog.findViewById(R.id.btnAlertDialogAvisoOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

        ultimaTrocaOleo = (TextView) findViewById(R.id.ultimaTrocaOleo);
        ultimoKmTrocaOleo = (TextView) findViewById(R.id.ultimoKmTrocaOleo);
        ultimoKmManutencao = (TextView) findViewById(R.id.ultimoKmManutencao);
        proximaTrocaOleo = (TextView) findViewById(R.id.proximaTrocaOleo);
        ultimaManutencao = (TextView) findViewById(R.id.ultimaManutencao);
        proximaManutencao = (TextView) findViewById(R.id.kmProximaManutencao);

        btnTrocaOleo = (Button) findViewById(R.id.btnTrocaOleo);
        btnManutencao = (Button) findViewById(R.id.btnManutencao);
        btnVoltarUtils = (Button) findViewById(R.id.btnVoltarUtils);

        if (db.getTrocaOleo().getCount() == 0) {
            ultimaTrocaOleo.setText("");
            ultimoKmTrocaOleo.setText("");
        } else {
            mostraUltimaTrocaOleo();
            mostraProximaTrocaOleo();
        }
        if (db.getmanutencoes().getCount() == 0) {
            ultimaManutencao.setText("");
            ultimoKmManutencao.setText("");
        } else {
            mostraUltimaManutencao();
            mostraProximaManutencao();
        }


        btnVoltarUtils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void pegaKms() {
        database = db.read();
        Cursor c = database.rawQuery("SELECT * FROM kms", null);
        Cursor cc = database.rawQuery("SELECT * FROM trocasDeOleo", null);
        Cursor ccc = database.rawQuery("SELECT * FROM manutencoes", null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            c.getInt(0);
            kmFinal = Integer.valueOf(c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_FINAL)));
            c.moveToNext();
        }
        c.close();
        cc.moveToFirst();
        for (int i = 0; i < cc.getCount(); i++) {
            cc.getInt(0);
            kmTroca = cc.getInt(cc.getColumnIndex(DatabaseHelper.KM_PROXIMA_TROCA));
            cc.moveToNext();
        }
        cc.close();
        ccc.moveToFirst();
        for (int i = 0; i < ccc.getCount(); i++) {
            ccc.getInt(0);
            kmManutencao = ccc.getInt(ccc.getColumnIndex(DatabaseHelper.KM_PROXIMA_MANUTENCAO));
            ccc.moveToNext();
        }
        ccc.close();
    }

    public void selecionarOpcao(View v) {
        switch (v.getId()) {
            case R.id.btnTrocaOleo:
                startActivity(new Intent(this, CadastrarTrocaOleo.class));
                finish();
                break;
            case R.id.btnManutencao:
                startActivity(new Intent(this, CadastrarManutencao.class));
                finish();
                break;
        }
    }

    private void mostraUltimaTrocaOleo() {
        String data = "";
        String kmTroca = "";
        database = db.read();
        Cursor cursor = database.rawQuery("SELECT * FROM trocasDeOleo", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.getInt(0);
            data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATA_TROCA_OLEO));
            kmTroca = String.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KM_TROCA_OLEO)));
            cursor.moveToNext();
        }
        cursor.close();
        try {
            ultimaTrocaOleo.setText(inverteOrdemData(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ultimoKmTrocaOleo.setText(kmTroca);
    }

    private void mostraUltimaManutencao() {
        String data = "";
        String kmManutencao = "";
        database = db.read();
        Cursor cursor = database.rawQuery("SELECT * FROM manutencoes", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.getInt(0);
            data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATA_MANUTENCAO));
            kmManutencao = String.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KM_MANUTENCAO)));
            cursor.moveToNext();
        }
        cursor.close();
        try {
            ultimaManutencao.setText(inverteOrdemData(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ultimoKmManutencao.setText(kmManutencao);
    }

    private void mostraProximaTrocaOleo() {
        Integer kmProximaTroca = 0;
        database = db.read();
        Cursor cursor = database.rawQuery("SELECT * FROM trocasDeOleo", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.getInt(0);
            kmProximaTroca = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KM_PROXIMA_TROCA));
            cursor.moveToNext();
        }
        cursor.close();
        pegaKms();
        if (kmFinal > kmTroca || kmFinal.equals(kmTroca)) {
            proximaTrocaOleo.setTextColor(Color.RED);
            proximaTrocaOleo.setText(String.valueOf(kmProximaTroca));
        } else {
            proximaTrocaOleo.setText(String.valueOf(kmProximaTroca));
        }
    }

    private void mostraProximaManutencao() {
        Integer kmProximaManutencao = 0;
        database = db.read();
        Cursor cursor = database.rawQuery("SELECT * FROM manutencoes", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.getInt(0);
            kmProximaManutencao = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KM_PROXIMA_MANUTENCAO));
            cursor.moveToNext();
        }
        cursor.close();
        pegaKms();
        if (kmFinal > kmManutencao || kmFinal.equals(kmManutencao)) {
            proximaManutencao.setTextColor(Color.RED);
            proximaManutencao.setText(String.valueOf(kmProximaManutencao));
        } else {
            proximaManutencao.setText(String.valueOf(kmProximaManutencao));
        }
    }


    private static String inverteOrdemData(String data) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(data);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        String _date = simpleDateFormat1.format(date);
        return _date;
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}




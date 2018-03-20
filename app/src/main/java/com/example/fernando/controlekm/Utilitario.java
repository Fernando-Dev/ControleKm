package com.example.fernando.controlekm;


import android.app.AlertDialog;

import android.content.DialogInterface;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import android.widget.TextView;

import com.example.fernando.controlekm.DAO.DBAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Flavia on 13/01/2018.
 */

public class Utilitario extends AppCompatActivity {
    private TextView ultimaTrocaOleo, ultimoKmTrocaOleo, proximaTrocaOleo, ultimaManutencao, proximaManutencao;
    private Button btnTrocaOleo, btnManutencao, btnVoltarUtils;
    private DBAdapter db;
    private SQLiteDatabase database;
    private Integer kmFinal = -1;
    private Integer kmTroca = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.utilitario);
        db = new DBAdapter(this);

        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Atenção!")
                .setMessage("Por favor, para eficiência do controle de manutenção da moto, faça o cadastro de Km após toda viagem!")
                .setPositiveButton("OK", null)
                .create()
                .show();

        ultimaTrocaOleo = (TextView) findViewById(R.id.ultimaTrocaOleo);
        ultimoKmTrocaOleo = (TextView) findViewById(R.id.ultimoKmTrocaOleo);
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




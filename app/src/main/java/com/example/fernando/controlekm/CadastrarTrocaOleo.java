package com.example.fernando.controlekm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.TrocaOleo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CadastrarTrocaOleo extends AppCompatActivity {
    private TextView txtDatatroca;
    private Button btnSalvarTrocaOleo, btnVoltarTrocaOleo;
    private EditText edtKmTrocaOleo;
    private RadioGroup radio;
    private Cursor cursor;
    private SQLiteDatabase database;
    private String kmFinalTrocaOleo;
    private ArrayList<TrocaOleo> listTrocaOleo;
    private Integer parametroTrocaOleo, kmFinal;
    private DBAdapter db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_oleo);
        db = new DBAdapter(this);

        edtKmTrocaOleo = (EditText) findViewById(R.id.edtKmTrocaOleo);
        txtDatatroca = (TextView) findViewById(R.id.txtDatatroca);
        radio = (RadioGroup) findViewById(R.id.radio);
        btnSalvarTrocaOleo = (Button) findViewById(R.id.btnSalvarTroca);
        btnVoltarTrocaOleo = (Button) findViewById(R.id.btnVoltarTroca);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String data = simpleDateFormat.format(date);
        txtDatatroca.setText(data);

        btnVoltarTrocaOleo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSalvarTrocaOleo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtKmTrocaOleo.getText().toString().isEmpty()) {
                    edtKmTrocaOleo.setError("Campo vazio!");
                } else {
                    geraTrocaOleo();
                }
            }
        });

    }

    private void geraTrocaOleo() {
        database = db.read();
        cursor = database.rawQuery("SELECT * FROM kms WHERE kmFinal = (SELECT MAX(kmFinal) FROM kms)", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            int a = cursor.getInt(0);
            String km = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_KM_FINAL));
            kmFinal = Integer.valueOf(km);
            cursor.moveToNext();
        }
        cursor.close();
        int contador = 1;
        Integer kmDaTroca = Integer.valueOf(edtKmTrocaOleo.getText().toString());
        String data = txtDatatroca.getText().toString();
        int tipo = radio.getCheckedRadioButtonId();
        if (tipo == R.id.rb1000) {
            parametroTrocaOleo = Constante.VIDA_UTIL_OLEO_1000;
        } else if (tipo == R.id.rb2000) {
            parametroTrocaOleo = Constante.VIDA_UTIL_OLEO_2000;
        }
        listTrocaOleo = new ArrayList<TrocaOleo>();
//        for (int i = 0; i < contador; i++) {
        TrocaOleo trocaOleo = new TrocaOleo(contador, kmDaTroca, data, parametroTrocaOleo);
        listTrocaOleo.add(trocaOleo);
//        contador += contador;
//        }

        if (kmFinal <= kmDaTroca) {
            String date = listTrocaOleo.get(contador - 1).getDataTroca();
            int km = listTrocaOleo.get(contador - 1).getKmTroca();
            int proximaTroca = listTrocaOleo.get(contador - 1).getKmTroca() + listTrocaOleo.get(contador - 1).getVidaUtilOleo();
            Intent intent = new Intent(this, Utilitario.class);
            intent.putExtra("ULTIMA_TROCA_OLEO", date);
            intent.putExtra("KM_ULTIMA_TROCA_OLEO", km);
            intent.putExtra("KM_PROXIMA_TROCA_OLEO", proximaTroca);
            startActivity(intent);
            finish();
        }
    }

}

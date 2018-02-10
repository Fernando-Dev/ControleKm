package com.example.fernando.controlekm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Km;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Flavia on 21/07/2017.
 */

public class AlterarKm extends AppCompatActivity {
    private int ano, mes, dia;
    private Date edtDataKm;
    private Button btnAlterar, btnVoltarKm, btnData;
    private EditText altIdKm, altKmInicial, altKmFinal, altItinerario, altQtdCliente;
    private TextView txvKmTotal;
    private DBAdapter db;
    private SimpleDateFormat dateFormat;
    private Integer id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alterar_km);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        AtualizarData();


        btnAlterar = (Button) findViewById(R.id.btnAlterarKm);
        btnVoltarKm = (Button) findViewById(R.id.btnVoltarKm);

        altIdKm = (EditText) findViewById(R.id.altIdKm);
        btnData = (Button) findViewById(R.id.altData);
        altKmInicial = (EditText) findViewById(R.id.altKmInicial);
        altKmFinal = (EditText) findViewById(R.id.altKmFinal);
        altItinerario = (EditText) findViewById(R.id.altItinerario);
        altQtdCliente = (EditText) findViewById(R.id.altQtdeCliente);
        txvKmTotal = (TextView) findViewById(R.id.altTxvKmTotal);
        db = new DBAdapter(AlterarKm.this);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            id = extra.getInt("EXTRA_ID_KM");
            preparaEdicao();
        }


        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterarKm();
            }
        });
        btnVoltarKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void alterarKm() {

        try {
            db.open();
            String kmIni = altKmInicial.getText().toString();
            int _kmIni = Integer.parseInt(kmIni);
            String kmFim = altKmFinal.getText().toString();
            int _kmFim = Integer.parseInt(kmFim);
            boolean result = (_kmIni > _kmFim);
            if (result) {
                Toast.makeText(getBaseContext(), "Km inicial maior!", Toast.LENGTH_LONG).show();
            } else {
                Integer diferenca = (_kmFim - _kmIni);
                String resultado = String.valueOf(diferenca);
                txvKmTotal.setText(resultado);
                Km km = new Km();
                km.setId(id);
                DateFormat dateFormat = DateFormat.getDateInstance();
                edtDataKm = dateFormat.parse(btnData.getText().toString());
                km.setData(edtDataKm);
                km.setItinerario(altItinerario.getText().toString());
                int qtdeCliente = Integer.parseInt(altQtdCliente.getText().toString());
                km.setQtdCliente(qtdeCliente);
                km.setKmInicial(altKmInicial.getText().toString());
                km.setKmFinal(altKmFinal.getText().toString());
                km.setKmTotal(txvKmTotal.getText().toString());
                db.updateKm(km);
                Toast.makeText(getBaseContext(), "Alterado com sucesso!", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception ex) {
            Toast.makeText(getBaseContext(), "Erro ao alterar!", Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    public void preparaEdicao() {

        SQLiteDatabase database = db.open();

        Cursor c = database.rawQuery("SELECT _id,data,itinerario,qtdCliente,kmInicial,kmFinal,kmTotal FROM kms WHERE _id=?",
                new String[]{id.toString()});
        c.moveToFirst();
        String periodo = dateFormat.format(new Date(c.getLong(c.getColumnIndex(DatabaseHelper.KEY_DATA))));
        btnData.setText(periodo);
        altItinerario.setText(c.getString(c.getColumnIndex(DatabaseHelper.KEY_ITINERARIO)));
        altQtdCliente.setText(c.getInt(c.getColumnIndex(DatabaseHelper.KEY_QTD_CLIENTE)));
        altKmInicial.setText(c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_INICIAL)));
        altKmFinal.setText(c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_FINAL)));
        txvKmTotal.setText(c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_TOTAL)));
        c.close();


    }


    public void selecionarData(View view) {
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case R.id.altData:
                return new DatePickerDialog(this, dataKmListener, ano, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dataKmListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int anoSelecionado, int mesSelecionado, int diaSelecionado) {
            ano = anoSelecionado;
            mes = mesSelecionado;
            dia = diaSelecionado;
            edtDataKm = criarData(anoSelecionado, mesSelecionado, diaSelecionado);
            AtualizarData();
        }
    };

    private Date criarData(int anoSelecionado, int mesSelecionado, int diaSelecionado) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(anoSelecionado, mesSelecionado, diaSelecionado);
        return calendar.getTime();
    }

    private void AtualizarData() {
        btnData.setText(new StringBuilder().append(dia).append("/").append(mes +
                1).append("/").append(ano).append(""));
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
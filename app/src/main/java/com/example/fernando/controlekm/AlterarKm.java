package com.example.fernando.controlekm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Km;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Flavia on 21/07/2017.
 */

public class AlterarKm extends AppCompatActivity {
    private int ano, mes, dia;
    private Date edtDataKm;
    private Button btnAlterar, btnVoltarKm, btnData;
    private EditText altIdKm, altKmInicial, altKmFinal, altItinerario;
    private TextView txvKmTotal;
    private DBAdapter db;
    private String codigo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alterar_km);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        AtualizarData();


        btnAlterar = (Button) findViewById(R.id.btnAlterarKm);
        btnVoltarKm = (Button) findViewById(R.id.btnVoltarKm);

        altIdKm = (EditText) findViewById(R.id.altIdKm);
        btnData = (Button) findViewById(R.id.altData);
        altKmInicial = (EditText) findViewById(R.id.altKmInicial);
        altKmFinal = (EditText) findViewById(R.id.altKmFinal);
        altItinerario = (EditText) findViewById(R.id.altItinerario);
        txvKmTotal = (TextView) findViewById(R.id.altTxvKmTotal);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            codigo = extra.getString(Constante.KEY_ROWID);
        }
        db = new DBAdapter(AlterarKm.this);


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
            Cursor c = (Cursor) db.getKm(Integer.parseInt(codigo));
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
                km.setId(c.getInt(c.getColumnIndex(DatabaseHelper.KEY_ROWID)));
                km.setData(new Date(c.getLong(c.getColumnIndex(DatabaseHelper.KEY_DATA))));
                km.setItinerario(c.getString(c.getColumnIndex(DatabaseHelper.KEY_ITINERARIO)));
                km.setQtdCliente(c.getInt(c.getColumnIndex(DatabaseHelper.KEY_QTD_CLIENTE)));
                km.setKmInicial(c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_INICIAL)));
                km.setKmFinal(c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_FINAL)));
                km.setKmTotal(c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_TOTAL)));
                db.updateKm(Integer.valueOf(codigo), km);
                Toast.makeText(getBaseContext(), "Alterado", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception ex) {
            Toast.makeText(getBaseContext(), "Erro ao alterar!", Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
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

}
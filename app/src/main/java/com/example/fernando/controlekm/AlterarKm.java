package com.example.fernando.controlekm;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
    private Button btnAlterar, btnVoltarKm;
    private EditText altIdKm, altKmInicial, altKmFinal, altItinerario, altData;
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
        altData = (EditText) findViewById(R.id.altData);
        altKmInicial = (EditText) findViewById(R.id.altKmInicial);
        altKmFinal = (EditText) findViewById(R.id.altKmFinal);
        altItinerario = (EditText) findViewById(R.id.altItinerario);
        txvKmTotal = (TextView) findViewById(R.id.altTxvKmTotal);
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
            codigo = this.getIntent().getStringExtra("codigo");
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
                txvKmTotal.setText(resultado.toString() + " Km");
                Km km = new Km();
                DateFormat dateFormat = DateFormat.getDateInstance();
                edtDataKm = dateFormat.parse(altData.getText().toString());
                km.setData(edtDataKm);
                km.setItinerario(altItinerario.getText().toString());
                km.setKmInicial(altKmInicial.getText().toString());
                km.setKmFinal(altKmFinal.getText().toString());
//                km.setKmTotal(txvKmTotal.getText().toString());
                db.updateKm(Long.parseLong(codigo),km);
                Toast.makeText(getBaseContext(), "Alterado", Toast.LENGTH_LONG).show();

                altData.setText("");
                altItinerario.setText("");
                altKmInicial.setText("");
                altKmFinal.setText("");

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
        altData.setText(new StringBuilder().append(dia).append("/").append(mes +
                1).append("/").append(ano).append(""));
    }

}
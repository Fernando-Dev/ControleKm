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

public class CadastrarKm extends AppCompatActivity {
    private int ano, mes, dia;
    private Date edtDataKm;
    private Button btnSalvarKm, btnVoltarKm, btnData;
    private EditText edtKmInicial, edtKmFinal, edtItinerario, qtdCliente;
    private TextView txvKmTotal;
    private DBAdapter db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_km);

        btnSalvarKm = (Button) findViewById(R.id.btnSalvarKm);
        btnVoltarKm = (Button) findViewById(R.id.btnVoltarKm);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        btnData = (Button) findViewById(R.id.btnData);
        AtualizarData();

        edtDataKm = new Date();
        edtKmInicial = (EditText) findViewById(R.id.edtKmInicial);
        edtKmFinal = (EditText) findViewById(R.id.edtKmFinal);
        edtItinerario = (EditText) findViewById(R.id.edtItinerario);
        qtdCliente = (EditText) findViewById(R.id.edtQtdeCliente);
        txvKmTotal = (TextView) findViewById(R.id.txvKmTotal);
        db = new DBAdapter(CadastrarKm.this);


        btnVoltarKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSalvarKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarKm();
            }
        });

    }

    public void cadastrarKm() {
        try {
            DatabaseHelper helper = new DatabaseHelper(this);
            db.open();
            String kmIni = edtKmInicial.getText().toString();
            int _kmIni = Integer.parseInt(kmIni);
            String kmFim = edtKmFinal.getText().toString();
            int _kmFim = Integer.parseInt(kmFim);
            boolean result = (_kmIni > _kmFim);
//            String kmData = btnData.getText().toString();
//            DateFormat dateFormat1 = DateFormat.getDateInstance();
//            Date dataKm = dateFormat1.parse(kmData);
//            if (dataKm.equals(helper.KEY_DATA)) {
//                Toast.makeText(this, "Esta data já existe!", Toast.LENGTH_SHORT).show();
//            }
            if (result) {
                Toast.makeText(getBaseContext(), "Km inicial maior!", Toast.LENGTH_LONG).show();
            } else {
                Integer diferenca = (_kmFim - _kmIni);
                String resultado = String.valueOf(diferenca);
                txvKmTotal.setText(resultado);
                Km km = new Km();
                DateFormat dateFormat = DateFormat.getDateInstance();
                edtDataKm = dateFormat.parse(btnData.getText().toString());
                km.setData(edtDataKm);
                km.setItinerario(edtItinerario.getText().toString());
                int cliente = Integer.parseInt(qtdCliente.getText().toString());
                km.setQtdCliente(cliente);
                km.setKmInicial(edtKmInicial.getText().toString());
                km.setKmFinal(edtKmFinal.getText().toString());
                km.setKmTotal(txvKmTotal.getText().toString());
                db.inserirKm(km);
                Toast.makeText(getBaseContext(), "Salvo", Toast.LENGTH_LONG).show();

                finish();
            }
        } catch (Exception ex) {
            Toast.makeText(getBaseContext(), "Erro ao salvar!", Toast.LENGTH_LONG).show();
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
            case R.id.btnData:
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
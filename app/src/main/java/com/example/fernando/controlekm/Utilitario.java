package com.example.fernando.controlekm;


import android.app.AlertDialog;

import android.content.DialogInterface;

import android.content.Intent;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import android.widget.TextView;

import com.example.fernando.controlekm.DAO.DBAdapter;


/**
 * Created by Flavia on 13/01/2018.
 */

public class Utilitario extends AppCompatActivity {
    private TextView ultimaTrocaOleo, ultimoKmTrocaOleo, proximaTrocaOleo, ultimaManutencao, proximaManutencao;
    private Button btnTrocaOleo, btnManutencao, btnVoltarUtils;
    private DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.utilitario);
        db = new DBAdapter(this);

        if (db.getAllKm().isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage("Por favor, faça o cadastro de Km para fazer o controle de manutenção!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
        }


        ultimaTrocaOleo = (TextView) findViewById(R.id.ultimaTrocaOleo);
        ultimoKmTrocaOleo = (TextView) findViewById(R.id.ultimoKmTrocaOleo);
        proximaTrocaOleo = (TextView) findViewById(R.id.proximaTrocaOleo);
        ultimaManutencao = (TextView) findViewById(R.id.ultimaManutencao);
        proximaManutencao = (TextView) findViewById(R.id.proximaManutencao);


        btnTrocaOleo = (Button) findViewById(R.id.btnTrocaOleo);
        btnManutencao = (Button) findViewById(R.id.btnManutencao);
        btnVoltarUtils = (Button) findViewById(R.id.btnVoltarUtils);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            ultimaTrocaOleo.setText(extra.getString("ULTIMA_TROCA_OLEO"));
            ultimoKmTrocaOleo.setText(String.valueOf(extra.getInt("KM_ULTIMA_TROCA_OLEO")));
            proximaTrocaOleo.setText(String.valueOf(extra.getInt("KM_PROXIMA_TROCA_OLEO")));
        }

        btnVoltarUtils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void selecionarOpcao(View v) {
        switch (v.getId()) {
            case R.id.btnTrocaOleo:
                startActivity(new Intent(this, CadastrarTrocaOleo.class));
                break;
            case R.id.btnManutencao:
                startActivity(new Intent(this, CadastrarManutencao.class));
                break;
        }
    }


}




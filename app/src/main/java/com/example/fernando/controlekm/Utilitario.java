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
    private TextView ultimaTrocaOleo, proximaTrocaOleo, ultimaManutencao, proximaManutencao;
    private Button btnTrocaOleo, btnManutencao;
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
        proximaTrocaOleo = (TextView) findViewById(R.id.proximaTrocaOleo);
        ultimaManutencao = (TextView) findViewById(R.id.ultimaManutencao);
        proximaManutencao = (TextView) findViewById(R.id.proximaManutencao);


        btnTrocaOleo = (Button) findViewById(R.id.btnTrocaOleo);
        btnManutencao = (Button) findViewById(R.id.btnManutencao);
    }

    public void selecionarOpcao(View v) {
        switch (v.getId()) {
            case R.id.btnTrocaOleo:
                startActivity(new Intent(this, TrocaOleo.class));
                break;
            case R.id.btnManutencao:
                startActivity(new Intent(this, Manutencao.class));
                break;
        }
    }


}




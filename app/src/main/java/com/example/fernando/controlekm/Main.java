package com.example.fernando.controlekm;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void selecionarOpcao(View v) {
        switch (v.getId()) {
            case R.id.card_add_km:
                startActivity(new Intent(this, CadastrarKm.class));
                break;
            case R.id.card_listar_km:
                startActivity(new Intent(this, RecyclerViewKm.class));
                break;
            case R.id.card_add_user:
                startActivity(new Intent(this, CadastrarUsuario.class));
                break;
            case R.id.card_listar_user:
                startActivity(new Intent(this, RecyclerViewUsuario.class));
                break;
            case R.id.card_relatorio:
                startActivity(new Intent(this, GeradorPdf.class));
                break;
            case R.id.card_utils:
                startActivity(new Intent(this, Utilitario.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(Main.this, R.style.DialogoSemTitulo);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_alert_dialog_sair);
        dialog.setCancelable(false);
        TextView txtMsgem = dialog.findViewById(R.id.mensagemAlertDialogSair);
        txtMsgem.setText(R.string.sair_main);
        Button btnSim = dialog.findViewById(R.id.btnAlertDialogSairSim);
        Button btnNao = dialog.findViewById(R.id.btnAlertDialogSairNao);
        btnSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}

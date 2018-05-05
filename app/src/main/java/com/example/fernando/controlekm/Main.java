package com.example.fernando.controlekm;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
                startActivity(new Intent(this,RecyclerViewUsuario.class));
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

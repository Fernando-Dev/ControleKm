package com.example.fernando.controlekm;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.fernando.controlekm.DAO.DBAdapter;

/**
 * Created by Flavia on 27/12/2017.
 */

public class CadastrarUsuario extends AppCompatActivity {

    private EditText edtNome,edtPlaca;
    private Spinner spUnidade,spFuncao,spGerencia;
    private RadioGroup rgTipoVeiculo;
    private Button  btnSalvar, btnVoltar;
    private DBAdapter db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_user);

        edtNome = (EditText) findViewById(R.id.edtNome);
        spUnidade = (Spinner) findViewById(R.id.spUnidade);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,R.array.tipo_unidade,R.layout.spinner_item);
        spUnidade.setAdapter(adapter);
        rgTipoVeiculo = (RadioGroup) findViewById(R.id.rgTipoVeiculo);
        spFuncao = (Spinner) findViewById(R.id.spFuncao);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.tipo_funcao,R.layout.spinner_item);
        spFuncao.setAdapter(adapter1);
        edtPlaca = (EditText) findViewById(R.id.edtPlaca);
        spGerencia = (Spinner) findViewById(R.id.spGerencia);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,R.array.tipo_gerencia,R.layout.spinner_item);
        spGerencia.setAdapter(adapter2);

        db = new DBAdapter(CadastrarUsuario.this);

    }
}

package com.example.fernando.controlekm;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Km;
import com.example.fernando.controlekm.dominio.Usuario;

import java.util.Date;

/**
 * Created by Flavia on 27/12/2017.
 */

public class CadastrarUsuario extends AppCompatActivity {

    private EditText edtNome, edtPlaca;
    private Spinner spUnidade, spFuncao, spGerencia;
    private RadioGroup rgTipoVeiculo;
    private Button btnSalvar, btnVoltar;
    private DBAdapter db;
    private Integer codigo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_user);

        edtNome = (EditText) findViewById(R.id.edtNome);
        spUnidade = (Spinner) findViewById(R.id.spUnidade);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tipo_unidade, R.layout.spinner_item);
        spUnidade.setAdapter(adapter);
        rgTipoVeiculo = (RadioGroup) findViewById(R.id.rgTipoVeiculo);
        spFuncao = (Spinner) findViewById(R.id.spFuncao);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.tipo_funcao, R.layout.spinner_item);
        spFuncao.setAdapter(adapter1);
        edtPlaca = (EditText) findViewById(R.id.edtPlaca);
        spGerencia = (Spinner) findViewById(R.id.spGerencia);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.tipo_gerencia, R.layout.spinner_item);
        spGerencia.setAdapter(adapter2);
        btnSalvar = (Button) findViewById(R.id.btnSalvarUser);
        btnVoltar = (Button) findViewById(R.id.btnVoltarUser);

        db = new DBAdapter(CadastrarUsuario.this);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUsuario();
            }
        });
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    public void addUsuario() {
        try {
            DatabaseHelper helper = new DatabaseHelper(this);
            db.open();
            Usuario usuario = new Usuario();
            usuario.setNome(edtNome.getText().toString());
            usuario.setUnidade(spUnidade.getSelectedItem().toString());
            String tipoVeiculo = String.valueOf(rgTipoVeiculo.getCheckedRadioButtonId());
            String veiculoInec = String.valueOf(R.id.veiculoInec);
            String veiculoProprio = String.valueOf(R.id.veiculoProprio);
            String veiculoAlternativo = String.valueOf(R.id.veiculoAlternativo);
            if (tipoVeiculo.equals(veiculoInec)) {
                usuario.setTipoVeiculo(Constante.TIPO_VEICULO_INEC);
            } else if (tipoVeiculo.equals(veiculoProprio)) {
                usuario.setTipoVeiculo(Constante.TIPO_VEICULO_PARTICULAR);
            } else if (tipoVeiculo.equals(veiculoAlternativo)) {
                usuario.setTipoVeiculo(Constante.TIPO_VEICULO_ALTERNATIVO);
            }
            usuario.setFuncao(spFuncao.getSelectedItem().toString());
            String placa = edtPlaca.getText().toString();
            placa = placa.toUpperCase();
            usuario.setPlaca(placa);
            usuario.setGerencia(spGerencia.getSelectedItem().toString());

            db.inserirUsuario(usuario);

            Toast.makeText(getBaseContext(), "Salvo", Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Erro ao salvar", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}

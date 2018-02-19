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
    private EditText edtUnidade, edtFuncao, edtGerencia;
//    private RadioGroup rgTipoVeiculo;
    private Button btnSalvar, btnVoltar;
    private DBAdapter db;
    private Integer codigo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_user);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtUnidade = (EditText) findViewById(R.id.edtUnidade);
//        rgTipoVeiculo = (RadioGroup) findViewById(R.id.rgTipoVeiculo);
        edtFuncao = (EditText)findViewById(R.id.edtFuncao);
        edtPlaca = (EditText) findViewById(R.id.edtPlaca);
        edtGerencia = (EditText) findViewById(R.id.edtGerencia);
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
            String nome  = edtNome.getText().toString();
            nome = nome.toUpperCase();
            usuario.setNome(nome);
            String unidade = edtUnidade.getText().toString();
            unidade = unidade.toUpperCase();
            usuario.setUnidade(unidade);
//            String tipoVeiculo = String.valueOf(rgTipoVeiculo.getCheckedRadioButtonId());
//            String veiculoInec = String.valueOf(R.id.veiculoInec);
//            String veiculoProprio = String.valueOf(R.id.veiculoParticular);
//            String veiculoAlternativo = String.valueOf(R.id.veiculoAlternativo);
//            if (tipoVeiculo.equals(veiculoInec)) {
//                usuario.setTipoVeiculo(Constante.TIPO_VEICULO_INEC);
//            } else if (tipoVeiculo.equals(veiculoProprio)) {
//                usuario.setTipoVeiculo(Constante.TIPO_VEICULO_PARTICULAR);
//            } else if (tipoVeiculo.equals(veiculoAlternativo)) {
//                usuario.setTipoVeiculo(Constante.TIPO_VEICULO_ALTERNATIVO);
//            }
            String funcao = edtFuncao.getText().toString();
            funcao = funcao.toUpperCase();
            usuario.setFuncao(funcao);
            String placa = edtPlaca.getText().toString();
            placa = placa.toUpperCase();
            usuario.setPlaca(placa);
            String gerencia = edtGerencia.getText().toString();
            gerencia = gerencia.toUpperCase();
            usuario.setGerencia(gerencia);

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

package com.example.fernando.controlekm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fernando.controlekm.BD.DatabaseHelper;
import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Usuario;

/**
 * Created by Flavia on 27/12/2017.
 */

public class CadastrarUsuario extends AppCompatActivity {

    private EditText edtNome, edtPlaca;
    private EditText edtUnidade, edtFuncao, edtGerencia;
    private Button btnSalvar, btnVoltar;
    private DBAdapter db;
    private Integer codigo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_user);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtUnidade = (EditText) findViewById(R.id.edtUnidade);
        edtFuncao = (EditText) findViewById(R.id.edtFuncao);
        edtPlaca = (EditText) findViewById(R.id.edtPlaca);
        edtGerencia = (EditText) findViewById(R.id.edtGerencia);
        btnSalvar = (Button) findViewById(R.id.btnSalvarUser);
        btnVoltar = (Button) findViewById(R.id.btnVoltarUser);

        db = new DBAdapter(CadastrarUsuario.this);

        if (db.listaUsuario().size() == 1) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Atenção!")
                    .setMessage("Já existe um usuário cadastrado! Para cadastrar um novo usuário, faça exclusão do usuário existente!")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .create()
                    .show();
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNome.getText().toString().isEmpty()) {
                    edtNome.setError("Campo vazio!");
                } else if (edtUnidade.getText().toString().isEmpty()) {
                    edtUnidade.setError("Campo vazio!");
                } else if (edtFuncao.getText().toString().isEmpty()) {
                    edtFuncao.setError("Campo vazio!");
                } else if (edtPlaca.getText().toString().isEmpty()) {
                    edtPlaca.setError("Campo vazio!");
                } else if (edtGerencia.getText().toString().isEmpty()) {
                    edtGerencia.setError("Campo vazio!");
                } else {
                    addUsuario();
                }
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
            String nome = edtNome.getText().toString();
            nome = nome.toUpperCase();
            usuario.setNome(nome);
            String unidade = edtUnidade.getText().toString();
            unidade = unidade.toUpperCase();
            usuario.setUnidade(unidade);
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

            Toast.makeText(getBaseContext(), "Salvo com sucesso!", Toast.LENGTH_LONG).show();
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

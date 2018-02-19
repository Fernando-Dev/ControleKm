package com.example.fernando.controlekm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Usuario;

public class AlterarUser extends AppCompatActivity {
    private EditText altNome, altPlaca;
    private EditText altUnidade, altFuncao, altGerencia;
//    private RadioGroup altTipoVeiculo;
    private Button btnAlterar, btnVoltar;
    private DBAdapter db;
    private Integer id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alterar_user);


        altNome = (EditText) findViewById(R.id.altNome);
        altUnidade = (EditText)findViewById(R.id.altUnidade);
//        altTipoVeiculo = (RadioGroup) findViewById(R.id.altTipoVeiculo);
        altFuncao = (EditText) findViewById(R.id.altFuncao);
        altPlaca = (EditText) findViewById(R.id.altPlaca);
        altGerencia = (EditText)findViewById(R.id.altGerencia);
        btnAlterar = (Button) findViewById(R.id.btnAlterarUser);
        btnVoltar = (Button) findViewById(R.id.btnVoltarUser);
        db = new DBAdapter(AlterarUser.this);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            id = extra.getInt("EXTRA_ID_USER");
            preparaEdicao();
        }
        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUsuario();
            }
        });
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void updateUsuario() {
        try {

            db.open();
            Usuario usuario = new Usuario();
            usuario.setId(id);
            String nome  = altNome.getText().toString();
            nome = nome.toUpperCase();
            usuario.setNome(nome);
            String unidade = altUnidade.getText().toString();
            unidade = unidade.toUpperCase();
            usuario.setUnidade(unidade);
//            String tipoVeiculo = String.valueOf(altTipoVeiculo.getCheckedRadioButtonId());
//            String veiculoInec = String.valueOf(R.id.altVeiculoInec);
//            String veiculoProprio = String.valueOf(R.id.altVeiculoParticular);
//            String veiculoAlternativo = String.valueOf(R.id.altVeiculoAlternativo);
//            if (tipoVeiculo.equals(veiculoInec)) {
//                usuario.setTipoVeiculo(Constante.TIPO_VEICULO_INEC);
//            } else if (tipoVeiculo.equals(veiculoProprio)) {
//                usuario.setTipoVeiculo(Constante.TIPO_VEICULO_PARTICULAR);
//            } else if (tipoVeiculo.equals(veiculoAlternativo)) {
//                usuario.setTipoVeiculo(Constante.TIPO_VEICULO_ALTERNATIVO);
//            }
            String funcao = altFuncao.getText().toString();
            funcao = funcao.toUpperCase();
            usuario.setFuncao(funcao);
            String placa = altPlaca.getText().toString();
            placa = placa.toUpperCase();
            usuario.setPlaca(placa);
            String gerencia = altGerencia.getText().toString();
            gerencia = gerencia.toUpperCase();
            usuario.setGerencia(gerencia);
            db.updateUser(usuario);

            Toast.makeText(getBaseContext(), "Alterado com sucesso!", Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Erro ao salvar", Toast.LENGTH_LONG).show();
        }

    }

    public void preparaEdicao() {
        SQLiteDatabase database = db.read();
        Cursor c = database.rawQuery("SELECT nome, unidade, tipoVeiculo,funcao,placa,gerencia FROM usuarios WHERE id=?",
                new String[]{id.toString()});
        c.moveToFirst();
        altNome.setText(c.getString(c.getColumnIndex(DatabaseHelper.KEY_NOME)));
        altUnidade.setText(c.getString(c.getColumnIndex(DatabaseHelper.KEY_UNIDADE)));
//        String cursorVeiculo = c.getString(2);
//        Integer veiculoInec = R.id.altVeiculoInec;
//        Integer veiculoProprio = R.id.altVeiculoParticular;
//        Integer veiculoAlternativo = R.id.altVeiculoAlternativo;
//        if (cursorVeiculo.equals(Constante.TIPO_VEICULO_INEC)) {
//                altTipoVeiculo.check(veiculoInec);
//        } else if (cursorVeiculo.equals(Constante.TIPO_VEICULO_PARTICULAR)) {
//            altTipoVeiculo.check(veiculoProprio);
//        } else if (cursorVeiculo.equals(Constante.TIPO_VEICULO_ALTERNATIVO)) {
//            altTipoVeiculo.check(veiculoAlternativo);
//        }
        altFuncao.setText(c.getString(c.getColumnIndex(DatabaseHelper.KEY_FUNCAO)));
        altPlaca.setText(c.getString(c.getColumnIndex(DatabaseHelper.KEY_PLACA)));
        altGerencia.setText(c.getString(c.getColumnIndex(DatabaseHelper.KEY_GERENCIA)));

        c.close();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}

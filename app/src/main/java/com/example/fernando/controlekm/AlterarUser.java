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
    private Spinner altUnidade, altFuncao, altGerencia;
    private RadioGroup altTipoVeiculo;
    private Button btnAlterar, btnVoltar;
    private DBAdapter db;
    private Integer id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alterar_user);


        altNome = (EditText) findViewById(R.id.altNome);
        altUnidade = (Spinner) findViewById(R.id.altUnidade);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tipo_unidade, R.layout.spinner_item);
        altUnidade.setAdapter(adapter);
        altTipoVeiculo = (RadioGroup) findViewById(R.id.altTipoVeiculo);
        altFuncao = (Spinner) findViewById(R.id.altFuncao);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.tipo_funcao, R.layout.spinner_item);
        altFuncao.setAdapter(adapter1);
        altPlaca = (EditText) findViewById(R.id.altPlaca);
        altGerencia = (Spinner) findViewById(R.id.altGerencia);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.tipo_gerencia, R.layout.spinner_item);
        altGerencia.setAdapter(adapter2);
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
            usuario.setNome(altNome.getText().toString());
            usuario.setUnidade(altUnidade.getSelectedItem().toString());
            String tipoVeiculo = String.valueOf(altTipoVeiculo.getCheckedRadioButtonId());
            String veiculoInec = String.valueOf(R.id.altVeiculoInec);
            String veiculoProprio = String.valueOf(R.id.altVeiculoParticular);
            String veiculoAlternativo = String.valueOf(R.id.altVeiculoAlternativo);
            if (tipoVeiculo.equals(veiculoInec)) {
                usuario.setTipoVeiculo(Constante.TIPO_VEICULO_INEC);
            } else if (tipoVeiculo.equals(veiculoProprio)) {
                usuario.setTipoVeiculo(Constante.TIPO_VEICULO_PARTICULAR);
            } else if (tipoVeiculo.equals(veiculoAlternativo)) {
                usuario.setTipoVeiculo(Constante.TIPO_VEICULO_ALTERNATIVO);
            }
            usuario.setFuncao(altFuncao.getSelectedItem().toString());
            String placa = altPlaca.getText().toString();
            placa = placa.toUpperCase();
            usuario.setPlaca(placa);
            usuario.setGerencia(altGerencia.getSelectedItem().toString());
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
        altUnidade.setPrompt(c.getString(c.getColumnIndex(DatabaseHelper.KEY_UNIDADE)));
        String cursorVeiculo = c.getString(2);
        Integer veiculoInec = R.id.altVeiculoInec;
        Integer veiculoProprio = R.id.altVeiculoParticular;
        Integer veiculoAlternativo = R.id.altVeiculoAlternativo;
        if (cursorVeiculo.equals(Constante.TIPO_VEICULO_INEC)) {
                altTipoVeiculo.check(veiculoInec);
        } else if (cursorVeiculo.equals(Constante.TIPO_VEICULO_PARTICULAR)) {
            altTipoVeiculo.check(veiculoProprio);
        } else if (cursorVeiculo.equals(Constante.TIPO_VEICULO_ALTERNATIVO)) {
            altTipoVeiculo.check(veiculoAlternativo);
        }
        if(altFuncao.getSelectedItem().equals(c.getString(3))){

        }
        altFuncao.setPrompt(c.getString(c.getColumnIndex(DatabaseHelper.KEY_FUNCAO)));
        altPlaca.setText(c.getString(c.getColumnIndex(DatabaseHelper.KEY_PLACA)));
        altGerencia.setPrompt(c.getString(c.getColumnIndex(DatabaseHelper.KEY_GERENCIA)));

        c.close();




    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}

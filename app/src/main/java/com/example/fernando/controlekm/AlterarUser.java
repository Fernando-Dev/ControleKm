package com.example.fernando.controlekm;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private Integer codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alterar_user);


        altNome = (EditText) findViewById(R.id.altNome);
        altUnidade = (Spinner) findViewById(R.id.altUnidade);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tipo_unidade, R.layout.spinner_item);
        altUnidade.setAdapter(adapter);
        altTipoVeiculo = (RadioGroup) findViewById(R.id.rgTipoVeiculo);
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
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            codigo = extra.getInt("EXTRA_ID_USER");
            preparaEdicao();
        }

        db = new DBAdapter(AlterarUser.this);

    }

    public void addUsuario() {
        try {
            DatabaseHelper helper = new DatabaseHelper(this);
            db.open();
            Usuario usuario = new Usuario();
            usuario.setNome(altNome.getText().toString());
            usuario.setUnidade(altUnidade.getSelectedItem().toString());
            String tipoVeiculo = String.valueOf(altTipoVeiculo.getCheckedRadioButtonId());
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
            usuario.setFuncao(altFuncao.getSelectedItem().toString());
            String placa = altPlaca.getText().toString();
            placa = placa.toUpperCase();
            usuario.setPlaca(placa);
            usuario.setGerencia(altGerencia.getSelectedItem().toString());
            db.updateUser(codigo, usuario);

            Toast.makeText(getBaseContext(), "Salvo", Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Erro ao salvar", Toast.LENGTH_LONG).show();
        }

    }

    public void preparaEdicao() {

//        db.read();
        Cursor c = (Cursor) db.getUsuario(codigo);
        altNome.setText(c.getString(c.getColumnIndex(DatabaseHelper.KEY_NOME)));
        altUnidade.setPrompt(c.getString(c.getColumnIndex(DatabaseHelper.KEY_UNIDADE)));
        if (c.getString(c.getColumnIndex(DatabaseHelper.KEY_TIPO_VEICULO)).equals(Constante.TIPO_VEICULO_INEC)) {
            altTipoVeiculo.check(R.id.veiculoInec);
        } else if (c.getString(c.getColumnIndex(DatabaseHelper.KEY_TIPO_VEICULO)).equals(Constante.TIPO_VEICULO_PARTICULAR)) {
            altTipoVeiculo.check(R.id.veiculoProprio);
        } else if (c.getString(c.getColumnIndex(DatabaseHelper.KEY_TIPO_VEICULO)).equals(Constante.TIPO_VEICULO_ALTERNATIVO)) {
            altTipoVeiculo.check(R.id.veiculoAlternativo);
        }
        altFuncao.setPrompt(c.getString(c.getColumnIndex(DatabaseHelper.KEY_FUNCAO)));
        altPlaca.setText(c.getString(c.getColumnIndex(DatabaseHelper.KEY_PLACA)));
        altGerencia.setPrompt(c.getString(c.getColumnIndex(DatabaseHelper.KEY_GERENCIA)));
        db.close();


    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}

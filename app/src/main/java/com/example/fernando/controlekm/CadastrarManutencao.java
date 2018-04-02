package com.example.fernando.controlekm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Manutencao;
import com.example.fernando.controlekm.dominio.TrocaOleo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CadastrarManutencao extends AppCompatActivity {
    private TextView txtDataManutencao;
    private Button btnSalvarManutencao, btnVoltarManutencao;
    private EditText edtKmManutencao;
    private RadioGroup radioManutencao;
    private Cursor cursor;
    private SQLiteDatabase database;
    private String kmFinalManutencao;
    private ArrayList<TrocaOleo> listManutencao;
    private Integer parametroTrocaOleo, kmFinal;
    private DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manutencao);

        db = new DBAdapter(this);

        edtKmManutencao = (EditText) findViewById(R.id.edtKmManutencao);
        txtDataManutencao = (TextView) findViewById(R.id.txtDataManutencao);
        radioManutencao = (RadioGroup) findViewById(R.id.radioManutencao);
        btnSalvarManutencao = (Button) findViewById(R.id.btnSalvarManutencao);
        btnVoltarManutencao = (Button) findViewById(R.id.btnVoltarManutencao);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String data = simpleDateFormat.format(date);
        txtDataManutencao.setText(data);

        btnVoltarManutencao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSalvarManutencao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtKmManutencao.getText().toString().isEmpty()) {
                    edtKmManutencao.setError("Campo vazio!");
                } else {
                    cadastrarManutencao();
                }
            }
        });

    }

    private void cadastrarManutencao() {
        db.open();
        try {
            Integer resultado = 0;
            Manutencao manutencao = new Manutencao();
            manutencao.setKmManutencao(Integer.valueOf(edtKmManutencao.getText().toString()));
            String data = inverteOrdemData(txtDataManutencao.getText().toString());
            manutencao.setDataManutencao(data);
            int tipo = radioManutencao.getCheckedRadioButtonId();
            if (tipo == R.id.rbM1000) {
                manutencao.setDistanciaManutencao(Constante.DISTANCIA_MANUTENCAO_1000);
            } else if (tipo == R.id.rbM2000) {
                manutencao.setDistanciaManutencao(Constante.DISTANCIA_MANUTENCAO_2000);
            } else if (tipo == R.id.rbM3000) {
                manutencao.setDistanciaManutencao(Constante.DISTANCIA_MANUTENCAO_3000);
            } else if (tipo == R.id.rbM4000) {
                manutencao.setDistanciaManutencao(Constante.DISTANCIA_MANUTENCAO_4000);
            }
            resultado = manutencao.getKmManutencao() + manutencao.getDistanciaManutencao();
            manutencao.setKmProximaManutencao(resultado);
            db.inserirManutencao(manutencao);
            Toast.makeText(getBaseContext(), "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Erro ao salvar!", Toast.LENGTH_SHORT).show();
        }
    }

    private static String inverteOrdemData(String data) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = simpleDateFormat.parse(data);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        String _date = simpleDateFormat1.format(date);
        return _date;
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

}

package br.fernando.controlekm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

import br.fernando.controlekm.DAO.DBAdapter;

import com.example.fernando.controlekm.R;

import br.fernando.controlekm.Receiver.AlarmReceiverTrocaOleo;
import br.fernando.controlekm.dominio.TrocaOleo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CadastrarTrocaOleo extends AppCompatActivity {
    private TextView txtDatatroca;
    private Button btnSalvarTrocaOleo, btnVoltarTrocaOleo;
    private EditText edtKmTrocaOleo;
    private RadioGroup radio;
    private Cursor cursor;
    private SQLiteDatabase database;
    private String kmFinalTrocaOleo;
    private ArrayList<TrocaOleo> listTrocaOleo;
    private Integer parametroTrocaOleo, kmFinal;
    private DBAdapter db;
    public AlarmManager alarmManager;
    public static int ALARM_TROCA_OLEO= 101;
    public PendingIntent broadcast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_oleo);
        db = new DBAdapter(this);

        edtKmTrocaOleo = (EditText) findViewById(R.id.edtKmTrocaOleo);
        txtDatatroca = (TextView) findViewById(R.id.txtDatatroca);
        radio = (RadioGroup) findViewById(R.id.radio);
        btnSalvarTrocaOleo = (Button) findViewById(R.id.btnSalvarTroca);
        btnVoltarTrocaOleo = (Button) findViewById(R.id.btnVoltarTroca);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String data = simpleDateFormat.format(date);
        txtDatatroca.setText(data);

        btnVoltarTrocaOleo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastrarTrocaOleo.this,Utilitario.class);
                startActivity(intent);
                finish();
            }
        });

        btnSalvarTrocaOleo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtKmTrocaOleo.getText().toString().isEmpty()) {
                    edtKmTrocaOleo.setError("Campo vazio!");
                } else {
                    cadastrarTrocaOleo();
                }
            }
        });

    }

    private void cadastrarTrocaOleo() {
        db.open();
        try {
            Integer resultado = 0;
            TrocaOleo trocaOleo = new TrocaOleo();
            trocaOleo.setKmTroca(Integer.valueOf(edtKmTrocaOleo.getText().toString()));
            String data = inverteOrdemData(txtDatatroca.getText().toString());
            trocaOleo.setDataTroca(data);
            int tipo = radio.getCheckedRadioButtonId();
            if (tipo == R.id.rb1000) {
                trocaOleo.setVidaUtilOleo(Constante.VIDA_UTIL_OLEO_1000);
            } else if (tipo == R.id.rb2000) {
                trocaOleo.setVidaUtilOleo(Constante.VIDA_UTIL_OLEO_2000);
            }
            resultado = trocaOleo.getKmTroca() + trocaOleo.getVidaUtilOleo();
            trocaOleo.setProximaTroca(resultado);
            db.inserirTrocaOleo(trocaOleo);
            cancelarNotificacaoTrocaOleo();
            Toast.makeText(getBaseContext(), "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CadastrarTrocaOleo.this,Utilitario.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Erro ao salvar!", Toast.LENGTH_SHORT).show();
        }

    }
    public boolean cancelarNotificacaoTrocaOleo() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(CadastrarTrocaOleo.this, AlarmReceiverTrocaOleo.class);
        intent.putExtra("ALARM_TROCA_OLEO", ALARM_TROCA_OLEO);
        broadcast = PendingIntent.getBroadcast(this, ALARM_TROCA_OLEO, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(broadcast);
        return true;
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

package com.example.fernando.controlekm;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Km;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Flavia on 21/07/2017.
 */

public class CadastrarKm extends AppCompatActivity {
    private int ano, mes, dia;
    private String data;
    private Date edtDataKm;
    private Button btnSalvarKm, btnVoltarKm, btnData;
    private EditText edtKmInicial, edtKmFinal, edtItinerario, qtdCliente;
    private TextView txvKmTotal;
    private DBAdapter db;
    private NotificationManager notificationManager;
    private android.support.v4.app.NotificationCompat.Builder notificationBuilder;
    private Bitmap icon;
    private int currentNotificationID = 0;
    private String notificationTitle;
    private String notificationText;
    private SQLiteDatabase database;
    private Integer kmFinal = -1;
    private Integer kmTroca = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_km);
        db = new DBAdapter(CadastrarKm.this);


        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        btnData = (Button) findViewById(R.id.btnData);
        AtualizarData();
        edtDataKm = new Date();


        edtKmInicial = (EditText) findViewById(R.id.edtKmInicial);
        edtKmFinal = (EditText) findViewById(R.id.edtKmFinal);
        edtItinerario = (EditText) findViewById(R.id.edtItinerario);
        qtdCliente = (EditText) findViewById(R.id.edtQtdeCliente);
        txvKmTotal = (TextView) findViewById(R.id.txvKmTotal);
        btnSalvarKm = (Button) findViewById(R.id.btnSalvarKm);
        btnVoltarKm = (Button) findViewById(R.id.btnVoltarKm);
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        icon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.new_ic_controlekm);

        btnVoltarKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSalvarKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtItinerario.getText().toString().isEmpty()) {
                    edtItinerario.setError("Campo Vazio!");
                } else if (qtdCliente.getText().toString().isEmpty()) {
                    qtdCliente.setError("Campo Vazio!");
                } else if (edtKmInicial.getText().toString().isEmpty()) {
                    edtKmInicial.setError("Campo Vazio!");
                } else if (edtKmFinal.getText().toString().isEmpty()) {
                    edtKmFinal.setError("Campo Vazio!");
                } else {
                    cadastrarKm();
                }
            }
        });

    }

    private void enviaNotificacao() {
        Intent notificationIntent = new Intent(CadastrarKm.this, Utilitario.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Utilitario.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
//        long segundos = 1000;
//        segundos = SystemClock.elapsedRealtime() + segundos;
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,segundos,contentIntent);
        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        currentNotificationID++;
        int notificationId = currentNotificationID;
        if (notificationId == Integer.MAX_VALUE - 1)
            notificationId = 0;
        notificationManager.notify(notificationId, notification);

    }

    private void atribuirDadosNotificacao() {
        notificationTitle = this.getString(R.string.app_name);
        notificationText = "Você precisa fazer a troca do óleo da sua moto!";
    }

    private void atribuirNotificacaoAltaPrioridade() {
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.new_ic_controlekm)
                .setLargeIcon(icon)
                .setAutoCancel(false)
                .setContentTitle(notificationTitle)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                .setPriority(Notification.PRIORITY_MAX)
                .setContentText(notificationText);
//        return notificationBuilder.build();
        enviaNotificacao();
    }

    private void limparTodasNotificacoes() {
        if (notificationManager != null) {
            currentNotificationID = 0;
            notificationManager.cancelAll();
        }
    }

    private void chamaNotificacao() {
        database = db.read();
        Cursor c = database.rawQuery("SELECT * FROM kms", null);
        Cursor cc = database.rawQuery("SELECT * FROM trocasDeOleo", null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            c.getInt(0);
            kmFinal = Integer.valueOf(c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_FINAL)));
            c.moveToNext();
        }
        c.close();
        cc.moveToFirst();
        for (int i = 0; i < cc.getCount(); i++) {
            cc.getInt(0);
            kmTroca = cc.getInt(cc.getColumnIndex(DatabaseHelper.KM_PROXIMA_TROCA));
            cc.moveToNext();
        }
        cc.close();
    }

    public void cadastrarKm() {
        try {
            DatabaseHelper helper = new DatabaseHelper(this);
            db.open();
            String kmIni = edtKmInicial.getText().toString();
            int _kmIni = Integer.parseInt(kmIni);
            String kmFim = edtKmFinal.getText().toString();
            int _kmFim = Integer.parseInt(kmFim);
            boolean result = (_kmIni > _kmFim);
            if (result) {
                Toast.makeText(getBaseContext(), "Km inicial maior!", Toast.LENGTH_LONG).show();
            } else {
                Integer diferenca = (_kmFim - _kmIni);
                String resultado = String.valueOf(diferenca);
                txvKmTotal.setText(resultado);
                Km km = new Km();
                try {
                    data = inverteOrdemData(btnData.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                km.setData(data);
                km.setItinerario(edtItinerario.getText().toString());
                int cliente = Integer.parseInt(qtdCliente.getText().toString());
                km.setQtdCliente(cliente);
                km.setKmInicial(edtKmInicial.getText().toString());
                km.setKmFinal(edtKmFinal.getText().toString());
                km.setKmTotal(txvKmTotal.getText().toString());
                db.inserirKm(km);
                chamaNotificacao();
                if (kmFinal.equals(-1) || kmTroca.equals(0)) {
                    Toast.makeText(getBaseContext(), "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                } else if (kmFinal > kmTroca || kmFinal.equals(kmTroca)) {
                    atribuirDadosNotificacao();
                    atribuirNotificacaoAltaPrioridade();
                    Toast.makeText(getBaseContext(), "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        } catch (Exception ex) {
            Toast.makeText(getBaseContext(), "Erro ao salvar!", Toast.LENGTH_LONG).show();
        }
    }


    public void selecionarData(View view) {
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case R.id.btnData:
                return new DatePickerDialog(this, dataKmListener, ano, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dataKmListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int anoSelecionado, int mesSelecionado, int diaSelecionado) {
            ano = anoSelecionado;
            mes = mesSelecionado;
            dia = diaSelecionado;
            edtDataKm = criarData(anoSelecionado, mesSelecionado, diaSelecionado);
            AtualizarData();
        }
    };

    private Date criarData(int anoSelecionado, int mesSelecionado, int diaSelecionado) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(anoSelecionado, mesSelecionado, diaSelecionado);
        return calendar.getTime();
    }

    private void AtualizarData() {
        btnData.setText(new StringBuilder().append(dia).append("/").append(mes +
                1).append("/").append(ano).append(""));
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
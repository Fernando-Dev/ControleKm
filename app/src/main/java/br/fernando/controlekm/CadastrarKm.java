package br.fernando.controlekm;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import br.fernando.controlekm.BD.DatabaseHelper;
import br.fernando.controlekm.DAO.DBAdapter;

import br.fernando.controlekm.R;

import br.fernando.controlekm.Receiver.AlarmReceiverManutencao;
import br.fernando.controlekm.Receiver.AlarmReceiverTrocaOleo;
import br.fernando.controlekm.dominio.Km;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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
    private int currentNotificationID = 0;
    private String notificationTitle;
    private String notificationText;
    private SQLiteDatabase database;
    private Integer kmInicial = 0;
    private Integer kmFinal = -1;
    private Integer kmTroca = 0;
    private Integer kmManutencao = 0;
    public static int ALARM_TROCA_OLEO = 101;
    public static int ALARM_MANUTENCAO = 102;
    public AlarmManager alarmManager;
    public PendingIntent broadcast;


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

    public boolean enviaNotificacaoTrocaOleo(boolean confirma) {
        if (confirma) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(CadastrarKm.this, AlarmReceiverTrocaOleo.class);
            broadcast = PendingIntent.getBroadcast(this, ALARM_TROCA_OLEO, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar cal = Calendar.getInstance();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                    AlarmManager.INTERVAL_HOUR + AlarmManager.INTERVAL_HOUR, broadcast);
            return true;
        } else {
            return false;
        }
    }

    public boolean enviaNotificacaoManutencao(boolean confirma) {
        if (confirma) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(CadastrarKm.this, AlarmReceiverManutencao.class);
            broadcast = PendingIntent.getBroadcast(this, ALARM_MANUTENCAO, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar cal = Calendar.getInstance();

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                    AlarmManager.INTERVAL_HOUR + AlarmManager.INTERVAL_HOUR, broadcast);
            return true;
        } else {
            return false;
        }

    }

    public boolean cancelarNotificacaoTrocaOleo() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(CadastrarKm.this, AlarmReceiverTrocaOleo.class);
        intent.putExtra("ALARM_TROCA_OLEO", ALARM_TROCA_OLEO);
        broadcast = PendingIntent.getBroadcast(this, ALARM_TROCA_OLEO, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(broadcast);
        return true;
    }

    public boolean cancelarNotificacaoManutencao() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(CadastrarKm.this, AlarmReceiverManutencao.class);
        intent.putExtra("ALARM_MANUTENCAO", ALARM_MANUTENCAO);
        broadcast = PendingIntent.getBroadcast(this, ALARM_MANUTENCAO, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(broadcast);
        return true;
    }

    public static void enableBootReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, AlarmReceiverTrocaOleo.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void consultaKms() {
        database = db.read();
        Cursor c = database.rawQuery("SELECT * FROM kms", null);
        Cursor cc = database.rawQuery("SELECT * FROM trocasDeOleo", null);
        Cursor ccc = database.rawQuery("SELECT * FROM manutencoes", null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            c.getInt(0);
            kmInicial = Integer.valueOf(c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_INICIAL)));
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
        ccc.moveToFirst();
        for (int i = 0; i < ccc.getCount(); i++) {
            ccc.getInt(0);
            kmManutencao = ccc.getInt(ccc.getColumnIndex(DatabaseHelper.KM_PROXIMA_MANUTENCAO));
            ccc.moveToNext();
        }
        ccc.close();
    }

    public void cadastrarKm() {
        try {

            int _kmIni = Integer.parseInt(edtKmInicial.getText().toString());
            int _kmFim = Integer.parseInt(edtKmFinal.getText().toString());
            try {
                data = inverteOrdemData(btnData.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            boolean dataChecada = false;
            database = db.read();
            Cursor cursor = database.rawQuery("SELECT * FROM kms WHERE data LIKE '" + data + "'", null);
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.getInt(0);
                String dataQuery = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATA));
                if (dataQuery.compareTo(data) == 0) {
                    dataChecada = true;
                }
                cursor.moveToNext();
            }
            cursor.close();
            consultaKms();
            if (kmInicial == null & kmFinal == null) {
                consultaKms();
            } else if (_kmIni < kmInicial) {
                Toast.makeText(getBaseContext(), "Km inicial digitado é menor que o ultimo Km inicial do banco de dados!", Toast.LENGTH_LONG).show();
            } else if (_kmFim < kmFinal) {
                Toast.makeText(getBaseContext(), "Km final digitado é menor que o ultimo Km final do banco de dados!", Toast.LENGTH_LONG).show();
            } else if (dataChecada) {
                Toast.makeText(getBaseContext(), "A data já existe no banco de dados!", Toast.LENGTH_LONG).show();
            } else if (_kmIni > _kmFim) {
                Toast.makeText(getBaseContext(), "Km inicial maior!", Toast.LENGTH_LONG).show();
            } else if (_kmIni == _kmFim) {
                Toast.makeText(getBaseContext(), "Km inicial é igual ao Km final!", Toast.LENGTH_LONG).show();
            } else {
                Integer diferenca = (_kmFim - _kmIni);
                String resultado = String.valueOf(diferenca);
                txvKmTotal.setText(resultado);
                db.open();
                Km km = new Km();
                km.setData(data);
                km.setItinerario(edtItinerario.getText().toString());
                int cliente = Integer.parseInt(qtdCliente.getText().toString());
                km.setQtdCliente(cliente);
                km.setKmInicial(edtKmInicial.getText().toString());
                km.setKmFinal(edtKmFinal.getText().toString());
                km.setKmTotal(txvKmTotal.getText().toString());
                db.inserirKm(km);
                consultaKms();
                if ((kmFinal.equals(-1) || kmTroca.equals(0)) & (kmFinal.equals(-1) || kmManutencao.equals(0))) {
                    Toast.makeText(getBaseContext(), "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                } else if (kmFinal > kmTroca & kmFinal > kmManutencao) {
                    enviaNotificacaoTrocaOleo(true);
                    enviaNotificacaoManutencao(true);
                    Toast.makeText(getBaseContext(), "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                } else if (kmFinal.equals(kmTroca) & kmFinal.equals(kmManutencao)) {
                    enviaNotificacaoTrocaOleo(true);
                    enviaNotificacaoManutencao(true);
                    Toast.makeText(getBaseContext(), "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                } else if (kmFinal > kmTroca & kmFinal.equals(kmManutencao)) {
                    enviaNotificacaoTrocaOleo(true);
                    enviaNotificacaoManutencao(true);
                    Toast.makeText(getBaseContext(), "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                } else if (kmFinal > kmManutencao & kmFinal.equals(kmTroca)) {
                    enviaNotificacaoTrocaOleo(true);
                    enviaNotificacaoManutencao(true);
                    Toast.makeText(getBaseContext(), "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                } else if (kmFinal > kmTroca || kmFinal.equals(kmTroca)) {
                    enviaNotificacaoTrocaOleo(true);
                    Toast.makeText(getBaseContext(), "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                } else if (kmFinal > kmManutencao || kmFinal.equals(kmManutencao)) {
                    /*chama notificacao da manutencao*/
                    enviaNotificacaoManutencao(true);
                    Toast.makeText(getBaseContext(), "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    enviaNotificacaoTrocaOleo(false);
                    enviaNotificacaoManutencao(false);
                    cancelarNotificacaoTrocaOleo();
                    cancelarNotificacaoManutencao();
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
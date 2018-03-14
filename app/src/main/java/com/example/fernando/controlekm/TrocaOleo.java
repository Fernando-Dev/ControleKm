package com.example.fernando.controlekm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.fernando.controlekm.DAO.DBAdapter;

import java.util.List;

public class TrocaOleo extends AppCompatActivity {
    private TextView ultimaTrocaOleo, proximaTrocaOleo, ultimaManutencao, proximaManutencao;
    private Button btnTrocaOleo, btnManutencao;
    private Cursor cursor;
    private SQLiteDatabase database;
    private String kmFinalTrocaOleo;
    private List<Integer> kmTrocaOleo;
    private Integer parametroTrocaOleo;
    private DBAdapter db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_oleo);
        db = new DBAdapter(this);
    }
    private void geraTrocaOleo() {

        Integer kmFinal = 0;
        database = db.read();
        cursor = database.rawQuery("SELECT * FROM kms WHERE kmFinal = (SELECT MAX(kmFinal) FROM kms)", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            int a = cursor.getInt(0);
            String km = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_KM_FINAL));
            kmFinal = Integer.valueOf(km);
            cursor.moveToNext();
        }
        cursor.close();
        new AlertDialog.Builder(TrocaOleo.this, R.layout.dialog_troca_oleo)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText edtKmTrocaOleo = (EditText) findViewById(R.id.edtKmTrocaOleo);
                        kmFinalTrocaOleo = edtKmTrocaOleo.getText().toString();
                        kmTrocaOleo.add(Integer.valueOf(kmFinalTrocaOleo));
                        RadioGroup rg = (RadioGroup) findViewById(R.id.rg);
                        int tipo = rg.getCheckedRadioButtonId();
                        if (tipo == R.id.rb1000) {
                            parametroTrocaOleo = 1000;
                        } else {
                            parametroTrocaOleo = 2000;
                        }
                    }
                }).setNegativeButton("Cancelar", null)
                .create()
                .show();


    }
}

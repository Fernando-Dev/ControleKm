package com.example.fernando.controlekm;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Km;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.fernando.controlekm.DatabaseHelper.KEY_DATA;
import static com.example.fernando.controlekm.DatabaseHelper.KEY_ITINERARIO;
import static com.example.fernando.controlekm.DatabaseHelper.KEY_KM_FINAL;
import static com.example.fernando.controlekm.DatabaseHelper.KEY_KM_INICIAL;
import static com.example.fernando.controlekm.DatabaseHelper.KEY_ROWID;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }

    public void selecionarOpcao(View v) {
        switch (v.getId()) {
            case R.id.ivAddkm:
                startActivity(new Intent(this, CadastrarKm.class));
                break;
            case R.id.ivListkm:
                startActivity(new Intent(this, ListKm.class));
                break;
            case R.id.ivAddUsuario:
                startActivity(new Intent(this, CadastrarUsuario.class));
                break;
            case R.id.ivRelatorio:
                startActivity(new Intent(this, GeradorPdf.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }


    //    private void criandoDiretorio(String diretorio) {
//        File file1 = new File(Environment.getExternalStorageDirectory(), "/" + diretorio + "/");
//        if (!file1.exists()) {
//            file1.mkdir();
//            file1 = new File(Environment.getExternalStorageDirectory(), "/" + diretorio + "/");
//        }
//    }

//    public ArrayList<Km> RelatorioPdf() throws DocumentException, FileNotFoundException {
//        final String DEST = "ControleKm/RelatorioKm.pdf";
////        String diretorio = null;
////        criandoDiretorio(diretorio);
//        Document documento = new Document();
//        ArrayList<Km> lista = new ArrayList<Km>();
//        File file = new File(Environment.getExternalStorageDirectory(), DEST);
//        file.getParentFile().mkdirs();
//        try {
//        String sql_innerJoin = "SELECT kms.id, kms.data, kms.itinerario, kms.kmInicial, kms.kmFinal, kms.kmTotal"
//                + " FROM kms ";

//
//            Cursor c = helper.getReadableDatabase().rawQuery(sql_innerJoin, null);
//            // Cursor c = getReadableDatabase().query(t,VisitantesDB.COLUNAS,
//            // null, null, null, null, null,null);
////            PdfWriter.getInstance(documento, new FileOutputStream(Environment.getExternalStorageDirectory() +
////                    File.separator + "testeKm.pdf"));
//            FileOutputStream fileOut = new FileOutputStream(file);
//            if (file.exists()) {
//                new FileOutputStream(file);
//            } else {
//                file.createNewFile();
//                new FileOutputStream(file);
//            }
//            PdfWriter.getInstance(documento, fileOut);
//            documento.open();
//            documento.addAuthor("Fernando");
//
//
//            while (c.moveToNext()) {
//                Km kms = new Km();
//                kms.setId(c.getInt(0));
//                DateFormat dateFormat = DateFormat.getDateInstance();
//                kms.setData(dateFormat.parse(c.getString(1)));
//                kms.setItinerario(c.getString(2));
//                kms.setKmInicial(c.getString(3));
//                kms.setKmFinal(c.getString(4));
////                kms.setKmTotal(c.getString(5));
//                documento.add(new Paragraph("Id: " + kms.getId()));
//                documento.add(new Paragraph("Data: " + kms.getData()));
//                documento.add(new Paragraph("Itinerario: " + kms.getItinerario()));
//                documento.add(new Paragraph("Km inicial: " + kms.getKmInicial()));
//                documento.add(new Paragraph("Km final: " + kms.getKmFinal()));
//                documento.add(new Paragraph("Km total: " + kms.getKmTotal()));
//                documento.add(new Paragraph("|____________________________________|"));
//                lista.add(kms);
//            }
//            c.close();
//            documento.addCreationDate();
//            documento.close();
//            fileOut.close();
//        } catch (Exception e) {
//            Log.e("erro", e.toString());
//        }
//        return lista;
//    }


}

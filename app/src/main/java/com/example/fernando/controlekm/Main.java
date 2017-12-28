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
    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private DBAdapter db;
    private SimpleDateFormat dateFormat;
    private Cursor c;
    private DatabaseHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ImageView ivRelatorio = (ImageView) findViewById(R.id.ivRelatorio);
        ivRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Erro ao gerar relatório", Toast.LENGTH_SHORT).show();
                } catch (DocumentException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Relatório gerado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void PdfWrapper() throws FileNotFoundException, DocumentException {
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("Você precisa permitir o acesso ao Armazenamento",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            criarPdf();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        PdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL Permissão negada", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void criarPdf() throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Criou um novo diretório para PDF");
        }

        try {
            c = db.getAllKms();
//            db.open();
//            SQLiteDatabase bd = helper.getWritableDatabase();
//            String sql = "select * from kms";
//            c = bd.rawQuery(sql, new String[]{KEY_ROWID, KEY_DATA, KEY_ITINERARIO, KEY_KM_INICIAL, KEY_KM_FINAL});
//            String sql_consulta = "SELECT id, data, itinerario, kmInicial, kmFinal"
//                    + " FROM kms ";
            pdfFile = new File(docsFolder.getAbsolutePath(), "RelatórioKm.pdf");
            OutputStream output = new FileOutputStream(pdfFile);
            Document documento = new Document();
//            List<Km> listaKm = helper.getReadableDatabase().
//            c = db.getAllKms();
            PdfWriter.getInstance(documento, output);
            documento.open();
//            Paragraph p = new Paragraph();
//            p.add("Nome: ");
//            documento.add(p);
            PdfPTable table = new PdfPTable(5);
            table.setWidths(new int[]{1, 2, 1, 1, 1});
            table.addCell(createCell("Id", 2, 1, Element.ALIGN_LEFT));
            table.addCell(createCell("Data", 2, 1, Element.ALIGN_LEFT));
            table.addCell(createCell("Itinerário", 2, 1, Element.ALIGN_LEFT));
            table.addCell(createCell("Km inicial", 2, 1, Element.ALIGN_LEFT));
            table.addCell(createCell("Km final", 2, 1, Element.ALIGN_LEFT));

//            PdfPCell c1 = new PdfPCell(new Phrase("Id"));
//            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(c1);
//            PdfPCell c2 = new PdfPCell(new Phrase("Data"));
//            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(c2);
//            PdfPCell c3 = new PdfPCell(new Phrase("Itinerário"));
//            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(c3);
//            PdfPCell c4 = new PdfPCell(new Phrase("Km inicial"));
//            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(c4);
//            PdfPCell c5 = new PdfPCell(new Phrase("Km final"));
//            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(c5);
//            table.setHeaderRows(1);

            while (c.moveToNext()) {
                String id = String.valueOf(c.getInt(c.getColumnIndex(DatabaseHelper.KEY_ROWID)));
                String data = String.valueOf(new Date(c.getLong(c.getColumnIndex(DatabaseHelper.KEY_DATA))));
                String itinerario = c.getString(c.getColumnIndex(DatabaseHelper.KEY_ITINERARIO));
                String kmInicial = c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_INICIAL));
                String kmFinal = c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_FINAL));
                String[][] info = {{id}, {data}, {itinerario}, {kmInicial}, {kmFinal}};
                for (String[] linha : info) {
                    table.addCell(createCell(linha[0], 1, 1, Element.ALIGN_LEFT));
                    table.addCell(createCell(linha[1], 1, 1, Element.ALIGN_LEFT));
                    table.addCell(createCell(linha[2], 1, 1, Element.ALIGN_LEFT));
                    table.addCell(createCell(linha[3], 1, 1, Element.ALIGN_LEFT));
                    table.addCell(createCell(linha[4], 1, 1, Element.ALIGN_LEFT));
//                Km kms = new Km();
//                kms.setId(c.getInt(0));
//                DateFormat dateFormat = DateFormat.getDateInstance();
//                kms.setData(dateFormat.parse(c.getString(1)));
//                kms.setItinerario(c.getString(2));
//                kms.setKmInicial(c.getString(3));
//                kms.setKmFinal(c.getString(4));
//                    documento.add(new Paragraph("Id: " + km.getId().toString()));
//                    documento.add(new Paragraph("Data: " + km.getData().toString()));
//                    documento.add(new Paragraph("Itinerario: " + km.getItinerario().toString()));
//                    documento.add(new Paragraph("Km inicial: " + km.getKmInicial().toString()));
//                    documento.add(new Paragraph("Km final: " + km.getKmFinal().toString()));
//                    documento.add(new Paragraph("|____________________________________|"));
                }
            }
//                document.add(new Paragraph(mContentEditText.getText().toString()));
            documento.add(table);
            documento.addCreationDate();
            documento.close();
            c.close();
//            bd.close();
//            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        previaPdf();

//        return kms;
    }

    private void previaPdf() {

        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("aplicação / pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "aplicação / pdf");

            startActivity(intent);
        } else {
            Toast.makeText(this, "Baixe um Visualizador de PDF para ver o PDF gerado", Toast.LENGTH_SHORT).show();
        }
    }

    private Km criarKm(Cursor cursor) {
        Km km = new Km(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_ROWID)),
                new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.KEY_DATA))),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ITINERARIO)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_QTD_CLIENTE)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_KM_INICIAL)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_KM_FINAL)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_KM_TOTAL)));
        return km;
    }

    public PdfPCell createCell(String content, float borderWidth, int colspan, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setBorderWidth(borderWidth);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        return cell;
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


}

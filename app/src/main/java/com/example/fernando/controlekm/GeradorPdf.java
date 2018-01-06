package com.example.fernando.controlekm;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Km;
import com.itextpdf.awt.geom.CubicCurve2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Flavia on 30/12/2017.
 */

public class GeradorPdf extends AppCompatActivity {
    private Cursor c;
    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private SimpleDateFormat dateFormat;
    private DBAdapter db;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerar_relatorio);


        db = new DBAdapter(this);

        Button gerarRelatorio = (Button) findViewById(R.id.btnGerarPdf);
        Button voltarPdf = (Button) findViewById(R.id.btnVoltarPdf);
        Button filtrarData = (Button) findViewById(R.id.btnFiltroPdf);


        filtrarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Filtro efetuado com sucesso!", Toast.LENGTH_LONG).show();
            }
        });

        voltarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        gerarRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    PdfWrapper();
                    mProgressDialog = new ProgressDialog(GeradorPdf.this);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setTitle("Carregando...");
                    mProgressDialog.setMessage("Iniciando");
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setMax(100);
                    mProgressDialog.setProgress(0);
                    mProgressDialog.show();
                    ProgressData p = new ProgressData();
                    p.execute(10);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Algo deu errado, tente mais tarde.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private class ProgressData extends AsyncTask<Integer, String, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            int progress = 0;
            int total = integers[0];
            while (progress <= total) {
                try {
//                    PdfWrapper();
                    Thread.sleep(2000); // pausa de 2 segundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (DocumentException e) {
//                    e.printStackTrace();
                }

                String s = progress % 2 == 0 ? "Por favor aguarde..." : "Construindo relatório";

                //exibindo o progresso
                this.publishProgress(String.valueOf(progress), String.valueOf(total), s);
                progress++;

            }
            return "done";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Float progress = Float.valueOf(values[0]);
            Float total = Float.valueOf(values[1]);
            String message = values[2];
            mProgressDialog.setProgress((int) ((progress / total) * 100));
            mProgressDialog.setMessage(message);
            // mensagem de finalização
            String a = "90";
            if (values[0].equals(a)) {
                mProgressDialog.setMessage("Finalizando...");
            }
            //se os valores sao iguais terminamos nosso processamento
            if (values[0].equals(values[1])) {
                //removemos o dialog
                mProgressDialog.cancel();
            }


        }
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
        docsFolder.mkdir();


//            c = db.getAllKm(getBaseContext());
//            db.open();
//            SQLiteDatabase bd = helper.getWritableDatabase();
//            String sql = "select * from kms";
//            c = bd.rawQuery(sql, new String[]{KEY_ROWID, KEY_DATA, KEY_ITINERARIO, KEY_KM_INICIAL, KEY_KM_FINAL});
//            String sql_consulta = "SELECT id, data, itinerario, kmInicial, kmFinal"
//                    + " FROM kms ";
        pdfFile = new File(docsFolder.getAbsolutePath(), "RelatórioKm.pdf");
        c = db.open().rawQuery("SELECT * FROM " + DatabaseHelper.DATABASE_TABLE, null);
        OutputStream output = new FileOutputStream(pdfFile);
        Document documento = new Document();
//            List<Km> listaKm = helper.getReadableDatabase().

        PdfWriter writer = PdfWriter.getInstance(documento, output);
        PageOrientation event = new PageOrientation();
        writer.setPageEvent(event);
        documento.open();
//            Paragraph p = new Paragraph();
//            p.add("Nome: ");
//            documento.add(p);
//            c = db.getAllKms();

        PdfPTable table = new PdfPTable(7);
        table.setWidths(new int[]{1, 2, 1, 1, 1, 1, 1});
        table.addCell(createCell("Id", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Data", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Itinerário", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Quant. Clientes", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Km inicial", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Km final", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Total", 2, 1, Element.ALIGN_LEFT));


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

//        while (c.moveToFirst()) {
//            List<Km> listaKms = db.getAllKm();
//            for (Km km : listaKms) {
//
//
//                table.addCell(createCell(String.valueOf(km.getId()), 1, 1, Element.ALIGN_LEFT));
//                String dataKm = dateFormat.format(km.getData());
//                table.addCell(createCell(dataKm, 1, 1, Element.ALIGN_LEFT));
//                table.addCell(createCell(km.getItinerario(), 1, 1, Element.ALIGN_LEFT));
//                table.addCell(createCell(String.valueOf(km.getQtdCliente()), 1, 1, Element.ALIGN_LEFT));
//                table.addCell(createCell(km.getKmInicial(), 1, 1, Element.ALIGN_LEFT));
//                table.addCell(createCell(km.getKmFinal(), 1, 1, Element.ALIGN_LEFT));
//                table.addCell(createCell(km.getKmTotal(), 1, 1, Element.ALIGN_LEFT));
//
//                Comparator<Km> comparator = new Comparator<Km>() {
//                    @Override
//                    public int compare(Km km, Km t1) {
//                        if (km.getData().compareTo(t1.getData()) < 0) {
//                            return -1;
//                        }
//                        if (km.getData().compareTo(t1.getData()) > 0) {
//                            return 1;
//                        }
//                        return 0;
//                    }
//
//                };
//                Collections.sort(listaKms, comparator);
//            }
//        while (c.moveToFirst()) {
        c.moveToFirst();
        for (int a = 0; a < c.getCount(); a++) {


            String id = String.valueOf(c.getInt(c.getColumnIndex(DatabaseHelper.KEY_ROWID)));
            String data = String.valueOf(new Date(c.getLong(c.getColumnIndex(DatabaseHelper.KEY_DATA))));
            String itinerario = c.getString(c.getColumnIndex(DatabaseHelper.KEY_ITINERARIO));
            String qtdCliente = c.getString(c.getColumnIndex(DatabaseHelper.KEY_QTD_CLIENTE));
            String kmInicial = c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_INICIAL));
            String kmFinal = c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_FINAL));
            String kmTotal = String.valueOf(c.getInt(c.getColumnIndex(DatabaseHelper.KEY_KM_TOTAL)));
//            String[][] info = {{id}, {data}, {itinerario}, {qtdCliente}, {kmInicial}, {kmFinal}, {kmTotal}};
//            for (String[] linha : info) {
            table.addCell(createCell(id, 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(data, 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(itinerario, 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(qtdCliente, 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(kmInicial, 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(kmFinal, 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(kmTotal, 1, 1, Element.ALIGN_LEFT));
//            }
            c.moveToNext();
        }
//        }
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
//        }

        documento.add(table);
        event.setOrientation(PdfPage.LANDSCAPE);
        documento.addCreationDate();
        documento.close();
        c.close();
        db.close();

//                document.add(new Paragraph(mContentEditText.getText().toString()));
//            bd.close();
//            db.close();


//            c.close();

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

}

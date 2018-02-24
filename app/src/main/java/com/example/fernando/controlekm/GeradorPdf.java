package com.example.fernando.controlekm;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Km;
import com.itextpdf.awt.geom.CubicCurve2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Flavia on 30/12/2017.
 */

public class GeradorPdf extends AppCompatActivity {
    private Cursor c;
    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile, docsFolder;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private SimpleDateFormat dateFormat;
    private DBAdapter db;
    private SQLiteDatabase database;
    private ProgressBar progressBar;
    private int ano, mes, dia;
    private Date dataPdf1, dataPdf2;
    private Button btnPrimeiraData, btnSegundaData;
    private String data1, data2, maxData, minData;
    private TextView txtError1, txtError2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerar_relatorio);
        db = new DBAdapter(this);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);


        btnPrimeiraData = (Button) findViewById(R.id.btnDataPdf1);
        btnSegundaData = (Button) findViewById(R.id.btnDataPdf2);
        txtError1 = (TextView) findViewById(R.id.txtError1);
        txtError2 = (TextView) findViewById(R.id.txtError2);


//        GeradorPdf geradorPdf = new GeradorPdf();
//        btnPrimeiraData.setText(geradorPdf.firstData());
//        btnSegundaData.setText(geradorPdf.lastData());


        Button gerarRelatorio = (Button) findViewById(R.id.btnGerarPdf);
        Button voltarPdf = (Button) findViewById(R.id.btnVoltarPdf);

        voltarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        gerarRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnPrimeiraData.getText().toString().isEmpty()) {
                    txtError1.setText(R.string.campo_vazio);
                } else {
                    txtError1.setText("");
                }
                if (btnSegundaData.getText().toString().isEmpty()) {
                    txtError2.setText(R.string.campo_vazio);
                } else {
                    txtError2.setText("");
                    chamaPdf();
                }
            }
        });


//        gerarRelatorio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//
//                    mProgressDialog = new ProgressDialog(GeradorPdf.this);
//                    mProgressDialog.setCancelable(false);
//                    mProgressDialog.setTitle("Carregando...");
//                    mProgressDialog.setMessage("Iniciando");
//                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                    mProgressDialog.setMax(100);
//                    mProgressDialog.setProgress(0);
//                    mProgressDialog.show();
//                    ProgressData p = new ProgressData();
//                    p.execute(10);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(getBaseContext(), "Algo deu errado, tente mais tarde.", Toast.LENGTH_LONG).show();
//                }
//            }
//        });


    }

    public void chamaPdf() {
        ProgressData proressData = new ProgressData();
        proressData.execute((Void[]) null);


    }


    private class ProgressData extends AsyncTask<Void, Integer, Void> {
        int barraMax = 100;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = (ProgressBar) findViewById(R.id.progressBar2);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(barraMax);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values[0]);
        }

        @Override
        protected Void doInBackground(Void... params) {

                try {
                    PdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
//                try {
//                    Looper.prepare();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                }
                this.publishProgress(params);

//            int progress = 0;
//            int total = integers[0];
//            while (progress <= total) {
//                try {
//
//                    Thread.sleep(2000); // pausa de 2 segundos
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                String s = progress % 2 == 0 ? "Por favor aguarde..." : "Construindo relatório";
//
//                //exibindo o progresso
//                this.publishProgress(String.valueOf(progress), String.valueOf(total), s);
//                progress++;
//
//
//            }
//            return "done";
            return null;
        }

        private Void[] publishProgress(Void[] params) {
            return params;
        }

//        @Override
//        protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);
//            Float progress = Float.valueOf(values[0]);
//            Float total = Float.valueOf(values[1]);
//            String message = values[2];
//            mProgressDialog.setProgress((int) ((progress / total) * 100));
//            mProgressDialog.setMessage(message);
//            // mensagem de finalização
//            String a = "90";
//            if (values[0].equals(a)) {
//                mProgressDialog.setMessage("Finalizando...");
//            }
//            //se os valores sao iguais terminamos nosso processamento
//            if (values[0].equals(values[1])) {
//                //removemos o dialog
//                mProgressDialog.cancel();
//            }
//
//
//        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.INVISIBLE);
            previaPdf();
            super.onPostExecute(aVoid);
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

//    private String firstData() {
//        DatabaseHelper databaseHelper = new DatabaseHelper(this);
////        SQLiteDatabase database = databaseHelper.getReadableDatabase();
//        SQLiteDatabase database = db.read();
//        Cursor cs = database.rawQuery("SELECT MIN(data) FROM kms", null);
//
//        cs.moveToFirst();
//        for (int i = 1; i < cs.getCount(); i++) {
//            minData = cs.getString(cs.getColumnIndex(DatabaseHelper.KEY_DATA));
//            try {
//                minData = inverteOrdemData(minData);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            cs.moveToNext();
//        }
//        cs.close();
//        return minData;
//    }

//    private String lastData() {
//        DatabaseHelper databaseHelper = new DatabaseHelper(this);
//        SQLiteDatabase database = databaseHelper.getReadableDatabase();
////        SQLiteDatabase database = db.read();
//        Cursor cr = database.rawQuery("SELECT MAX(data) FROM kms", null);
//        cr.moveToFirst();
//        for (int d = 1; d < cr.getCount(); d++) {
//            maxData = cr.getString(1);
//            try {
//                maxData = inverteOrdemData(maxData);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            cr.moveToNext();
//        }
//        cr.close();
//        return maxData;
//    }

    private void criarPdf() throws FileNotFoundException, DocumentException {
        database = db.read();

        try {
            data1 = desenverterData(btnPrimeiraData.getText().toString());
            data2 = desenverterData(btnSegundaData.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        docsFolder.mkdir();


        pdfFile = new File(docsFolder.getAbsolutePath(), "RelatórioKm.pdf");
        c = database.rawQuery("SELECT * FROM kms WHERE data BETWEEN '" + data1 + "' AND '" + data2 + "' ORDER BY data", null);
        Cursor cursor = database.rawQuery("SELECT * FROM usuarios", null);

        OutputStream output = new FileOutputStream(pdfFile);
        Document documento = new Document(PageSize.A4.rotate());


        PdfWriter writer = PdfWriter.getInstance(documento, output);
        PageOrientation event = new PageOrientation();
        writer.setPageEvent(event);
        documento.open();

        PdfPTable table0 = new PdfPTable(1);
        table0.setWidthPercentage(100f);

        PdfPTable table1 = new PdfPTable(3);
        cursor.moveToFirst();
        for (int b = 0; b < cursor.getCount(); b++) {
            String nome = cursor.getString(1);
            String unidade = cursor.getString(2);
            String funcao = cursor.getString(3);
            String placa = cursor.getString(4);
            String gerencia = cursor.getString(5);
            table1.addCell(createCell("Nome: " + nome, 1, 1, Element.ALIGN_LEFT));
            table1.addCell(createCell("Unidade: " + unidade, 1, 1, Element.ALIGN_LEFT));
            table1.addCell(createCell("Função: " + funcao, 1, 1, Element.ALIGN_LEFT));
            table1.addCell(createCell("Placa: " + placa, 1, 1, Element.ALIGN_LEFT));
            table1.addCell(createCell("Gerência: " + gerencia, 1, 1, Element.ALIGN_LEFT));
            table1.addCell(createCell("Mês de Competência: ", 1, 1, Element.ALIGN_LEFT));

            cursor.moveToNext();
        }
        cursor.close();
        table0.addCell(new PdfPCell(table1));


        PdfPTable table = new PdfPTable(8);

        table.setWidths(new int[]{1, 2, 10, 2, 2, 2, 2, 2});
        table.addCell(createCell("Id", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Data", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Itinerário", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Quant. Clientes", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Km inicial", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Km final", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Km Percorrido", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Total", 2, 1, Element.ALIGN_LEFT));


        c.moveToFirst();
        for (int a = 0; a < c.getCount(); a++) {


            String id = String.valueOf(c.getInt(c.getColumnIndex(DatabaseHelper.KEY_ROWID)));
            String data = c.getString(c.getColumnIndex(DatabaseHelper.KEY_DATA));
            try {
                data = inverteOrdemData(data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String itinerario = c.getString(c.getColumnIndex(DatabaseHelper.KEY_ITINERARIO));
            String qtdCliente = c.getString(c.getColumnIndex(DatabaseHelper.KEY_QTD_CLIENTE));
            String kmInicial = c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_INICIAL));
            String kmFinal = c.getString(c.getColumnIndex(DatabaseHelper.KEY_KM_FINAL));
            String kmPercorrido = String.valueOf(c.getInt(c.getColumnIndex(DatabaseHelper.KEY_KM_TOTAL)));
            String kmTotal = String.valueOf(c.getInt(c.getColumnIndex(DatabaseHelper.KEY_KM_TOTAL)));
            Double _kmTotal = Double.parseDouble(kmTotal);
            _kmTotal = _kmTotal * Constante.VALOR_KM_RODADO;
            kmTotal = formatoMoeda(_kmTotal);
            table.addCell(createCell(id, 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(data, 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(itinerario, 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(qtdCliente, 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(kmInicial, 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(kmFinal, 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(kmPercorrido, 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(kmTotal, 1, 1, Element.ALIGN_LEFT));

            c.moveToNext();
        }
        c.close();
        event.setRotation(PdfPage.LANDSCAPE);
        table0.addCell(new PdfPCell(table));
        documento.add(table0);
        documento.addCreationDate();
        documento.close();

    }

    private static String formatoMoeda(Double valor) {
        Locale ptBr = new Locale("pt", "BR");
        String vlr = NumberFormat.getCurrencyInstance(ptBr).format(valor);
        return vlr;
    }

    private static String inverteOrdemData(String data) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(data);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        String _date = simpleDateFormat1.format(date);
        return _date;
    }

    private static String desenverterData(String data) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = simpleDateFormat.parse(data);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        String _date = simpleDateFormat1.format(date);
        return _date;
    }

    private void previaPdf() {
        if (pdfFile.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";
            new File(path, "RelatórioKm.pdf");
            intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
            startActivity(intent);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Baixe um visualizador de PDF para ver o PDF gerado")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();

        }
    }

    public PdfPCell createCell(String content, float borderWidth, int colspan, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setBorderWidth(borderWidth);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    public void selecionarData(View view) {
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case R.id.btnDataPdf1:
                return new DatePickerDialog(this, dataPdfPrimeiro, ano, mes, dia);
            case R.id.btnDataPdf2:
                return new DatePickerDialog(this, dataPdfSegundo, ano, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dataPdfPrimeiro = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int anoSelecionado, int mesSelecionado, int diaSelecionado) {
            ano = anoSelecionado;
            mes = mesSelecionado;
            dia = diaSelecionado;
            dataPdf1 = criarData(anoSelecionado, mesSelecionado, diaSelecionado);
            AtualizarData1();

        }
    };
    private DatePickerDialog.OnDateSetListener dataPdfSegundo = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int anoSelecionado, int mesSelecionado, int diaSelecionado) {
            ano = anoSelecionado;
            mes = mesSelecionado;
            dia = diaSelecionado;
            dataPdf2 = criarData(anoSelecionado, mesSelecionado, diaSelecionado);
            AtualizarData2();
        }
    };

    private Date criarData(int anoSelecionado, int mesSelecionado, int diaSelecionado) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(anoSelecionado, mesSelecionado, diaSelecionado);
        return calendar.getTime();
    }

    private void AtualizarData1() {
        btnPrimeiraData.setText(new StringBuilder().append(dia).append("/").append(mes +
                1).append("/").append(ano).append(""));
    }

    private void AtualizarData2() {
        btnSegundaData.setText(new StringBuilder().append(dia).append("/").append(mes +
                1).append("/").append(ano).append(""));
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}

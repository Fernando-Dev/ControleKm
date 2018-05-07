package com.example.fernando.controlekm;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.controlekm.BD.DatabaseHelper;
import com.example.fernando.controlekm.DAO.DBAdapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Flavia on 30/12/2017.
 */

public class GeradorPdf extends AppCompatActivity {
    private Cursor c, cc, cursor;
    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile, docsFolder;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private SimpleDateFormat dateFormat;
    private DBAdapter db;
    private SQLiteDatabase database;
    private int ano1, ano2, mes1, mes2, dia1, dia2;
    private Date dataPdf1, dataPdf2;
    private Button btnPrimeiraData, btnSegundaData;
    private String data1, data2, _maxData, _minData;
    private TextView txtError1, txtError2, textoInfo;
    private Spinner mesesAno;
    private Image image;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerar_relatorio);
        db = new DBAdapter(this);

        btnPrimeiraData = (Button) findViewById(R.id.btnDataPdf1);
        btnSegundaData = (Button) findViewById(R.id.btnDataPdf2);
        txtError1 = (TextView) findViewById(R.id.txtError1);
        txtError2 = (TextView) findViewById(R.id.txtError2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.meses_ano, R.layout.spinner_meses_ano);
        mesesAno = (Spinner) findViewById(R.id.mesesAno);
        mesesAno.setAdapter(adapter);
        if (db.listaUsuario().isEmpty() || db.getAllKm().isEmpty()) {
            final Dialog dialog = new Dialog(this, R.style.DialogoSemTitulo);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_alert_dialog_aviso);
            dialog.setCancelable(false);
            TextView txtMsgm = dialog.findViewById(R.id.mensagemAlertDialogAviso);
            txtMsgm.setText(R.string.aviso_relatorio);
            Button btnOK = dialog.findViewById(R.id.btnAlertDialogAvisoOK);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            dialog.show();
        } else {
            btnSegundaData.setText(lastData());
            btnPrimeiraData.setText(firstData());
            try {
                ano2 = pegaAno(lastData());
                mes2 = pegaMes(lastData());
                dia2 = pegaDia(lastData());
                ano1 = pegaAno(firstData());
                mes1 = pegaMes(firstData());
                dia1 = pegaDia(firstData());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


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
                } else if (btnSegundaData.getText().toString().isEmpty()) {
                    txtError1.setText("");
                    txtError2.setText(R.string.campo_vazio);
                } else if (btnPrimeiraData.getText().toString().equals(btnSegundaData.getText().toString())) {
                    txtError1.setText("");
                    txtError2.setText("");
                    final Dialog dialog = new Dialog(GeradorPdf.this, R.style.DialogoSemTitulo);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_alert_dialog_erro);
                    dialog.setCancelable(false);
                    TextView txtMsgem = dialog.findViewById(R.id.mensagemAlertDialogErro);
                    txtMsgem.setText(R.string.erro_relatorio_data_igual);
                    Button btnOK = dialog.findViewById(R.id.btnAlertDialogErroOK);
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else if (btnPrimeiraData.getText().toString().compareTo(btnSegundaData.getText().toString()) == 1) {
                    txtError1.setText("");
                    txtError2.setText("");
                    final Dialog dialog = new Dialog(GeradorPdf.this, R.style.DialogoSemTitulo);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_alert_dialog_erro);
                    dialog.setCancelable(false);
                    TextView txtMsgem = dialog.findViewById(R.id.mensagemAlertDialogErro);
                    txtMsgem.setText(R.string.erro_relatorio_inicial_maior);
                    Button btnOK = dialog.findViewById(R.id.btnAlertDialogErroOK);
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else if (mesesAno.getSelectedItem().toString().equals("Selecione")) {
                    mesesAno.setBackgroundResource(R.drawable.spinner_background_erro);
                    final Dialog dialog = new Dialog(GeradorPdf.this, R.style.DialogoSemTitulo);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_alert_dialog_erro);
                    dialog.setCancelable(false);
                    TextView txtMsgem = dialog.findViewById(R.id.mensagemAlertDialogErro);
                    txtMsgem.setText(R.string.erro_relatorio_mes_competencia);
                    Button btnOK = dialog.findViewById(R.id.btnAlertDialogErroOK);
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    txtError1.setText("");
                    txtError2.setText("");
                    mesesAno.setBackgroundResource(R.drawable.spinner_background);
                    chamaPdf();
                }
            }
        });
    }

    public void chamaPdf() {
        ProgressData proressData = new ProgressData();
        proressData.execute();


    }


    private class ProgressData extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(GeradorPdf.this);
            p.setMessage("Carregando... Por favor aguarde!");
            p.setIndeterminate(false);
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected Void doInBackground(Void... integers) {

            try {
                PdfWrapper();
                criarPdf();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            p.dismiss();
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

    private static Integer pegaDia(String data) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = simpleDateFormat.parse(data);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd");
        String _date = simpleDateFormat1.format(date);
        Integer dia = Integer.valueOf(_date);
        return dia;
    }

    private static Integer pegaMes(String data) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = simpleDateFormat.parse(data);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM");
        String _date = simpleDateFormat1.format(date);
        Integer mes = Integer.valueOf(_date);
        return mes - 1;
    }

    private static Integer pegaAno(String data) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = simpleDateFormat.parse(data);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy");
        String _date = simpleDateFormat1.format(date);
        Integer ano = Integer.valueOf(_date);
        return ano;
    }

    private String lastData() {
        SQLiteDatabase database = db.read();
        Cursor maxData = database.rawQuery("SELECT data FROM kms WHERE kmFinal = (SELECT MAX(kmFinal) FROM kms)", null);
        maxData.moveToFirst();
        for (int i = 0; i < maxData.getCount(); i++) {
            data2 = maxData.getString(maxData.getColumnIndex(DatabaseHelper.KEY_DATA));
            maxData.moveToNext();
        }
        maxData.close();

        try {
            _maxData = inverteOrdemData(data2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _maxData;
    }

    private String firstData() {
        SQLiteDatabase database = db.read();
        Cursor minData = database.rawQuery("SELECT data FROM kms WHERE kmInicial = (SELECT MIN(kmInicial) FROM kms)", null);
        minData.moveToFirst();
        for (int i = 0; i < minData.getCount(); i++) {
            data1 = minData.getString(minData.getColumnIndex(DatabaseHelper.KEY_DATA));
            minData.moveToNext();
        }
        minData.close();

        try {
            _minData = inverteOrdemData(data1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _minData;
    }


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
        /*
        * tentar fazer um select para duas tabelas kms e usuario
         * exemplo
         * select * from kms
         * inner join usuarios
         * on (kms.data = usuarios.nome)
         * where kms.data
         * between 'data1' and 'data2'
         * oder by kms.data
        * */

        pdfFile = new File(docsFolder.getAbsolutePath(), "RelatórioKm.pdf");
        c = database.rawQuery("SELECT * FROM kms WHERE data BETWEEN '" + data1 + "' AND '" + data2 + "' ORDER BY data", null);
        cc = database.rawQuery("SELECT * FROM kms WHERE data BETWEEN '" + data1 + "' AND '" + data2 + "' ORDER BY data", null);
        cursor = database.rawQuery("SELECT * FROM usuarios", null);

        FileOutputStream output = new FileOutputStream(pdfFile);
        Document documento = new Document(PageSize.A4.rotate());
        Calendar calendar = Calendar.getInstance();
        String ano = String.valueOf(calendar.get(Calendar.YEAR));

        PdfWriter writer = PdfWriter.getInstance(documento, output);
        PageOrientation event = new PageOrientation();

        writer.setPageEvent(event);
        documento.open();

        PdfPTable table0 = new PdfPTable(1);
        table0.setWidthPercentage(100f);

        PdfPTable table5 = new PdfPTable(2);
        table5.setWidths(new int[]{1, 9});
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.new_ic_controlekm_4);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            image = Image.getInstance(stream.toByteArray());
            image.setAlignment(Image.MIDDLE);
            table5.addCell(new PdfPCell(image, true));
            Font font = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            String frase = "Relatório de Quilometragens";
            Paragraph titulo = new Paragraph(frase, font);
            titulo.setLeading(0, 1);
            titulo.setAlignment(Element.ALIGN_CENTER);
            PdfPCell cell = new PdfPCell();
            cell.setMinimumHeight(50);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.addElement(titulo);
            table5.addCell(cell);
        } catch (IOException e) {
            e.printStackTrace();
        }
        table0.addCell(new PdfPCell(table5));

        PdfPTable table1 = new PdfPTable(3);
        cursor.moveToFirst();
        for (int b = 0; b < cursor.getCount(); b++) {
            String nome = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_NOME));
            String unidade = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_UNIDADE));
            String funcao = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FUNCAO));
            String placa = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_PLACA));
            String gerencia = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_GERENCIA));
            String competencia = mesesAno.getSelectedItem().toString();
            table1.addCell(createCell("Nome: " + nome, 1, 1, Element.ALIGN_LEFT));
            table1.addCell(createCell("Unidade: " + unidade, 1, 1, Element.ALIGN_LEFT));
            table1.addCell(createCell("Função: " + funcao, 1, 1, Element.ALIGN_LEFT));
            table1.addCell(createCell("Placa: " + placa, 1, 1, Element.ALIGN_LEFT));
            table1.addCell(createCell("Gerência: " + gerencia, 1, 1, Element.ALIGN_LEFT));
            table1.addCell(createCell("Mês de Competência: " + competencia.toUpperCase() + "/" + ano, 1, 1, Element.ALIGN_LEFT));

            cursor.moveToNext();
        }

        cursor.close();
        table0.addCell(new PdfPCell(table1));


        PdfPTable table = new PdfPTable(8);

        table.setWidths(new int[]{1, 2, 10, 2, 2, 2, 2, 2});
        table.addCell(createCell("Id", 2, 1, Element.ALIGN_CENTER));
        table.addCell(createCell("Data", 2, 1, Element.ALIGN_CENTER));
        table.addCell(createCell("Itinerário", 2, 1, Element.ALIGN_CENTER));
        table.addCell(createCell("Quant. Clientes", 2, 1, Element.ALIGN_CENTER));
        table.addCell(createCell("Km inicial", 2, 1, Element.ALIGN_CENTER));
        table.addCell(createCell("Km final", 2, 1, Element.ALIGN_CENTER));
        table.addCell(createCell("Km Percorrido", 2, 1, Element.ALIGN_CENTER));
        table.addCell(createCell("Total", 2, 1, Element.ALIGN_CENTER));


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
            table.addCell(createCell(id, 1, 1, Element.ALIGN_CENTER));
            table.addCell(createCell(data, 1, 1, Element.ALIGN_CENTER));
            table.addCell(createCell(itinerario, 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(qtdCliente, 1, 1, Element.ALIGN_CENTER));
            table.addCell(createCell(kmInicial, 1, 1, Element.ALIGN_CENTER));
            table.addCell(createCell(kmFinal, 1, 1, Element.ALIGN_CENTER));
            table.addCell(createCell(kmPercorrido, 1, 1, Element.ALIGN_CENTER));
            table.addCell(createCell(kmTotal, 1, 1, Element.ALIGN_CENTER));

            c.moveToNext();
        }
        c.close();
        table0.addCell(new PdfPCell(table));

        PdfPTable table2 = new PdfPTable(8);

        table2.setWidths(new int[]{1, 2, 10, 2, 2, 2, 2, 2});
        Integer totalCliente = 0, totalKm = 0;
        String vlrDeslocamento = "";
        Double _vlrDeslocamento = null;
        cc.moveToFirst();
        for (int e = 0; e < cc.getCount(); e++) {
            Integer qtdCliente = Integer.valueOf(cc.getString(cc.getColumnIndex(DatabaseHelper.KEY_QTD_CLIENTE)));
            totalCliente = totalCliente + qtdCliente;
            Integer _totalKm = cc.getInt(cc.getColumnIndex(DatabaseHelper.KEY_KM_TOTAL));
            totalKm = totalKm + _totalKm;

            String sttotalKm = String.valueOf(totalKm);
            Double dbTotalKm = Double.valueOf(sttotalKm);
            _vlrDeslocamento = dbTotalKm * Constante.VALOR_KM_RODADO;
            vlrDeslocamento = formatoMoeda(_vlrDeslocamento);

            cc.moveToNext();
        }
        cc.close();
        table2.addCell(createCell("-", 1, 1, Element.ALIGN_CENTER));
        table2.addCell(createCell("-", 1, 1, Element.ALIGN_CENTER));
        table2.addCell(createCell("-", 1, 1, Element.ALIGN_CENTER));
        table2.addCell(createCell(totalCliente.toString(), 1, 1, Element.ALIGN_CENTER));
        table2.addCell(createCell("-", 1, 1, Element.ALIGN_CENTER));
        table2.addCell(createCell("-", 1, 1, Element.ALIGN_CENTER));
        table2.addCell(createCell(totalKm.toString() + " Km", 1, 1, Element.ALIGN_CENTER));
        table2.addCell(createCell(vlrDeslocamento, 1, 1, Element.ALIGN_CENTER));

        table0.addCell(new PdfPCell(table2));

        PdfPTable table3 = new PdfPTable(2);
        table3.setWidthPercentage(50f);
        table3.setHorizontalAlignment(Element.ALIGN_LEFT);
        String cliente = String.valueOf(totalCliente);
        Double dClientes = Double.valueOf(cliente);
        Double despesa = (_vlrDeslocamento / dClientes);
        String sDespesa = formatoMoeda(despesa);
        table3.addCell(createCell("VALOR TOTAL: ", 1, 1, Element.ALIGN_CENTER));
        table3.addCell(createCell(vlrDeslocamento, 1, 1, Element.ALIGN_CENTER));
        table3.addCell(createCell("QUANTIDADE DE CLIENTES: ", 1, 1, Element.ALIGN_CENTER));
        table3.addCell(createCell(totalCliente.toString(), 1, 1, Element.ALIGN_CENTER));
        table3.addCell(createCell("DESPESA POR CLIENTE: ", 1, 1, Element.ALIGN_CENTER));
        table3.addCell(createCell(sDespesa, 1, 1, Element.ALIGN_CENTER));


        PdfPTable footer = new PdfPTable(1);
        footer.setWidthPercentage(100);
        footer.getDefaultCell().setFixedHeight(40);
        footer.getDefaultCell().setBorder(Rectangle.TOP);
        footer.setHorizontalAlignment(Element.ALIGN_CENTER);
        footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        footer.getDefaultCell().setPaddingTop(10);
        Font font = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);
        Paragraph frase = new Paragraph("Create by ControleKm", font);
        frase.setAlignment(Element.ALIGN_CENTER);
        footer.addCell(frase);

        event.setRotation(PdfPage.LANDSCAPE);
        documento.add(table0);
        documento.add(table3);
        documento.add(footer);
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
            final Dialog dialog = new Dialog(GeradorPdf.this, R.style.DialogoSemTitulo);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_alert_dialog_relatorio);
            dialog.setCancelable(false);
            TextView txtMsgem = dialog.findViewById(R.id.mensagemAlertDialogRelatorio);
            txtMsgem.setText("O RelatórioKm.pdf foi gerado e está salvo neste caminho " + docsFolder + ". Deseja abrir o relatório?");
            Button btnOK = dialog.findViewById(R.id.btnAlertDialogRelatorioOK);
            Button btnCancelar = dialog.findViewById(R.id.btnAlertDialogRelatorioCancelar);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";
                    new File(path, "RelatórioKm.pdf");
                    intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
                    startActivity(intent);
                    finish();
                }
            });
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            final Dialog dialog = new Dialog(GeradorPdf.this, R.style.DialogoSemTitulo);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_alert_dialog_aviso);
            dialog.setCancelable(false);
            TextView txtMsgem = dialog.findViewById(R.id.mensagemAlertDialogAviso);
            txtMsgem.setText("O RelatórioKm está salvo em " + docsFolder + " e baixe um visualizador de PDF para ver o relatório gerado.");
            Button btnOK = dialog.findViewById(R.id.btnAlertDialogAvisoOK);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            dialog.show();
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
                return new DatePickerDialog(this, dataPdfPrimeiro, ano1, mes1, dia1);
            case R.id.btnDataPdf2:
                return new DatePickerDialog(this, dataPdfSegundo, ano2, mes2, dia2);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dataPdfPrimeiro = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int anoSelecionado, int mesSelecionado, int diaSelecionado) {
            ano1 = anoSelecionado;
            mes1 = mesSelecionado;
            dia1 = diaSelecionado;
            dataPdf1 = criarData(anoSelecionado, mesSelecionado, diaSelecionado);
            AtualizarData1();

        }
    };
    private DatePickerDialog.OnDateSetListener dataPdfSegundo = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int anoSelecionado, int mesSelecionado, int diaSelecionado) {
            ano2 = anoSelecionado;
            mes2 = mesSelecionado;
            dia2 = diaSelecionado;
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
        btnPrimeiraData.setText(new StringBuilder().append(dia1).append("/").append(mes1 +
                1).append("/").append(ano1).append(""));
    }

    private void AtualizarData2() {
        btnSegundaData.setText(new StringBuilder().append(dia2).append("/").append(mes2 +
                1).append("/").append(ano2).append(""));
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}

package com.example.fernando.controlekm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;


import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Km;


/**
 * Created by Flavia on 23/07/2017.
 */

public class ListKm extends ListActivity implements
        OnItemClickListener, OnClickListener {
    public final static String KEY_EXTRA_ROW_ID = "KEY_EXTRA_ROW_ID";
    private AlertDialog dialogConfirmacao;
    private DBAdapter db;
    private List<Map<String, Object>> kms;
    private SimpleDateFormat dateFormat;
    private AlertDialog alertDialog;
    private int kmSelecionado;
    private Km km;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        db = new DBAdapter(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        getListView().setOnItemClickListener(ListKm.this);
        alertDialog = criaAlertDialog();
        dialogConfirmacao = criarDialogConfirmacao();


        new Task().execute((Void[]) null);

    }

    private class Task extends AsyncTask<Void, Void, List<Map<String, Object>>> {

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            return listarKms();
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> result) {

            String[] de = {"id", "data", "itinerario", "kms"/*, "Total"*/};
            int[] para = {R.id.lstIdKm, R.id.lstDataKm, R.id.lstItinerario, R.id.lstKm/*, R.id.lstKmTotal*/};
            SimpleAdapter simpleAdapter = new SimpleAdapter(ListKm.this, result,
                    R.layout.listar_km, de, para);
            setListAdapter(simpleAdapter);
        }

    }

    private List<Map<String, Object>> listarKms() {
        kms = new ArrayList<Map<String, Object>>();
        List<Km> listaKms = db.getAllKm();
        for (Km km : listaKms) {
            Map<String, Object> item = new HashMap<String, Object>();
            String periodo = dateFormat.format(km.getData());
            item.put("id", km.getId() + " - ");
            item.put("data", periodo);
            item.put("itinerario", km.getItinerario());
            item.put("kms", km.getKmInicial() + " Km" + " a " + km.getKmFinal() + " Km ");
//            item.put("Total",km.getKmTotal());
            kms.add(item);
            Comparator<Km> comparator = new Comparator<Km>() {
                @Override
                public int compare(Km km, Km t1) {
                    if (km.getData().compareTo(t1.getData()) < 0) {
                        return -1;
                    }
                    if (km.getData().compareTo(t1.getData()) > 0) {
                        return 1;
                    }
                    return 0;
                }
            };
            Collections.sort(listaKms, comparator);
        }
        return kms;
    }

    private AlertDialog criarDialogConfirmacao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_km);
        builder.setPositiveButton(getString(R.string.sim), this);
        builder.setNegativeButton(getString(R.string.nao), this);

        return builder.create();
    }

    private AlertDialog criaAlertDialog() {
        final CharSequence[] items = {getString(R.string.alterar), getString(R.string.remover)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcoes);
        builder.setItems(items, this);

        return builder.create();

    }

    @Override
    public void onClick(DialogInterface dialog, int item) {
        final int editar = 0;
        final int excluir = 1;
        Intent intent;

        Long id = (Long) kms.get(kmSelecionado).get("id");

        switch (item) {
            case editar: //editar
                intent = new Intent(this, AlterarKm.class);
                intent.putExtra(Constante.KEY_ROWID, id);
                startActivity(intent);
                break;
            case excluir: //confirmacao de exclusao
                dialogConfirmacao.show();
                break;
            case DialogInterface.BUTTON_POSITIVE: //exclusao
                kms.remove(kmSelecionado);
                db.deleteKm(Long.valueOf(id));
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        boolean kmClicado = true;
//        if (kmClicado) {
//        if (position == parent.getSelectedItemPosition()) {
////            Cursor itemCursor = (Cursor) kms.get(position).get("id");
////            int idKm = itemCursor.getInt(itemCursor.getColumnIndex(DatabaseHelper.KEY_ROWID));
//
//            String idKm = (String) kms.get(position).get("id");
//            Intent data = new Intent();
//            data.putExtra(KEY_EXTRA_ROW_ID, idKm);
//            setResult(Activity.RESULT_OK, data);
//            alertDialog.show();
//            finish();
//        } else {
//            position = parent.getSelectedItemPosition();
            kmSelecionado = position;
//            String idKm = (String) kms.get(kmSelecionado).get("id");
//            Intent data = new Intent();
//            data.putExtra(KEY_EXTRA_ROW_ID,idKm);
//            setResult(Activity.RESULT_OK,data);
            alertDialog.show();
//            finish();
//        }
//        }
    }


}
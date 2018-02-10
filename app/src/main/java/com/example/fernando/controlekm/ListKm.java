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
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;


import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


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
        OnItemClickListener {

    private DBAdapter db;
    private List<Map<String, Object>> kms;
    private SimpleDateFormat dateFormat;
    private int kmSelecionado;
    private Km km;
    private String data;
    private Integer Id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBAdapter(this);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        getListView().setOnItemClickListener(ListKm.this);

        registerForContextMenu(getListView());

        new Task().execute((Void[]) null);

    }

    private class Task extends AsyncTask<Void, Void, List<Map<String, Object>>> {

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            return listarKms();
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> result) {

            String[] de = {"id", "data", "itinerario", "qtdCliente", "kms", "Total"};
            int[] para = {R.id.lstIdKm, R.id.lstDataKm, R.id.lstItinerario,
                    R.id.lstQtdCliente, R.id.lstKm, R.id.lstKmTotal};
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
            item.put("id", km.getId());
            item.put("data", "Data: " + periodo);
            item.put("itinerario", "Itinerário: " + km.getItinerario());
            item.put("qtdCliente", "Cliente(s): " + km.getQtdCliente());
            item.put("kms", "Km Inicial: " + km.getKmInicial() + " Km" + " <---> " + "Km Final: " + km.getKmFinal() + " Km ");
            item.put("Total", "Total: " + km.getKmTotal() + " Km ");
            kms.add(item);

        }
        return kms;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = kms.get(position);
        kmSelecionado = position;
        Integer codigo = (Integer) map.get("id");
        Id = (Integer) db.returnIdKm(codigo); // validar id do item selecionado
        Toast.makeText(getBaseContext(), "Km selecionado: " + Id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        setTitle(R.string.opcoes);
        menu.setHeaderTitle(R.string.opcoes);
        inflater.inflate(R.menu.list_km_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Remover:
                new AlertDialog.Builder(this).setTitle("Deletando Quilometragem").
                        setMessage(R.string.confirmacao_exclusao_km).
                        setPositiveButton(R.string.sim,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        AdapterView.AdapterContextMenuInfo info =
                                                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                                        int position = info.position;
                                        kms.remove(position);
                                        getListView().invalidateViews();
                                        data = "";
                                        db.deleteKm(Id);
                                    }
                                })
                        .setNegativeButton(R.string.nao, null)
                        .show();
                break;
            case R.id.Editar:
                AdapterView.AdapterContextMenuInfo
                        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int posicao = info.position;
                Map<String, Object> map = kms.get(posicao);
                Integer id = (Integer) map.get("id");
                Intent intent = new Intent(this, AlterarKm.class);
                intent.putExtra("EXTRA_ID_KM", id);
                startActivity(intent);
                finish();
                break;
        }
        return super.onContextItemSelected(item);
    }

}

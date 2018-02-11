package com.example.fernando.controlekm;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Km;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ListarKm extends ListActivity implements AdapterView.OnItemClickListener {

    private DBAdapter db;
    private List<Map<String, Object>> kms;
    private SimpleDateFormat dateFormat;
    private String data;
    private Integer Id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DBAdapter(this);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);

        getListView().setOnItemClickListener(ListarKm.this);

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
            SimpleAdapter simpleAdapter = new SimpleAdapter(ListarKm.this, result, R.layout.lista_km, de, para);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Map<String, Object> map = kms.get(i);
        Id = (Integer) map.get("id");
        Toast.makeText(getBaseContext(), "Km selecionado: " + Id, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle(R.string.opcoes);
        inflater.inflate(R.menu.list_km_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Editar:
                AdapterView.AdapterContextMenuInfo informacao =
                        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int posicao = informacao.position;
                Map<String, Object> map = kms.get(posicao);
                Integer id = (Integer) map.get("id");
                Intent intent = new Intent(ListarKm.this, AlterarKm.class);
                intent.putExtra("EXTRA_ID_KM", id);
                startActivity(intent);
                finish();
                break;
            case R.id.Remover:
                new AlertDialog.Builder(this).setTitle("Deletando Km").
                        setMessage(R.string.confirmacao_exclusao_km).
                        setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AdapterView.AdapterContextMenuInfo information = (
                                        AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                                int pos = information.position;
                                kms.remove(pos);
                                getListView().invalidateViews();
                                data = "";
                                db.deleteKm(Id);
                            }
                        })
                        .setNegativeButton(R.string.nao, null)
                        .show();
                break;
        }
        return super.onContextItemSelected(item);
    }
}

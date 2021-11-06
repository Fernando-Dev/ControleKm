package br.fernando.controlekm.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import br.fernando.controlekm.dao.DBAdapter;

import br.fernando.controlekm.R;

import br.fernando.controlekm.dominio.Km;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ListarKm extends AppCompatActivity implements AdapterView.OnItemClickListener/*,
        SearchView.OnQueryTextListener*/ {

    private DBAdapter dbAdapter;
    private List<Map<String, Object>> kms;
    private SimpleDateFormat dateFormat;
    private String data;
    private Integer Id;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consulta_km);


        listView = (ListView) findViewById(R.id.listView);

        dbAdapter = new DBAdapter(this);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);

        listView.setOnItemClickListener(ListarKm.this);

        registerForContextMenu(listView);
        new Task().execute((Void[]) null);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.listView, new PlaceholderFragment())
//                    .commit();
//
//
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_km_menu_buscar, menu);
        MenuItem searchItem = menu.findItem(R.id.buscarKm);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }
//
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        query = query.toLowerCase();
//        TextView text = (TextView) getSupportFragmentManager()
//                .findFragmentById(R.id.listView).getView()
//                .findViewById(R.id.search_result);
//        kms = new ArrayList<Map<String, Object>>();
//        List<Km> listaKms = dbAdapter.getAllKm();
//        for (Km km : listaKms) {
//            if (km.getId().equals(query)) {
//                String result = getString(R.string.results_found, query);
//                text.setText(result);
//                return true;
//            } else if (km.getData().equals(query)) {
//                String result = getString(R.string.results_found, query);
//                text.setText(result);
//                return true;
//            } else if (km.getItinerario().equals(query)) {
//                String result = getString(R.string.results_found, query);
//                text.setText(result);
//                return true;
//            } else if (km.getQtdCliente().equals(query)) {
//                String result = getString(R.string.results_found, query);
//                text.setText(result);
//                return true;
//            } else if (km.getKmInicial().equals(query)) {
//                String result = getString(R.string.results_found, query);
//                text.setText(result);
//                return true;
//            } else if (km.getKmFinal().equals(query)) {
//                String result = getString(R.string.results_found, query);
//                text.setText(result);
//                return true;
//            } else if (km.getKmTotal().equals(query)) {
//                String result = getString(R.string.results_found, query);
//                text.setText(result);
//                return true;
//            }
//        }
//        String result = getString(R.string.results_not_found, query);
//        text.setText(result);
//        return true;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        return false;
//    }
//    public static class PlaceholderFragment extends android.support.v4.app.Fragment {
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_lista_usuario, container, false);
//            return rootView;
//        }
//    }

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
            listView.setAdapter(simpleAdapter);
        }

    }

    private List<Map<String, Object>> listarKms() {
        kms = new ArrayList<Map<String, Object>>();
        List<Km> listaKms = dbAdapter.getAllKm();
        for (Km km : listaKms) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", km.getId());
            try {
                item.put("data", "Data: " + inverteOrdemData(km.getData()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            item.put("itinerario", "Itinerário: " + km.getItinerario());
            item.put("qtdCliente", "Cliente(s): " + km.getQtdCliente());
            item.put("kms", "Km Inicial: " + km.getKmInicial() + " Km" + " <---> " + "Km Final: " + km.getKmFinal() + " Km ");
            item.put("Total", "Total: " + km.getKmTotal() + " Km ");
            kms.add(item);

        }
        return kms;
    }

    private static String inverteOrdemData(String data) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(data);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        String _date = simpleDateFormat1.format(date);
        return _date;
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
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.Editar:
                int posicao = info.position;
                Map<String, Object> map = kms.get(posicao);
                Integer id = (Integer) map.get("id");
                Intent intent = new Intent(this, AlterarKm.class);
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
                                listView.invalidateViews();
                                data = "";
                                dbAdapter.deleteKm(Id);
                            }
                        })
                        .setNegativeButton(R.string.nao, null)
                        .show();
                break;
        }
        return super.onContextItemSelected(item);

    }
}

package com.example.fernando.controlekm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.*;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Flavia on 01/01/2018.
 */

public class ListUser extends ListActivity implements AdapterView.OnItemClickListener {

    private List<Map<String, Object>> usuarios;
    private DBAdapter dbAdapter;
    private String data;
    private String Id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = new DBAdapter(this);

        getListView().setOnItemClickListener(ListUser.this);

        registerForContextMenu(getListView());

        new Task().execute((Void[]) null);

    }

    private class Task extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        @Override
        protected List<Map<String, Object>> doInBackground(Void... voids) {
            return listarUsuario();
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> maps) {
            String[] de = {"id", "nome", "unidade", "funcao", "placa", "gerencia"};
            int[] para = {R.id.lstIdUser, R.id.lstNomeUser, R.id.lstUnidade, R.id.lstFuncao,
                    R.id.lstPlaca, R.id.lstGerencia};
            SimpleAdapter simpleAdapter = new SimpleAdapter(ListUser.this, maps, R.layout.listar_users, de, para);
            setListAdapter(simpleAdapter);
        }
    }

    private List<Map<String, Object>> listarUsuario() {

        usuarios = new ArrayList<Map<String, Object>>();
        List<Usuario> listaUsuario = dbAdapter.listaUsuario();
        for (Usuario usuario : listaUsuario) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", usuario.getId());
            item.put("nome", "Nome: " + usuario.getNome());
            item.put("unidade", "Unidade: " + usuario.getUnidade());
//            item.put("tipoVeiculo", "Veículo: " + usuario.getTipoVeiculo());
            item.put("funcao", "Função: " + usuario.getFuncao());
            item.put("placa", "Placa: " + usuario.getPlaca());
            item.put("gerencia", "Gerência: " + usuario.getGerencia());

            usuarios.add(item);

        }
        return usuarios;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Map<String, Object> map = usuarios.get(i);
        Integer codigo = (Integer) map.get("id");
        Id = String.valueOf(dbAdapter.returnIdUser(codigo));
        Toast.makeText(getBaseContext(), "Usuario selecionado: " + Id, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        setTitle(R.string.opcoes);
        menu.setHeaderTitle(R.string.opcoes);
        inflater.inflate(R.menu.lista_usuario_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mnRemover:
                new AlertDialog.Builder(this).setTitle("Deletando Usuário").
                        setMessage(R.string.confirmacao_exclusao_usuario).
                        setPositiveButton(R.string.sim,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        AdapterView.AdapterContextMenuInfo info =
                                                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                                        int position = info.position;
                                        usuarios.remove(position);
                                        getListView().invalidateViews();
                                        data = "";
                                        dbAdapter.deleteUser(Integer.valueOf(Id));
                                    }
                                })
                        .setNegativeButton(R.string.nao, null)
                        .show();
                break;
            case R.id.mnEditar:
                AdapterView.AdapterContextMenuInfo
                        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int posicao = info.position;
                Map<String, Object> map = usuarios.get(posicao);
                Integer id = (Integer) map.get("id");
                Intent intent = new Intent(this, AlterarUser.class);
                intent.putExtra("EXTRA_ID_USER", id);
                startActivity(intent);
                finish();
                break;
        }
        return super.onContextItemSelected(item);
    }
}

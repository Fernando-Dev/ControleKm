package com.example.fernando.controlekm;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
        String[] de = {"id", "nome", "unidade", "tipoVeiculo", "funcao", "placa", "gerencia"};
        int[] para = {R.id.lstIdUser, R.id.lstNomeUser, R.id.lstUnidade, R.id.lstTipoVeiculo, R.id.lstFuncao,
                R.id.lstPlaca, R.id.lstGerencia};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listarUsuario(), R.layout.listar_users, de, para);
        setListAdapter(simpleAdapter);
        getListView().setOnItemClickListener(ListUser.this);

        registerForContextMenu(getListView());

    }

    private List<Map<String, Object>> listarUsuario() {

        usuarios = new ArrayList<Map<String, Object>>();
        List<Usuario> listaUsuario = dbAdapter.listaUsuario();
        for (Usuario usuario : listaUsuario) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id",usuario.getId());
            item.put("nome",usuario.getNome());
            item.put("unidade",usuario.getUnidade());
            item.put("tipoVeiculo",usuario.getTipoVeiculo());
            item.put("funcao",usuario.getFuncao());
            item.put("placa",usuario.getPlaca());
            item.put("gerencia",usuario.getGerencia());

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
    public boolean onContextItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.mnRemover) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = info.position;
            usuarios.remove(position);
            getListView().invalidateViews();
            data = "";
            dbAdapter.deleteUser(Integer.valueOf(Id));
            return true;
        }
        if(item.getItemId()== R.id.mnEditar){
            AdapterView.AdapterContextMenuInfo info=
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int posicao = info.position;
            Map<String,Object> map = usuarios.get(posicao);
            Integer id = (Integer) map.get("id");
            Intent intent = new Intent(this,AlterarUser.class);
            intent.putExtra("EXTRA_ID_USER",id);
            startActivity(intent);

        }

        return super.onContextItemSelected(item);
    }
}

package com.example.fernando.controlekm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fernando.controlekm.BD.DatabaseHelper;
import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Usuario;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewUsuario extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView rv;
    private List<Usuario> usuarioList;
    private Cursor cursor;
    private GestureDetectorCompat detector;
    private DBAdapter db;
    private String data;
    private Integer Id;
    private SearchView searchView;
    private GridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_usuario);
        db = new DBAdapter(this);
        if (db.listaUsuario().isEmpty()) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Atenção")
                    .setMessage("Lista vazia! Por favor cadastre um usuário.")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .create()
                    .show();
        }

        rv = (RecyclerView) findViewById(R.id.rv);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        adapter = new GridAdapter(loadUsuarios());
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_usuario_menu_busca, menu);
        final MenuItem searchItem = menu.findItem(R.id.buscarUsuario);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        trocaCorTextoBuscado(searchView);
        searchView.setOnQueryTextListener(RecyclerViewUsuario.this);
        return true;
    }

    public List<Usuario> loadUsuarios() {
        usuarioList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = db.read();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM usuarios", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            Usuario usuario = new Usuario();
            Integer id = cursor.getInt(0);
            String nome = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_NOME));
            String unidade = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_UNIDADE));
            String funcao = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FUNCAO));
            String placa = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_PLACA));
            String gerencia = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_GERENCIA));
            usuario.setId(id);
            usuario.setNome(nome);
            usuario.setUnidade(unidade);
            usuario.setFuncao(funcao);
            usuario.setPlaca(placa);
            usuario.setGerencia(gerencia);
            usuarioList.add(usuario);
            cursor.moveToNext();
        }
        cursor.close();

        return usuarioList;
    }


    private void trocaCorTextoBuscado(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.GREEN);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    trocaCorTextoBuscado(viewGroup.getChildAt(i));
                }
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<Usuario> newList = new ArrayList<>();
        for (Usuario usuario : usuarioList) {
            String id = usuario.getId().toString().toLowerCase();
            String name = usuario.getNome().toLowerCase();
            String unidade = usuario.getUnidade().toLowerCase();
            String funcao = usuario.getFuncao().toLowerCase();
            String placa = usuario.getPlaca().toLowerCase();
            String gerencia = usuario.getGerencia().toLowerCase();
            if (id.contains(newText) || name.contains(newText) || unidade.contains(newText)
                    || funcao.contains(newText) || placa.contains(newText) || gerencia.contains(newText)) {
                newList.add(usuario);
            }
        }
        adapter.setFilter(newList);
        return true;
    }


    public static class GridViewHolder extends RecyclerView.ViewHolder {
        protected TextView id;
        protected TextView nome;
        protected TextView unidade;
        protected TextView funcao;
        protected TextView placa;
        protected TextView gerencia;
        protected CardView card;

        /*
        * diminui o numero de solicitacao atraves do metodo find view by id
        * melhorando o desempenho da aplicação
        * */
        public GridViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.rvIdUser);
            nome = (TextView) itemView.findViewById(R.id.rvNomeUser);
            unidade = (TextView) itemView.findViewById(R.id.rvUnidade);
            funcao = (TextView) itemView.findViewById(R.id.rvFuncao);
            placa = (TextView) itemView.findViewById(R.id.rvPlaca);
            gerencia = (TextView) itemView.findViewById(R.id.rvGerencia);
            card = (CardView) itemView.findViewById(R.id.card);


        }
    }

    public class GridAdapter extends RecyclerView.Adapter<RecyclerViewUsuario.GridViewHolder> {
        private List<Usuario> usuarioList;

        public GridAdapter(List<Usuario> usuarioList) {
            this.usuarioList = usuarioList;
        }


        /*devolve a quantidade de elementos*/
        @Override
        public int getItemCount() {
            return usuarioList.size();
        }

        //        metodo para atribuir o filtro
        public void setFilter(List<Usuario> list) {
            usuarioList = new ArrayList<>();
            usuarioList.addAll(list);
            notifyDataSetChanged();
        }


        /*
        * metodo chamado quando e necessario criar um novo item na recycler view
        * */
        @Override
        public RecyclerViewUsuario.GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_recyclerview_usuario, parent, false);
            GridViewHolder holder = new GridViewHolder(view);
            return holder;
        }

        /*
        * metodo chamado quando um item precisa ser exibido
        * */
        @Override
        public void onBindViewHolder(final RecyclerViewUsuario.GridViewHolder holder, final int position) {

            holder.id.setText(String.valueOf(usuarioList.get(position).getId()));
            holder.nome.setText("Nome: " + usuarioList.get(position).getNome());
            holder.unidade.setText("Unidade: " + usuarioList.get(position).getUnidade());
            holder.funcao.setText("Função: " + usuarioList.get(position).getFuncao());
            holder.placa.setText("Placa: " + usuarioList.get(position).getPlaca());
            holder.gerencia.setText("Gerência: " + usuarioList.get(position).getGerencia());
//            codigo para chamar o menupopup na recyclerview
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(RecyclerViewUsuario.this, view);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.lista_usuario_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.mnEditar:
                                    Integer id = (usuarioList.get(position).getId());
                                    Intent intent = new Intent(RecyclerViewUsuario.this, AlterarUser.class);
                                    intent.putExtra("EXTRA_ID_USUARIO", id);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case R.id.mnRemover:
                                    new AlertDialog.Builder(RecyclerViewUsuario.this).setTitle("Deletando Usuário").
                                            setMessage(R.string.confirmacao_exclusao_usuario).
                                            setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Integer id = (usuarioList.get(position).getId());
                                                    usuarioList.remove(position);
                                                    rv.invalidate();
                                                    data = "";
                                                    db.deleteUser(id);
                                                    notifyDataSetChanged();
                                                }
                                            })
                                            .setNegativeButton(R.string.nao, null)
                                            .show();
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });
        }
    }
}

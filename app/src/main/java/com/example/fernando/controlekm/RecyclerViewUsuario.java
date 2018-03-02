package com.example.fernando.controlekm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Km;
import com.example.fernando.controlekm.dominio.Usuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RecyclerViewUsuario extends ActionBarActivity {
    private RecyclerView rv;
    private List<Map<String, Object>> usuarios;
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
                    .setTitle("Atenção")
                    .setMessage("Lista vazia! Por favor cadastre um usuário.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

        adapter = new GridAdapter(listarUsuarios());
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_usuario_menu_busca, menu);
        final MenuItem searchItem = menu.findItem(R.id.buscarUsuario);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        trocaCorTextoBuscado(searchView);
        return true;
    }

    private List<Map<String, Object>> filter(List<Map<String, Object>> user, String query) {
        usuarios = new ArrayList<Map<String, Object>>();
        query = query.toLowerCase();
        List<Usuario> filterUser = db.listaUsuario();
        for (Usuario usuario : filterUser) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("nome", usuario.getNome());
            String text = (String) item.get("nome");
            text = text.toLowerCase();
            if (text.startsWith(query)) {
                usuarios.add(item);
            }
        }
        return usuarios;
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


    private List<Map<String, Object>> listarUsuarios() {

        usuarios = new ArrayList<Map<String, Object>>();
        List<Usuario> listaUsuario = db.listaUsuario();
        for (Usuario usuario : listaUsuario) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", usuario.getId());
            item.put("nome", "Nome: " + usuario.getNome());
            item.put("unidade", "Unidade: " + usuario.getUnidade());
            item.put("funcao", "Função: " + usuario.getFuncao());
            item.put("placa", "Placa: " + usuario.getPlaca());
            item.put("gerencia", "Gerência: " + usuario.getGerencia());

            usuarios.add(item);
        }
        return usuarios;
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
        private List<Map<String, Object>> usuarios;

        public GridAdapter(List<Map<String, Object>> usuarios) {
            this.usuarios = usuarios;
        }


        /*devolve a quantidade de elementos*/
        @Override
        public int getItemCount() {
            return usuarios.size();
        }

        //        metodo para atribuir o filtro
        public void setFilter(List<Map<String, Object>> usuarioList) {
            usuarios = new ArrayList<Map<String, Object>>();
            usuarios.addAll(usuarioList);
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
            Map<String, Object> map = usuarios.get(position);
            holder.id.setText(String.valueOf(map.get("id")));
            holder.nome.setText((String) map.get("nome"));
            holder.unidade.setText((String) map.get("unidade"));
            holder.placa.setText((String) (map.get("funcao")));
            holder.funcao.setText((String) map.get("placa"));
            holder.gerencia.setText((String) map.get("gerencia"));

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
                                    Map<String, Object> map = usuarios.get(holder.getAdapterPosition());
                                    Integer id = (Integer) map.get("id");
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
                                                    Map<String, Object> map = usuarios.get(holder.getAdapterPosition());
                                                    Integer id = (Integer) map.get("id");
                                                    usuarios.remove(holder.getAdapterPosition());
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

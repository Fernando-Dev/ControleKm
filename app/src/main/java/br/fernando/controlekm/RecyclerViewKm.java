package br.fernando.controlekm;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import br.fernando.controlekm.BD.DatabaseHelper;
import br.fernando.controlekm.DAO.DBAdapter;

import br.fernando.controlekm.R;

import br.fernando.controlekm.dominio.Km;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerViewKm extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView rv;
    private List<Km> kmList;
    private Cursor cursor;
    private GridAdapter adapter;
    private SearchView searchView;
    private DBAdapter db;
    private String data;
    private Integer Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_km);
        db = new DBAdapter(this);
        if (db.getAllKm().isEmpty()) {
            final Dialog dialog = new Dialog(this, R.style.DialogoSemTitulo);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_alert_dialog_aviso);
            dialog.setCancelable(false);
            TextView txtMsgem = dialog.findViewById(R.id.mensagemAlertDialogAviso);
            txtMsgem.setText(R.string.lista_vazia_km);
            Button btnOK = dialog.findViewById(R.id.btnAlertDialogAvisoOK);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            dialog.show();
        }

        rv = (RecyclerView) findViewById(R.id.rv);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        adapter = new GridAdapter(loadKM());
        rv.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_km_menu_buscar, menu);
        MenuItem searchItem = menu.findItem(R.id.buscarKm);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        trocaCorTextoBuscado(searchView);
        searchView.setOnQueryTextListener(RecyclerViewKm.this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<Km> newList = new ArrayList<>();
        for (Km km : kmList) {
            String id = km.getId().toString().toLowerCase();
            String data = km.getData().toLowerCase();
            String itinerario = km.getItinerario().toLowerCase();
            String qtdClientes = km.getQtdCliente().toString().toLowerCase();
            String kmInicial = km.getKmInicial().toLowerCase();
            String kmFinal = km.getKmFinal().toLowerCase();
            String kmTotal = km.getKmTotal().toLowerCase();
            if (id.contains(newText) || data.contains(newText) || itinerario.contains(newText)
                    || qtdClientes.contains(newText) || kmInicial.contains(newText) || kmFinal.contains(newText)
                    || kmTotal.contains(newText)) {
                newList.add(km);
            }
        }
        adapter.setFilter(newList);
        return true;
    }


    private List<Km> loadKM() {
        kmList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = db.read();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM kms", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            Km km = new Km();
            Integer id = cursor.getInt(0);
            String data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATA));
            String itinerario = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ITINERARIO));
            Integer qtdCliente = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_QTD_CLIENTE));
            String kmInicial = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_KM_INICIAL));
            String kmFinal = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_KM_FINAL));
            String kmTotal = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_KM_TOTAL));
            km.setId(id);
            try {
                data = inverteOrdemData(data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            km.setData(data);
            km.setItinerario(itinerario);
            km.setQtdCliente(qtdCliente);
            km.setKmInicial(kmInicial);
            km.setKmFinal(kmFinal);
            km.setKmTotal(kmTotal);
            kmList.add(km);
            cursor.moveToNext();
        }
        cursor.close();

        return kmList;


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

    private static String inverteOrdemData(String data) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(data);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        String _date = simpleDateFormat1.format(date);
        return _date;
    }


    public static class GridViewHolder extends RecyclerView.ViewHolder {
        protected TextView id;
        protected TextView data;
        protected TextView itinerario;
        protected TextView qtdCliente;
        protected TextView kmInicial;
        protected TextView kmFinal;
        protected TextView kmTotal;
        protected CardView card;

        /*
        * diminui o numero de solicitacao atraves do metodo find view by id
        * melhorando o desempenho da aplicação
        * */
        public GridViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.rvIdKm);
            data = (TextView) itemView.findViewById(R.id.rvDataKm);
            itinerario = (TextView) itemView.findViewById(R.id.rvItinerario);
            qtdCliente = (TextView) itemView.findViewById(R.id.rvQtdCliente);
            kmInicial = (TextView) itemView.findViewById(R.id.rvKmInicial);
            kmFinal = (TextView) itemView.findViewById(R.id.rvKmFinal);
            kmTotal = (TextView) itemView.findViewById(R.id.rvKmTotal);
            card = (CardView) itemView.findViewById(R.id.card_view);

        }
    }

    /*
    * metodo de criacao de um adapter para recycler view
    * */
    public class GridAdapter extends RecyclerView.Adapter<GridViewHolder> {
        private List<Km> kmList;

        public GridAdapter(List<Km> kmList) {
            this.kmList = kmList;
        }


        /*devolve a quantidade de elementos*/
        @Override
        public int getItemCount() {
            return kmList.size();
        }


        //        metodo para atribuir o filtro
        public void setFilter(List<Km> list) {
            kmList = new ArrayList<>();
            kmList.addAll(list);
            notifyDataSetChanged();
        }

        /*
        * metodo chamado quando e necessario criar um novo item na recycler view
        * */
        @Override
        public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_recyclerview_km, parent, false);
            GridViewHolder holder = new GridViewHolder(view);
            return holder;
        }

        /*
        * metodo chamado quando um item precisa ser exibido
        * */
        @Override
        public void onBindViewHolder(final GridViewHolder holder, final int position) {

            holder.id.setText(String.valueOf(kmList.get(position).getId()));
            holder.data.setText("Data: " + kmList.get(position).getData());
            holder.itinerario.setText("Itinerário: " + kmList.get(position).getItinerario());
            holder.qtdCliente.setText("Clientes: " + String.valueOf(kmList.get(position).getQtdCliente()));
            holder.kmInicial.setText("Km Inicial: " + kmList.get(position).getKmInicial() + " Km " + "<-->");
            holder.kmFinal.setText("Km Final: " + kmList.get(position).getKmFinal() + " Km ");
            holder.kmTotal.setText("Distância Percorrida: " + kmList.get(position).getKmTotal());

//            codigo para chamar o menupopup na recyclerview
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(RecyclerViewKm.this, view);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.list_km_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.Editar:
                                    Integer id = (kmList.get(position).getId());
                                    Intent intent = new Intent(RecyclerViewKm.this, AlterarKm.class);
                                    intent.putExtra("EXTRA_ID_KM", id);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case R.id.Remover:
                                    final Dialog dialog = new Dialog(RecyclerViewKm.this, R.style.DialogoSemTitulo);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.layout_alert_dialog_menu_delete);
                                    dialog.setCancelable(false);
                                    TextView txtTitulo = dialog.findViewById(R.id.tituloalertDialogDelete);
                                    txtTitulo.setText(R.string.deletar_km);
                                    TextView txtMsgem = dialog.findViewById(R.id.mensagemAlertDialogDelete);
                                    txtMsgem.setText(R.string.confirmacao_exclusao_km);
                                    Button btnSim = dialog.findViewById(R.id.btnAlertDialogDeleteSim);
                                    Button btnNao = dialog.findViewById(R.id.btnAlertDialogDeleteNao);
                                    btnSim.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Integer id = (kmList.get(position).getId());
                                            kmList.remove(position);
                                            rv.invalidate();
                                            data = "";
                                            db.deleteKm(id);
                                            notifyDataSetChanged();
                                            dialog.dismiss();
                                        }
                                    });
                                    btnNao.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
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

package com.example.fernando.controlekm;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.fernando.controlekm.DAO.DBAdapter;
import com.example.fernando.controlekm.dominio.Km;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewKm extends ActionBarActivity implements RecyclerView.OnItemTouchListener {
    private RecyclerView rv;
    private List<Map<String, Object>> kms;
    private GestureDetectorCompat detector;
    private DBAdapter db;
    private String data;
    private Integer Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_km);
        db = new DBAdapter(this);

        rv = (RecyclerView) findViewById(R.id.rv);
        detector = new GestureDetectorCompat(this, new GridGestureDetector());
        rv.addOnItemTouchListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        GridAdapter adapter = new GridAdapter(listarKms());
        rv.setAdapter(adapter);

    }


    private List<Map<String, Object>> listarKms() {
        kms = new ArrayList<Map<String, Object>>();
        List<Km> listaKms = db.getAllKm();
        for (Km km : listaKms) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", km.getId());
            try {
                item.put("data", "Data: " + inverteOrdemData(km.getData()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            item.put("itinerario", "Itinerário: " + km.getItinerario());
            item.put("qtdCliente", "Quant. Clientes: " + km.getQtdCliente());
            item.put("kmInicial", "Km Inicial: " + km.getKmInicial() + " Km ");
            item.put("kmFinal", "<--> Km Final: " + km.getKmFinal() + " Km ");
            item.put("kmTotal", "Distância Total: " + km.getKmTotal() + " Km ");
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
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        detector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class GridGestureDetector extends GestureDetector.SimpleOnGestureListener {
        /*
        * verificamos qual view se
        encontra nas coordenadas X e Y do evento de toque disparado pelo
        usuário, e posteriormente buscamos qual é a posição na grade do
        item que foi selecionado. Uma vez que temos a posição do item,
        basta obtê-lo da nossa lista de livros.
        * */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View v = rv.findChildViewUnder(e.getX(), e.getY());
            int i = rv.getChildPosition(v);
            Map<String, Object> km = kms.get(i);
            Id = (Integer) km.get("id");
            Toast.makeText(RecyclerViewKm.this, "Km selecionado: " + km.get("id"), Toast.LENGTH_SHORT).show();
            return super.onSingleTapConfirmed(e);
        }
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
        private List<Map<String, Object>> kms;

        public GridAdapter(List<Map<String, Object>> kms) {
            this.kms = kms;
        }


        /*devolve a quantidade de elementos*/
        @Override
        public int getItemCount() {
            return kms.size();
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
            Map<String, Object> km = kms.get(position);
            holder.id.setText(String.valueOf(km.get("id")));
            holder.data.setText((String) km.get("data"));
            holder.itinerario.setText((String) km.get("itinerario"));
            holder.qtdCliente.setText(String.valueOf(km.get("qtdCliente")));
            holder.kmInicial.setText((String) km.get("kmInicial"));
            holder.kmFinal.setText((String) km.get("kmFinal"));
            holder.kmTotal.setText((String) km.get("kmTotal"));

//            codigo para chamar o menupopup na recyclerview
            holder.card.setOnLongClickListener(new View.OnLongClickListener() {
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
                                    Map<String, Object> map = kms.get(position);
                                    Integer id = (Integer) map.get("id");
                                    Intent intent = new Intent(RecyclerViewKm.this, AlterarKm.class);
                                    intent.putExtra("EXTRA_ID_KM", id);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case R.id.Remover:
                                    new AlertDialog.Builder(RecyclerViewKm.this).setTitle("Deletando Km").
                                            setMessage(R.string.confirmacao_exclusao_km).
                                            setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    kms.remove(position);
                                                    rv.invalidate();
                                                    data = "";
                                                    db.deleteKm(Id);
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

package br.fernando.controlekm0.ui;

/**
 * Created by Flavia on 01/01/2018.
 */
/*
public class ListUser extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private List<Map<String, Object>> usuarios;
    private DBAdapter dbAdapter;
    private String data;
    private String Id;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consulta_user);

        dbAdapter = new DBAdapter(this);

        listView = (ListView) findViewById(R.id.lv);

        listView.setOnItemClickListener(ListUser.this);

        registerForContextMenu(listView);

        new Task().execute((Void[]) null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_usuario_menu_busca, menu);
        MenuItem searchItem = menu.findItem(R.id.buscarUsuario);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
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
            listView.setAdapter(simpleAdapter);
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
                                        listView.invalidateViews();
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
*/
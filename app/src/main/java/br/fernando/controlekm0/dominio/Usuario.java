package br.fernando.controlekm0.dominio;

/**
 * Created by Flavia on 25/12/2017.
 */

public class Usuario {
    private Integer id;
    private String nome;
    private String unidade;
    private String funcao;
    private String placa;
    private String gerencia;

    public Usuario(){

    }

    public Usuario(Integer id, String nome, String unidade, String funcao, String placa, String gerencia) {
        this.id = id;
        this.nome = nome;
        this.unidade = unidade;
        this.funcao = funcao;
        this.placa = placa;
        this.gerencia = gerencia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getGerencia() {
        return gerencia;
    }

    public void setGerencia(String gerencia) {
        this.gerencia = gerencia;
    }
}

package br.fernando.controlekm0.dominio;

/**
 * Created by Flavia on 18/03/2018.
 */

public class Manutencao {
    private Integer idManutencao;
    private Integer kmManutencao;
    private String dataManutencao;
    private Integer distanciaManutencao;
    private Integer kmProximaManutencao;

    public Manutencao(Integer idManutencao, Integer kmManutencao, String dataManutencao,Integer distanciaManutencao, Integer kmProximaManutencao) {
        this.idManutencao = idManutencao;
        this.kmManutencao = kmManutencao;
        this.dataManutencao = dataManutencao;
        this.distanciaManutencao = distanciaManutencao;
        this.kmProximaManutencao = kmProximaManutencao;
    }

    public Manutencao() {
    }

    public Integer getDistanciaManutencao() {
        return distanciaManutencao;
    }

    public void setDistanciaManutencao(Integer distanciaManutencao) {
        this.distanciaManutencao = distanciaManutencao;
    }

    public Integer getIdManutencao() {
        return idManutencao;
    }

    public void setIdManutencao(Integer idManutencao) {
        this.idManutencao = idManutencao;
    }

    public Integer getKmManutencao() {
        return kmManutencao;
    }

    public void setKmManutencao(Integer kmManutencao) {
        this.kmManutencao = kmManutencao;
    }

    public String getDataManutencao() {
        return dataManutencao;
    }

    public void setDataManutencao(String dataManutencao) {
        this.dataManutencao = dataManutencao;
    }

    public Integer getKmProximaManutencao() {
        return kmProximaManutencao;
    }

    public void setKmProximaManutencao(Integer kmProximaManutencao) {
        this.kmProximaManutencao = kmProximaManutencao;
    }
}

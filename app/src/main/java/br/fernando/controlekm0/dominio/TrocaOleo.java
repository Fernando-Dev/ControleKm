package br.fernando.controlekm0.dominio;

/**
 * Created by Flavia on 17/03/2018.
 */

public class TrocaOleo {
    private Integer idTroca;
    private Integer kmTroca;
    private String dataTroca;
    private Integer vidaUtilOleo;
    private Integer proximaTroca;

    public TrocaOleo(Integer idTroca, Integer kmTroca, String dataTroca, Integer vidaUtilOleo, Integer proximaTroca) {
        this.idTroca = idTroca;
        this.kmTroca = kmTroca;
        this.dataTroca = dataTroca;
        this.vidaUtilOleo = vidaUtilOleo;
        this.proximaTroca = proximaTroca;
    }

    public TrocaOleo() {
    }

    public Integer getProximaTroca() {
        return proximaTroca;
    }

    public void setProximaTroca(Integer proximaTroca) {
        this.proximaTroca = proximaTroca;
    }

    public Integer getIdTroca() {
        return idTroca;
    }

    public void setIdTroca(Integer idTroca) {
        this.idTroca = idTroca;
    }

    public Integer getKmTroca() {
        return kmTroca;
    }

    public void setKmTroca(Integer kmTroca) {
        this.kmTroca = kmTroca;
    }

    public String getDataTroca() {
        return dataTroca;
    }

    public void setDataTroca(String dataTroca) {
        this.dataTroca = dataTroca;
    }

    public Integer getVidaUtilOleo() {
        return vidaUtilOleo;
    }

    public void setVidaUtilOleo(Integer vidaUtilOleo) {
        this.vidaUtilOleo = vidaUtilOleo;
    }
}

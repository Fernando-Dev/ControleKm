package com.example.fernando.controlekm.dominio;

import java.util.Date;

/**
 * Created by Flavia on 17/03/2018.
 */

public class TrocaOleo {
    private Integer idTroca;
    private Integer kmTroca;
    private String dataTroca;
    private Integer vidaUtilOleo;

    public TrocaOleo(Integer idTroca, Integer kmTroca, String dataTroca, Integer vidaUtilOleo) {
        this.idTroca = idTroca;
        this.kmTroca = kmTroca;
        this.dataTroca = dataTroca;
        this.vidaUtilOleo = vidaUtilOleo;
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

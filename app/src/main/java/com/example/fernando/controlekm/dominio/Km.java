package com.example.fernando.controlekm.dominio;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Flavia on 27/07/2017.
 */

public class Km {
    private Integer id;
    private Date data;
    private String itinerario;
    private String kmInicial;
    private String kmFinal;
    private String kmTotal;


    public Km() {

    }

    public Km(Integer id, Date data, String itinerario, String kmInicial, String kmFinal/*, String kmTotal*/) {
        this.id = id;
        this.data = data;
        this.itinerario = itinerario;
        this.kmInicial = kmInicial;
        this.kmFinal = kmFinal;
//        this.kmTotal = kmTotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getItinerario() {
        return itinerario;
    }

    public void setItinerario(String itinerario) {
        this.itinerario = itinerario;
    }

    public String getKmInicial() {
        return kmInicial;
    }

    public void setKmInicial(String kmInicial) {
        this.kmInicial = kmInicial;
    }

    public String getKmFinal() {
        return kmFinal;
    }

    public void setKmFinal(String kmFinal) {
        this.kmFinal = kmFinal;
    }

    /*public String getKmTotal() {
        return kmTotal;
    }

    public void setKmTotal(String kmTotal) {
        this.kmTotal = kmTotal;
    }*/

    @Override
    public String toString() {
        return "Data: " + data + "Itinerário: " + itinerario + "Km inicial: " + kmInicial
                + " a " + "Km final: " + kmFinal;


    }
}

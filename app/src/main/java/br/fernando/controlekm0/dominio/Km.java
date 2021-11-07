package br.fernando.controlekm0.dominio;

import java.io.Serializable;

/**
 * Created by Flavia on 27/07/2017.
 */

public class Km implements Serializable {
    private Integer id;
    private String data;
    private String itinerario;
    private Integer qtdCliente;
    private String kmInicial;
    private String kmFinal;
    private String kmTotal;


    public Km() {

    }

    public Km(Integer id, String data, String itinerario, Integer qtdCliente,
              String kmInicial, String kmFinal, String kmTotal) {
        this.id = id;
        this.data = data;
        this.itinerario = itinerario;
        this.qtdCliente = qtdCliente;
        this.kmInicial = kmInicial;
        this.kmFinal = kmFinal;
        this.kmTotal = kmTotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getItinerario() {
        return itinerario;
    }

    public void setItinerario(String itinerario) {
        this.itinerario = itinerario;
    }

    public Integer getQtdCliente() {
        return qtdCliente;
    }

    public void setQtdCliente(Integer qtdCliente) {
        this.qtdCliente = qtdCliente;
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

    public String getKmTotal() {
        return kmTotal;
    }

    public void setKmTotal(String kmTotal) {
        this.kmTotal = kmTotal;
    }

    @Override
    public String toString() {
        return "Data: " + data + "Itinerário: " + itinerario + "Km inicial: " + kmInicial
                + " a " + "Km final: " + kmFinal;


    }
}

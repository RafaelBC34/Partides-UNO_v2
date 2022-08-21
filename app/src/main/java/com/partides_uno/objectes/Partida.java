package com.partides_uno.objectes;

/**
 * Created by Rafael on 27/01/2018.
 */

public class Partida {

    private String id, nom, limitPunts, data, guanyador, activa;

    public Partida(String id, String nom, String limitPunts, String data, String activa) {
        this.id = id;
        this.nom = nom;
        this.limitPunts = limitPunts;
        this.data = data;
        this.guanyador = "";
        this.activa = activa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLimitPunts() {
        return limitPunts;
    }

    public void setLimitPunts(String limitPunts) {
        this.limitPunts = limitPunts;
    }

    public String getGuanyador() {
        return guanyador;
    }

    public void setGuanyador(String guanyador) {
        this.guanyador = guanyador;
    }

    public String isActiva() {
        return activa;
    }

    public void setActiva(String activa) {
        this.activa = activa;
    }
}

package com.partides_uno.objectes;

/**
 * Created by Rafael on 27/01/2018.
 */

public class Jugador {

    private String id, nom, punts, idPartida;

    public Jugador(String id, String nom, String punts, String idPartida) {
        this.id = id;
        this.nom = nom;
        this.punts = punts;
        this.idPartida = idPartida;
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

    public String getPunts() {
        return punts;
    }

    public void setPunts(String punts) {
        this.punts = punts;
    }

    public String getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(String idPartida) {
        this.idPartida = idPartida;
    }
}

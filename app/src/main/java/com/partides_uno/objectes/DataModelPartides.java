package com.partides_uno.objectes;

/**
 * Created by Rafael on 29/01/2018.
 */

public class DataModelPartides {

    private String nom, punts, data, guanyador;

    public DataModelPartides(String nom, String punts, String data, String guanyador) {
        this.nom = nom;
        this.punts = punts;
        this.data = data;
        if (guanyador == null) {
            this.guanyador = "Sense guanyador";
        } else {
            this.guanyador = guanyador;
        }
    }

    public String getNom() {
        return nom;
    }

    public String getPunts() {
        return punts;
    }

    public String getData() {
        return data;
    }

    public String getGuanyador() {
        return guanyador;
    }
}

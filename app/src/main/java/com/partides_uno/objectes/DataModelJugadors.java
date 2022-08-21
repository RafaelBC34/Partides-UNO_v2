package com.partides_uno.objectes;

/**
 * Created by Rafael on 28/01/2018.
 */

public class DataModelJugadors {

    private String nom, punts;

    public DataModelJugadors(String nom, String punts) {
        this.nom = nom;
        this.punts = punts;
    }

    public String getNom() {
        return nom;
    }

    public String getPunts() {
        return punts;
    }
}

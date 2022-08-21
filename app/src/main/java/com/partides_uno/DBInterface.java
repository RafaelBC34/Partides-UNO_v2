package com.partides_uno;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.partides_uno.objectes.Jugador;
import com.partides_uno.objectes.Partida;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafael on 24/01/2018.
 */

public class DBInterface {

    //Constants

    private static final String TAG = "DBInterface";

    private static final String BD_NOM = "BDPartidesUNO";
    private static final String BD_TAULA_PARTIDES = "partides";
    private static final String BD_TAULA_JUGADORS = "jugadors";

    //Camps comuns entre les dues taules
    private static final String CLAU_ID = "_id";
    private static final String CLAU_NOM = "nom";
    private static final String CLAU_PUNTS = "punts";

    //Camps de la taula Partides
    private static final String CLAU_DATA = "data";
    private static final String CLAU_PARTIDA_ACTIVA = "activa";
    private static final String CLAU_LIMIT_PUNTS = "limit_punts";
    private static final String CLAU_GUANYADOR = "guanyador";

    //Camps de la taula Jugadors
    private static final String CLAU_ID_PARTIDA = "partida_id";


    private static final int VERSIO = 1;

    private static final String BD_CREATE_PARTIDES =
            "create table " + BD_TAULA_PARTIDES + "( " +
                    CLAU_ID + " integer primary key autoincrement, " +
                    CLAU_NOM + " text not null, " +
                    CLAU_LIMIT_PUNTS + " integer not null, " +
                    CLAU_DATA + " text not null, " +
                    CLAU_GUANYADOR + " text, " +
                    CLAU_PARTIDA_ACTIVA +  " boolean);";

    private static final String BD_CREATE_JUGADORS =
            "create table " + BD_TAULA_JUGADORS + "( " + CLAU_ID + " integer primary key autoincrement, " +
                    CLAU_NOM + " text not null, " +
                    CLAU_PUNTS + " integer not null, " +
                    CLAU_ID_PARTIDA + " integer not null);";

    private final Context context;
    private AjudaBD ajuda;
    private SQLiteDatabase bd;

    public DBInterface(Context con) {
        this.context = con;
        ajuda = new AjudaBD(context);
    }

    //Obre la BD
    public DBInterface obre() throws SQLException {
        bd = ajuda.getWritableDatabase();
        return this;
    }

    //Tanca la BD
    public void tanca() {
        ajuda.close();
    }

    //Insereix una partida
    public long insereixPartida(String nom, Integer limitPunts, String data) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CLAU_NOM, nom);
        initialValues.put(CLAU_LIMIT_PUNTS, limitPunts);
        initialValues.put(CLAU_DATA, data);
        initialValues.put(CLAU_PARTIDA_ACTIVA, true);
        return bd.insert(BD_TAULA_PARTIDES, null, initialValues);
    }

    //Retorna true si hi ha cap partida acabada
    public boolean hiHaPartidesAcabades() {
        Cursor c = bd.query(BD_TAULA_PARTIDES, new String[]{CLAU_ID}, CLAU_PARTIDA_ACTIVA + " = 0", null, null, null, null);
        if (c.moveToFirst()) {
            return true;
        }
        return false;
    }

    //Retorna una llista amb totes les partides acabades
    public List<Partida> obtenirTotesLesPartidesAcabades() {
        Cursor c = bd.query(BD_TAULA_PARTIDES,
                new String[]{CLAU_ID, CLAU_NOM, CLAU_LIMIT_PUNTS, CLAU_DATA, CLAU_GUANYADOR, CLAU_PARTIDA_ACTIVA},
                CLAU_PARTIDA_ACTIVA + " = 0", null, null, null, null);

        if (c.moveToFirst()) {
            List<Partida> llistatPartides = new ArrayList<>();
            do {
                Partida partida = new Partida(c.getString(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(5));
                partida.setGuanyador(c.getString(4));
                llistatPartides.add(partida);
            } while (c.moveToNext());
            c.close();
            return llistatPartides;
        }
        return null;
    }

    //Retorna la partida activa, si n'hi ha cap
    public Partida obtenirPartidaActiva() {
        Cursor c = bd.query(BD_TAULA_PARTIDES,
                new String[]{CLAU_ID, CLAU_NOM, CLAU_LIMIT_PUNTS, CLAU_DATA, CLAU_PARTIDA_ACTIVA},
                CLAU_PARTIDA_ACTIVA + " = 1", null, null, null, null);

        if (c.moveToFirst()) {
            Partida partida = new Partida(c.getString(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4));
            c.close();
            return partida;
        }
        return null;
    }

    //Retorna una partida segons la seva _id
    public Partida obtenirPartida(String idPartida) {
        Cursor c = bd.query(BD_TAULA_PARTIDES, new String[]{CLAU_ID, CLAU_NOM, CLAU_LIMIT_PUNTS, CLAU_DATA, CLAU_PARTIDA_ACTIVA},
                CLAU_ID + " = " + idPartida, null, null, null, null);
        if (c.moveToFirst()) {
            //return c.getString(0) + c.getString(1) + c.getString(2) + c.getString(3) + c.getString(4);
            return new Partida(c.getString(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4));
        }
        c.close();
        return null;
    }

    //Posa un guanyador a la partida i la tanca
    public boolean tancaIFesGuanyador(String idPartida, String nomGuanyador) {
        ContentValues args = new ContentValues();
        args.put(CLAU_GUANYADOR, nomGuanyador);
        args.put(CLAU_PARTIDA_ACTIVA, false);
        return bd.update(BD_TAULA_PARTIDES, args, CLAU_ID + " = " + idPartida, null) > 0;
    }

    //Tanca la partida actual
    public boolean tancaPartida(String idPartida) {
        ContentValues args = new ContentValues();
        args.put(CLAU_PARTIDA_ACTIVA, false);
        return bd.update(BD_TAULA_PARTIDES, args, CLAU_ID + " = " + idPartida, null) > 0;
    }

    //Insereix un jugador
    public long insereixJugador(String nom, Integer punts, long idPartida) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CLAU_NOM, nom);
        initialValues.put(CLAU_PUNTS, punts);
        initialValues.put(CLAU_ID_PARTIDA, idPartida);
        return bd.insert(BD_TAULA_JUGADORS, null, initialValues);
    }

    //Retorna tots els jugadors d'una partida donada
    public List<Jugador> obtenirTotsElsJugadorsDeLaPartida(String idPartida) {
        Cursor c = bd.query(BD_TAULA_JUGADORS, new String[]{CLAU_ID, CLAU_NOM, CLAU_PUNTS, CLAU_ID_PARTIDA},
                CLAU_ID_PARTIDA + " = " + idPartida, null, null, null, null);
        if (c.moveToFirst()) {
            List<Jugador> llistatJugadors = new ArrayList<>();
            do {
                Jugador jugador = new Jugador(c.getString(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3));
                llistatJugadors.add(jugador);
            } while (c.moveToNext());
            c.close();
            return llistatJugadors;
        }
        return null;
    }

    //Suma punts a un jugador
    public boolean sumaPuntsJugador(String idJugador, Integer punts) {
        ContentValues args = new ContentValues();
        args.put(CLAU_PUNTS, punts);
        return bd.update(BD_TAULA_JUGADORS, args, CLAU_ID + " = " + idJugador, null) > 0;
    }

    //Esborra un jugador
    public boolean esborraJugador(String idJugador) {
        return bd.delete(BD_TAULA_JUGADORS, CLAU_ID + " = " + idJugador, null) > 0;
    }


    private static class AjudaBD extends SQLiteOpenHelper {
        AjudaBD(Context con) {
            super(con, BD_NOM, null, VERSIO);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(BD_CREATE_PARTIDES);
                db.execSQL(BD_CREATE_JUGADORS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int VersioAntiga, int VersioNova) {
            Log.w(TAG, "Actualitzant Base de dades de la versió" + VersioAntiga + " a " + VersioNova + ". Destruirà totes les dades");
            db.execSQL("DROP TABLE IF EXISTS " + BD_TAULA_PARTIDES);
            db.execSQL("DROP TABLE IF EXISTS " + BD_TAULA_JUGADORS);

            db.execSQL(BD_CREATE_PARTIDES);
            db.execSQL(BD_CREATE_JUGADORS);

            onCreate(db);
        }
    }
}

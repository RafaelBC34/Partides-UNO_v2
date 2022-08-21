package com.partides_uno.objectes;

import java.util.ArrayList;
import java.util.List;

public class GestorJugadorsPuntuacions {

    public GestorJugadorsPuntuacions() {
    }

    /** Obté el nombre de jugadors que està per sota del límit de punts
     *
     * @param jugadors El llistat de jugadors
     * @param limitPunts La puntuació límit
     * @return El nombre de jugadors
     */
    public int getNumJugadorsPerSotaDelLimitDePunts(List<Jugador> jugadors, int limitPunts) {
        int n = 0;

        //System.out.println("Jugadors per sota del límit de punts (" + puntuacio + "):");
        for (Jugador jugador : jugadors) {
            int puntsJugador = Integer.parseInt(jugador.getPunts());

            if (puntsJugador < limitPunts) {
                //System.out.println("idJugador: " + jugador.getId() + "; punts: " + jugador.getPunts());
                n++;
            }
        }

        //System.out.println("Total: " + n);
        return n;
    }

    /** Retorna l'objecte jugador amb la puntuació més baixa.
     * És útil fer-lo servir quan se sap prèviament que només hi ha un jugador amb la puntuació més baixa.
     *
     * @param jugadors El llistat de jugadors
     * @return Jugador jugador
     */
    public Jugador getJugadorAmbPuntuacioMesBaixa(List<Jugador> jugadors) {
        Jugador j = null;
        int menorPuntuacio = getPuntuacioMesBaixaActual(jugadors);

        // Copia de l'objecte Jugador amb la puntuació més baixa
        for (Jugador jugador : jugadors) {
            if (Integer.parseInt(jugador.getPunts()) == menorPuntuacio) {
                j = jugador;
                break;
            }
        }

        return j;
    }

    /** *  Retorna la puntuació més baixa de tots els jugadors.
     *
     * @param jugadors El llistat de jugadors
     * @return La puntuació més baixa
     */
    public int getPuntuacioMesBaixaActual(List<Jugador> jugadors) {
        int n = Integer.parseInt(jugadors.get(0).getPunts());

        for (Jugador jugador : jugadors) {
            int puntsJugador = Integer.parseInt(jugador.getPunts());

            if (puntsJugador < n) {
                n = puntsJugador;
            }
        }

        //System.out.println("Puntuació més baixa actual: " + n);
        return n;
    }

    /** Recorre el llistat de jugadors i omple una nova llista amb els què superen o igualen la puntuació
     *
     * @param jugadors El llistat de jugadors
     * @param puntuacio Puntuació
     * @return Llistat de jugadors que es poden eliminar
     */
    private List<Jugador> getJugadorsEliminables(List<Jugador> jugadors, int puntuacio) {
        List<Jugador> jugadorsEliminables = new ArrayList<>();

        for (Jugador jugador : jugadors) {
            int puntsJugador = Integer.parseInt(jugador.getPunts());

            if (puntsJugador >= puntuacio) {
                jugadorsEliminables.add(jugador);
            }
        }

        return jugadorsEliminables;
    }

    /** Elimina del llistat els jugadors passats per paràmetre
     *
     * @param jugadors El llistat de jugadors
     * @param jugadorsEliminables Els jugadors que s'han d'eliminar
     */
    private void eliminaJugadors(List<Jugador> jugadors, List<Jugador> jugadorsEliminables) {
        if (jugadorsEliminables.size() > 0) {
            for (Jugador jugador : jugadorsEliminables) {
                jugadors.remove(jugador);
            }
        }
    }

    /** Comprova si hi ha dos jugadors o més empatats
     *
     * @param jugadors El llistat de jugadors
     * @return True si hi ha empat. False si no n'hi ha
     */
    public boolean hiHaEmpat(List<Jugador> jugadors) {
        int nJugadorsAmbMateixaPuntuacioActual = getNumJugadorsAmbMateixaPuntuacio(jugadors, getPuntuacioMesBaixaActual(jugadors));
        if (nJugadorsAmbMateixaPuntuacioActual > 1) {
            //System.out.println("Hi ha empat");
            return true;
        } else {
            //System.out.println("No hi ha empat");
            return false;
        }
    }

    /** Retorna el nombre de jugadors que tenen la puntuació passada per paràmetre
     *
     * @param jugadors El llistat de jugadors
     * @param puntuacio La puntuació
     * @return El nombre de jugadors amb la mateixa puntuació
     */
    public int getNumJugadorsAmbMateixaPuntuacio(List<Jugador> jugadors, int puntuacio) {
        int n = 0;

        for (Jugador jugador : jugadors) {
            int puntsJugador = Integer.parseInt(jugador.getPunts());

            if (puntsJugador == puntuacio) {
                n++;
            }
        }

        /* System.out.println("Nombre de jugadors amb la mateixa puntuació (" + puntuacio +
                "): " + n); */
        return n;
    }

    /** Automatitza el procés de comprovació i eliminació de jugadors, donat que es fa servir sovint
     *
     * @param jugadors El llistat de jugadors
     * @param puntuacio La puntuació
     */
    public void comprovaIeliminaJugadors(List<Jugador> jugadors, int puntuacio) {
        List<Jugador> jugadorsEliminables = getJugadorsEliminables(jugadors, puntuacio);
        eliminaJugadors(jugadors, jugadorsEliminables);
    }
}

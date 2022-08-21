package com.partides_uno;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.partides_uno.objectes.DataModelJugadors;
import com.partides_uno.objectes.GestorJugadorsPuntuacions;
import com.partides_uno.objectes.Jugador;
import com.partides_uno.objectes.Partida;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PartidaActivity extends AppCompatActivity {

    // Codi de l'Activity
    private static final Integer CODI_NOM_ACTIVITY = 1;
    // Variables essencials
    private Partida partidaActual;
    private List<DataModelJugadors> llistatDataModelJugadors;
    private List<Jugador> jugadorsPartidaActual;
    private RecyclerView recyclerViewJugadorsPartida;
    private CustomRecyclerViewPartidaAdapter adapter;
    //private static CustomAdapterJugadorsPartida adapter;
    //private ListView listViewJugadorsPartida;
    private CollapsingToolbarLayout toolbarLayout;

    // El límit de punts de la partida
    private int limitPunts = 0;
    // El guanyador de la partida
    private Jugador jugadorGuanyador = null;
    // Per control
    private int nJugada = 0;
    // Interfície de connexió amb la BBDD
    private DBInterface db;
    //private boolean limitDePuntsSuperat = false;
    //private boolean empat = false;

    // El gestor
    private GestorJugadorsPuntuacions gestorJugadorsPuntuacions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //listViewJugadorsPartida = (ListView) findViewById(R.id.list_view_jugadors_partida);
        recyclerViewJugadorsPartida = (RecyclerView) findViewById(R.id.recycler_view_jugadors_partida);

        // La BBDD
        db = new DBInterface(this);
        // El gestor
        gestorJugadorsPuntuacions = new GestorJugadorsPuntuacions();

        // Informació rebuda de la MainActivity
        Intent intentRebut = getIntent();
        Bundle extrasRebuts = intentRebut.getExtras();

        // S'obre la partida
        final String idPartida = extrasRebuts.getString("idPartida");
        db.obre();
        partidaActual = db.obtenirPartida(idPartida);
        limitPunts = Integer.parseInt(partidaActual.getLimitPunts());

        // Es configura la pantalla
        String titolPartida = partidaActual.getNom() + " - " + partidaActual.getLimitPunts() + " punts";
        //PartidaActivity.this.setTitle(titolPartida);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(titolPartida);
        //toolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.black));

        // S'obtenen els jugadors
        jugadorsPartidaActual = db.obtenirTotsElsJugadorsDeLaPartida(idPartida);
        db.tanca();

        // Es construeix el DataModel
        construeixLlistatDataModelJugadors();

        adapter = new CustomRecyclerViewPartidaAdapter(llistatDataModelJugadors);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewJugadorsPartida.setLayoutManager(mLayoutManager);
        recyclerViewJugadorsPartida.setItemAnimator(new DefaultItemAnimator());
        recyclerViewJugadorsPartida.setAdapter(adapter);

        //adapter = new CustomAdapterJugadorsPartida(llistatDataModelJugadors, PartidaActivity.this);
        //listViewJugadorsPartida.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Aquest botó servirà per sumar una ronda de punts", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                if (jugadorGuanyador == null) {
                    Bundle extras = new Bundle();
                    extras.putString("idPartida", idPartida);
                    Intent intentSumaPunts = new Intent(PartidaActivity.this, SumaRondaPuntsActivity.class);
                    intentSumaPunts.putExtras(extras);
                    startActivityForResult(intentSumaPunts, CODI_NOM_ACTIVITY);
                } else {
                    Toast.makeText(PartidaActivity.this, "La partida ja s'ha tancat. Vés enrera per començar-ne una de nova.", Toast.LENGTH_LONG).show();
                }

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODI_NOM_ACTIVITY) {
            if (resultCode == RESULT_OK) {

                //Increment del número de jugada
                nJugada++;
                actualitzaLlistatJugadorsPartidaActual(partidaActual.getId());

                // Originalment, al mètode: comprovaPuntuacions();
                int numJugadorsPerSotaDelLimitDePunts = gestorJugadorsPuntuacions.getNumJugadorsPerSotaDelLimitDePunts(jugadorsPartidaActual, limitPunts);
                //Toast.makeText(PartidaActivity.this, "numJugadorsPerSotaDelLimitDePunts=" + numJugadorsPerSotaDelLimitDePunts, Toast.LENGTH_LONG).show();

                /* OPCIO: 1 - Un sol jugador per sota del límit de punts - Guanyador directe */
                // Si només hi ha un jugador per sota del límit de punts, ha guanyat la partida
                if (numJugadorsPerSotaDelLimitDePunts == 1) {
                    // Originalment, al mètode: fesGuanyador(), però d'una altra forma
                    // Es marca el guanyador
                    jugadorGuanyador = gestorJugadorsPuntuacions.getJugadorAmbPuntuacioMesBaixa(jugadorsPartidaActual);
                    // S'eliminen la resta de jugadors
                    gestorJugadorsPuntuacions.comprovaIeliminaJugadors(jugadorsPartidaActual, limitPunts);

                    /* OPCIO: 2 - Més d'un jugador per sota del límit de punts */
                } else if (numJugadorsPerSotaDelLimitDePunts > 1) {
                    // Cal eliminar els què superen el límit de punts, si n'hi ha cap
                    gestorJugadorsPuntuacions.comprovaIeliminaJugadors(jugadorsPartidaActual, limitPunts);

                    // Fins aquí queda resolt el joc dins del límit de punts

                    // ¿Què passa si hi ha un empat per sobre del límit de punts?

                } else if (numJugadorsPerSotaDelLimitDePunts == 0) {
                    // Si hi ha un empat: cal saber quina és la puntuació més baixa actual i després eliminar els que no han quedat empatats, si n'hi ha cap.
                    int menorPuntuacio = Integer.parseInt(gestorJugadorsPuntuacions.getJugadorAmbPuntuacioMesBaixa(jugadorsPartidaActual).getPunts());

                    /* OPCIO: 3 - Cap jugador per sota del límit de punts i empat */
                    if (gestorJugadorsPuntuacions.hiHaEmpat(jugadorsPartidaActual)) {
                        gestorJugadorsPuntuacions.comprovaIeliminaJugadors(jugadorsPartidaActual, menorPuntuacio + 1);

                    /* OPCIO: 4 - Cap jugador per sota del límit de punts SENSE empat. Guanya el jugador que té menys punts */
                    } else {
                        // Originalment, al mètode: resolEmpatAmbGuanyador(), però d'una altra forma
                        // Es marca el guanyador
                        jugadorGuanyador = gestorJugadorsPuntuacions.getJugadorAmbPuntuacioMesBaixa(jugadorsPartidaActual);
                        // I s'eliminen la resta de jugadors
                        gestorJugadorsPuntuacions.comprovaIeliminaJugadors(jugadorsPartidaActual, menorPuntuacio + 1);
                    }

                }

                // Després de totes les comprovacions, s'actualitza el DataModel
                actualitzaLlistatDataModelJugadors();
                adapter.notifyDataSetChanged();

                // Un cop realitzades les comprovacions
                /* OPCIONS: 1 i 4 - Ja hi ha guanyador i s'ha acabat la partida */
                if (jugadorGuanyador != null) {
                    //Es tanca la partida a nivell de BBDD i es mostra el missatge (o Activity)
                    db.obre();
                    db.tancaIFesGuanyador(partidaActual.getId(), jugadorGuanyador.getNom());
                    db.tanca();

                    // La idea és cridar una nova Activity amb aquesta informació i alguna imatge festiva
                    String textGuanyador ="\"" + jugadorGuanyador.getNom() + "\" ha guanyat la partida a la jugada " + nJugada + "! Enhorabona!!";
                    Toast.makeText(PartidaActivity.this, textGuanyador, Toast.LENGTH_LONG).show();

                /* OPCIONS: 2 i 3 - Hi ha més d'un jugador per sota del límit de punts, o bé un empat per sobre del límit. La partida continua */
                } else {
                    // Això confirma la opció 2
                    if (numJugadorsPerSotaDelLimitDePunts > 1) {
                        Toast.makeText(PartidaActivity.this, "Els punts s'han sumat correctament.", Toast.LENGTH_SHORT).show();
                    }
                    // Això confirma la opció 3, sempre que s'hagi superat el límit de punts
                    if ((numJugadorsPerSotaDelLimitDePunts == 0) && (gestorJugadorsPuntuacions.hiHaEmpat(jugadorsPartidaActual))) {
                        String textEmpat ="Hi ha un empat i, per tant, encara no s'ha acabat la partida";
                        Toast.makeText(PartidaActivity.this, textEmpat, Toast.LENGTH_LONG).show();
                    }
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(PartidaActivity.this, "Els punts no s'han sumat", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void construeixLlistatDataModelJugadors() {
        llistatDataModelJugadors = new ArrayList<>();
        for (int i = 0; i < jugadorsPartidaActual.size(); i++) {
            //builder.append(jugadorsPartidaActual.get(i).getNom() + "\n");
            Jugador jugador = jugadorsPartidaActual.get(i);
            DataModelJugadors dataModel = new DataModelJugadors(jugador.getNom(),
                    jugador.getPunts());
            llistatDataModelJugadors.add(dataModel);
        }
    }

    private void actualitzaLlistatDataModelJugadors() {
        llistatDataModelJugadors.clear();
        for (int i = 0; i < jugadorsPartidaActual.size(); i++) {
            Jugador jugador = jugadorsPartidaActual.get(i);
            DataModelJugadors dataModel = new DataModelJugadors(jugador.getNom(),
                    jugador.getPunts());
            llistatDataModelJugadors.add(dataModel);
        }
        // Ordenació
        Collections.sort(llistatDataModelJugadors, new Comparator<DataModelJugadors>() {
            @Override
            public int compare(DataModelJugadors Jugador1, DataModelJugadors Jugador2) {
                return Jugador1.getPunts().compareTo(Jugador2.getPunts());
            }
        });
    }

    private void actualitzaLlistatJugadorsPartidaActual(String idPartida) {
        db.obre();
        jugadorsPartidaActual.clear();
        jugadorsPartidaActual = new ArrayList<>();
        jugadorsPartidaActual = db.obtenirTotsElsJugadorsDeLaPartida(idPartida);
        db.tanca();
    }
}

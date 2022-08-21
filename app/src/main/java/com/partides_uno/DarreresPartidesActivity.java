package com.partides_uno;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.partides_uno.objectes.DataModelJugadors;
import com.partides_uno.objectes.DataModelPartides;
import com.partides_uno.objectes.Jugador;
import com.partides_uno.objectes.Partida;

import java.util.ArrayList;
import java.util.List;

public class DarreresPartidesActivity extends AppCompatActivity {

    private ArrayList<DataModelPartides> llistatDataModelPartides;
    private List<Partida> partidesDesades;
    private ListView listViewDarreresPartides;
    private static CustomAdapterDarreresPartides adapter;

    private DBInterface db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_darreres_partides);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewDarreresPartides = (ListView) findViewById(R.id.list_view_darreres_partides);
        db = new DBInterface(this);

        db.obre();
        partidesDesades = db.obtenirTotesLesPartidesAcabades();
        db.tanca();

        if(partidesDesades != null) {
            construeixLlistatDataModelPartides();
            adapter = new CustomAdapterDarreresPartides(llistatDataModelPartides, DarreresPartidesActivity.this);
            listViewDarreresPartides.setAdapter(adapter);
        }


        /*StringBuilder builder = new StringBuilder();

        for (int i = 0; i < partidesAcabades.size(); i++) {
            Partida partidaActual = partidesAcabades.get(i);
            builder.append("Nom de la partida: " + partidaActual.getNom() + "\n");
            builder.append("Limit de punts: " + partidaActual.getLimitPunts() + "\n");

            ArrayList<Jugador> jugadorsPartidaActual = db.obtenirTotsElsJugadorsDeLaPartida(partidaActual.getId());

            builder.append("Nombre de jugadors: " + jugadorsPartidaActual.size() + "\n");
            builder.append("Noms dels jugadors:\n");
            for (int k = 0; k < jugadorsPartidaActual.size(); k++) {
                builder.append(jugadorsPartidaActual.get(k).getNom() + "\n");
            }
        }



        String textPerMostrar = builder.toString();

        textViewDarreresPartides = (TextView) findViewById(R.id.text_view_darreres_partides);
        textViewDarreresPartides.setText(textPerMostrar);*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void construeixLlistatDataModelPartides() {
        llistatDataModelPartides = new ArrayList<>();
        for (int i = 0; i < partidesDesades.size(); i++) {
            Partida partida = partidesDesades.get(i);
            DataModelPartides dataModel = new DataModelPartides(partida.getNom(),
                    partida.getLimitPunts(), partida.getData(), partida.getGuanyador());
            llistatDataModelPartides.add(dataModel);
        }
    }
}

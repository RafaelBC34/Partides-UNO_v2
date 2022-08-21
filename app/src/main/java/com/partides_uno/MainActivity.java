package com.partides_uno;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.partides_uno.objectes.Partida;

public class MainActivity extends AppCompatActivity {

    private static final Integer CODI_NOM_ACTIVITY = 0;

    private Button btnPartidaActualONova;
    private Button btnDarreresPartides;
    private MenuItem menuItemActionTancaPartidaActual;
    private Partida partidaActiva = null;
    private boolean hiHaPartidaActiva = false;
    private boolean hiHaPartidesDesades = false;
    private DBInterface bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Base de dades
        bd = new DBInterface(this);

        btnPartidaActualONova = (Button) findViewById(R.id.btn_partida_actual_o_nova);

        carregaPartidaActiva();

        btnPartidaActualONova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hiHaPartidaActiva) {
                    startActivityForResult(new Intent(MainActivity.this, ConfigNovaPartidaActivity.class), CODI_NOM_ACTIVITY);
                } else {
                    Bundle extras = new Bundle();
                    extras.putString("idPartida", partidaActiva.getId());
                    Intent intentContinuaPartida = new Intent(MainActivity.this, PartidaActivity.class);
                    intentContinuaPartida.putExtras(extras);
                    startActivity(intentContinuaPartida);
                }

            }
        });

        btnDarreresPartides = (Button) findViewById(R.id.btn_darreres_partides);
        btnDarreresPartides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DarreresPartidesActivity.class));
            }
        });

        //S'habilita o es deshabilita el botó si hi ha partides desades o no
        comprovaPartidesDesades();
        btnDarreresPartides.setEnabled(hiHaPartidesDesades);

        //S'habilita o es deshabilita el botó de 'Tanca partida actual' del menú si hi ha una partida activa o no
        //menuItemActionTancaPartidaActual = (MenuItem) findViewById(R.id.action_tanca_partida_actual);
        //menuItemActionTancaPartidaActual.setEnabled(hiHaPartidaActiva);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //S'habilita o es deshabilita el botó de 'Tanca partida actual' del menú si hi ha una partida activa o no
        menuItemActionTancaPartidaActual = menu.findItem(R.id.action_tanca_partida_actual);
        menuItemActionTancaPartidaActual.setEnabled(hiHaPartidaActiva);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_regles_joc) {
            Intent intentReglesJoc = new Intent(MainActivity.this, ReglesJocActivity.class);
            startActivity(intentReglesJoc);

        } else if (id == R.id.action_tanca_partida_actual) {
            if (hiHaPartidaActiva) {
                bd.obre();
                bd.tancaPartida(partidaActiva.getId());
                bd.tanca();

                String textBtn = "Nova Partida";
                btnPartidaActualONova.setText(textBtn);

                hiHaPartidaActiva = false;
                menuItemActionTancaPartidaActual.setEnabled(hiHaPartidaActiva);

                comprovaPartidesDesades();
                btnDarreresPartides.setEnabled(hiHaPartidesDesades);

                Toast.makeText(MainActivity.this, "S'ha tancat la partida.", Toast.LENGTH_LONG).show();
            } /* else {
                Toast.makeText(MainActivity.this, "No hi ha cap partida oberta, actualment. Pots començar-ne una de nova!", Toast.LENGTH_SHORT).show();
            } */
        } else if (id == R.id.action_info) {
            Toast.makeText(MainActivity.this, "Fes partides a l'UNO amb els teus amics i/o familiars. Només un serà el guanyador!!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODI_NOM_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Intent intentPartida = new Intent(MainActivity.this, PartidaActivity.class);
                intentPartida.putExtras(extras);

                startActivity(intentPartida);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        carregaPartidaActiva();
        //S'habilita o es deshabilita el botó de 'Tanca partida actual' del menú si hi ha una partida activa o no
        menuItemActionTancaPartidaActual.setEnabled(hiHaPartidaActiva);
        //S'habilita o es deshabilita el botó de 'Darreres partides' si hi ha partides desades o no
        comprovaPartidesDesades();
        btnDarreresPartides.setEnabled(hiHaPartidesDesades);
    }

    private void carregaPartidaActiva() {
        bd.obre();
        partidaActiva = bd.obtenirPartidaActiva();
        bd.tanca();

        String textBtn = "";
        if (partidaActiva != null) {
            textBtn = "Continuar partida";
            btnPartidaActualONova.setText(textBtn);
            hiHaPartidaActiva = true;
        } else {
            textBtn = "Nova Partida";
            btnPartidaActualONova.setText(textBtn);
            hiHaPartidaActiva = false;
        }
    }

    private void comprovaPartidesDesades() {
        bd.obre();
        hiHaPartidesDesades = bd.hiHaPartidesAcabades();
        bd.tanca();
    }
}

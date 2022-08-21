package com.partides_uno;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.partides_uno.objectes.Jugador;

import java.util.List;

public class SumaRondaPuntsActivity extends AppCompatActivity {

    private TextView textViewNomJugadorSumaPunts;
    private TextView textViewValorPuntsSeleccionat;
    private NumberPicker numberPickerSumaPuntsJugadors;
    private Button btnSumaPuntsJugadorsOK;

    String idPartida;

    private int comptadorJugadors = 0;
    private Integer valorSeleccionatPunts = 0;
    private List<Jugador> jugadors;

    private DBInterface db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suma_ronda_punts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_suma_ronda_punts);
        setSupportActionBar(toolbar);

        Intent intentRebut = getIntent();
        idPartida = intentRebut.getExtras().getString("idPartida");

        db = new DBInterface(this);

        db.obre();
        jugadors = db.obtenirTotsElsJugadorsDeLaPartida(idPartida);
        db.tanca();

        textViewNomJugadorSumaPunts = (TextView) findViewById(R.id.text_view_nom_jugador_suma_punts);
        textViewNomJugadorSumaPunts.setText(jugadors.get(comptadorJugadors).getNom());
        textViewValorPuntsSeleccionat = (TextView) findViewById(R.id.text_view_valor_punts_seleccionat);

        numberPickerSumaPuntsJugadors = (NumberPicker) findViewById(R.id.number_picker_suma_punts_jugadors);
        numberPickerSumaPuntsJugadors.setMaxValue(1000);
        numberPickerSumaPuntsJugadors.setMinValue(0);
        numberPickerSumaPuntsJugadors.setWrapSelectorWheel(true);
        numberPickerSumaPuntsJugadors.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                valorSeleccionatPunts = newVal;
                String textValorPuntsSeleccionat = "Valor seleccionat: " + valorSeleccionatPunts;
                textViewValorPuntsSeleccionat.setText(textValorPuntsSeleccionat);
            }
        });

        btnSumaPuntsJugadorsOK = (Button) findViewById(R.id.btn_suma_punts_jugadors_ok);
        btnSumaPuntsJugadorsOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comptadorJugadors < jugadors.size()) {
                    Jugador jugador = jugadors.get(comptadorJugadors);
                    Integer puntsJugador = Integer.parseInt(jugador.getPunts());
                    Integer puntsTotals = puntsJugador + valorSeleccionatPunts;
                    jugador.setPunts(String.valueOf(puntsTotals));
                    comptadorJugadors++;
                    if (comptadorJugadors < jugadors.size()) {
                        textViewNomJugadorSumaPunts.setText(jugadors.get(comptadorJugadors).getNom());
                        numberPickerSumaPuntsJugadors.setValue(0);
                        valorSeleccionatPunts = 0;
                        String textValorPuntsSeleccionat = "Valor seleccionat: " + valorSeleccionatPunts;
                        textViewValorPuntsSeleccionat.setText(textValorPuntsSeleccionat);
                    } else {
                        Toast.makeText(SumaRondaPuntsActivity.this, "Dades actualitzades correctament. Prem OK per continuar", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    actualitzaPuntsJugadors();

                    Intent data = new Intent();
                    data.putExtra("idPartida", idPartida);

                    setResult(RESULT_OK, data);

                    finish();
                }
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("idPartida", idPartida);

        setResult(RESULT_CANCELED, data);
        finish();
        //super.onBackPressed();
    }

    private void actualitzaPuntsJugadors() {
        db.obre();
        for (int i = 0; i < jugadors.size(); i++) {
            Jugador jugador = jugadors.get(i);
            db.sumaPuntsJugador(jugador.getId(), Integer.parseInt(jugador.getPunts()));
        }
        db.tanca();
    }
}

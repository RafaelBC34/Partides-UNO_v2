package com.partides_uno;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfigNovaPartidaActivity extends AppCompatActivity {

    private static final String TITOL_TEXT_CONFIG_JUGADORS = "Nom del jugador ";
    private static final int MIN_JUGADORS = 2;
    private static final int MAX_JUGADORS = 30;
    private static final int MIN_PUNTS = 100;
    private static final int MAX_PUNTS = 5000;
    private TextView textViewPartidaJugadors, textViewJugadors, textViewLimitPunts;
    private EditText editTextNomsPartidaJugadors;
    private Button btnPartidaJugadorsOk;
    private NumberPicker numberPickerJugadors, numberPickerLimitPunts;
    private Integer valorSeleccionatJugadors = MIN_JUGADORS;
    private Integer valorSeleccionatPunts = MIN_PUNTS;
    private String nomPartida;
    private Boolean enPrimeraFase = true, enSegonaFase = false;
    private Integer comptadorJugadors = 1;
    private String[] nomsJugadors;

    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private DBInterface db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_nova_partida);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DBInterface(this);

        textViewPartidaJugadors = (TextView) findViewById(R.id.text_view_partida_jugadors);
        textViewJugadors = (TextView) findViewById(R.id.text_view_jugadors);
        textViewLimitPunts = (TextView) findViewById(R.id.text_view_limit_punts);
        editTextNomsPartidaJugadors = (EditText) findViewById(R.id.edit_text_noms_partida_jugadors);

        numberPickerJugadors = (NumberPicker) findViewById(R.id.number_picker_jugadors);
        numberPickerJugadors.setMaxValue(MAX_JUGADORS);
        numberPickerJugadors.setMinValue(MIN_JUGADORS);
        numberPickerJugadors.setWrapSelectorWheel(true);
        numberPickerJugadors.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                valorSeleccionatJugadors = newVal;
            }
        });

        numberPickerLimitPunts = (NumberPicker) findViewById(R.id.number_picker_limit_punts);
        numberPickerLimitPunts.setMaxValue(MAX_PUNTS);
        numberPickerLimitPunts.setMinValue(MIN_PUNTS);
        numberPickerLimitPunts.setWrapSelectorWheel(true);
        numberPickerLimitPunts.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                valorSeleccionatPunts = newVal;
            }
        });

        btnPartidaJugadorsOk = (Button) findViewById(R.id.btn_partida_jugadors_ok);
        btnPartidaJugadorsOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Editable editTextNomsPartidaJugadorsEditable = editTextNomsPartidaJugadors.getText();

                if (enPrimeraFase) {
                    nomPartida = editTextNomsPartidaJugadorsEditable.toString();

                    if (!nomPartida.isEmpty()) {
                        nomsJugadors = new String[valorSeleccionatJugadors];
                        enPrimeraFase = false;
                        enSegonaFase = true;

                        textViewJugadors.setVisibility(View.GONE);
                        numberPickerJugadors.setVisibility(View.GONE);
                        textViewLimitPunts.setVisibility(View.GONE);
                        numberPickerLimitPunts.setVisibility(View.GONE);

                        editTextNomsPartidaJugadorsEditable.clear();
                        String titol = TITOL_TEXT_CONFIG_JUGADORS + comptadorJugadors;
                        textViewPartidaJugadors.setText(titol);

                    } else {
                        Toast.makeText(ConfigNovaPartidaActivity.this, "Cal introduïr un nom per a la partida", Toast.LENGTH_SHORT).show();
                    }
                }

                if (enSegonaFase) {
                    if (comptadorJugadors <= valorSeleccionatJugadors) {
                        if (editTextNomsPartidaJugadorsEditable.length() > 0) {
                            nomsJugadors[comptadorJugadors - 1] = editTextNomsPartidaJugadorsEditable.toString();
                            comptadorJugadors++;
                            if (comptadorJugadors <= valorSeleccionatJugadors) {
                                String titol = TITOL_TEXT_CONFIG_JUGADORS + comptadorJugadors;
                                textViewPartidaJugadors.setText(titol);
                                editTextNomsPartidaJugadorsEditable.clear();
                            } else {
                                Toast.makeText(ConfigNovaPartidaActivity.this, "Configuració correcta. Prem OK per continuar", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ConfigNovaPartidaActivity.this, "Cal introduïr un nom", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Date date = new Date();
                        String stringDate = sdf.format(date);

                        db.obre();
                        long idPartida = db.insereixPartida(nomPartida, valorSeleccionatPunts, stringDate);

                        for (int i = 0; i < nomsJugadors.length; i++) {
                            db.insereixJugador(nomsJugadors[i], 0, idPartida);
                        }
                        db.tanca();

                        Bundle extrasPartida = new Bundle();
                        extrasPartida.putInt("valorSeleccionatJugadors", valorSeleccionatJugadors);
                        extrasPartida.putInt("valorSeleccionatPunts", valorSeleccionatPunts);
                        extrasPartida.putString("nomPartida", nomPartida);
                        extrasPartida.putStringArray("nomsJugadors", nomsJugadors);
                        String idPartidaString = String.valueOf(idPartida);
                        extrasPartida.putString("idPartida", idPartidaString);

                        Intent data = new Intent();
                        data.putExtras(extrasPartida);

                        setResult(RESULT_OK, data);

                        finish();
                    }
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}

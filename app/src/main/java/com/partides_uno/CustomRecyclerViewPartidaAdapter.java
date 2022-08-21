package com.partides_uno;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partides_uno.objectes.DataModelJugadors;

import java.util.List;

/**
 * Created by Rafael on 31/01/2018.
 */

public class CustomRecyclerViewPartidaAdapter extends RecyclerView.Adapter<CustomRecyclerViewPartidaAdapter.MyViewHolder> {

    private List<DataModelJugadors> jugadorsPartidaActual;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nom, punts;

        public MyViewHolder(View view) {
            super(view);
            nom = (TextView) view.findViewById(R.id.text_view_nom_jugador);
            punts = (TextView) view.findViewById(R.id.text_view_punts_jugador);
        }
    }


    public CustomRecyclerViewPartidaAdapter(List<DataModelJugadors> jugadorsPartidaActual) {
        this.jugadorsPartidaActual = jugadorsPartidaActual;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_jugador_partida, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataModelJugadors jugador = jugadorsPartidaActual.get(position);
        holder.nom.setText(jugador.getNom());
        holder.punts.setText(jugador.getPunts());
    }

    @Override
    public int getItemCount() {
        return jugadorsPartidaActual.size();
    }
}

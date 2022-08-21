package com.partides_uno;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.partides_uno.objectes.DataModelPartides;

import java.util.ArrayList;

/**
 * Created by Rafael on 29/01/2018.
 */

public class CustomAdapterDarreresPartides extends ArrayAdapter<DataModelPartides> {

    private ArrayList<DataModelPartides> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtNomPartida;
        TextView txtPuntsPartida;
        TextView txtDataPartida;
        TextView txtNomGuanyador;
    }

    public CustomAdapterDarreresPartides(ArrayList<DataModelPartides> data, Context context) {
        super(context, R.layout.item_darrera_partida, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        DataModelPartides dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CustomAdapterDarreresPartides.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new CustomAdapterDarreresPartides.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_darrera_partida, parent, false);
            viewHolder.txtNomPartida = (TextView) convertView.findViewById(R.id.text_view_nom_partida);
            viewHolder.txtPuntsPartida = (TextView) convertView.findViewById(R.id.text_view_punts_partida);
            viewHolder.txtDataPartida = (TextView) convertView.findViewById(R.id.text_view_data_partida);
            viewHolder.txtNomGuanyador = (TextView) convertView.findViewById(R.id.text_view_nom_guanyador);

            result=convertView;

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (CustomAdapterDarreresPartides.ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.txtNomPartida.setText(dataModel.getNom());
        String textPunts = dataModel.getPunts() + " punts";
        viewHolder.txtPuntsPartida.setText(textPunts);
        viewHolder.txtDataPartida.setText(dataModel.getData());
        viewHolder.txtNomGuanyador.setText(dataModel.getGuanyador());

        // Return the completed view to render on screen
        return convertView;
    }
}

package com.example.camirwin.invoicetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.camirwin.invoicetracker.R;

public class ClientAdapter extends ArrayAdapter<String> {

    private final Context context;

    public ClientAdapter(Context context, String[] objects) {
        super(context, R.layout.row_client, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_client, parent, false);

        TextView tvClientName = (TextView) rowView.findViewById(R.id.tvClientName);
        tvClientName.setText(tvClientName.getText() + " " + (position+1));

        return rowView;
    }

    @Override
    public int getCount() {
        return 15;
    }

}

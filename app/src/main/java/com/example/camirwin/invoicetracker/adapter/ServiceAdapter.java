package com.example.camirwin.invoicetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.camirwin.invoicetracker.R;

public class ServiceAdapter extends ArrayAdapter<String> {

    private final Context context;

    public ServiceAdapter(Context context, String[] objects) {
        super(context, R.layout.row_service, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_service, parent, false);

        return rowView;
    }

    @Override
    public int getCount() {
        return 5;
    }

}

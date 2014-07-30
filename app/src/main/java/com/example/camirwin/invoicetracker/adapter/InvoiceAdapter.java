package com.example.camirwin.invoicetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.camirwin.invoicetracker.R;

public class InvoiceAdapter extends ArrayAdapter<String> {

    private final Context context;

    public InvoiceAdapter(Context context, String[] objects) {
        super(context, R.layout.row_invoice, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_invoice, parent, false);

        return rowView;
    }

    @Override
    public int getCount() {
        return 0;
    }
}

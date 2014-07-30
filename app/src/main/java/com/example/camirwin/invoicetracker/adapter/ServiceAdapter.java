package com.example.camirwin.invoicetracker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.camirwin.invoicetracker.R;
import com.example.camirwin.invoicetracker.model.Services;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ServiceAdapter extends ArrayAdapter<Services> {

    private final Context context;
    private ArrayList<Services> services;

    public ServiceAdapter(Context context, ArrayList<Services> services) {
        super(context, R.layout.row_service, services);
        this.context = context;
        this.services = services;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_service, parent, false);

        TextView tvServiceName = (TextView) rowView.findViewById(R.id.tvServiceName);
        TextView tvServiceLastWorked = (TextView) rowView.findViewById(R.id.tvServiceLastWorked);
        TextView tvServiceRate = (TextView) rowView.findViewById(R.id.tvServiceRate);
        TextView tvServiceOutstandingBalance = (TextView) rowView.findViewById(R.id.tvServiceOutstandingBalance);

        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        Services service = services.get(position);

        tvServiceName.setText(service.getName());
        tvServiceRate.setText("$" + decimalFormat.format(service.getRate()) + "/hour");
        tvServiceOutstandingBalance.setText("$" + decimalFormat.format(service.getOutstandingBalance()));

        if (service.getLastWorkedDate().equals(Long.valueOf(0))) {
            tvServiceLastWorked.setText("never worked");
        } else {
            tvServiceLastWorked.setText("last worked " + dateFormat.format(service.getLastWorkedAsDateObject()));
        }

        return rowView;
    }

}

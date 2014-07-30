package com.example.camirwin.invoicetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.camirwin.invoicetracker.R;
import com.example.camirwin.invoicetracker.model.Client;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ClientAdapter extends ArrayAdapter<Client> {

    private final Context context;
    private ArrayList<Client> clients;

    public ClientAdapter(Context context, ArrayList<Client> clients) {
        super(context, R.layout.row_client, clients);
        this.context = context;
        this.clients = clients;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_client, parent, false);

        TextView tvClientName = (TextView) rowView.findViewById(R.id.tvClientName);
        TextView tvClientLastInvoice = (TextView) rowView.findViewById(R.id.tvClientLastInvoice);
        TextView tvClientOutstandingBalance = (TextView) rowView.findViewById(R.id.tvClientOutstandingBalance);

        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy, h:mm a", Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        Client client = clients.get(position);

        tvClientName.setText(client.getName());
        tvClientOutstandingBalance.setText("$" + decimalFormat.format(client.getOutstandingBalance()));

        if (client.getLastInvoiceDate().equals(Long.valueOf(0))) {
            tvClientLastInvoice.setText("Never");
        } else {
            tvClientLastInvoice.setText(dateFormat.format(client.getLastInvoiceAsDateObject()));
        }

        return rowView;
    }

}

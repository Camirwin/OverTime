package com.example.camirwin.invoicetracker.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.camirwin.invoicetracker.adapter.ClientAdapter;
import com.example.camirwin.invoicetracker.R;
import com.example.camirwin.invoicetracker.db.InvoiceTrackerDataSource;
import com.example.camirwin.invoicetracker.model.Client;

import java.util.ArrayList;


public class ClientsActivity extends ListActivity {

    public static final String CLIENT_ID = "client_id";

    InvoiceTrackerDataSource dataSource;
    ArrayList<Client> clients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);

        dataSource = new InvoiceTrackerDataSource(this);
        clients = dataSource.getAllClients();

        ClientAdapter adapter = new ClientAdapter(this, clients);
        setListAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.clients, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_add_client) {
            Intent intent = new Intent(this, CreateClientActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, ClientActivity.class);
        intent.putExtra(CLIENT_ID, clients.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        dataSource.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        dataSource.open();

        clients = dataSource.getAllClients();

        ClientAdapter adapter = new ClientAdapter(this, clients);
        setListAdapter(adapter);
    }

}

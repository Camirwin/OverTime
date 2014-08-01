package com.example.camirwin.invoicetracker.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.camirwin.invoicetracker.adapter.ClientAdapter;
import com.example.camirwin.invoicetracker.R;
import com.example.camirwin.invoicetracker.db.InvoiceTrackerDataSource;
import com.example.camirwin.invoicetracker.model.Client;

import java.util.ArrayList;


public class ClientsActivity extends ListActivity {

    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_NAME = "client_name";

    ActionMode actionMode;
    ClientAdapter adapter;
    InvoiceTrackerDataSource dataSource;
    ArrayList<Client> clients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        dataSource = new InvoiceTrackerDataSource(this);
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
        } else if (id == R.id.action_add_client) {
            Intent intent = new Intent(this, ClientCreateActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, ClientActivity.class);
        intent.putExtra(CLIENT_ID, clients.get(position).getId());
        intent.putExtra(CLIENT_NAME, clients.get(position).getName());
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
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
    }

    private void updateUI() {
        clients = dataSource.getAllClients();

        SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();
        ArrayList<Integer> positionsToSelect = new ArrayList<Integer>();
        for (Client client : clients) {
            if (checkedItemPositions.get(clients.indexOf(client))) {
                positionsToSelect.add(clients.indexOf(client));
            }
        }

        adapter = new ClientAdapter(this, clients);
        setListAdapter(adapter);

        final Context context = this;
        final Activity activity = this;
        getListView().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                mode.invalidate();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.client_contextual, menu);
                actionMode = mode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                MenuItem actionView = menu.findItem(R.id.action_view);
                MenuItem actionEdit = menu.findItem(R.id.action_edit);
                MenuItem actionCopy = menu.findItem(R.id.action_copy);

                Boolean oneSelected = getListView().getCheckedItemCount() == 1;

                actionView.setVisible(oneSelected);
                actionEdit.setVisible(oneSelected);
                actionCopy.setVisible(oneSelected);

                mode.setTitle(getListView().getCheckedItemCount() + " selected");

                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
                final ActionMode finalMode = mode;
                switch (menuItem.getItemId()) {
                    case R.id.action_delete:
                        new AlertDialog.Builder(context)
                                .setTitle("Delete Selected Clients")
                                .setMessage("Would you like to delete the selected clients? Doing so will remove all "
                                        + "invoices, services, time entries, deliverables, and expenses attached "
                                        + "to the clients. This action cannot be undone.")
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        }
                                )
                                .setPositiveButton("Delete Clients",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                SparseBooleanArray selectedList = getListView().getCheckedItemPositions();
                                                for (Client client : clients) {
                                                    if (selectedList.get(clients.indexOf(client))) {
                                                        dataSource.deleteClient(client.getId());
                                                    }
                                                }
                                                dialog.cancel();
                                                finalMode.finish();
                                                updateUI();
                                            }
                                        }
                                ).create().show();
                        return true;
                    case R.id.action_view:
                        Client clientToView = new Client();
                        SparseBooleanArray selectedList = getListView().getCheckedItemPositions();
                        for (Client client : clients) {
                            if (selectedList.get(clients.indexOf(client))) {
                                clientToView = client;
                                break;
                            }
                        }
                        Intent intent = new Intent(activity, ClientActivity.class);
                        intent.putExtra(CLIENT_ID, clientToView.getId());
                        intent.putExtra(CLIENT_NAME, clientToView.getName());
                        startActivity(intent);
                        mode.finish();
                        return true;
                    case R.id.action_copy:
                        return true;
                    case R.id.action_edit:
                        return true;
                    case R.id.action_select_all:
                        getListView().clearChoices();
                        for (int i = 0; i < clients.size(); i++) {
                            getListView().setItemChecked(i, true);
                        }
                        return true;
                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
            }
        });

        getListView().clearChoices();
        for (int position : positionsToSelect) {
            getListView().setItemChecked(position, true);
        }

    }

}

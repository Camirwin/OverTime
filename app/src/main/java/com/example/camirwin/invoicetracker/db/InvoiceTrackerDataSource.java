package com.example.camirwin.invoicetracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.camirwin.invoicetracker.model.Client;
import com.example.camirwin.invoicetracker.model.Services;

import java.util.ArrayList;

public class InvoiceTrackerDataSource {

    public static final String LOGTAG = "InvoiceTracker";

    private static final String[] allClientColumns = {
            InvoiceTrackerDBOpenHelper.CLIENTS_ID,
            InvoiceTrackerDBOpenHelper.CLIENTS_NAME,
            InvoiceTrackerDBOpenHelper.CLIENTS_LOCATION,
            InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_FIRST_NAME,
            InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_LAST_NAME,
            InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_EMAIL,
            InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_PHONE,
            InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_BALANCE,
            InvoiceTrackerDBOpenHelper.CLIENTS_LAST_INVOICE_DATE
    };

    private static final String[] allServiceColumns = {
            InvoiceTrackerDBOpenHelper.SERVICES_ID,
            InvoiceTrackerDBOpenHelper.SERVICES_CLIENT_ID,
            InvoiceTrackerDBOpenHelper.SERVICES_INVOICE_ID,
            InvoiceTrackerDBOpenHelper.SERVICES_NAME,
            InvoiceTrackerDBOpenHelper.SERVICES_RATE,
            InvoiceTrackerDBOpenHelper.SERVICES_LAST_WORKED_DATE,
            InvoiceTrackerDBOpenHelper.SERVICES_OUTSTANDING_BALANCE
    };

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase database;
    Boolean open = false;

    public InvoiceTrackerDataSource(Context context) {
        dbHelper = new InvoiceTrackerDBOpenHelper(context);
        open();
    }

    public void open() {
        if (!open) {
            // Open database
            database = dbHelper.getWritableDatabase();

            // Enable foreign key constraints
            if (!database.isReadOnly()) {
                database.execSQL("PRAGMA foreign_keys = ON;");
            }

            open = true;
        }
    }

    public void close() {
        if (open) {
            // Close database
            dbHelper.close();

            open = false;
        }
    }

    public Client createClient(Client client) {
        // Variable to hold map of values to columns
        ContentValues values = new ContentValues();

        // Put supplied values into variable
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_NAME, client.getName());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_LOCATION, client.getLocation());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_FIRST_NAME, client.getContactFirstName());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_LAST_NAME, client.getContactLastName());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_EMAIL, client.getContactEmail());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_PHONE, client.getContactPhone());

        // Insert new entry and return the generated id
        int insertId;
        try {
            insertId = (int) database.insert(InvoiceTrackerDBOpenHelper.TABLE_CLIENTS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Log.i(LOGTAG, "Created client " + insertId);

        // Set id in client object to created id and return
        client.setId(insertId);
        return client;
    }

    public ArrayList<Client> getAllClients() {
        // Variable to hold clients
        ArrayList<Client> clients = new ArrayList<Client>();

        // Cursor holding query to database for all clients
        Cursor cursor = database.query(InvoiceTrackerDBOpenHelper.TABLE_CLIENTS,
                allClientColumns, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            // Loop through values retrieved by cursor
            while (cursor.moveToNext()) {
                // Create client object from cursor location
                Client client = new Client();
                client.setId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_ID)));
                client.setName(cursor.getString(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_NAME)));
                client.setLocation(cursor.getString(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_LOCATION)));
                client.setContactFirstName(cursor.getString(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_FIRST_NAME)));
                client.setContactLastName(cursor.getString(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_LAST_NAME)));
                client.setContactEmail(cursor.getString(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_EMAIL)));
                client.setContactPhone(cursor.getString(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_PHONE)));
                client.setOutstandingBalance(cursor.getDouble(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_BALANCE)));
                client.setLastInvoiceDate(cursor.getLong(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_LAST_INVOICE_DATE)));

                // Add client to list
                clients.add(client);
            }
        }
        cursor.close();

        Log.i(LOGTAG, "Retrieved " + clients.size() + " clients");

        // Return full list of clients
        return clients;
    }

    public Client getClientById(int clientId) {
        // Cursor holding query for client by id
        Cursor cursor = database.query(InvoiceTrackerDBOpenHelper.TABLE_CLIENTS,
                allClientColumns, InvoiceTrackerDBOpenHelper.CLIENTS_ID + " = ?",
                new String[] { String.valueOf(clientId) }, null, null, null);

        // Empty client variable to hold the client from the table if it exists
        Client client = null;

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            client = new Client();
            client.setId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_ID)));
            client.setName(cursor.getString(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_NAME)));
            client.setLocation(cursor.getString(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_LOCATION)));
            client.setContactFirstName(cursor.getString(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_FIRST_NAME)));
            client.setContactLastName(cursor.getString(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_LAST_NAME)));
            client.setContactEmail(cursor.getString(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_EMAIL)));
            client.setContactPhone(cursor.getString(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_PHONE)));
            client.setOutstandingBalance(cursor.getDouble(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_BALANCE)));
            client.setLastInvoiceDate(cursor.getLong(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_LAST_INVOICE_DATE)));

            Log.i(LOGTAG, "Retrieved client " + clientId);
        } else {
            Log.e(LOGTAG, "No client " + clientId + " found");
        }
        cursor.close();

        // Return the retrieved client or null if none was found.
        return client;
    }

    public Services createService(Services service) {
        // Variable to hold map of values to columns
        ContentValues values = new ContentValues();

        // Put supplied values into variable
        values.put(InvoiceTrackerDBOpenHelper.SERVICES_CLIENT_ID, service.getClientId());
        values.put(InvoiceTrackerDBOpenHelper.SERVICES_INVOICE_ID, service.getInvoiceId());
        values.put(InvoiceTrackerDBOpenHelper.SERVICES_NAME, service.getName());
        values.put(InvoiceTrackerDBOpenHelper.SERVICES_RATE, service.getRate());
        values.put(InvoiceTrackerDBOpenHelper.SERVICES_LAST_WORKED_DATE, service.getLastWorkedDate());
        values.put(InvoiceTrackerDBOpenHelper.SERVICES_OUTSTANDING_BALANCE, service.getOutstandingBalance());

        // Insert new entry and return the generated id
        int insertId;
        try {
            insertId = (int) database.insert(InvoiceTrackerDBOpenHelper.TABLE_SERVICES, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Log.i(LOGTAG, "Created service " + insertId);

        // Set id in service object to created id and return
        service.setId(insertId);
        return service;
    }

    public ArrayList<Services> getAllServicesForClient(int clientId) {
        // Variable to hold services
        ArrayList<Services> services = new ArrayList<Services>();

        // Cursor holding query to database for all services for client
        Cursor cursor = database.query(InvoiceTrackerDBOpenHelper.TABLE_SERVICES,
                allServiceColumns,
                InvoiceTrackerDBOpenHelper.SERVICES_CLIENT_ID + " = ?",
                new String[] { String.valueOf(clientId) }, null, null, null);

        if (cursor.getCount() > 0) {
            // Loop through values retrieved by cursor
            while (cursor.moveToNext()) {
                // Create service object from cursor location
                Services service = new Services();
                service.setId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.SERVICES_ID)));
                service.setClientId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.SERVICES_CLIENT_ID)));
                service.setInvoiceId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.SERVICES_INVOICE_ID)));
                service.setName(cursor.getString(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.SERVICES_NAME)));
                service.setRate(cursor.getDouble(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.SERVICES_RATE)));
                service.setLastWorkedDate(cursor.getLong(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.SERVICES_LAST_WORKED_DATE)));
                service.setOutstandingBalance(cursor.getDouble(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.SERVICES_OUTSTANDING_BALANCE)));

                // Add client to list
                services.add(service);
            }
        }
        cursor.close();

        Log.i(LOGTAG, "Retrieved " + services.size() + " services for client " + clientId);

        // Return full list of services
        return services;
    }

}

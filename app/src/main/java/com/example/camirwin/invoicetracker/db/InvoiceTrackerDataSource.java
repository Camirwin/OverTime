package com.example.camirwin.invoicetracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.camirwin.invoicetracker.model.Client;

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

}

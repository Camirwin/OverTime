package com.example.camirwin.invoicetracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.camirwin.invoicetracker.model.Client;
import com.example.camirwin.invoicetracker.model.Services;
import com.example.camirwin.invoicetracker.model.TimeEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
            InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_SERVICES,
            InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_DELIVERABLES,
            InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_EXPENSES,
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

    private static final String[] allTimeEntryColumns = {
            InvoiceTrackerDBOpenHelper.TIME_ENTRIES_ID,
            InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLIENT_ID,
            InvoiceTrackerDBOpenHelper.TIME_ENTRIES_SERVICE_ID,
            InvoiceTrackerDBOpenHelper.TIME_ENTRIES_INVOICE_ID,
            InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLOCK_IN_DATE,
            InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLOCK_OUT_DATE,
            InvoiceTrackerDBOpenHelper.TIME_ENTRIES_RATE
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
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_SERVICES, client.getOutstandingServices());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_DELIVERABLES, client.getOutstandingDeliverables());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_EXPENSES, client.getOutstandingExpenses());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_LAST_INVOICE_DATE, client.getLastInvoiceDate());

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
                client.setOutstandingServices(cursor.getDouble(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_SERVICES)));
                client.setOutstandingDeliverables(cursor.getDouble(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_DELIVERABLES)));
                client.setOutstandingExpenses(cursor.getDouble(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_EXPENSES)));
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
                new String[]{String.valueOf(clientId)}, null, null, null);

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
            client.setOutstandingServices(cursor.getDouble(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_SERVICES)));
            client.setOutstandingDeliverables(cursor.getDouble(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_DELIVERABLES)));
            client.setOutstandingExpenses(cursor.getDouble(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_EXPENSES)));
            client.setLastInvoiceDate(cursor.getLong(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.CLIENTS_LAST_INVOICE_DATE)));

            Log.i(LOGTAG, "Retrieved client " + clientId);
        } else {
            Log.e(LOGTAG, "No client " + clientId + " found");
        }
        cursor.close();

        // Return the retrieved client or null if none was found.
        return client;
    }

    public Client updateClient(Client client) {
        // Variable to hold map of values to columns
        ContentValues values = new ContentValues();

        // Put supplied values into variable
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_NAME, client.getName());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_LOCATION, client.getLocation());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_FIRST_NAME, client.getContactFirstName());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_LAST_NAME, client.getContactLastName());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_EMAIL, client.getContactEmail());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_CONTACT_PHONE, client.getContactPhone());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_SERVICES, client.getOutstandingServices());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_DELIVERABLES, client.getOutstandingDeliverables());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_OUTSTANDING_EXPENSES, client.getOutstandingExpenses());
        values.put(InvoiceTrackerDBOpenHelper.CLIENTS_LAST_INVOICE_DATE, client.getLastInvoiceDate());

        try {
            database.update(InvoiceTrackerDBOpenHelper.TABLE_CLIENTS, values,
                    InvoiceTrackerDBOpenHelper.CLIENTS_ID + " = ?",
                    new String[]{String.valueOf(client.getId())});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Log.i(LOGTAG, "Updated client " + client.getId());
        return client;
    }

    public void deleteClient(int clientId) {
        // Query the clients table to delete the client with the supplied id
        database.delete(InvoiceTrackerDBOpenHelper.TABLE_CLIENTS,
                InvoiceTrackerDBOpenHelper.CLIENTS_ID + " = ?",
                new String[]{String.valueOf(clientId)});

        Log.i(LOGTAG, "Deleted client " + clientId);
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
                new String[]{String.valueOf(clientId)}, null, null, null);

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

    public Services getServiceById(int serviceId) {
        // Variable to hold service
        Services service = null;

        // Cursor holding query to database for all services for client
        Cursor cursor = database.query(InvoiceTrackerDBOpenHelper.TABLE_SERVICES,
                allServiceColumns,
                InvoiceTrackerDBOpenHelper.SERVICES_ID + " = ?",
                new String[]{String.valueOf(serviceId)}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            // Create service object from cursor location
            service = new Services();
            service.setId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.SERVICES_ID)));
            service.setClientId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.SERVICES_CLIENT_ID)));
            service.setInvoiceId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.SERVICES_INVOICE_ID)));
            service.setName(cursor.getString(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.SERVICES_NAME)));
            service.setRate(cursor.getDouble(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.SERVICES_RATE)));
            service.setLastWorkedDate(cursor.getLong(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.SERVICES_LAST_WORKED_DATE)));
            service.setOutstandingBalance(cursor.getDouble(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.SERVICES_OUTSTANDING_BALANCE)));

            Log.i(LOGTAG, "Retrieved service " + serviceId);
        } else {
            Log.i(LOGTAG, "No service " + serviceId + " found");
        }
        cursor.close();

        // Return service
        return service;
    }

    public Services updateService(Services service) {
        // Variable to hold map of values to columns
        ContentValues values = new ContentValues();

        // Put supplied values into variable
        values.put(InvoiceTrackerDBOpenHelper.SERVICES_CLIENT_ID, service.getClientId());
        values.put(InvoiceTrackerDBOpenHelper.SERVICES_INVOICE_ID, service.getInvoiceId() == 0 ? null : service.getInvoiceId());
        values.put(InvoiceTrackerDBOpenHelper.SERVICES_NAME, service.getName());
        values.put(InvoiceTrackerDBOpenHelper.SERVICES_RATE, service.getRate());
        values.put(InvoiceTrackerDBOpenHelper.SERVICES_LAST_WORKED_DATE, service.getLastWorkedDate());
        values.put(InvoiceTrackerDBOpenHelper.SERVICES_OUTSTANDING_BALANCE, service.getOutstandingBalance());

        try {
            database.update(InvoiceTrackerDBOpenHelper.TABLE_SERVICES, values,
                    InvoiceTrackerDBOpenHelper.SERVICES_ID + " = ?",
                    new String[]{String.valueOf(service.getId())});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Log.i(LOGTAG, "Updated service " + service.getId());
        return service;
    }

    public void deleteService(Services service) {
        ArrayList<TimeEntry> timeEntriesForService = getAllClockedOutTimeEntriesForService(service.getId());

        // Query the services table to delete the service with the supplied id
        database.delete(InvoiceTrackerDBOpenHelper.TABLE_SERVICES,
                InvoiceTrackerDBOpenHelper.SERVICES_ID + " = ?",
                new String[]{String.valueOf(service.getId())});

        double outstandingBalanceToRemove = 0;
        for (TimeEntry entry : timeEntriesForService) {
            outstandingBalanceToRemove += entry.getEarnedIncome();
        }

        Client client = getClientById(service.getClientId());
        client.setOutstandingServices(client.getOutstandingServices() - outstandingBalanceToRemove);
        updateClient(client);

        Log.i(LOGTAG, "Deleted service " + service.getId());
    }

    public TimeEntry createTimeEntry(TimeEntry timeEntry) {
        // Variable to hold map of values to columns
        ContentValues values = new ContentValues();

        // Put supplied values into variable
        values.put(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLIENT_ID, timeEntry.getClientId());
        values.put(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_SERVICE_ID, timeEntry.getServiceId());
        values.put(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_INVOICE_ID, timeEntry.getInvoiceId());
        values.put(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLOCK_IN_DATE, timeEntry.getClockInDate());
        values.put(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLOCK_OUT_DATE, timeEntry.getClockOutDate());
        values.put(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_RATE, timeEntry.getRate());

        // Insert new entry and return the generated id
        int insertId;
        try {
            insertId = (int) database.insert(InvoiceTrackerDBOpenHelper.TABLE_TIME_ENTRIES, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Log.i(LOGTAG, "Created time entry " + insertId);

        // Set id in time entry object to created id and return
        timeEntry.setId(insertId);
        return timeEntry;
    }

    public TimeEntry getClockedInEntryForClient(int clientId) {
        // Cursor holding query to database for a time entry that is clocked in for the client
        Cursor cursor = database.query(InvoiceTrackerDBOpenHelper.TABLE_TIME_ENTRIES,
                allTimeEntryColumns,
                InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLIENT_ID + " = ? "
                        + "AND " + InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLOCK_OUT_DATE + " IS NULL",
                new String[]{String.valueOf(clientId)}, null, null, null
        );

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            TimeEntry timeEntry = new TimeEntry();
            timeEntry.setId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_ID)));
            timeEntry.setClientId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLIENT_ID)));
            timeEntry.setServiceId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_SERVICE_ID)));
            timeEntry.setInvoiceId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_INVOICE_ID)));
            timeEntry.setClockInDate(cursor.getLong(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLOCK_IN_DATE)));
            timeEntry.setClockOutDate(cursor.getLong(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLOCK_OUT_DATE)));
            timeEntry.setRate(cursor.getDouble(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_RATE)));

            cursor.close();
            Log.i(LOGTAG, "Retrieved clock in time entry " + timeEntry.getId() + " for client " + clientId);
            return timeEntry;
        }

        cursor.close();
        Log.i(LOGTAG, "No clocked in time entry for client " + clientId);
        return null;
    }

    public TimeEntry updateTimeEntry(TimeEntry timeEntry) {
        // Variable to hold map of values to columns
        ContentValues values = new ContentValues();

        // Put supplied values into variable
        values.put(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLIENT_ID, timeEntry.getClientId());
        values.put(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_SERVICE_ID, timeEntry.getServiceId());
        values.put(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_INVOICE_ID, timeEntry.getInvoiceId() == 0 ? null : timeEntry.getInvoiceId());
        values.put(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLOCK_IN_DATE, timeEntry.getClockInDate());
        values.put(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLOCK_OUT_DATE, timeEntry.getClockOutDate());
        values.put(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_RATE, timeEntry.getRate());

        try {
            database.update(InvoiceTrackerDBOpenHelper.TABLE_TIME_ENTRIES, values,
                    InvoiceTrackerDBOpenHelper.TIME_ENTRIES_ID + " = ?",
                    new String[]{String.valueOf(timeEntry.getId())});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Log.i(LOGTAG, "Updated time entry " + timeEntry.getId());

        Services service = getServiceById(timeEntry.getServiceId());
        service.setOutstandingBalance(service.getOutstandingBalance() + timeEntry.getEarnedIncome());
        service.setLastWorkedDate(timeEntry.getClockOutDate());
        updateService(service);

        Client client = getClientById(timeEntry.getClientId());
        client.setOutstandingServices(client.getOutstandingServices() + timeEntry.getEarnedIncome());
        updateClient(client);

        return timeEntry;
    }

    public ArrayList<TimeEntry> getAllClockedOutTimeEntriesForService(int serviceId) {
        // Variable to hold time entries
        ArrayList<TimeEntry> timeEntries = new ArrayList<TimeEntry>();

        // Cursor holding query to database for all clocked out time entries for the service
        Cursor cursor = database.query(InvoiceTrackerDBOpenHelper.TABLE_TIME_ENTRIES,
                allTimeEntryColumns,
                InvoiceTrackerDBOpenHelper.TIME_ENTRIES_SERVICE_ID + " = ? "
                        + "AND " + InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLOCK_OUT_DATE + " NOT NULL",
                new String[]{String.valueOf(serviceId)}, null, null, null
        );

        if (cursor.getCount() > 0) {
            // Loop through values retrieved by cursor
            while (cursor.moveToNext()) {
                // Create time entry object from cursor location
                TimeEntry timeEntry = new TimeEntry();
                timeEntry.setId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_ID)));
                timeEntry.setClientId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLIENT_ID)));
                timeEntry.setServiceId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_SERVICE_ID)));
                timeEntry.setInvoiceId(cursor.getInt(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_INVOICE_ID)));
                timeEntry.setClockInDate(cursor.getLong(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLOCK_IN_DATE)));
                timeEntry.setClockOutDate(cursor.getLong(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_CLOCK_OUT_DATE)));
                timeEntry.setRate(cursor.getDouble(cursor.getColumnIndex(InvoiceTrackerDBOpenHelper.TIME_ENTRIES_RATE)));

                // Add client to list
                timeEntries.add(timeEntry);
            }
        }
        cursor.close();

        Log.i(LOGTAG, "Retrieved " + timeEntries.size() + " time stamps for service " + serviceId);

        // Sort and return full list of time entries
        Collections.sort(timeEntries, new Comparator<TimeEntry>() {
            @Override
            public int compare(TimeEntry timeEntry, TimeEntry timeEntry2) {
                return timeEntry2.getClockOutDate().compareTo(timeEntry.getClockOutDate());
            }
        });

        return timeEntries;
    }

}

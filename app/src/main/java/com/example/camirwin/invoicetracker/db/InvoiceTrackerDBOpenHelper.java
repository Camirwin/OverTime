package com.example.camirwin.invoicetracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InvoiceTrackerDBOpenHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "invoice_tracker.db";
    private static final int DATABASE_VERSION = 1;

    // Clients table constants
    public static final String TABLE_CLIENTS = "clients";
    public static final String CLIENTS_ID = "_id";
    public static final String CLIENTS_NAME = "name";
    public static final String CLIENTS_LOCATION = "location";
    public static final String CLIENTS_CONTACT_FIRST_NAME = "contact_first_name";
    public static final String CLIENTS_CONTACT_LAST_NAME = "contact_last_name";
    public static final String CLIENTS_CONTACT_EMAIL = "contact_email";
    public static final String CLIENTS_CONTACT_PHONE = "contact_phone";
    public static final String CLIENTS_OUTSTANDING_BALANCE = "outstanding_balance";
    public static final String CLIENTS_LAST_INVOICE_DATE = "last_invoice_date";

    // Clients table create statement
    private static final String TABLE_CLIENTS_CREATE = "CREATE TABLE " + TABLE_CLIENTS + " ("
            + CLIENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CLIENTS_NAME + " TEXT NOT NULL, "
            + CLIENTS_LOCATION + " TEXT, "
            + CLIENTS_CONTACT_FIRST_NAME + " TEXT, "
            + CLIENTS_CONTACT_LAST_NAME + " TEXT, "
            + CLIENTS_CONTACT_EMAIL + " TEXT, "
            + CLIENTS_CONTACT_PHONE + " TEXT, "
            + CLIENTS_OUTSTANDING_BALANCE + " REAL, "
            + CLIENTS_LAST_INVOICE_DATE + " INTEGER" + ")";

    // Invoices table constants
    public static final String TABLE_INVOICES = "invoices";
    public static final String INVOICES_ID = "_id";
    public static final String INVOICES_CLIENT_ID = "client_id";
    public static final String INVOICES_CREATED_DATE = "created_date";
    public static final String INVOICES_SENT_DATE = "sent_date";
    public static final String INVOICES_PAYED_DATE = "payed_date";

    // Invoices table create statement
    private static final String TABLE_INVOICES_CREATE = "CREATE TABLE " + TABLE_INVOICES + " ("
            + INVOICES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + INVOICES_CLIENT_ID + " INTEGER NOT NULL, "
            + INVOICES_CREATED_DATE + " INTEGER, "
            + INVOICES_SENT_DATE + " INTEGER, "
            + INVOICES_PAYED_DATE + " INTEGER, "
            + "FOREIGN KEY(" + INVOICES_CLIENT_ID + ") REFERENCES " + TABLE_CLIENTS + "(_id) ON DELETE CASCADE" + ")";

    // Services table constants
    public static final String TABLE_SERVICES = "services";
    public static final String SERVICES_ID = "_id";
    public static final String SERVICES_CLIENT_ID = "client_id";
    public static final String SERVICES_INVOICE_ID = "invoice_id";
    public static final String SERVICES_NAME = "name";
    public static final String SERVICES_RATE = "rate";
    public static final String SERVICES_LAST_WORKED_DATE = "last_worked_date";
    public static final String SERVICES_OUTSTANDING_BALANCE = "outstanding_balance";

    // Services table create statement
    private static final String TABLE_SERVICES_CREATE = "CREATE TABLE " + TABLE_SERVICES + " ("
            + SERVICES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SERVICES_CLIENT_ID + " INTEGER NOT NULL, "
            + SERVICES_INVOICE_ID + " INTEGER, "
            + SERVICES_NAME + " TEXT NOT NULL, "
            + SERVICES_RATE + " REAL NOT NULL, "
            + SERVICES_LAST_WORKED_DATE + " INTEGER, "
            + SERVICES_OUTSTANDING_BALANCE + " REAL, "
            + "FOREIGN KEY(" + SERVICES_CLIENT_ID + ") REFERENCES " + TABLE_CLIENTS + "(_id) ON DELETE CASCADE, "
            + "FOREIGN KEY(" + SERVICES_INVOICE_ID + ") REFERENCES " + TABLE_INVOICES + "(_id)" + ")";

    public InvoiceTrackerDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CLIENTS_CREATE);
        database.execSQL(TABLE_INVOICES_CREATE);
        database.execSQL(TABLE_SERVICES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICES);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);

        onCreate(database);
    }
}

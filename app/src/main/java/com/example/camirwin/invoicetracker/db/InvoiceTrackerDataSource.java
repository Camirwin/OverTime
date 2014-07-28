package com.example.camirwin.invoicetracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InvoiceTrackerDataSource {

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

}

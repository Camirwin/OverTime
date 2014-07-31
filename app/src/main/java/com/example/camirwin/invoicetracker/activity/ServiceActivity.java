package com.example.camirwin.invoicetracker.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.camirwin.invoicetracker.R;
import com.example.camirwin.invoicetracker.adapter.TimeEntryAdapter;
import com.example.camirwin.invoicetracker.db.InvoiceTrackerDataSource;
import com.example.camirwin.invoicetracker.fragment.ServicesFragment;
import com.example.camirwin.invoicetracker.model.Services;
import com.example.camirwin.invoicetracker.model.TimeEntry;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ServiceActivity extends ListActivity {

    InvoiceTrackerDataSource dataSource;
    int serviceId;
    Services service;
    TimeEntry clockedInTimeEntry;
    ArrayList<TimeEntry> clockedOutTimeEntries;

    ActionBar actionBar;
    TextView tvServiceStatus;
    TextView tvServiceClockInOrOut;
    TextView tvServiceRate;
    TextView tvTimeEntryEarnedIncome;
    TextView tvListTotalBalance;
    Chronometer chronTimeEntryElapsedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        actionBar = getActionBar();

        tvServiceStatus = (TextView) findViewById(R.id.tvServiceStatus);
        tvServiceClockInOrOut = (TextView) findViewById(R.id.tvServiceClockInOrOut);
        tvServiceRate = (TextView) findViewById(R.id.tvServiceRate);
        tvTimeEntryEarnedIncome = (TextView) findViewById(R.id.tvTimeEntryEarnedIncome);
        tvListTotalBalance = (TextView) findViewById(R.id.tvListTotalBalance);
        chronTimeEntryElapsedTime = (Chronometer) findViewById(R.id.chronTimeEntryTimeElapsed);

        if (getIntent().getExtras() != null) {
            serviceId = getIntent().getIntExtra(ServicesFragment.SERVICE_ID, 0);
        }

        updateUI();
    }

    private void updateUI() {
        dataSource = new InvoiceTrackerDataSource(this);
        service = dataSource.getServiceById(serviceId);
        clockedInTimeEntry = dataSource.getClockedInEntryForClient(service.getClientId());
        clockedOutTimeEntries = dataSource.getAllClockedOutTimeEntriesForService(serviceId);

        actionBar.setTitle(service.getName());

        TimeEntryAdapter adapter = new TimeEntryAdapter(this, clockedOutTimeEntries);
        setListAdapter(adapter);

        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy, h:mm a", Locale.US);
        final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        tvListTotalBalance.setText("$" + decimalFormat.format(service.getOutstandingBalance()));

        if (clockedInTimeEntry != null && clockedInTimeEntry.getServiceId() == serviceId) {
            tvServiceStatus.setText("Clocked In");
            tvServiceClockInOrOut.setText("since " + dateFormat.format(clockedInTimeEntry.getClockInAsDateObject()));
            tvServiceRate.setText("$" + decimalFormat.format(clockedInTimeEntry.getRate()) + "/hour");

            chronTimeEntryElapsedTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer cArg) {
                    long time = System.currentTimeMillis() - clockedInTimeEntry.getClockInDate();
                    int h = (int) (time / 3600000);
                    int m = (int) (time - h * 3600000) / 60000;
                    int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                    String hString = h == 0 ? "" : h + "h ";
                    String mString = m == 0 && h == 0 ? "" : m + "m ";
                    String sString = s + "s";
                    String timeElapsed = hString + mString + sString;
                    cArg.setText(timeElapsed);
                    tvTimeEntryEarnedIncome.setText("$" + decimalFormat.format(clockedInTimeEntry.getEarnedIncome()));
                }
            });
            chronTimeEntryElapsedTime.start();
        } else {
            tvServiceStatus.setText("Clocked Out");
            if (!service.getLastWorkedDate().equals(Long.valueOf(0))) {
                tvServiceClockInOrOut.setText("since " + dateFormat.format(service.getLastWorkedAsDateObject()));
            } else {
                tvServiceClockInOrOut.setText("never worked");
            }
            tvServiceRate.setText("$" + decimalFormat.format(service.getRate()) + "/hour");

            chronTimeEntryElapsedTime.setText("0h 0m 0s");
            tvTimeEntryEarnedIncome.setText("$0.00");
            chronTimeEntryElapsedTime.stop();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.service, menu);
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
        } else if (id == R.id.action_clock_in_or_out) {
            if (clockedInTimeEntry != null && clockedInTimeEntry.getServiceId() == serviceId) {
                new AlertDialog.Builder(this)
                        .setTitle("Clock Out")
                        .setMessage("Would you like to clock out of this service?")
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }
                        )
                        .setPositiveButton("Clock Out",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        clockedInTimeEntry.setClockOutDate(System.currentTimeMillis());
                                        dataSource.updateTimeEntry(clockedInTimeEntry);
                                        updateUI();
                                        dialog.cancel();
                                    }
                                }
                        ).create().show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Clock In")
                        .setMessage("Would you like to clock in to this service?")
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }
                        )
                        .setPositiveButton("Clock In",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        TimeEntry timeEntry = new TimeEntry();
                                        timeEntry.setClientId(service.getClientId());
                                        timeEntry.setServiceId(serviceId);
                                        timeEntry.setRate(service.getRate());
                                        timeEntry.setClockInDate(System.currentTimeMillis());
                                        dataSource.createTimeEntry(timeEntry);

                                        updateUI();
                                        dialog.cancel();
                                    }
                                }
                        ).create().show();
            }
        } else if (id == R.id.action_delete_service) {
            final Context context = this;
            final Activity activity = this;
            new AlertDialog.Builder(this)
                    .setTitle("Delete Service")
                    .setMessage("Would you like to delete this service? Doing so will remove all "
                            + "time entries attached to this service. This action cannot be undone.")
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }
                    )
                    .setPositiveButton("Delete Service",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    InvoiceTrackerDataSource dataSource = new InvoiceTrackerDataSource(context);
                                    dataSource.deleteService(service);
                                    dataSource.close();
                                    dialog.cancel();
                                    NavUtils.navigateUpFromSameTask(activity);
                                }
                            }
                    ).create().show();
        }
        return super.onOptionsItemSelected(item);
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
    }
}

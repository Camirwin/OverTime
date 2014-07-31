package com.example.camirwin.invoicetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.camirwin.invoicetracker.R;
import com.example.camirwin.invoicetracker.model.TimeEntry;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TimeEntryAdapter extends ArrayAdapter<TimeEntry> {

    private final Context context;
    private ArrayList<TimeEntry> timeEntries;

    public TimeEntryAdapter(Context context, ArrayList<TimeEntry> timeEntries) {
        super(context, R.layout.row_time_entry, timeEntries);

        this.context = context;
        this.timeEntries = timeEntries;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_time_entry, parent, false);

        TextView tvTimeEntryClockInDate = (TextView) rowView.findViewById(R.id.tvTimeEntryClockInDate);
        TextView tvTimeEntryClockInAndOut = (TextView) rowView.findViewById(R.id.tvTimeEntryClockInAndOut);
        TextView tvTimeEntryEarnedIncome = (TextView) rowView.findViewById(R.id.tvTimeEntryEarnedIncome);
        TextView tvTimeEntryTimeElapsed = (TextView) rowView.findViewById(R.id.tvTimeEntryElapsedTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        TimeEntry timeEntry = timeEntries.get(position);

        tvTimeEntryClockInDate.setText(dateFormat.format(timeEntry.getClockInAsDateObject()));
        tvTimeEntryClockInAndOut.setText(timeFormat.format(timeEntry.getClockInAsDateObject()) + " - " + timeFormat.format(timeEntry.getClockOutAsDateObject()));
        tvTimeEntryEarnedIncome.setText("$" + decimalFormat.format(timeEntry.getEarnedIncome()));

        long time = timeEntry.getClockOutDate() - timeEntry.getClockInDate();
        int h = (int) (time / 3600000);
        int m = (int) (time - h * 3600000) / 60000;
        int s = (int) (time - h * 3600000 - m * 60000) / 1000;
        String hString = h == 0 ? "" : h + "h ";
        String mString = m == 0 && h == 0 ? "" : m + "m ";
        String sString = s + "s";
        String timeElapsed = hString + mString + sString;

        tvTimeEntryTimeElapsed.setText(timeElapsed);

        return rowView;
    }

}

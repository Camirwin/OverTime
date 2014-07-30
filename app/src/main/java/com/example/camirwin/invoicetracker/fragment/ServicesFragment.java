package com.example.camirwin.invoicetracker.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.camirwin.invoicetracker.R;
import com.example.camirwin.invoicetracker.activity.ServiceCreateActivity;
import com.example.camirwin.invoicetracker.adapter.ServiceAdapter;
import com.example.camirwin.invoicetracker.db.InvoiceTrackerDataSource;
import com.example.camirwin.invoicetracker.model.Services;
import com.example.camirwin.invoicetracker.model.TimeEntry;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServicesFragment.OnServiceInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ServicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ServicesFragment extends ListFragment {

    public static final String CLIENT_ID = "clientId";

    int clientId;
    InvoiceTrackerDataSource dataSource;
    ArrayList<Services> services;
    TimeEntry clockedInTimeEntry;
    Services clockedInService;
    LinearLayout llClockedInServiceLayout;
    Button btnClockOut;
    TextView tvServiceName;
    TextView tvTimeEntryClockIn;
    TextView tvTimeEntryRate;
    TextView tvTimeEntryEarnedIncome;
    Chronometer chronTimeEntryTimeElapsed;

    private OnServiceInteractionListener mListener;

    public static ServicesFragment newInstance(int clientId) {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        args.putInt(CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    public ServicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            clientId = getArguments().getInt(CLIENT_ID);
        }

        dataSource = new InvoiceTrackerDataSource(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.services, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_service:
                Intent intent = new Intent(getActivity(), ServiceCreateActivity.class);
                intent.putExtra(CLIENT_ID, clientId);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final Services service = services.get(position);

        new AlertDialog.Builder(getActivity())
                .setTitle("Clock In")
                .setMessage("Would you like to clock in to the service \"" + service.getName() + "\"?")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Clock In",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                TimeEntry timeEntry = new TimeEntry();
                                timeEntry.setClientId(clientId);
                                timeEntry.setServiceId(service.getId());
                                timeEntry.setClockInDate(System.currentTimeMillis());
                                timeEntry.setRate(service.getRate());

                                dataSource.createTimeEntry(timeEntry);

                                updateUI();
                                dialog.cancel();
                            }
                        }
                ).create().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_services, container, false);

        tvServiceName = (TextView) layout.findViewById(R.id.tvServiceName);
        tvTimeEntryClockIn = (TextView) layout.findViewById(R.id.tvTimeEntryClockIn);
        tvTimeEntryRate = (TextView) layout.findViewById(R.id.tvTimeEntryRate);
        tvTimeEntryEarnedIncome = (TextView) layout.findViewById(R.id.tvTimeEntryEarnedIncome);
        chronTimeEntryTimeElapsed = (Chronometer) layout.findViewById(R.id.chronTimeEntryTimeElapsed);

        llClockedInServiceLayout = (LinearLayout) layout.findViewById(R.id.llClockedInServiceLayout);
        btnClockOut = (Button) layout.findViewById(R.id.btnClockOut);
        btnClockOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clockOut();
            }
        });

        return layout;
    }

    public void clockOut() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Clock Out")
                .setMessage("Would you like to clock out of the service \"" + clockedInService.getName() + "\"?")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Clock Out",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                clockedInTimeEntry.setClockOutDate(System.currentTimeMillis());
                                dataSource.updateTimeEntry(clockedInTimeEntry);
                                mListener.onChangeInServices();
                                updateUI();
                                dialog.cancel();
                            }
                        }
                ).create().show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnServiceInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnServiceInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        Log.i("InvoiceTracker", "Service fragment paused");
        super.onPause();
        dataSource.close();
    }

    @Override
    public void onResume() {
        Log.i("InvoiceTracker", "Service fragment resumed");
        super.onResume();
        dataSource.open();



        updateUI();
    }

    private void updateUI() {
        clockedInTimeEntry = dataSource.getClockedInEntryForClient(clientId);
        services = dataSource.getAllServicesForClient(clientId);
        if (clockedInTimeEntry == null) {
            clockedInService = new Services();
            if (services.isEmpty()) {
                ((TextView) getListView().getEmptyView()).setText("You have not created any services for this client. Select the add button at the top of the screen to create one.");
            }
        } else {
            for (Services s : services) {
                if (s.getId() == clockedInTimeEntry.getServiceId()) {
                    clockedInService = s;
                    break;
                }
            }
            services.remove(clockedInService);
            if (services.isEmpty()) {
                ((TextView) getListView().getEmptyView()).setText("No off the clock services");
            }
        }

        if (clockedInTimeEntry == null) {
            llClockedInServiceLayout.setVisibility(View.GONE);
            chronTimeEntryTimeElapsed.stop();
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy, h:mm a", Locale.US);
            final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

            llClockedInServiceLayout.setVisibility(View.VISIBLE);
            tvServiceName.setText(clockedInService.getName());
            tvTimeEntryClockIn.setText("since " + dateFormat.format(clockedInTimeEntry.getClockInAsDateObject()));
            tvTimeEntryRate.setText("$" + decimalFormat.format(clockedInTimeEntry.getRate()) + "/hour");

            chronTimeEntryTimeElapsed.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer cArg) {
                    long time = System.currentTimeMillis() - clockedInTimeEntry.getClockInDate();
                    int h = (int) (time / 3600000);
                    int m = (int) (time - h * 3600000) / 60000;
                    int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                    String hString = h == 0 ? "" : h + "h ";
                    String mString = m == 0 ? "" : m + "m ";
                    String sString = s + "s";
                    String timeElapsed = hString + mString + sString;
                    cArg.setText(timeElapsed);
                    tvTimeEntryEarnedIncome.setText("$" + decimalFormat.format(clockedInTimeEntry.getEarnedIncome()));
                }
            });

            chronTimeEntryTimeElapsed.start();
        }

        ServiceAdapter adapter = new ServiceAdapter(getActivity(), services);
        setListAdapter(adapter);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnServiceInteractionListener {
        public void onChangeInServices();
    }

}

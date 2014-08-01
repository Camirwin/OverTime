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
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.camirwin.invoicetracker.R;
import com.example.camirwin.invoicetracker.activity.ServiceActivity;
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
 */
public class ServicesFragment extends ListFragment {

    public static final String CLIENT_ID = "clientId";
    public static final String SERVICE_ID = "serviceId";

    int clientId;
    InvoiceTrackerDataSource dataSource;
    ArrayList<Services> services;
    TimeEntry clockedInTimeEntry;
    Services clockedInService;
    LinearLayout layout;
    LinearLayout llClockedInServiceLayout;
    TextView tvClockedInServiceHeader;
    Button btnClockOut;
    TextView tvServiceName;
    TextView tvTimeEntryClockIn;
    TextView tvTimeEntryRate;
    TextView tvTimeEntryEarnedIncome;
    Chronometer chronTimeEntryTimeElapsed;
    ActionMode actionMode;
    ServiceAdapter adapter;

    private OnServiceInteractionListener mListener;

    public ServicesFragment() {
        // Required empty public constructor
    }

    public static ServicesFragment newInstance(int clientId) {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        args.putInt(CLIENT_ID, clientId);
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
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
        Intent intent = new Intent(getActivity(), ServiceActivity.class);
        intent.putExtra(SERVICE_ID, services.get(position).getId());
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (layout == null) {
            layout = (LinearLayout) inflater.inflate(R.layout.fragment_services, container, false);

            tvServiceName = (TextView) layout.findViewById(R.id.tvServiceName);
            tvTimeEntryClockIn = (TextView) layout.findViewById(R.id.tvTimeEntryClockIn);
            tvTimeEntryRate = (TextView) layout.findViewById(R.id.tvTimeEntryRate);
            tvTimeEntryEarnedIncome = (TextView) layout.findViewById(R.id.tvTimeEntryEarnedIncome);
            chronTimeEntryTimeElapsed = (Chronometer) layout.findViewById(R.id.chronTimeEntryTimeElapsed);
            tvClockedInServiceHeader = (TextView) layout.findViewById(R.id.tvClockedInServiceHeader);

            llClockedInServiceLayout = (LinearLayout) layout.findViewById(R.id.llClockedInServiceLayout);
            llClockedInServiceLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClockedInServiceClick();
                }
            });

            btnClockOut = (Button) layout.findViewById(R.id.btnClockOut);
            btnClockOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clockOut();
                }
            });
        }

        return layout;
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
        super.onPause();
        dataSource.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        dataSource.open();
        updateUI();
    }

    private void updateUI() {
        clockedInTimeEntry = dataSource.getClockedInEntryForClient(clientId);
        services = dataSource.getAllServicesForClient(clientId);

        setupServiceList();
        setupHeader();
    }

    private void setupServiceList() {
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

        if (adapter == null || actionMode == null) {
            adapter = new ServiceAdapter(getActivity(), services);
            setListAdapter(adapter);
        }

        if (actionMode == null) {
            getListView().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    mode.invalidate();
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.service_contextual, menu);
                    actionMode = mode;
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    MenuItem actionClockIn = menu.findItem(R.id.action_clock_in);
                    MenuItem actionView = menu.findItem(R.id.action_view);
                    MenuItem actionEdit = menu.findItem(R.id.action_edit);
                    MenuItem actionCopy = menu.findItem(R.id.action_copy);

                    Boolean oneSelected = getListView().getCheckedItemCount() == 1;

                    actionClockIn.setVisible(oneSelected);
                    actionView.setVisible(oneSelected);
                    actionEdit.setVisible(oneSelected);
                    actionCopy.setVisible(oneSelected);

                    mode.setTitle(getListView().getCheckedItemCount() + " selected");

                    return true;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.action_clock_in:
                            clockIn();
                            return true;
                        case R.id.action_delete:
                            deleteSelected();
                            return true;
                        case R.id.action_view:
                            viewSelected();
                            return true;
                        case R.id.action_copy:
                            return true;
                        case R.id.action_edit:
                            return true;
                        case R.id.action_select_all:
                            selectAll();
                            return true;
                    }

                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    actionMode = null;
                }
            });
        }
    }

    private void setupHeader() {
        if (clockedInTimeEntry == null) {
            tvClockedInServiceHeader.setVisibility(View.GONE);
            llClockedInServiceLayout.setVisibility(View.GONE);
            chronTimeEntryTimeElapsed.stop();
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy, h:mm a", Locale.US);
            final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

            tvClockedInServiceHeader.setVisibility(View.VISIBLE);
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
                    String mString = m == 0 && h == 0 ? "" : m + "m ";
                    String sString = s + "s";
                    String timeElapsed = hString + mString + sString;
                    cArg.setText(timeElapsed);
                    tvTimeEntryEarnedIncome.setText("$" + decimalFormat.format(clockedInTimeEntry.getEarnedIncome()));
                }
            });

            chronTimeEntryTimeElapsed.start();
        }
    }

    public void clockOut() {
        if (actionMode != null) {
            actionMode.finish();
        }
        new AlertDialog.Builder(getActivity())
                .setTitle("Clock Out")
                .setMessage("Would you like to clock out of the service \"" + clockedInService.getName() + "\"?")
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
                                mListener.onChangeInServices();
                                updateUI();
                                dialog.cancel();
                            }
                        }
                ).create().show();
    }

    private void clockIn() {
        final ActionMode finalMode = actionMode;
        new AlertDialog.Builder(getActivity())
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
                                Services serviceToClockIn = new Services();
                                SparseBooleanArray selectedList = getListView().getCheckedItemPositions();
                                for (Services service : services) {
                                    if (selectedList.get(services.indexOf(service))) {
                                        serviceToClockIn = service;
                                        break;
                                    }
                                }

                                TimeEntry timeEntry = new TimeEntry();
                                timeEntry.setClientId(serviceToClockIn.getClientId());
                                timeEntry.setServiceId(serviceToClockIn.getId());
                                timeEntry.setRate(serviceToClockIn.getRate());
                                timeEntry.setClockInDate(System.currentTimeMillis());
                                dataSource.createTimeEntry(timeEntry);

                                dialog.cancel();
                                finalMode.finish();
                                updateUI();
                            }
                        }
                ).create().show();
    }

    private void deleteSelected() {
        final ActionMode finalMode = actionMode;
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Selected Services")
                .setMessage("Would you like to delete the selected services? Doing so will remove all "
                        + "time entries attached to these services. This action cannot be undone.")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }
                )
                .setPositiveButton("Delete Services",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SparseBooleanArray selectedList = getListView().getCheckedItemPositions();
                                for (Services service : services) {
                                    if (selectedList.get(services.indexOf(service))) {
                                        dataSource.deleteService(service);
                                    }
                                }
                                mListener.onChangeInServices();
                                dialog.cancel();
                                finalMode.finish();
                                updateUI();
                            }
                        }
                ).create().show();
    }

    private void viewSelected() {
        Services serviceToView = new Services();
        SparseBooleanArray selectedList = getListView().getCheckedItemPositions();
        for (Services service : services) {
            if (selectedList.get(services.indexOf(service))) {
                serviceToView = service;
                break;
            }
        }
        Intent intent = new Intent(getActivity(), ServiceActivity.class);
        intent.putExtra(SERVICE_ID, serviceToView.getId());
        startActivity(intent);
        actionMode.finish();
    }

    public void onClockedInServiceClick() {
        Intent intent = new Intent(getActivity(), ServiceActivity.class);
        intent.putExtra(SERVICE_ID, clockedInService.getId());
        startActivity(intent);
    }

    public void selectAll() {
        getListView().clearChoices();
        for (int i = 0; i < services.size(); i++) {
            getListView().setItemChecked(i, true);
        }
    }

    public void endActionMode() {
        if (actionMode != null) {
            actionMode.finish();
            actionMode = null;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnServiceInteractionListener {
        public void onChangeInServices();
    }

}

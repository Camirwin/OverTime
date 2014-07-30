package com.example.camirwin.invoicetracker.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.camirwin.invoicetracker.R;
import com.example.camirwin.invoicetracker.adapter.ClientAdapter;
import com.example.camirwin.invoicetracker.adapter.InvoiceAdapter;
import com.example.camirwin.invoicetracker.db.InvoiceTrackerDataSource;
import com.example.camirwin.invoicetracker.model.Client;

import java.text.DecimalFormat;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.example.camirwin.invoicetracker.fragment.ExpensesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.example.camirwin.invoicetracker.fragment.ExpensesFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class OverviewFragment extends ListFragment {

    private static final String CLIENT_ID = "clientId";

    int clientId;
    InvoiceTrackerDataSource dataSource;

    private OnFragmentInteractionListener mListener;

    public static OverviewFragment newInstance(int clientId) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putInt(CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    public OverviewFragment() {
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
        inflater.inflate(R.menu.overview, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_invoice:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_overview, container, false);

        String[] list = new String[] { "temp" };
        InvoiceAdapter adapter = new InvoiceAdapter(getActivity(), list);
        setListAdapter(adapter);

        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        Log.i("InvoiceTracker", "Overview fragment paused");

        super.onPause();
        dataSource.close();
    }

    @Override
    public void onResume() {
        Log.i("InvoiceTracker", "Overview fragment resumed");

        super.onResume();
        dataSource.open();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}


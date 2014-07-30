package com.example.camirwin.invoicetracker.fragment;

import android.app.Activity;
import android.app.ListFragment;
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
import android.widget.LinearLayout;

import com.example.camirwin.invoicetracker.R;
import com.example.camirwin.invoicetracker.activity.ServiceCreateActivity;
import com.example.camirwin.invoicetracker.adapter.ServiceAdapter;
import com.example.camirwin.invoicetracker.db.InvoiceTrackerDataSource;
import com.example.camirwin.invoicetracker.model.Services;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServicesFragment.OnFragmentInteractionListener} interface
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

    private OnFragmentInteractionListener mListener;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("InvoiceTracker", "Service fragment create view");
        // Inflate the layout for this fragment
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_services, container, false);

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
        Log.i("InvoiceTracker", "Service fragment paused");
        super.onPause();
        dataSource.close();
    }

    @Override
    public void onResume() {
        Log.i("InvoiceTracker", "Service fragment resumed");
        super.onResume();

        dataSource.open();
        services = dataSource.getAllServicesForClient(clientId);

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

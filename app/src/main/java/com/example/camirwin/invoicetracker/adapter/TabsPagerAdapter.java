package com.example.camirwin.invoicetracker.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.camirwin.invoicetracker.fragment.DeliverablesFragment;
import com.example.camirwin.invoicetracker.fragment.ExpensesFragment;
import com.example.camirwin.invoicetracker.fragment.OverviewFragment;
import com.example.camirwin.invoicetracker.fragment.ServicesFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    int clientId;

    public TabsPagerAdapter(FragmentManager fm, int clientId) {
        super(fm);
        this.clientId = clientId;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("InvoiceTracker", "getItem called");
        switch (position) {
            case 0:
                return OverviewFragment.newInstance(clientId);
            case 1:
                return ServicesFragment.newInstance(clientId);
            case 2:
                return new DeliverablesFragment();
            case 3:
                return new ExpensesFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}

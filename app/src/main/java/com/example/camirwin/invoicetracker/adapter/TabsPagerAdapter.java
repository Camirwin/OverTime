package com.example.camirwin.invoicetracker.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.example.camirwin.invoicetracker.fragment.DeliverablesFragment;
import com.example.camirwin.invoicetracker.fragment.ExpensesFragment;
import com.example.camirwin.invoicetracker.fragment.OverviewFragment;
import com.example.camirwin.invoicetracker.fragment.ServicesFragment;
import com.example.camirwin.invoicetracker.model.Client;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    Client client;

    public TabsPagerAdapter(FragmentManager fm, Client client) {
        super(fm);
        this.client = client;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OverviewFragment(client);
            case 1:
                return new ServicesFragment();
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

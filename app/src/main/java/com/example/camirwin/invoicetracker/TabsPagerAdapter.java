package com.example.camirwin.invoicetracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OverviewFragment();
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

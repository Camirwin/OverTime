package com.example.camirwin.invoicetracker.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.camirwin.invoicetracker.db.InvoiceTrackerDataSource;
import com.example.camirwin.invoicetracker.fragment.DeliverablesFragment;
import com.example.camirwin.invoicetracker.fragment.ExpensesFragment;
import com.example.camirwin.invoicetracker.fragment.OverviewFragment;
import com.example.camirwin.invoicetracker.R;
import com.example.camirwin.invoicetracker.fragment.ServicesFragment;
import com.example.camirwin.invoicetracker.adapter.TabsPagerAdapter;
import com.example.camirwin.invoicetracker.model.Client;

public class ClientActivity extends FragmentActivity implements ActionBar.TabListener,
        DeliverablesFragment.OnFragmentInteractionListener,
        OverviewFragment.OnFragmentInteractionListener,
        ExpensesFragment.OnFragmentInteractionListener,
        ServicesFragment.OnServiceInteractionListener {

    ActionBar actionBar;
    int clientId;
    String clientName;
    String[] tabs = {"OVERVIEW", "SERVICES", "DELIVERABLES", "EXPENSES"};
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private TabsPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        if (getIntent().getExtras() != null) {
            clientId = getIntent().getIntExtra(ClientsActivity.CLIENT_ID, 0);
            clientName = getIntent().getStringExtra(ClientsActivity.CLIENT_NAME);
        }

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabsPagerAdapter(getFragmentManager(), clientId);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);

        actionBar = getActionBar();
        actionBar.setTitle(clientName);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab).setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.client, menu);
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
        } else if (id == R.id.action_delete_client) {
            final Context context = this;
            final Activity activity = this;
            new AlertDialog.Builder(this)
                    .setTitle("Delete Client")
                    .setMessage("Would you like to delete this client? Doing so will remove all "
                            + "invoices, services, time entries, deliverables, and expenses attached "
                            + "to this client. This action cannot be undone.")
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }
                    )
                    .setPositiveButton("Delete Client",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    InvoiceTrackerDataSource dataSource = new InvoiceTrackerDataSource(context);
                                    dataSource.deleteClient(clientId);
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
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // on tab selected
        // show respected fragment view
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (tab.getPosition() == 1) {
            ServicesFragment servicesFragment = (ServicesFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 1);
            servicesFragment.endActionMode();
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onChangeInServices() {
        OverviewFragment overviewFragment = (OverviewFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
        overviewFragment.updateUI();
    }
}

package com.example.camirwin.invoicetracker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.camirwin.invoicetracker.R;
import com.example.camirwin.invoicetracker.db.InvoiceTrackerDataSource;
import com.example.camirwin.invoicetracker.fragment.ServicesFragment;
import com.example.camirwin.invoicetracker.model.Services;

public class ServiceCreateActivity extends Activity {

    int clientId;
    EditText etServiceName;
    EditText etServiceRate;

    InvoiceTrackerDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_create);

        dataSource = new InvoiceTrackerDataSource(this);

        clientId = getIntent().getIntExtra(ServicesFragment.CLIENT_ID, 0);
        etServiceName = (EditText) findViewById(R.id.etServiceName);
        etServiceRate = (EditText) findViewById(R.id.etServiceRate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.service_create, menu);
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
        }
        else if (id == R.id.action_create_service) {
            if(!etServiceName.getText().toString().isEmpty() && !etServiceRate.getText().toString().isEmpty()) {
                Services service = new Services();
                service.setClientId(clientId);
                service.setName(etServiceName.getText().toString());
                service.setRate(Double.valueOf(etServiceRate.getText().toString()));

                dataSource.createService(service);

                NavUtils.navigateUpFromSameTask(this);
            } else {
                if (etServiceName.getText().toString().isEmpty()) {
                    etServiceName.setError("Service name required");
                }
                if (etServiceRate.getText().toString().isEmpty()) {
                    etServiceRate.setError("Service rate required");
                }
            }
        }
        else if (id == R.id.action_cancel) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}

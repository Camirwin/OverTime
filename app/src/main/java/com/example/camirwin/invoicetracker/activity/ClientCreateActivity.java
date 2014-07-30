package com.example.camirwin.invoicetracker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.camirwin.invoicetracker.R;
import com.example.camirwin.invoicetracker.db.InvoiceTrackerDataSource;
import com.example.camirwin.invoicetracker.model.Client;

public class ClientCreateActivity extends Activity {

    EditText etClientName;
    EditText etClientLocation;
    EditText etClientContactFirstName;
    EditText etClientContactLastName;
    EditText etClientContactEmail;
    EditText etClientContactPhone;

    InvoiceTrackerDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_create);

        dataSource = new InvoiceTrackerDataSource(this);

        etClientName = (EditText) findViewById(R.id.etClientName);
        etClientLocation = (EditText) findViewById(R.id.etClientLocation);
        etClientContactFirstName = (EditText) findViewById(R.id.etClientContactFirstName);
        etClientContactLastName = (EditText) findViewById(R.id.etClientContactLastName);
        etClientContactEmail = (EditText) findViewById(R.id.etClientContactEmail);
        etClientContactPhone = (EditText) findViewById(R.id.etClientContactPhone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.client_create, menu);
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
        else if (id == R.id.action_create_client) {
            if (!etClientName.getText().toString().isEmpty()) {
                Client client = new Client();
                client.setName(etClientName.getText().toString());
                client.setLocation(etClientLocation.getText().toString());
                client.setContactFirstName(etClientContactFirstName.getText().toString());
                client.setContactLastName(etClientContactLastName.getText().toString());
                client.setContactEmail(etClientContactEmail.getText().toString());
                client.setContactPhone(etClientContactPhone.getText().toString());

                dataSource.createClient(client);

                NavUtils.navigateUpFromSameTask(this);
            } else {
                etClientName.setError("Client name required");
            }
        }
        else if (id == R.id.action_cancel) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
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
    }

}

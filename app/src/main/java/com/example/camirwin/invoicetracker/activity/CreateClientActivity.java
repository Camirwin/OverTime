package com.example.camirwin.invoicetracker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.camirwin.invoicetracker.R;

public class CreateClientActivity extends Activity {

    TextView tvClientName;
    EditText etClientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_create);

        tvClientName = (TextView) findViewById(R.id.tvClientName);
        etClientName = (EditText) findViewById(R.id.etClientName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_client, menu);
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
}

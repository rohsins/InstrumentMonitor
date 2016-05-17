package com.instrumentmonitor.rohsins.instrumentmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Home extends Sockets {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intentSettings = new Intent(this, Settings.class);
            startActivity(intentSettings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void bkPrecisionMultimeter(View view) {
        Intent intentBKPrecisionMultimeter = new Intent(this, BKPrecisionMeter.class);
        startActivity(intentBKPrecisionMultimeter);
    }

    public void serialViewer(View view) {
        Intent intentSerialViewer = new Intent(this, SerialViewer.class);
        startActivity(intentSerialViewer);
    }
}

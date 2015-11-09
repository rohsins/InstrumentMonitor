package com.instrumentmonitor.rohsins.instrumentmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Home extends Sockets{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        on_create_func();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

    public void settings(View view) {
        Intent intentSettings = new Intent(this, Settings.class);
        startActivity(intentSettings);
    }
}

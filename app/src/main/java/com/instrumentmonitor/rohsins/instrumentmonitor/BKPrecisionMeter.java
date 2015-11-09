package com.instrumentmonitor.rohsins.instrumentmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


public class BKPrecisionMeter extends Sockets {

    TextView textViewVoltage;
    TextView textViewCurrent;
    TextView textViewResistance;
    TextView textViewInductance;
    TextView textViewCapacitance;
    Switch sync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkprecision_meter);

        on_create_func();

        textViewVoltage = (TextView) findViewById(R.id.textViewValue1);
        textViewCurrent = (TextView) findViewById(R.id.textViewValue2);
        textViewResistance = (TextView) findViewById(R.id.textViewValue3);
        textViewInductance = (TextView) findViewById(R.id.textViewValue4);
        textViewCapacitance = (TextView) findViewById(R.id.textViewValue5);
        sync = (Switch) findViewById(R.id.syncSwitch);

        SharedPreferences settings = getSharedPreferences("msettings",0);
        sync.setChecked(settings.getBoolean("SYNCSWITCH", false));

        sync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences settings = getSharedPreferences("msettings", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("SYNCSWITCH", sync.isChecked());
                editor.commit();
                //sync.setEnabled(settings.getBoolean("SYNCSWITCH", false));
                if (!settings.getBoolean("SYNCSWITCH", true)) {
                    exchangeData("SYNCOFF");
                    textViewVoltage.setText("SyncOff");
                } else if (settings.getBoolean("SYNCSWITCH", true)) {
                    exchangeData("SYNCON");
                    textViewVoltage.setText("SyncOn");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bkprecision_meter, menu);
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
}

package com.instrumentmonitor.rohsins.instrumentmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class BKPrecisionMeter extends Sockets {

    TextView textViewVoltage;
    TextView textViewCurrent;
    TextView textViewResistance;
    TextView textViewInductance;
    TextView textViewCapacitance;
    Switch sync;
    EditText editTextUpdateRate;

    int i;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            textViewResistance.setText(String.valueOf(i));
            i++;
            exchangeData("sendMeAllParameters");
        }
    };

    Handler handler = new Handler();
    TimerTask timerTask;
    Timer timer;

    public void startTransfer(int period) {
        try {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        handler.post(runnable);
                    } catch (Exception e ) {
                        e.printStackTrace();
                    }
                }
            };
            timer = new Timer("updateTimer");
            timer.scheduleAtFixedRate(timerTask, 1, period);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopTransfer() {
        timerTask.cancel();
        timer.purge();
        timer.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkprecision_meter);

        textViewVoltage = (TextView) findViewById(R.id.textViewValue1);
        textViewCurrent = (TextView) findViewById(R.id.textViewValue2);
        textViewResistance = (TextView) findViewById(R.id.textViewValue3);
        textViewInductance = (TextView) findViewById(R.id.textViewValue4);
        textViewCapacitance = (TextView) findViewById(R.id.textViewValue5);
        sync = (Switch) findViewById(R.id.syncSwitch);
        editTextUpdateRate = (EditText) findViewById(R.id.editTextUpdateRate);

        SharedPreferences settings = getSharedPreferences("msettings", 0);
        on_create_func();
        editTextUpdateRate.setText(String.valueOf(settings.getInt("UPDATERATE", 1)));
        if (!settings.getBoolean("SYNCSWITCH", false)) {
            editTextUpdateRate.setEnabled(true);
        } else if (settings.getBoolean("SYNCSWITCH", false)) {
            editTextUpdateRate.setEnabled(false);
        }
        sync.setChecked(settings.getBoolean("SYNCSWITCH", false));

        sync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences settings = getSharedPreferences("msettings", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("UPDATERATE", Integer.parseInt(editTextUpdateRate.getText().toString()));
                editor.putBoolean("SYNCSWITCH", sync.isChecked());
                editor.commit();
                //sync.setEnabled(settings.getBoolean("SYNCSWITCH", false));
                if (!settings.getBoolean("SYNCSWITCH", false)) {
                    editTextUpdateRate.setEnabled(true);
//                    exchangeData("SYNCOFF");
                    textViewVoltage.setText("SyncOff");
                    stopTransfer();
                } else if (settings.getBoolean("SYNCSWITCH", false)) {
                    editTextUpdateRate.setEnabled(false);
//                    exchangeData("SYNCON");
                    textViewVoltage.setText("SyncOn");
                    startTransfer(settings.getInt("UPDATERATE", 1000));
                }
            }
        });
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        textViewVoltage.setText("Syti");
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

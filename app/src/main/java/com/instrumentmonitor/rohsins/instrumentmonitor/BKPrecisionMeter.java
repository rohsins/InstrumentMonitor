package com.instrumentmonitor.rohsins.instrumentmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    TextView textViewActiveParamValue;
    Switch sync;
    EditText editTextUpdateRate;

    static int paramValueCheck = 0;

//    int i;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            textViewResistance.setText(String.valueOf(i));
//            i++;
            switch (paramValueCheck) {
                case 0:
                    exchangeData("0xd0");
                    break;
                case 1:
                    exchangeData("0xd1");
                    break;
                case 2:
                    exchangeData("0xd2");
                    break;
                case 3:
                    exchangeData("0xd3");
                    break;
                case 4:
                    exchangeData("0xd4");
                    break;
            }
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
        textViewActiveParamValue = (TextView) findViewById(R.id.textViewValue6);
        sync = (Switch) findViewById(R.id.syncSwitch);
        editTextUpdateRate = (EditText) findViewById(R.id.editTextUpdateRate);

        SharedPreferences settings = getSharedPreferences("msettings", 0);
        on_create_func();
        paramValueCheck = 0;
        textViewActiveParamValue.setText("Voltage");
        editTextUpdateRate.setText(String.valueOf(settings.getInt("UPDATERATE", 1000)));
        if (!settings.getBoolean("SYNCSWITCH", false)) {
            editTextUpdateRate.setEnabled(true);
        } else if (settings.getBoolean("SYNCSWITCH", false)) {
            editTextUpdateRate.setEnabled(false);
        }
        sync.setChecked(settings.getBoolean("SYNCSWITCH", false));


        /*
         to fix app crash because of calling stopTransfer() before startTransfer();
         not a good fix. work on it later.
         */
        startTransfer(settings.getInt("UPDATERATE", 1000));
        stopTransfer();

        if (settings.getBoolean("SYNCSWITCH", false)) {
            startTransfer(settings.getInt("UPDATERATE", 1000));
        }

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
                    stopTransfer();
                    editTextUpdateRate.setEnabled(true);
//                    exchangeData("SYNCOFF");
//                    textViewVoltage.setText("SyncOff");
                } else if (settings.getBoolean("SYNCSWITCH", false)) {
                    startTransfer(settings.getInt("UPDATERATE", 1000));
                    editTextUpdateRate.setEnabled(false);
//                    exchangeData("SYNCON");
//                    textViewVoltage.setText("SyncOn");
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTransfer();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        startTransfer(settings.getInt("UPDATERATE", 1000));
//    }

    @Override
    public void onStop() {
        super.onStop();
        stopTransfer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTransfer();

    }

    public void volFunc(View view) {
        paramValueCheck = 0;
        textViewActiveParamValue.setText("Voltage");
    }

    public void curFunc(View view) {
        paramValueCheck = 1;
        textViewActiveParamValue.setText("Current");

    }

    public void resFunc(View view) {
        paramValueCheck = 2;
        textViewActiveParamValue.setText("Resistance");

    }

    public void indFunc(View view) {
        paramValueCheck = 3;
        textViewActiveParamValue.setText("Inductance");

    }

    public void capFunc(View view) {
        paramValueCheck = 4;
        textViewActiveParamValue.setText("Capacitance");

    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
//        textViewVoltage.setText("Syti"); // just for testing if it enters in menu or not
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

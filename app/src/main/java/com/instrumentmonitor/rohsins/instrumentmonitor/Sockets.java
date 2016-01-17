package com.instrumentmonitor.rohsins.instrumentmonitor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sockets extends Activity {

    public volatile String Address;
    public volatile int Port;
    String ipAddressPort[];
    String inputIpAddressPort;
    String response = "";
    TextView textViewVoltage;
    TextView textViewCurrent;
    TextView textViewResistance;
    TextView textViewInductance;
    TextView textViewCapacitance;

    public void on_create_func() {
        SharedPreferences settings = getSharedPreferences("msettings",0);
        if(settings.getString("SERVERIPADDRESS", "192,168.1.9:8080").contains(":")) {
            ipAddressPort = settings.getString("SERVERIPADDRESS", "192.168.1.9:8080").split(":");
            Address = ipAddressPort[0];
            Port = Integer.parseInt(ipAddressPort[1]);
        }
        else {
            Address = settings.getString("SERVERIPADDRESS", "192.168.1.9");
        }
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String errorResponse = "";
        String msgToServer;

        MyClientTask(String addr, int port, String msgTo) {
            dstAddress = addr;
            dstPort = port;
            msgToServer = msgTo;
            textViewVoltage = (TextView) findViewById(R.id.textViewValue1);
            textViewCurrent = (TextView) findViewById(R.id.textViewValue2);
            textViewResistance = (TextView) findViewById(R.id.textViewValue3);
            textViewInductance = (TextView) findViewById(R.id.textViewValue4);
            textViewCapacitance = (TextView) findViewById(R.id.textViewValue5);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                if(msgToServer != null){
                    dataOutputStream.writeUTF(msgToServer);
                }

                response = dataInputStream.readUTF();
//                response = msgToServer;

            } catch (UnknownHostException e) {
                e.printStackTrace();
                errorResponse = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                errorResponse = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//			editText.setText(response);
            if(!errorResponse.equals( "IOException: java.io.EOFException") && !errorResponse.equals("")) {
                Toast.makeText(Sockets.this, errorResponse, Toast.LENGTH_SHORT).show();
            }
            textViewInductance.setText(response);

            String switchCheck[] = null;
            switchCheck = response.split("-");
            switch (switchCheck[0]) {
                case "voltage":
                    textViewVoltage.setText(switchCheck[1]);
                    break;
                case "current":
                    textViewCurrent.setText(switchCheck[1]);
                    break;
                case "resistance":
                    textViewCurrent.setText(switchCheck[1]);
                    break;
                case "inductance":
                    textViewCurrent.setText(switchCheck[1]);
                    break;
                case "capacitance":
                    textViewCapacitance.setText(switchCheck[1]);
                    break;
            }
            super.onPostExecute(result);
        }
    }

    public void exchangeData(String tMsg) {

//		String tMsg = welcomeMsg.getText().toString();
//		if(tMsg.equals("")){
//			tMsg = null;
//			Toast.makeText(MainActivity.this, "No Welcome Msg sent", Toast.LENGTH_SHORT).show();
//		}
//		SharedPreferences settings = getSharedPreferences("msettings",0);
//		putAddress(settings.getString("SERVERIPADDRESS","192.168.1.9"));

        MyClientTask myClientTask = new MyClientTask(Address, Port,
                tMsg);
        //Toast.makeText(socket.this, tMsg, Toast.LENGTH_SHORT).show();
        myClientTask.execute();

    }

}
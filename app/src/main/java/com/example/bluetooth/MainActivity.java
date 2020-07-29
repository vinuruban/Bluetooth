package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView statusText;
    Button searchButton;
    ArrayList<String> bluetoothDevices = new ArrayList<>(); //created this instead of a Bluetooth Object
    ArrayList<String> addresses = new ArrayList<>(); //to check previous address and avoid duplication
    ArrayAdapter arrayAdapter;

    BluetoothAdapter bluetoothAdapter;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("TEST", "searchDevices: TEST");
            String action = intent.getAction();
            Log.i("Action", action);

            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) { //if search is finished...
                statusText.setText("Finished");
                searchButton.setEnabled(true);
            } else if (action.equals(BluetoothDevice.ACTION_FOUND)) { //if bluetooth device is found...
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE); //how strong the bluetooth is

                if (!addresses.contains(address)) { //if the address was already added to the list, avoid it. This dismisses duplicates.
                    addresses.add(address);
                    String deviceString = "";
                    if (name == null || name.equals("")) { //if no names are found, display address instead!
                        deviceString = address + " - RSSI: " + rssi + "dBm";
                    } else {
                        deviceString = name + " - RSSI: " + rssi + "dBm";
                    }
                    bluetoothDevices.add(deviceString);
                    arrayAdapter.notifyDataSetChanged(); //updates adapter!
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        statusText = findViewById(R.id.statusTextView);
        searchButton = findViewById(R.id.searchButton);

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,bluetoothDevices); //
        listView.setAdapter(arrayAdapter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //what we looking for from the response
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        //to register the intent filter
        registerReceiver(broadcastReceiver, intentFilter);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Don't forget to unregister the ACTION_FOUND receiver.
//        unregisterReceiver(broadcastReceiver);
//    }

    public void searchDevices(View view) {
        statusText.setText("Searching...");
        searchButton.setEnabled(false);
        bluetoothDevices.clear(); //clear previous results from ListView
        addresses.clear(); //clear previous addresses
        bluetoothAdapter.startDiscovery(); //searches for bluetooth devices
    }
}
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView statusText;
    Button searchButton;

    BluetoothAdapter bluetoothAdapter;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Action", action);
            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) { //if search is finished...
                statusText.setText("Finished");
                searchButton.setEnabled(true);
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

    public void searchDevices(View view) {
        statusText.setText("Searching...");
        searchButton.setEnabled(false);
        bluetoothAdapter.startDiscovery(); //searches for bluetooth devices
    }
}
package com.example.bookthiti.masai2.bluetoothattackscreen;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;

import static com.example.bookthiti.masai2.utils.LogConstants.TAG_INFO;

public class BluetoothDiscoveryActivity extends AppCompatActivity {
    Context mContext;
    BluetoothAdapter bluetoothAdapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceMacAddress = device.getAddress();
                BluetoothClass deviceBluetoothClass = device.getBluetoothClass();
                int deviceClass = deviceBluetoothClass.getDeviceClass();
                int deviceMajorClass = deviceBluetoothClass.getMajorDeviceClass();
                Log.i(TAG_INFO, "Found bluetooth device " + deviceName + " (" + deviceMacAddress + ") class: " + deviceClass + " major class: " + deviceMajorClass);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_discovery);
        mContext = getApplicationContext();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(broadcastReceiver, intentFilter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetoothIntent, BluetoothManagementService.REQUEST_ENABLE_CODE);
            } else {
                startBluetoothDiscovery();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BluetoothManagementService.REQUEST_ENABLE_CODE:
                if (requestCode == RESULT_OK) {
                    startBluetoothDiscovery();
                } else {
                    // TODO: Show disable function
                    Toast.makeText(mContext, "Please enable bluetooth", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void startBluetoothDiscovery() {
        if (!bluetoothAdapter.isDiscovering()) {
            if (bluetoothAdapter.startDiscovery()) {
                Log.i(TAG_INFO, "Starting bluetooth discovery");
            }
        }
    }
}

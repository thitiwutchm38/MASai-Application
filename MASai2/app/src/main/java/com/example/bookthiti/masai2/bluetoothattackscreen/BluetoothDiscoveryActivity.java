package com.example.bookthiti.masai2.bluetoothattackscreen;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.example.bookthiti.masai2.utils.LogConstants.TAG_INFO;

public class BluetoothDiscoveryActivity extends AppCompatActivity {
    Context mContext;
    BluetoothAdapter bluetoothAdapter;

    List<BluetoothDeviceModel> bluetoothDeviceModelList;
    BluetoothDeviceRecyclerAdapter bluetoothDeviceRecyclerAdapter;

    RecyclerView recyclerViewBluetoothDevice;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    boolean discoveryStarted = false;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (discoveryStarted) {
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Integer rssi = new Integer(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                    BluetoothDeviceModel bluetoothDeviceModel = new BluetoothDeviceModel(device, rssi);
                    bluetoothDeviceModelList.add(bluetoothDeviceModel);
                    bluetoothDeviceRecyclerAdapter.notifyDataSetChanged();

                    String deviceName = device.getName();
                    String deviceMacAddress = device.getAddress();
                    BluetoothClass deviceBluetoothClass = device.getBluetoothClass();
                    int majorClass = deviceBluetoothClass.getMajorDeviceClass();
                    String deviceClass = "";
                    Class<?> c = BluetoothClass.Device.Major.class;
                    for (Field field : c.getDeclaredFields()) {
                        try {
                            if (majorClass == Integer.parseInt(field.get(c).toString()))
                                deviceClass = field.getName();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    String deviceType = "";
                    switch (device.getType()) {
                        case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                            deviceType = "Classic";
                            break;
                        case BluetoothDevice.DEVICE_TYPE_LE:
                            deviceType = "LE";
                            break;
                        case BluetoothDevice.DEVICE_TYPE_DUAL:
                            deviceType = "Dual";
                            break;
                        case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
                            deviceType = "Unknown";
                            break;
                    }
                    Log.i(TAG_INFO, "Found bluetooth device "
                            + deviceName
                            + " ("
                            + deviceMacAddress
                            + ") class: "
                            + deviceClass
                            + " RSSI: "
                            + rssi
                            + " Type: "
                            + deviceType);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setEnabled(true);
                    discoveryStarted = false;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_discovery);
        mContext = getApplicationContext();

        recyclerViewBluetoothDevice = findViewById(R.id.rv_bluetooth_list);
        progressBar = findViewById(R.id.progressBar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        bluetoothDeviceModelList = new ArrayList<BluetoothDeviceModel>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        bluetoothDeviceRecyclerAdapter = new BluetoothDeviceRecyclerAdapter(mContext, bluetoothDeviceModelList);
        recyclerViewBluetoothDevice.setLayoutManager(linearLayoutManager);
        recyclerViewBluetoothDevice.setAdapter(bluetoothDeviceRecyclerAdapter);
        recyclerViewBluetoothDevice.addItemDecoration(new DividerItemDecoration(recyclerViewBluetoothDevice.getContext(), linearLayoutManager.getOrientation()));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
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
        if (discoveryStarted) {
            swipeRefreshLayout.setEnabled(false);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bluetoothDeviceModelList.clear();
                bluetoothDeviceRecyclerAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                startBluetoothDiscovery();
            }
        });
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
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        if (bluetoothAdapter.startDiscovery()) {
            discoveryStarted = true;
            Log.i(TAG_INFO, "Starting bluetooth discovery");
        }
    }
}

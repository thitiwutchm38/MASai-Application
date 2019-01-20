package com.example.bookthiti.masai2;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MasaiSettingActivity extends AppCompatActivity {
    private final static String TAG_INFO = "Log info";
    private final static String TAG_DEBUG = "Log debug";
    private final static String TAG_ERROR = "Log error";
    private final static int REQUEST_ENABLE_BLUETOOTH = 0;
    private final Activity activity = this;

    private boolean mBound = false;

    private Button mScanQrButton;
    private ProgressBar mProgressBarQrScan;

    private Context mContext;
    private BluetoothManagementService mBluetoothManagementService;
    JSONObject boxInformation;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.i(TAG_INFO, "Discovering is stopped");
                mProgressBarQrScan.setVisibility(View.GONE);
            }
        }
    };

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothManagementService.ACTION_BLUETOOTH_CONNECTED.equals(action)) {
                Log.i(TAG_INFO, "Bluetooth is connected to remote device");
                mProgressBarQrScan.setVisibility(View.GONE);
            } else if (BluetoothManagementService.ACTION_PAIRED_DEVICE_FOUND.equals(action)) {
                mProgressBarQrScan.setVisibility(View.GONE);
            } else if (BluetoothManagementService.ACTION_BLUETOOTH_UNABLE_TO_CONNECT.equals(action)) {
                mProgressBarQrScan.setVisibility(View.GONE);
                Toast.makeText(mContext, "Please Check Box", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BluetoothManagementService.LocalBinder binder = (BluetoothManagementService.LocalBinder) service;
            mBluetoothManagementService = binder.getBluetoothManagementServiceInstance();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
            Log.i(TAG_INFO, "Service is unbounded");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masai_setting);
        mContext = this.getApplicationContext();
        Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
        if(!mBound) {
            bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
        }

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //TODO: Add Action that will receive from BluetoothManagementService when it broadcasts the message received from MASai Box
        intentFilter.addAction(BluetoothManagementService.ACTION_BLUETOOTH_CONNECTED);
        intentFilter.addAction(BluetoothManagementService.ACTION_BLUETOOTH_DISCONNECTED);
        intentFilter.addAction(BluetoothManagementService.ACTION_BLUETOOTH_UNABLE_TO_CONNECT);
        this.registerReceiver(mBroadcastReceiver, intentFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);

        mScanQrButton = (Button) this.findViewById(R.id.button_scan_qr);
        mProgressBarQrScan = (ProgressBar) this.findViewById(R.id.progress_bar_qr_scan);
        mProgressBarQrScan.setVisibility(View.GONE);
        mScanQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                mProgressBarQrScan.setVisibility(View.VISIBLE);
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setPrompt("Scan QR Code at MASai Box");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalBroadcastReceiver);
        if(mBound){
            unbindService(mConnection);
        }
        Log.i(TAG_INFO, "unregister broadcast receiver");
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BLUETOOTH:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG_INFO, "Bluetooth is enabled");
                    Toast.makeText(mContext, "Bluetooth is enabled", Toast.LENGTH_SHORT).show();
                    if(boxInformation != null) {
                        connectToRemoteDevice(boxInformation);
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Log.e(TAG_ERROR, "Bluetooth is not enabled by the user");
                    Toast.makeText(mContext, "Bluetooth is not enabled", Toast.LENGTH_SHORT).show();
                }
                super.onActivityResult(requestCode, resultCode, data);
                break;
            default:
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    if (result.getContents() == null) {
                        Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_SHORT).show();
                    } else {
                        String readQrCode = result.getContents();
                        Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                        try {
                            boxInformation = new JSONObject(readQrCode);
                            Log.i(TAG_INFO, "name: " + boxInformation.getString("name") + ", address: " + boxInformation.getString("address") + ", uuid: " + boxInformation.getString("uuid"));
                            this.connectToRemoteDevice(boxInformation);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void connectToRemoteDevice(JSONObject deviceInformation) {
        if(mBluetoothManagementService != null && mBound) {
            Log.i(TAG_INFO, "Connect Remote Device start");
            mBluetoothManagementService.connectRemoteDevice(deviceInformation, activity);
        } else {
            Log.i(TAG_INFO, "Connect Remote Device failed");
        }
    }
}

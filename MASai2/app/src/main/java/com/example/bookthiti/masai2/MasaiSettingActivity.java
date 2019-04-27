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
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.eyalbira.loadingdots.LoadingDots;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.bookthiti.masai2.utils.LogConstants.TAG_ERROR;
import static com.example.bookthiti.masai2.utils.LogConstants.TAG_INFO;

public class MasaiSettingActivity extends AppCompatActivity {
    private final static int REQUEST_ENABLE_BLUETOOTH = 0;
    private final Activity activity = this;

    private boolean mBound = false;
    private boolean isConnected = false;

    private TextView mTextViewInstructionDescription;
    private ImageView mImageViewInstructionConnect;
    private ImageView mImageViewInstruction;
    private ImageView mImageViewMobile;
    private ImageView mImageViewBox;
    private ToggleButton mScanQrToggleButton;
    private LoadingDots mLoadingDot;

    private Context mContext;
    private BluetoothManagementService mBluetoothManagementService;
    JSONObject boxInformation;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.i(TAG_INFO, "Discovering is stopped");
//                mImageViewMobile.setVisibility(View.INVISIBLE);
//                mImageViewBox.setVisibility(View.INVISIBLE);
//                mLoadingDot.setVisibility(View.INVISIBLE);
//                mImageViewInstructionConnect.setVisibility(View.VISIBLE);
            }
        }
    };

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            mScanQrToggleButton.setEnabled(true);
            if (BluetoothManagementService.ACTION_BLUETOOTH_CONNECTED.equals(action)) {
                Log.i(TAG_INFO, "Bluetooth is connected to remote device");
                mImageViewMobile.setVisibility(View.INVISIBLE);
                mImageViewBox.setVisibility(View.INVISIBLE);
                mLoadingDot.setVisibility(View.INVISIBLE);
                mImageViewInstructionConnect.setVisibility(View.VISIBLE);
                mTextViewInstructionDescription.setText("Click disconnect button to disconnect MASai box and mobile application.");
                isConnected = true;
                mScanQrToggleButton.setTextOff("Disconnect");
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("MASAI_SHARED_PREF", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("need_to_connect_masai", false);
                editor.commit();

            } else if (BluetoothManagementService.ACTION_PAIRED_DEVICE_FOUND.equals(action)) {
            } else if (BluetoothManagementService.ACTION_BLUETOOTH_UNABLE_TO_CONNECT.equals(action)) {
                mImageViewMobile.setVisibility(View.INVISIBLE);
                mImageViewBox.setVisibility(View.INVISIBLE);
                mLoadingDot.setVisibility(View.INVISIBLE);
                mImageViewInstruction.setVisibility(View.VISIBLE);
                mTextViewInstructionDescription.setText("Cannot connect to MASai Box. Please check whether the box is turned on.");
                mScanQrToggleButton.setChecked(true);
                mScanQrToggleButton.setTextOff("Reconnect");
        }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BluetoothManagementService.LocalBinder binder = (BluetoothManagementService.LocalBinder) service;
            mBluetoothManagementService = binder.getBluetoothManagementServiceInstance();
            mBound = true;
            Log.i(TAG_INFO, "Service is connected");
            if(isRemoteDeviceConnected()) {
                mTextViewInstructionDescription.setText("Click disconnect button to disconnect MASai box and mobile application.");
                mImageViewInstruction.setVisibility(View.INVISIBLE);
                mImageViewInstructionConnect.setVisibility(View.VISIBLE);
                isConnected = true;
                mScanQrToggleButton.setChecked(false);
                mScanQrToggleButton.setTextOff("Disconnect");
            } else {
                mTextViewInstructionDescription.setText("Click connect to start QR-Code scanner, scan QR code at MASai Box, ensure that MASai Box is working");
                mImageViewInstruction.setVisibility(View.VISIBLE);
                isConnected = false;
                mScanQrToggleButton.setTextOn("Connect");
            }
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
        setTitle("MASai Box Setting");

        mScanQrToggleButton = (ToggleButton) this.findViewById(R.id.toggle_btn_scan_qr);
        mImageViewMobile = (ImageView) this.findViewById(R.id.imageView_Mobile);
        mImageViewBox = (ImageView) this.findViewById(R.id.imageView_Box);
        mImageViewInstruction = (ImageView) this.findViewById(R.id.image_setting_instruction);
        mImageViewInstructionConnect = (ImageView) this.findViewById(R.id.image_setting_instruction2);
        mTextViewInstructionDescription = (TextView) this.findViewById(R.id.text_description);

        mLoadingDot = (LoadingDots) this.findViewById(R.id.loading_dot);
        mLoadingDot.setDotsCount(6);
        mLoadingDot.setDotsSize(20);
        mLoadingDot.setDotsSpace(20);
       // mLoadingDot.setDotsColor(Color.YELLOW);
        mImageViewMobile.setVisibility(View.INVISIBLE);
        mImageViewBox.setVisibility(View.INVISIBLE);
        mLoadingDot.setVisibility(View.INVISIBLE);
        mImageViewInstruction.setVisibility(View.VISIBLE);

        mScanQrToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    Log.i(TAG_INFO, "Toggle button is changed from true to false");
                    if (!isConnected) {
                        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                        intentIntegrator.setPrompt("Scan QR Code at MASai Box");
                        intentIntegrator.setCameraId(0);
                        intentIntegrator.setBeepEnabled(false);
                        intentIntegrator.setBarcodeImageEnabled(false);
                        intentIntegrator.initiateScan();
                        mScanQrToggleButton.setEnabled(false);
                    }
                } else {
                    Log.i(TAG_INFO, "Toggle button is changed from false to true");
                    if (isConnected)
                        disconnectFromRemoteDevice();
                    mScanQrToggleButton.setTextOn("Connect");
                    mImageViewMobile.setVisibility(View.INVISIBLE);
                    mImageViewBox.setVisibility(View.INVISIBLE);
                    mLoadingDot.setVisibility(View.INVISIBLE);
                    mImageViewInstructionConnect.setVisibility(View.INVISIBLE);
                    mImageViewInstruction.setVisibility(View.VISIBLE);
                    mTextViewInstructionDescription.setText("Click connect to start QR-Code scanner, scan QR code at MASai Box, ensure that MASai Box is working");
                }
            }
        });

        Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
        if(!mBound) {
            bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
        }

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothManagementService.ACTION_BLUETOOTH_CONNECTED);
        intentFilter.addAction(BluetoothManagementService.ACTION_BLUETOOTH_DISCONNECTED);
        intentFilter.addAction(BluetoothManagementService.ACTION_BLUETOOTH_UNABLE_TO_CONNECT);
        this.registerReceiver(mBroadcastReceiver, intentFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);

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
                        mScanQrToggleButton.setEnabled(true);
                        mScanQrToggleButton.setChecked(true);
                    } else {
                        mImageViewMobile.setVisibility(View.VISIBLE);
                        mImageViewBox.setVisibility(View.VISIBLE);
                        mLoadingDot.setVisibility(View.VISIBLE);
                        mImageViewInstruction.setVisibility(View.INVISIBLE);
                        mTextViewInstructionDescription.setText("Connecting to MASai Box. Please wait for a moment.");
                        mScanQrToggleButton.setTextOn("Connecting...");
                        mScanQrToggleButton.setEnabled(false);
                        String readQrCode = result.getContents();
//                        Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
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
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("MASAI_SHARED_PREF", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("need_to_connect_masai", true);
            editor.commit();
            mBluetoothManagementService.connectRemoteDevice(deviceInformation, activity);
        } else {
            Log.i(TAG_INFO, "Connect Remote Device failed");
        }
    }

    private void disconnectFromRemoteDevice() {
        if(mBluetoothManagementService != null && mBound) {
            Log.i(TAG_INFO, "Disconnect from remote device");
            mBluetoothManagementService.disconnectFromRemoteDevice();
            isConnected = false;
        } else {
            Log.i(TAG_INFO, "Disconnection failed");
        }
    }

    private boolean isRemoteDeviceConnected() {
        if (mBluetoothManagementService != null && mBound) {
            return mBluetoothManagementService.isRemoteDeviceConnected();
        }
        return false;
    }
}

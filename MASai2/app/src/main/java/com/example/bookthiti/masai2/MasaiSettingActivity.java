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
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.eyalbira.loadingdots.LoadingDots;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

import static com.example.bookthiti.masai2.LogConstants.TAG_ERROR;
import static com.example.bookthiti.masai2.LogConstants.TAG_INFO;

public class MasaiSettingActivity extends AppCompatActivity {
    private final static int REQUEST_ENABLE_BLUETOOTH = 0;
    private final Activity activity = this;

    private boolean mBound = false;

    private TextView mTextViewInstructionDescription;
    private ImageView mImageViewInstructionConnect;




    private ImageView mImageViewInstruction;

    private ImageView mImageViewMobile;

    private ImageView mImageViewBox;


    private Button mScanQrButton;

    private  LoadingDots mLoadingDot;
    private Context mContext;
    private BluetoothManagementService mBluetoothManagementService;
    JSONObject boxInformation;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.i(TAG_INFO, "Discovering is stopped");
                // TODO: update image view
            }
        }
    };

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothManagementService.ACTION_BLUETOOTH_CONNECTED.equals(action)) {
                Log.i(TAG_INFO, "Bluetooth is connected to remote device");
                mTextViewInstructionDescription.setText("Click disconnect button to disconnect MASai box and mobile application.");
                mScanQrButton.setText("Disconnect");
            } else if (BluetoothManagementService.ACTION_PAIRED_DEVICE_FOUND.equals(action)) {
            } else if (BluetoothManagementService.ACTION_BLUETOOTH_UNABLE_TO_CONNECT.equals(action)) {
                Toast.makeText(mContext, "Please Check The Box", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BluetoothManagementService.LocalBinder binder = (BluetoothManagementService.LocalBinder) service;
            mBluetoothManagementService = binder.getBluetoothManagementServiceInstance();
            mBound = true;
            if(isRemoteDeviceConnected()) {
                mTextViewInstructionDescription.setText("Click disconnect button to disconnect MASai box and mobile application.");
                mScanQrButton.setText("Disconnect");
            } else {
                mScanQrButton.setText("Connect");
                mTextViewInstructionDescription.setText("Click connect to start QR-Code scanner, scan QR code at MASai Box, ensure that MASai Box is working");
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

        mScanQrButton = (Button) this.findViewById(R.id.button_scan_qr);

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
        mScanQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
//                ActivityCompat.requestPermissions(activity,
//                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
//                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//                intentIntegrator.setPrompt("Scan QR Code at MASai Box");
//                intentIntegrator.setCameraId(0);
//                intentIntegrator.setBeepEnabled(false);
//                intentIntegrator.setBarcodeImageEnabled(false);
//                intentIntegrator.initiateScan();

                mImageViewMobile.setVisibility(View.VISIBLE);
                mImageViewBox.setVisibility(View.VISIBLE);
                mLoadingDot.setVisibility(View.VISIBLE);
                mImageViewInstruction.setVisibility(View.INVISIBLE);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        mImageViewMobile.setVisibility(View.INVISIBLE);
                        mImageViewBox.setVisibility(View.INVISIBLE);
                        mLoadingDot.setVisibility(View.INVISIBLE);
                        mImageViewInstructionConnect.setVisibility(View.VISIBLE);
                        mTextViewInstructionDescription.setText("Click disconnect button to disconnect MASai box and mobile application.");
                        mScanQrButton.setText("Disconnect");


                    }
                }, 5000); // Millisecond 1000 = 1 sec


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

    private boolean isRemoteDeviceConnected() {
        if (mBluetoothManagementService != null && mBound) {
            return mBluetoothManagementService.isRemoteDeviceConnected();
        }
        return false;
    }
}

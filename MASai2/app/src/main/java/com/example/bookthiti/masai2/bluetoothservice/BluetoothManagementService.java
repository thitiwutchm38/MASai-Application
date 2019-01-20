package com.example.bookthiti.masai2.bluetoothservice;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothManagementService extends Service {
    private final static String TAG_INFO = "Log info";
    private final static String TAG_DEBUG = "Log debug";
    private final static String TAG_ERROR = "Log error";
    private static final int REQUEST_ENABLE_CODE = 0;
    public static final String ACTION_BLUETOOTH_CONNECTED = "ACTION BLUETOOTH CONNECTED";
    public static final String ACTION_BLUETOOTH_DISCONNECTED = "ACTION_BLUETOOTH_DISCONNECTED";
    public static final String ACTION_BLUETOOTH_MESSAGE_RECEIVED = "ACTION BLUETOOTH MESSAGE RECEIVED";
    public static final String ACTION_WIFI_SCAN = "ACTION WIFI SCAN";
    public static final String ACTION_PAIRED_DEVICE_FOUND = "ACTION PAIRED DEVICE FOUND";
    public static final String ACTION_BLUETOOTH_UNABLE_TO_CONNECT = "ACTION BLUETOOTH UNABLE TO CONNECT";
    public static final String ACTION_WIFI_ATTACK = "ACTION WIFI ATTACK";
    public static final String ACTION_WIFI_CONNECT = "ACTION WIFI CONNECT";
    public static final String ACTION_DEVICE_SCAN = "ACTION DEVICE SCAN";

    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> mPairedDevices;

    private UUID mBoxUuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");
    private String mBoxName = "kali";
    private String mBoxAddress = "B8:27:EB:CC:2F:48";

    private ConnectingThread mConnectingThread;
    private ConnectedThread mConnectedThread;
    private final IBinder mBinder = new LocalBinder();
    private Context mContext;
    private LocalBroadcastManager mLocalBroadcastManager;

    private boolean mThreadStopped;

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG_INFO, "Receive ACTION_FOUND from broadcast receiver");
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceAddress = device.getAddress();
                Log.i(TAG_INFO, "A Device (" + deviceName + " " + deviceAddress + ")" + " is found");
                if (mBoxName.equals(deviceName) && mBoxAddress.equals(deviceAddress)) {
                    mConnectingThread = new ConnectingThread(device);
                    mConnectingThread.start();
                    Log.i(TAG_INFO, "Connecting to " + deviceName + " " + deviceAddress);
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG_DEBUG, "Bluetooth service is created");
        mContext = this.getApplicationContext();
        mThreadStopped = true;
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**
         * This override method will be called by MainActivity to start the service.
         * It will allow the service run in the background.
         *
         * First, the service will get bluetooth adapter by default and check whether the device \
         * support bluetooth or not
         */
        Log.i(TAG_INFO, "Bluetooth service is started");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG_DEBUG, "Bluetooth service is stopped");
        mThreadStopped = true;
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
        }
        if (mConnectingThread != null) {
            mConnectingThread.cancel();
        }
        unregisterReceiver(mBroadcastReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG_INFO, "ServiceModel is bind");
        return this.mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG_INFO, "ServiceModel is unbind");
        return super.onUnbind(intent);
    }

    public void sendMessageToRemoteDevice(String message) {
        if (mConnectedThread != null) {
            mConnectedThread.write(message);
        } else {
            Log.i(TAG_INFO, "No Connected Thread, cannot send msg");
            Toast.makeText(this, "No Connected Thread, cannot send msg", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isRemoteDeviceConnected() {
        return !mThreadStopped;
    }

    public void connectRemoteDevice(JSONObject deviceInformation, Activity activity) {
        if (isBluetoothAdapterSupported(mBluetoothAdapter)) {
            if (!mBluetoothAdapter.isEnabled()) {
                // Prompt user to enable
                Intent callingDialogIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(callingDialogIntent, REQUEST_ENABLE_CODE);
                Log.i(TAG_INFO, "Connect Remote Device from service is called");
            } else {
                try {
                    mBoxName = deviceInformation.getString("name");
                    mBoxAddress = deviceInformation.getString("address");
                    mBoxUuid = UUID.fromString(deviceInformation.getString("uuid"));
                } catch (JSONException e) {
                    Log.e(TAG_ERROR, e.toString());
                }

                mPairedDevices = mBluetoothAdapter.getBondedDevices();
                if (mPairedDevices.size() > 0) {
                    for (BluetoothDevice device : mPairedDevices) {
                        String deviceName = device.getName();
                        String deviceAddress = device.getAddress();
                        if (mBoxName.equals(deviceName) && mBoxAddress.equals(deviceAddress)) {
                            mLocalBroadcastManager.sendBroadcast(new Intent(BluetoothManagementService.ACTION_PAIRED_DEVICE_FOUND));
                            Thread connectingThread = new Thread(new ConnectingThread(device));
                            connectingThread.start();
                            Log.i(TAG_INFO, "Connecting to " + deviceName + " " + deviceAddress);
                            break;
                        }
                    }
                } else {
                    if (!mBluetoothAdapter.isDiscovering()) {
                        mBluetoothAdapter.startDiscovery();
                        Log.i(TAG_INFO, "Start discovering");
                        Toast.makeText(this, "Start Discovering", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(TAG_INFO, "Already discovering");
                        Toast.makeText(this, "Already discovering", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private boolean isBluetoothAdapterSupported(BluetoothAdapter bluetoothAdapter) {
        if (mBluetoothAdapter == null) {
            Log.i(TAG_INFO, "This device does not support bluetooth");
            Toast.makeText(this, "Does not support bluetooth", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private class ConnectingThread extends Thread {
        /**
         * This class is a thread class which will handle the bluetooth connection \
         * between this application and remote devices.
         */
        private final BluetoothSocket mmBluetoothSocket;
        private final BluetoothDevice mmBluetoothDevice;

        public ConnectingThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmBluetoothDevice = device;
            Log.i(TAG_INFO, "Thread is initiated");
            try {
                tmp = mmBluetoothDevice.createRfcommSocketToServiceRecord(mBoxUuid);
            } catch (IOException e) {
                Log.e(TAG_ERROR, "Socket's create() method failed", e);
            }
            mmBluetoothSocket = tmp;
            if (mmBluetoothSocket != null) {
                Log.i(TAG_INFO, "mmBluetoothSocket is created");
            }
        }

        public void run() {
            mBluetoothAdapter.cancelDiscovery();
            try {
                mmBluetoothSocket.connect();
            } catch (IOException connectException) {
                try {
                    Log.i(TAG_INFO, "Could not connect the client socket");
                    mLocalBroadcastManager.sendBroadcast(new Intent(BluetoothManagementService.ACTION_BLUETOOTH_UNABLE_TO_CONNECT));
                    mmBluetoothSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG_ERROR, "Could not close the client socket", closeException);
                }
                return;
            }
            // Manage Connection through new thread called ManageThread
            mThreadStopped = false;
            mConnectedThread = new ConnectedThread(mmBluetoothSocket);
            mConnectedThread.start();
        }

        public void cancel() {
            try {
                mmBluetoothSocket.close();
            } catch (IOException e) {
                Log.e(TAG_ERROR, "Could not close the client socket", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmBluetoothSocket;
        private final InputStream mmInputStream;
        private final OutputStream mmOutputStream;
        private byte[] mmBuffer;

        public ConnectedThread(BluetoothSocket bluetoothSocket) {
            mmBluetoothSocket = bluetoothSocket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = mmBluetoothSocket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG_ERROR, "Error occured when creating input stream", e);
            }
            try {
                tmpOut = mmBluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG_ERROR, "Error occured when creating output stream", e);
            }

            mmInputStream = tmpIn;
            mmOutputStream = tmpOut;
        }

        public void run() {
            int bufferSize = 1024;
            mmBuffer = new byte[bufferSize];
            int bytesRead;
            int readBufferPosition = 0;
            int count = 0;
            StringBuilder sb = new StringBuilder();
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(BluetoothManagementService.ACTION_BLUETOOTH_CONNECTED));
            while (true && !mThreadStopped) {
                try {
                    bytesRead = mmInputStream.read(mmBuffer);
                    String message = new String(mmBuffer, 0, bytesRead);
                    if (!message.contains("|")) {
                        sb.append(message);
                        count += message.length();
                    } else {
                        String[] splitString = message.split("\\|");
                        if(splitString.length > 0) {
                            message = splitString[0];
                            sb.append(message);
                        }

                        JsonParser jsonParser = new JsonParser();
                        JsonElement jsonElement = jsonParser.parse(sb.toString());
                        if (jsonElement.isJsonObject()) {
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            String resultType = jsonObject.get("resultType").getAsString();
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            Gson gson = new Gson();
                            String jsonString = gson.toJson(jsonObject.get("payload"));
                            bundle.putString("payload", jsonString);
                            intent.putExtras(bundle);
                            switch (resultType) {
                                case "wifiScan":
                                    intent.setAction(BluetoothManagementService.ACTION_WIFI_SCAN);
                                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                    break;
                                case "wifiCracking":
                                    intent.setAction(BluetoothManagementService.ACTION_WIFI_ATTACK);
                                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                    break;
                                case "wifiConnect":
                                    intent.setAction(BluetoothManagementService.ACTION_WIFI_CONNECT);
                                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                    break;
                                case "nmapScan":
                                    intent.setAction(BluetoothManagementService.ACTION_DEVICE_SCAN);
                                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                    break;
                            }
                        }
                        sb = new StringBuilder();
                    }


                    //TODO: get result from box using switch case


                } catch (IOException e) {
                    Log.i(TAG_INFO, "Input stream was disconnected", e);
                    cancel();
                    break;
                }
            }
        }

        public void write(String string) {
            //TODO: find format for object
            try {
                mmOutputStream.write(string.getBytes());
            } catch (IOException e) {
                Log.e(TAG_ERROR, "Error occured when sending data", e);
            }
        }

        public void cancel() {
            try {
                mmInputStream.close();
                mmOutputStream.close();
            } catch (IOException e) {
                Log.e(TAG_ERROR, "Could not close the connected socket", e);
            }
        }
    }

    public class LocalBinder extends Binder {
        public BluetoothManagementService getBluetoothManagementServiceInstance() {
            return BluetoothManagementService.this;
        }
    }
}
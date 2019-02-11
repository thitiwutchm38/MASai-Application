package com.example.bookthiti.masai2.portattackscreen;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.CVEModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.DeviceModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.ServiceModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static com.example.bookthiti.masai2.LogConstants.TAG_INFO;

public class PortAttackActivity extends AppCompatActivity {

    private Context mContext;

    private BluetoothManagementService mBluetoothManagementService;
    private boolean mBound = false;
    private boolean isRemoteDeviceConnected = false;

    private TextView mTextViewResult;
    private TextView mTextViewName;
    private TextView mTextViewUsername;
    private TextView mTextViewPassword;
    private ProgressBar mProgressBar;
    private TextView mTextViewProgress;

    private DeviceModel mDeviceModel;
    private String mTargetService;

    private PortAttackResult mPortAttackResult;

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothManagementService.ACTION_PORT_ATTACK.equals(action)) {
                Bundle bundle = intent.getExtras();
                String jsonString = bundle.getString("payload");
                Log.i(TAG_INFO, "Receive ACTION_PORT_ATTACK: " + jsonString);
                //TODO: load payload to the result
                mPortAttackResult = loadPortAttackResult(jsonString);
                if (mPortAttackResult != null) {
                    mTextViewResult.setText(mPortAttackResult.getResult());
                    if (mPortAttackResult.getResult().equals("success")) {
                        mTextViewResult.setTextColor(Color.GREEN);
                    } else {
                        mTextViewResult.setTextColor(Color.RED);
                    }
                    mTextViewName.setText(mPortAttackResult.getServiceModel().getName());
                    if (mPortAttackResult.getUsername() != null) {
                        mTextViewUsername.setText(mPortAttackResult.getUsername());
                    } else {
                        mTextViewUsername.setText("-");
                    }
                    if (mPortAttackResult.getPassword() != null) {
                        mTextViewPassword.setText(mPortAttackResult.getPassword());
                    } else {
                        mTextViewPassword.setText("-");
                    }
                }
                mProgressBar.setVisibility(View.GONE);
                mTextViewProgress.setVisibility(View.GONE);
            }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BluetoothManagementService.LocalBinder binder = (BluetoothManagementService.LocalBinder) service;
            mBluetoothManagementService = binder.getBluetoothManagementServiceInstance();
            mBound = true;
            isRemoteDeviceConnected = isRemoteDeviceConnected();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("command", "devicePortAttack");
            Gson gson = new Gson();
            String hostString = gson.toJson(mDeviceModel, DeviceModel.class);
            Log.i(TAG_INFO, hostString);
            JsonParser jsonParser = new JsonParser();
            JsonElement hostElement = jsonParser.parse(hostString);
            JsonObject payloadObject = new JsonObject();
            payloadObject.add("host", hostElement);
            payloadObject.addProperty("targetService", mTargetService);
            jsonObject.add("payload", payloadObject);
            String jsonString = jsonObject.toString();
            mBluetoothManagementService.sendMessageToRemoteDevice(jsonString + "|");
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
        setContentView(R.layout.activity_port_attack2);
        this.mContext = getApplicationContext();

        mDeviceModel = getIntent().getParcelableExtra("deviceModel");
        mTargetService = getIntent().getStringExtra("targetService");

        mTextViewResult = findViewById(R.id.text_result);
        mTextViewName = findViewById(R.id.text_service_name);
        mTextViewUsername = findViewById(R.id.text_username);
        mTextViewPassword = findViewById(R.id.text_password);
        mProgressBar = findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);
        mTextViewProgress = findViewById(R.id.text_progress);

        // FIXME: Uncomment for real application
//        Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
//        if (!mBound) {
//            bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
//        }
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothManagementService.ACTION_PORT_ATTACK);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);
    }

    private boolean isRemoteDeviceConnected() {
        if (mBluetoothManagementService != null && mBound) {
            return mBluetoothManagementService.isRemoteDeviceConnected();
        }
        return false;
    }

    private PortAttackResult loadPortAttackResult(String jsonString) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(CVEModel.Severity.class, new CVEModel.SeverityDeserializer());
        gsonBuilder.registerTypeAdapter(ServiceModel.class, new ServiceModel.ServiceModelDeserializer());
        gsonBuilder.registerTypeAdapter(PortAttackResult.class, new PortAttackResult.PortAttackResultDeserializer());
        PortAttackResult portAttackResult = gsonBuilder.create().fromJson(jsonString, PortAttackResult.class);
        return portAttackResult;
    }
}

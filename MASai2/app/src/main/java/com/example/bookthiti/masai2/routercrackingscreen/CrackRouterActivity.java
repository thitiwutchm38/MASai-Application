package com.example.bookthiti.masai2.routercrackingscreen;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.example.bookthiti.masai2.networksearchingscreen.RouterModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static com.example.bookthiti.masai2.LogConstants.TAG_INFO;

public class CrackRouterActivity extends AppCompatActivity {
    private Context mContext;
    private BluetoothManagementService mBluetoothManagementService;
    private boolean mBound = false;
    private boolean isRemoteDeviceConnected = false;

    private Button mStartCrackingButton;
    private TextView mTextViewBssid;
    private TextView mTextViewSignal;
    private TextView mTextViewSecurity;
    private TextView mTextViewFrequency;
    private TextView mTextViewChannel;
    private TextView mTextViewSsid;
    private TextView mTextViewCrackStatus;
    private TextView mTextViewProgressCrack;
    private ProgressBar mProgressBar;

    private EditText mEditTextPassword;
    private ImageButton mImageButtonClipboard;

    private RouterModel mRouterModel;


    // True can crqck
    // False cannot crack
    Boolean pass_status = false;


    private ClipboardManager mClipboardManager;
    private ClipData mClipData;

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothManagementService.ACTION_WIFI_ATTACK.equals(action)) {
                Bundle bundle = intent.getExtras();
                String jsonString = bundle.getString("payload");
                Log.i(TAG_INFO, "Receive ACTION_WIFI_ATTACK: " + jsonString);
                //TODO: load payload to crack result
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
            jsonObject.addProperty("command", "wifiScan");
            jsonObject.add("payload", null);
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
        setContentView(R.layout.activity_crack_router);
        mTextViewBssid = (TextView) findViewById(R.id.text_crack_router_bssid);
        mTextViewSignal = (TextView) findViewById(R.id.text_crack_router_signal);
        mTextViewSecurity = (TextView) findViewById(R.id.text_crack_router_security);
        mTextViewFrequency = (TextView) findViewById(R.id.text_crack_router_frequency);
        mTextViewChannel = (TextView) findViewById(R.id.text_crack_router_channel);
        mTextViewSsid = (TextView) findViewById(R.id.text_crack_router_ssid);
        mTextViewCrackStatus = (TextView) findViewById(R.id.text_static_crack_router_crack_status);
        mTextViewProgressCrack = (TextView) findViewById(R.id.text_crack_router_progress);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_crack_router);
        mImageButtonClipboard = (ImageButton) findViewById(R.id.image_crack_router_clipboard);
        mEditTextPassword = (EditText) findViewById(R.id.edit_crack_router_password);
        mProgressBar.setVisibility(View.INVISIBLE);
        mTextViewProgressCrack.setVisibility(View.INVISIBLE);
        mTextViewCrackStatus.setVisibility(View.INVISIBLE);
        mRouterModel = (RouterModel) getIntent().getParcelableExtra("router_information");
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mContext = getApplicationContext();
        Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
        if (!mBound) {
            bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothManagementService.ACTION_WIFI_ATTACK);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);

        mTextViewSsid.setText(mRouterModel.getSsid());
        mTextViewSecurity.setText(mRouterModel.getSecurity());
        mTextViewBssid.setText(mRouterModel.getBssid());
        mTextViewSignal.setText(Float.toString(mRouterModel.getSignal()));
//        mTextViewFrequency.setText(mRouterModel.getmFrequency());
        mTextViewChannel.setText(Integer.toString(mRouterModel.getChannel()));

        mStartCrackingButton = (Button) findViewById(R.id.button_start_wifi_cracking);
        mStartCrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRemoteDeviceConnected) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("command", "wifiCracking");
                    Gson gson = new Gson();
                    String payloadJsonString = gson.toJson(mRouterModel, RouterModel.class);
                    Log.i(TAG_INFO, payloadJsonString);
                    JsonParser jsonParser = new JsonParser();
                    JsonElement payloadJsonElement = jsonParser.parse(payloadJsonString);
                    jsonObject.add("payload", payloadJsonElement);
                    String jsonString = jsonObject.toString();
                    mBluetoothManagementService.sendMessageToRemoteDevice(jsonString + "|");
                }
            }
        });

        mImageButtonClipboard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text;
                text = mEditTextPassword.getText().toString();

                mClipData = ClipData.newPlainText("text", text);
                mClipboardManager.setPrimaryClip(mClipData);

                Toast.makeText(getApplicationContext(), "Text Copied",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (mBound) {
            unbindService(mConnection);
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalBroadcastReceiver);
        super.onDestroy();
    }

    void aleartWrongPass(String ssid) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(CrackRouterActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Cannot decrypt password");
        dialog.setMessage("The password's " + ssid + "'cannot be cracked");
        dialog.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() { // define the 'Cancel' mButton
            public void onClick(DialogInterface dialog, int which) {
                //Either of the following two lines should work.
                dialog.cancel();
                //dialog.dismiss();
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private boolean isRemoteDeviceConnected() {
        if (mBluetoothManagementService != null && mBound) {
            return mBluetoothManagementService.isRemoteDeviceConnected();
        }
        return false;
    }
}

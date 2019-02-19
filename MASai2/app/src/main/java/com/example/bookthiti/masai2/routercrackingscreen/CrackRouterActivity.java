package com.example.bookthiti.masai2.routercrackingscreen;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.PersistableBundle;
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
import com.example.bookthiti.masai2.bluetoothservice.INotificationId;
import com.example.bookthiti.masai2.networksearchingscreen.RouterModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import static com.example.bookthiti.masai2.LogConstants.TAG_INFO;

public class CrackRouterActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;
    private BluetoothManagementService mBluetoothManagementService;
    private boolean mBound = false;
    private boolean isRemoteDeviceConnected = false;

    private Button mStartCrackingButton;
    private TextView mTextViewBssid;
    private TextView mTextViewSignal;
    private TextView mTextViewSecurity;
    //    private TextView mTextViewFrequency;
    private TextView mTextViewChannel;
    private TextView mTextViewSsid;
    private TextView mTextViewCrackStatus;
    private TextView mTextViewResultHeader;
    private ProgressBar mProgressBar;

    private EditText mEditTextPassword;
    private ImageButton mImageButtonClipboard;

    private RouterModel mRouterModel;

    private CrackResult mCrackResult;

    private boolean isCrackable = false;

    private ClipboardManager mClipboardManager;
    private ClipData mClipData;

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothManagementService.ACTION_WIFI_ATTACK.equals(action)) {
                setResultFromIntent(intent);
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
        setContentView(R.layout.activity_crack_router2);
        setTitle("Wi-Fi Information");
        mContext = getApplicationContext();
        mActivity = this;
        mTextViewBssid = (TextView) findViewById(R.id.text_crack_router_bssid);
        mTextViewSignal = (TextView) findViewById(R.id.text_crack_router_signal);
        mTextViewSecurity = (TextView) findViewById(R.id.text_crack_router_security);
//        mTextViewFrequency = (TextView) findViewById(R.id.text_crack_router_frequency);
        mTextViewChannel = (TextView) findViewById(R.id.text_crack_router_channel);
        mTextViewSsid = (TextView) findViewById(R.id.text_ssid);
        mTextViewCrackStatus = (TextView) findViewById(R.id.text_cracking_status);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_crack_router);
        mImageButtonClipboard = (ImageButton) findViewById(R.id.image_crack_router_clipboard);
        mEditTextPassword = (EditText) findViewById(R.id.edit_crack_router_password);
        mTextViewResultHeader = (TextView) findViewById(R.id.text_crack_result_header);

        mProgressBar.setVisibility(View.INVISIBLE);
        mTextViewCrackStatus.setVisibility(View.INVISIBLE);
        mImageButtonClipboard.setVisibility(View.INVISIBLE);
        mEditTextPassword.setVisibility(View.INVISIBLE);
        mTextViewResultHeader.setVisibility(View.INVISIBLE);


        mRouterModel = (RouterModel) getIntent().getParcelableExtra("router_information");
        if (mRouterModel == null) {
            String json = getPreferences(Context.MODE_PRIVATE).getString("router_information", null);
            if (json != null) {
                mRouterModel = new Gson().fromJson(json, RouterModel.class);
            }
        }

        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        if (getIntent().getBooleanExtra(INotificationId.FLAG_IS_FROM_NOTIFICATION, false)) {
            setResultFromIntent(getIntent());
            Log.i(TAG_INFO, "here");
        } else {
            Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
            if (!mBound) {
                bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothManagementService.ACTION_WIFI_ATTACK);
            LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);
        }

        mTextViewSsid.setText("Name: " + mRouterModel.getSsid());
        mTextViewSecurity.setText(mRouterModel.getSecurity());
        mTextViewBssid.setText(mRouterModel.getBssid());
        mTextViewSignal.setText(Float.toString(mRouterModel.getSignal()));
//        mTextViewFrequency.setText(mRouterModel.getmFrequency());
        mTextViewChannel.setText(Integer.toString(mRouterModel.getChannel()));

        mStartCrackingButton = (Button) findViewById(R.id.button_start_wifi_cracking);
//        ColorDrawable colorDrawable = (ColorDrawable) mStartCrackingButton.getBackground();
//        final int itsColorId = colorDrawable.getColor();
        mStartCrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // FIXME: Uncomment for real application
//                if (isRemoteDeviceConnected) {
//                    JsonObject jsonObject = new JsonObject();
//                    jsonObject.addProperty("command", "wifiCracking");
//                    Gson gson = new Gson();
//                    String payloadJsonString = gson.toJson(mRouterModel, RouterModel.class);
//                    Log.i(TAG_INFO, payloadJsonString);
//                    JsonParser jsonParser = new JsonParser();
//                    JsonElement payloadJsonElement = jsonParser.parse(payloadJsonString);
//                    jsonObject.add("payload", payloadJsonElement);
//                    String jsonString = jsonObject.toString();
//                    mBluetoothManagementService.sendMessageToRemoteDevice(jsonString + "|");
//                }

                ////////////////////

                mProgressBar.setVisibility(View.VISIBLE);
                mTextViewCrackStatus.setVisibility(View.VISIBLE);
                mTextViewCrackStatus.setText("Cracking...");
                mStartCrackingButton.setText("Stop Cracking");
                mStartCrackingButton.setBackgroundColor(Color.parseColor("#FFFF0000"));

                // FIXME: Uncomment for mock up
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setTitle("Crack Result");
                        builder.setMessage("The target Wi-Fi router password cannot be cracked!");
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                mTextViewCrackStatus.setVisibility(View.INVISIBLE);
                                mImageButtonClipboard.setVisibility(View.VISIBLE);
                                mEditTextPassword.setVisibility(View.VISIBLE);
                                mTextViewResultHeader.setVisibility(View.VISIBLE);
                                mEditTextPassword.setText("12345678");
                            }
                        });
                        builder.show();

//                        mStartCrackingButton.setBackgroundColor(itsColorId);
                        mStartCrackingButton.setText("Start Cracking");
                    }
                }, 5000);
                mProgressBar.setVisibility(View.INVISIBLE);

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
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonString = mRouterModel == null ? null : new Gson().toJson(mRouterModel);
        editor.putString("router_information", jsonString).apply();
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

    private CrackResult loadCrackResult(String jsonString) {
        CrackResult crackResult = new CrackResult();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(jsonString).getAsJsonObject();

        String type = jsonObject.get("attackType").getAsString();

        JsonObject crackResultObject = jsonObject.get("crackResult").getAsJsonObject();
        String status = crackResultObject.get("status").getAsString();
        String bssid = crackResultObject.get("bssid").getAsString();
        String essid = crackResultObject.get("essid").getAsString();
        List<String> keys = new ArrayList<String>();
        if (crackResultObject.has("key")) {
            keys.add(crackResultObject.get("key").getAsString());
        }
        if (crackResultObject.has("hexKey")) {
            keys.add(crackResultObject.get("hexKey").getAsString());
        }
        if (crackResultObject.has("asciiKey")) {
            keys.add(crackResultObject.get("asciiKey").getAsString());
        }

        crackResult.setType(type);
        crackResult.setBssid(bssid);
        crackResult.setEssid(essid);
        crackResult.setStatus(status);
        crackResult.setKeys(keys);

        return crackResult;
    }

    private void setResultFromIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String jsonString = bundle.getString("payload");
        Log.i(TAG_INFO, "Receive ACTION_WIFI_ATTACK: " + jsonString);
        //TODO: load payload to crack result
        mCrackResult = loadCrackResult(jsonString);
        if ("success".equals(mCrackResult.getStatus())) {
            isCrackable = true;
        } else {
            isCrackable = false;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Crack Result");
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mImageButtonClipboard.setVisibility(View.VISIBLE);
                mEditTextPassword.setVisibility(View.VISIBLE);
                mTextViewResultHeader.setVisibility(View.VISIBLE);
                if (isCrackable) {
                    mEditTextPassword.setText(mCrackResult.getKeys().get(0));
                }

            }
        });
        if (isCrackable) {
            builder.setMessage("The target Wi-Fi router password was cracked!");
        } else {
            builder.setMessage("The target Wi-Fi router password cannot be cracked!");
        }
        builder.create().show();
        mProgressBar.setVisibility(View.INVISIBLE);
        mTextViewCrackStatus.setVisibility(View.INVISIBLE);

    }
}

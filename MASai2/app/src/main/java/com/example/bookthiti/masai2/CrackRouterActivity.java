package com.example.bookthiti.masai2;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import com.example.bookthiti.masai2.service.BluetoothManagementService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CrackRouterActivity extends AppCompatActivity {
    private final static String TAG_INFO = "Log info";
    private final static String TAG_DEBUG = "Log debug";
    private final static String TAG_ERROR = "Log error";

    private Context mContext;
    private BluetoothManagementService mBluetoothManagementService;
    private boolean mBound = false;
    private boolean isRemoteDeviceConnected = false;

    Button mStartCrackingButton;
    TextView TextView_mac;
    TextView TextView_sig;

    TextView TextView_company;
    TextView TextView_sec;
    TextView TextView_fre;

    TextView TextView_cha;
    TextView TextView_ssid;
    ProgressBar progressBar;
    Button button;


    TextView textView_crack_status;


    RelativeLayout pass_wifi_relative;

    EditText pass_result;
    TextView myprogress_crack;
    ImageButton copy_pass;

    MainModel information_data;


    // True can crqck
    // False cannot crack
    Boolean pass_status = false;


    private ClipboardManager myClipboard;
    private ClipData myClip;

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothManagementService.ACTION_WIFI_ATTACK.equals(action)) {
                Bundle bundle = intent.getExtras();
                String jsonString = bundle.getString("payload");
                Log.i(TAG_INFO, "Receive ACTION_WIFI_ATTACK: " + jsonString);
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

        TextView_mac = (TextView) findViewById(R.id.mac_value);
        TextView_sig = (TextView) findViewById(R.id.sig_value);
        TextView_company = (TextView) findViewById(R.id.comp_value);
        TextView_sec = (TextView) findViewById(R.id.sec_value);
        TextView_fre = (TextView) findViewById(R.id.fre_value);
        TextView_cha = (TextView) findViewById(R.id.cha_value);
        TextView_ssid = (TextView) findViewById(R.id.textView_ssid);

        textView_crack_status = (TextView) findViewById(R.id.textView_crack_status);

        myprogress_crack = (TextView) findViewById(R.id.textView_progress_crack);

        progressBar = (ProgressBar) findViewById(R.id.progressBar_crack);

        copy_pass = (ImageButton) findViewById(R.id.imageButton_copy_pass);

        pass_result = (EditText) findViewById(R.id.editText_pass_wifi);

        pass_wifi_relative = (RelativeLayout) findViewById(R.id.pass_wifi_relative);


        //Status
        progressBar.setVisibility(View.INVISIBLE);
        myprogress_crack.setVisibility(View.INVISIBLE);
        pass_wifi_relative.setVisibility(View.INVISIBLE);
        textView_crack_status.setVisibility(View.INVISIBLE);

        information_data = (MainModel) getIntent().getParcelableExtra("router_information");


        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        mContext = getApplicationContext();
        Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
        if (!mBound) {
            bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothManagementService.ACTION_WIFI_ATTACK);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);

        final MainModel information_data = (MainModel) getIntent().getParcelableExtra("router_information");

        TextView_ssid.setText(information_data.getOfferSSID());
        TextView_sec.setText(information_data.getOfferMac_address());
        TextView_mac.setText(information_data.getOfferSecurity());
        TextView_sig.setText(information_data.getOfferSignal());
        TextView_company.setText(information_data.getOfferCompany());
        TextView_fre.setText(information_data.getOfferFrequency());
        TextView_cha.setText(information_data.getOfferChannel());

        mStartCrackingButton = (Button) findViewById(R.id.button_start_wifi_cracking);
        mStartCrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRemoteDeviceConnected) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("command", "wifiCracking");
                    Gson gson = new Gson();
                    String payloadJsonString = gson.toJson(information_data, MainModel.class);
                    Log.i(TAG_INFO, payloadJsonString);
//                    jsonObject.addProperty("payload", payloadJsonString);
                    JsonParser jsonParser = new JsonParser();
                    JsonElement payloadJsonElement = jsonParser.parse(payloadJsonString);
                    jsonObject.add("payload", payloadJsonElement);
                    String jsonString = jsonObject.toString();
                    mBluetoothManagementService.sendMessageToRemoteDevice(jsonString + "|");
                }
            }
        });

        copy_pass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text;
                text = pass_result.getText().toString();

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getApplicationContext(), "Text Copied",
                        Toast.LENGTH_SHORT).show();
            }
        });


        button = (Button) findViewById(R.id.crack_btn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                myprogress_crack.setVisibility(View.VISIBLE);

                myprogress_crack.setText("Cracking....." + information_data.getOfferSSID() + "'s password.");
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
        dialog.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() { // define the 'Cancel' button
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

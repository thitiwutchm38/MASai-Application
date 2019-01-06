package com.example.bookthiti.masai2;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookthiti.masai2.service.BluetoothManagementService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Search_Network extends AppCompatActivity implements OnRecyclerViewItemClickListener {
    private final static String TAG_INFO = "Log info";
    private final static String TAG_DEBUG = "Log debug";
    private final static String TAG_ERROR = "Log error";

    final String wifi_password = null;

    private final int categoryIcon = R.drawable.wifi_device;


    private Context mContext;

    private BluetoothManagementService mBluetoothManagementService;
    private boolean mBound = false;
    private boolean isRemoteDeviceConnected = false;

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothManagementService.ACTION_WIFI_SCAN.equals(action)) {
                Bundle bundle = intent.getExtras();
                String jsonString = bundle.getString("payload");
                Log.i(TAG_INFO, jsonString);
                final RecyclerView mainRecyclerView = findViewById(R.id.rv_router_list);
                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search_Network.this,
                        LinearLayoutManager.VERTICAL, false);
                mainRecyclerView.setLayoutManager(linearLayoutManager);
                //Recycler Adapter
                final ArrayList<MainModel> mainModelArrayList = prepareList(jsonString);
                final MainRecyclerAdapter mainRecyclerAdapter = new MainRecyclerAdapter(Search_Network.this, mainModelArrayList);
                mainRecyclerAdapter.setOnRecyclerViewItemClickListener(Search_Network.this);
                mainRecyclerView.setAdapter(mainRecyclerAdapter);
                final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
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
            mBluetoothManagementService.sendMessageToRemoteDevice("wifiScan");
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
        setContentView(R.layout.activity_search_network);
        mContext = getApplicationContext();
        Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
        if(!mBound) {
            bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothManagementService.ACTION_WIFI_SCAN);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);

    }

    @Override
    protected void onDestroy() {
        if(mBound){
            unbindService(mConnection);
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalBroadcastReceiver);
        super.onDestroy();
    }

    private ArrayList<MainModel> prepareList(String jsonString) {
        ArrayList<MainModel> mainModelList = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int count = jsonObject.get("count").getAsInt();


        String SSID = null;
        String mode = null;
        String signal = null;
        String frequency = null;
        String channel = null;
        String company = null;
        String bssid = null;
        String security = null;


        for (int i = 0; i < count; i++) {
            JsonObject temp = jsonObject.getAsJsonObject("" + i);
            if(temp != null) {
                SSID = temp.get("SSID").getAsString();
                mode = temp.get("MODE").getAsString();
                signal = temp.get("SIGNAL").getAsString();
                channel = temp.get("CHAN").getAsString();
                security = temp.get("SECURITY").getAsString();
                bssid = temp.get("BSSID").getAsString();
                MainModel mainModel = new MainModel(SSID, mode, signal, channel, bssid, security, categoryIcon);
                mainModelList.add(mainModel);
            }

        }
        return mainModelList;
    }

    @Override
    public void onItemClick(final int position, View view) {
        //    MainModel mainModel = (MainModel) view.getTag();

        Intent intent = getIntent();
        Intent pass_intent = new Intent(this, CrackRouterActivity.class);
        final Intent pass_intent_blu = new Intent(this, Device_list.class);


//        switch (intent.getStringExtra("MyValue")) {
//            case "device_att":
//
//                //Toast.makeText(this,"Position clicked: " + String.valueOf(position) + ", "+ mainModel.getOfferSSID(),Toast.LENGTH_LONG).show();
//                //showAddItemDialog(this,prepareList().get(position).getOfferSSID() );
//
//                //pass_intent_blu.putExtra("router_information", prepareList().get(position));
//
//                promptForResult(new PromptRunnable() {
//                    // put whatever code you want to run after user enters a result
//                    public void run() {
//                        // get the value we stored from the dialog
//                        String value = this.getValue();
//                        // do something with this value...
//                        // In our example we are taking our value and passing it to
//                        // an activity intent, then starting the activity.
//                        //pass_intent_blu.putExtra("extraValue", value);
//                        pass_intent_blu.putExtra("router_information", prepareList().get(position));
//                        pass_intent_blu.putExtra("password", value);
//
//                        startActivity(pass_intent_blu);
//                    }
//                }, prepareList().get(position).getOfferSSID());
//
//
//                break;
//
//            case "router_att":
//
//                Toast.makeText(this, "Position clicked: " + String.valueOf(position) + ", " + prepareList().get(position).getOfferSSID(), Toast.LENGTH_LONG).show();
//                //showAddItemDialog(this,prepareList().get(position).getOfferSSID() );
//
//                pass_intent.putExtra("router_information", prepareList().get(position));
//                //pass_intent.putExtra("iis", );
//                startActivity(pass_intent);
//
//                break;
//        }
    }

    void promptForResult(final PromptRunnable postrun, String ssid) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Please input your WiFi Password");
        alert.setMessage("Your SSID: " + ssid);
        // Create textbox to put into the dialog
        final EditText input = new EditText(this);
        // put the textbox into the dialog
        alert.setView(input);
        // procedure for when the ok button is clicked.
        alert.setPositiveButton("Confirmed", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                dialog.dismiss();
                // set value from the dialog inside our runnable implementation
                postrun.setValue(value);
                // ** HERE IS WHERE THE MAGIC HAPPENS! **
                // now that we have stored the value, lets run our Runnable
                postrun.run();
                return;
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                return;
            }
        });
        alert.show();
    }


    class PromptRunnable implements Runnable {
        private String v;

        void setValue(String inV) {
            this.v = inV;
        }

        String getValue() {
            return this.v;
        }

        public void run() {
            this.run();
        }
    }

    private boolean isRemoteDeviceConnected() {
        if(mBluetoothManagementService != null && mBound) {
            return mBluetoothManagementService.isRemoteDeviceConnected();
        }
        return false;
    }

}
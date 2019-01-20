package com.example.bookthiti.masai2.networksearchingscreen;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bookthiti.masai2.routercrackingscreen.CrackRouterActivity;
import com.example.bookthiti.masai2.devicediscoveryscreen.DeviceDiscoveryActivity;
import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import static com.example.bookthiti.masai2.LogConstants.TAG_INFO;

public class SearchNetworkActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {
    private Context mContext;
    private BluetoothManagementService mBluetoothManagementService;
    private boolean mBound = false;
    private boolean isRemoteDeviceConnected = false;
    private ArrayList<RouterModel> mRouterModelArrayList;
    private RouterModel mConnectingRouterModel;

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothManagementService.ACTION_WIFI_SCAN.equals(action)) {
                Bundle bundle = intent.getExtras();
                String jsonString = bundle.getString("payload");
                Log.i(TAG_INFO, jsonString);
                RecyclerView mainRecyclerView = findViewById(R.id.rv_router_list);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchNetworkActivity.this,
                        LinearLayoutManager.VERTICAL, false);
                mainRecyclerView.setLayoutManager(linearLayoutManager);
                //Recycler Adapter
                mRouterModelArrayList = loadRouterModelList(jsonString);
                RouterRecyclerAdapter routerRecyclerAdapter = new RouterRecyclerAdapter(SearchNetworkActivity.this, mRouterModelArrayList);
                routerRecyclerAdapter.setOnRecyclerViewItemClickListener(SearchNetworkActivity.this);
                mainRecyclerView.setAdapter(routerRecyclerAdapter);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
            } else if (BluetoothManagementService.ACTION_WIFI_CONNECT.equals(action)) {
                Bundle bundle = intent.getExtras();
                String jsonString = bundle.getString("payload");
                Log.i(TAG_INFO, jsonString);
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(jsonString);
                JsonObject payload = jsonElement.getAsJsonObject();
                String connection_status = payload.get("status").getAsString();
                if (connection_status.equals("success")) {
                    startActivity(new Intent(mContext, DeviceDiscoveryActivity.class));
                } else if (connection_status.equals("failure")) {
                    Log.i(TAG_INFO, "Wrong password was input");
                    if (mConnectingRouterModel != null)
                        promptForPassword(mConnectingRouterModel, false);
                }
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
            Log.i(TAG_INFO, "ServiceModel is unbounded");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_network);
        mContext = getApplicationContext();
        Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
        if (!mBound) {
            bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothManagementService.ACTION_WIFI_SCAN);
        intentFilter.addAction(BluetoothManagementService.ACTION_WIFI_CONNECT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        if (mBound) {
            unbindService(mConnection);
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalBroadcastReceiver);
        super.onDestroy();
    }

    private ArrayList<RouterModel> loadRouterModelList(String jsonString) {
        ArrayList<RouterModel> routerModelList = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int count = jsonObject.get("count").getAsInt();
        JsonArray jsonArray = jsonObject.getAsJsonArray("routers");

        for (JsonElement router : jsonArray) {
            JsonObject temp = router.getAsJsonObject();
            if (temp != null) {
                String ssid = temp.get("SSID").getAsString();
                String mode = temp.get("MODE").getAsString();
                float signal = temp.get("SIGNAL").getAsFloat();
                int channel = temp.get("CHAN").getAsInt();
                String security = temp.get("SECURITY").getAsString();
                String bssid = temp.get("BSSID").getAsString();
                RouterModel routerModel = new RouterModel(ssid, mode, signal, channel, bssid, security);
                routerModelList.add(routerModel);
            }
        }

        return routerModelList;
    }

    @Override
    public void onItemClick(final int position, View view) {
        //    RouterModel mainModel = (RouterModel) view.getTag();

        Intent intent = getIntent();
        Intent crackRouterIntent = new Intent(this, CrackRouterActivity.class);
        Intent deviceDiscoveryIntent = new Intent(this, DeviceDiscoveryActivity.class);
        switch (intent.getStringExtra("MyValue")) {
            case "device_att":
                promptForPassword(mRouterModelArrayList.get(position), true);
                break;

            case "router_att":
                Toast.makeText(this, "Position clicked: " + String.valueOf(position) + ", " + mRouterModelArrayList.get(position).getSsid(), Toast.LENGTH_LONG).show();
                //showAddItemDialog(this,loadRouterModelList().get(position).getmSsid() );
                crackRouterIntent.putExtra("router_information", mRouterModelArrayList.get(position));
                //pass_intent.putExtra("iis", );
                startActivity(crackRouterIntent);
                break;
        }
    }

    private void promptForPassword(final RouterModel routerModel, boolean isFirstAttempt) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Please input your WiFi Password");
        alert.setMessage("Your SSID: " + routerModel.getSsid());
        // Create textbox to put into the dialog
        LayoutInflater inflater = getLayoutInflater();
        View customLayout = inflater.inflate(R.layout.dialog_incorrect_password, null);
        final EditText input = customLayout.findViewById(R.id.edit_password);
        final TextInputLayout textInputLayout = customLayout.findViewById(R.id.text_input_layout);
        if (!isFirstAttempt) {
            textInputLayout.setError("Incorrect password");
        }
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        alert.setView(customLayout);
        alert.setPositiveButton("Confirmed", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mConnectingRouterModel = routerModel;
                String value = input.getText().toString();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("command", "wifiConnect");
                JsonObject payload = new JsonObject();
                payload.addProperty("password", value);
                Gson gson = new Gson();
                String routerModelString = gson.toJson(routerModel, RouterModel.class);
                JsonParser jsonParser = new JsonParser();
                JsonElement routerObject = jsonParser.parse(routerModelString);
                payload.add("router", routerObject);
                jsonObject.add("payload", payload);
                String jsonString = jsonObject.toString();
                mBluetoothManagementService.sendMessageToRemoteDevice(jsonString + "|");
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private boolean isRemoteDeviceConnected() {
        if (mBluetoothManagementService != null && mBound) {
            return mBluetoothManagementService.isRemoteDeviceConnected();
        }
        return false;
    }

}
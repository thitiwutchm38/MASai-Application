package com.example.bookthiti.masai2.networksearchingscreen;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bookthiti.masai2.OnRecyclerViewItemClickListener;
import com.example.bookthiti.masai2.bluetoothservice.INotificationId;
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
import java.util.Collections;
import java.util.Comparator;

import static com.example.bookthiti.masai2.LogConstants.TAG_INFO;

public class SearchNetworkActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {
    private Context mContext;
    private BluetoothManagementService mBluetoothManagementService;
    private boolean mBound = false;
    private boolean isRemoteDeviceConnected = false;
    private ArrayList<RouterModel> mRouterModelArrayList;
    private RouterModel mConnectingRouterModel;
    private RouterRecyclerAdapter mRouterRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ConstraintLayout mConStraintLayoutHeaderLine;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mConnectingRouterPosition;
    private int sortingByNameState = 0;
    private int sortingBySignalState = 0;
    private static final String mockJson = "{\"routers\":[{\"SSID\":\"TEST\", " +
            "\"MODE\":\"infra\", " +
            "\"SIGNAL\":\"99.0\", " +
            "\"CHAN\":\"11\", " +
            "\"SECURITY\":\"WEP\", " +
            "\"BSSID\":\"AA:AA:AA:AA:AA:AA\"}" +
            "], " +
            "count:1}";

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothManagementService.ACTION_WIFI_SCAN.equals(action)) {
                setResultFromIntent(intent);
            } else if (BluetoothManagementService.ACTION_WIFI_CONNECT.equals(action)) {
                mConnectingRouterModel.setConnecting(false);
                mRouterRecyclerAdapter.notifyItemChanged(mConnectingRouterPosition);
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
                        promptForPassword(mConnectingRouterModel, false, mConnectingRouterPosition);
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
            Log.i(TAG_INFO, "Service is unbounded");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_network);
        mContext = getApplicationContext();
        mConStraintLayoutHeaderLine = findViewById(R.id.layout_header_line);
        mConStraintLayoutHeaderLine.setVisibility(View.INVISIBLE);

        //FIXME: Uncomment for mockup
        setRecyclerView(mockJson);


        //FIXME: Uncomment for real application
//        if (!getIntent().getBooleanExtra(INotificationId.FLAG_IS_FROM_NOTIFICATION, false)) {
//            Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
//            if (!mBound) {
//                bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
//            }
//            IntentFilter intentFilter = new IntentFilter();
//            intentFilter.addAction(BluetoothManagementService.ACTION_WIFI_SCAN);
//            intentFilter.addAction(BluetoothManagementService.ACTION_WIFI_CONNECT);
//            LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);
//            mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
//            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    if(isRemoteDeviceConnected) {
//                        JsonObject jsonObject = new JsonObject();
//                        jsonObject.addProperty("command", "wifiScan");
//                        jsonObject.add("payload", null);
//                        String jsonString = jsonObject.toString();
//                        mBluetoothManagementService.sendMessageToRemoteDevice(jsonString + "|");
//                    }
//                }
//            });
//        } else {
//            setResultFromIntent(getIntent());
//        }


    }

    @Override
    protected void onDestroy() {
        if (mBound) {
            unbindService(mConnection);
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_router_sorting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_name :
                if (mRouterModelArrayList != null) {
                    if (sortingByNameState == 0) {
                        Collections.sort(mRouterModelArrayList, new Comparator<RouterModel>() {
                            @Override
                            public int compare(RouterModel routerModel, RouterModel t1) {
                                return routerModel.getSsid().compareTo(t1.getSsid());
                            }
                        });
                        sortingByNameState = 1;
                    } else {
                        Collections.sort(mRouterModelArrayList, new Comparator<RouterModel>() {
                            @Override
                            public int compare(RouterModel routerModel, RouterModel t1) {
                                return t1.getSsid().compareTo(routerModel.getSsid());
                            }
                        });
                        sortingByNameState = 0;
                    }
                    mRouterRecyclerAdapter.notifyDataSetChanged();
                }
                return true;
            case R.id.menu_sort_by_signal:
                if (mRouterModelArrayList != null) {
                    if (sortingBySignalState == 0) {
                        Collections.sort(mRouterModelArrayList, new Comparator<RouterModel>() {
                            @Override
                            public int compare(RouterModel routerModel, RouterModel t1) {
                                return Math.round(routerModel.getSignal() - t1.getSignal());
                            }
                        });
                        sortingBySignalState = 1;
                    } else {
                        Collections.sort(mRouterModelArrayList, new Comparator<RouterModel>() {
                            @Override
                            public int compare(RouterModel routerModel, RouterModel t1) {
                                return Math.round(t1.getSignal() - routerModel.getSignal());
                            }
                        });
                        sortingBySignalState = 0;
                    }
                    mRouterRecyclerAdapter.notifyDataSetChanged();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(final int position, View view) {
        //    RouterModel mainModel = (RouterModel) view.getTag();
        Intent intent = getIntent();
        Intent crackRouterIntent = new Intent(this, CrackRouterActivity.class);
        Intent deviceDiscoveryIntent = new Intent(this, DeviceDiscoveryActivity.class);
        switch (intent.getStringExtra("MyValue")) {
            case "device_att":
                //FIXME: Uncomment for real application
//                promptForPassword(mRouterModelArrayList.get(position), true, position);

                //FIXME: Uncomment for mockup
                startActivity(new Intent(mContext, DeviceDiscoveryActivity.class));

                break;

            case "router_att":
//                Toast.makeText(this, "Position clicked: " + String.valueOf(position) + ", " + mRouterModelArrayList.get(position).getSsid(), Toast.LENGTH_LONG).show();
                //showAddItemDialog(this,loadRouterModelList().get(position).getmSsid() );
                crackRouterIntent.putExtra("router_information", mRouterModelArrayList.get(position));
                //pass_intent.putExtra("iis", );
                startActivity(crackRouterIntent);
                break;
        }
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

    private void setRecyclerView(String jsonString) {
        mRecyclerView = findViewById(R.id.rv_router_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //Recycler Adapter
        mRouterModelArrayList = loadRouterModelList(jsonString);
        mRouterRecyclerAdapter = new RouterRecyclerAdapter(mContext, mRouterModelArrayList);
        mRouterRecyclerAdapter.setOnRecyclerViewItemClickListener(SearchNetworkActivity.this);
        mRecyclerView.setAdapter(mRouterRecyclerAdapter);
    }

    private void promptForPassword(final RouterModel routerModel, boolean isFirstAttempt, final int position) {
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
                mConnectingRouterPosition = position;
                mConnectingRouterModel = routerModel;
                mConnectingRouterModel.setConnecting(true);
                mRouterRecyclerAdapter.notifyItemChanged(position);
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

    private void setResultFromIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String jsonString = bundle.getString("payload");
        Log.i(TAG_INFO, jsonString);
        setRecyclerView(jsonString);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        mConStraintLayoutHeaderLine.setVisibility(View.VISIBLE);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
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

import com.example.bookthiti.masai2.utils.OnRecyclerViewItemClickListener;
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

import static com.example.bookthiti.masai2.utils.LogConstants.TAG_INFO;

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
//        setRecyclerView(mockJson);


        //FIXME: Uncomment for real application
        if (!getIntent().getBooleanExtra(INotificationId.FLAG_IS_FROM_NOTIFICATION, false)) {
            Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
            if (!mBound) {
                bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothManagementService.ACTION_WIFI_SCAN);
            intentFilter.addAction(BluetoothManagementService.ACTION_WIFI_CONNECT);
            LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);
            mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if(isRemoteDeviceConnected) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("command", "wifiScan");
                        jsonObject.add("payload", null);
                        String jsonString = jsonObject.toString();
                        mBluetoothManagementService.sendMessageToRemoteDevice(jsonString + "|");
                    }
                }
            });
        } else {
            setResultFromIntent(getIntent());
        }

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
                promptForPassword(mRouterModelArrayList.get(position), true, position);

                //FIXME: Uncomment for mockup
//                startActivity(new Intent(mContext, DeviceDiscoveryActivity.class));

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

    private static final String mockJson = "{\n" +
            "        \"routers\": [\n" +
            "            {\n" +
            "                \"NAME\": \"AP[1]\",\n" +
            "                \"SSID\": \"DESKTOP-CFI22OE 8615\",\n" +
            "                \"BSSID\": \"3E:F8:62:EB:FD:7A\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"11\",\n" +
            "                \"SIGNAL\": \"100\",\n" +
            "                \"SECURITY\": \"WPA2\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[2]\",\n" +
            "                \"SSID\": \".@ AIS SUPER WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:77:80\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"11\",\n" +
            "                \"SIGNAL\": \"90\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[3]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:77:82\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"11\",\n" +
            "                \"SIGNAL\": \"90\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[4]\",\n" +
            "                \"SSID\": \"ICTenergy-Test\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:77:83\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"11\",\n" +
            "                \"SIGNAL\": \"89\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[5]\",\n" +
            "                \"SSID\": \"MU-WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:77:81\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"11\",\n" +
            "                \"SIGNAL\": \"89\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[6]\",\n" +
            "                \"SSID\": \".@ AIS SUPER WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:77:90\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"52\",\n" +
            "                \"SIGNAL\": \"79\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[7]\",\n" +
            "                \"SSID\": \"MU-WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:77:91\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"52\",\n" +
            "                \"SIGNAL\": \"79\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[8]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:77:92\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"52\",\n" +
            "                \"SIGNAL\": \"79\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[9]\",\n" +
            "                \"SSID\": \"ICTenergy-Test\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:77:93\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"52\",\n" +
            "                \"SIGNAL\": \"79\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[10]\",\n" +
            "                \"SSID\": \"VoIP ICT\",\n" +
            "                \"BSSID\": \"2A:A4:3C:0D:34:B3\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"1\",\n" +
            "                \"SIGNAL\": \"74\",\n" +
            "                \"SECURITY\": \"WPA1 WPA2\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[11]\",\n" +
            "                \"SSID\": \"--\",\n" +
            "                \"BSSID\": \"00:25:9C:49:E6:37\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"2\",\n" +
            "                \"SIGNAL\": \"69\",\n" +
            "                \"SECURITY\": \"WPA1 WPA2\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[12]\",\n" +
            "                \"SSID\": \"MU-WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:7A:A1\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"6\",\n" +
            "                \"SIGNAL\": \"64\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[13]\",\n" +
            "                \"SSID\": \".@ AIS SUPER WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:7A:A0\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"6\",\n" +
            "                \"SIGNAL\": \"64\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[14]\",\n" +
            "                \"SSID\": \"ICTenergy-Test\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:7A:A3\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"6\",\n" +
            "                \"SIGNAL\": \"62\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[15]\",\n" +
            "                \"SSID\": \"MU-WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:76:61\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"1\",\n" +
            "                \"SIGNAL\": \"55\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[16]\",\n" +
            "                \"SSID\": \"MU-WiFi\",\n" +
            "                \"BSSID\": \"24:F2:7F:02:A2:30\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"149\",\n" +
            "                \"SIGNAL\": \"55\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[17]\",\n" +
            "                \"SSID\": \".@ AIS SUPER WiFi\",\n" +
            "                \"BSSID\": \"24:F2:7F:02:A2:31\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"149\",\n" +
            "                \"SIGNAL\": \"55\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[18]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"24:F2:7F:02:A2:32\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"149\",\n" +
            "                \"SIGNAL\": \"55\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[19]\",\n" +
            "                \"SSID\": \"MU-WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:91\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"48\",\n" +
            "                \"SIGNAL\": \"54\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[20]\",\n" +
            "                \"SSID\": \"ICTenergy-Test\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:76:63\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"1\",\n" +
            "                \"SIGNAL\": \"52\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[21]\",\n" +
            "                \"SSID\": \".@ AIS SUPER WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:90\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"48\",\n" +
            "                \"SIGNAL\": \"52\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[22]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:92\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"48\",\n" +
            "                \"SIGNAL\": \"52\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[23]\",\n" +
            "                \"SSID\": \"ICTenergy-Test\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:93\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"48\",\n" +
            "                \"SIGNAL\": \"52\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[24]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:76:62\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"1\",\n" +
            "                \"SIGNAL\": \"50\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[25]\",\n" +
            "                \"SSID\": \".@ AIS SUPER WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:00\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"6\",\n" +
            "                \"SIGNAL\": \"50\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[26]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:02\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"6\",\n" +
            "                \"SIGNAL\": \"49\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[27]\",\n" +
            "                \"SSID\": \"MU-WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:01\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"6\",\n" +
            "                \"SIGNAL\": \"49\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[28]\",\n" +
            "                \"SSID\": \"ICTenergy-Test\",\n" +
            "                \"BSSID\": \"24:F2:7F:02:9E:03\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"6\",\n" +
            "                \"SIGNAL\": \"49\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[29]\",\n" +
            "                \"SSID\": \".@ AIS SUPER WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:76:70\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"157\",\n" +
            "                \"SIGNAL\": \"49\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[30]\",\n" +
            "                \"SSID\": \"MU-WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:76:71\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"157\",\n" +
            "                \"SIGNAL\": \"49\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[31]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:76:72\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"157\",\n" +
            "                \"SIGNAL\": \"49\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[32]\",\n" +
            "                \"SSID\": \"ICTenergy-Test\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:76:73\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"157\",\n" +
            "                \"SIGNAL\": \"49\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[33]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:7A:A2\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"6\",\n" +
            "                \"SIGNAL\": \"47\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[34]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"24:F2:7F:02:A2:22\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"6\",\n" +
            "                \"SIGNAL\": \"45\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[35]\",\n" +
            "                \"SSID\": \".@ AIS SUPER WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:7A:B0\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"100\",\n" +
            "                \"SIGNAL\": \"45\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[36]\",\n" +
            "                \"SSID\": \"MU-WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:7A:B1\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"100\",\n" +
            "                \"SIGNAL\": \"45\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[37]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:7A:B2\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"100\",\n" +
            "                \"SIGNAL\": \"45\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[38]\",\n" +
            "                \"SSID\": \"ICTenergy-Test\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:7A:B3\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"100\",\n" +
            "                \"SIGNAL\": \"45\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[39]\",\n" +
            "                \"SSID\": \".@ AIS SUPER WiFi\",\n" +
            "                \"BSSID\": \"24:F2:7F:02:A2:21\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"6\",\n" +
            "                \"SIGNAL\": \"44\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[40]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:A2\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"11\",\n" +
            "                \"SIGNAL\": \"37\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[41]\",\n" +
            "                \"SSID\": \"ICTenergy-Test\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:A3\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"11\",\n" +
            "                \"SIGNAL\": \"37\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[42]\",\n" +
            "                \"SSID\": \".@ AIS SUPER WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:B0\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"40\",\n" +
            "                \"SIGNAL\": \"37\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[43]\",\n" +
            "                \"SSID\": \"MU-WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:B1\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"40\",\n" +
            "                \"SIGNAL\": \"37\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[44]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:B2\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"40\",\n" +
            "                \"SIGNAL\": \"37\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[45]\",\n" +
            "                \"SSID\": \"ICTenergy-Test\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:B3\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"40\",\n" +
            "                \"SIGNAL\": \"37\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[46]\",\n" +
            "                \"SSID\": \".@ AIS SUPER WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:10\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"157\",\n" +
            "                \"SIGNAL\": \"34\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[47]\",\n" +
            "                \"SSID\": \"MU-WiFi\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:11\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"157\",\n" +
            "                \"SIGNAL\": \"34\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[48]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:12\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"157\",\n" +
            "                \"SIGNAL\": \"32\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[49]\",\n" +
            "                \"SSID\": \"ICTenergy-Test\",\n" +
            "                \"BSSID\": \"20:A6:CD:AE:78:13\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"157\",\n" +
            "                \"SIGNAL\": \"32\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[50]\",\n" +
            "                \"SSID\": \"AaAaaAAaAAAa4\",\n" +
            "                \"BSSID\": \"BA:D7:AF:90:C7:33\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"6\",\n" +
            "                \"SIGNAL\": \"27\",\n" +
            "                \"SECURITY\": \"WPA2\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[51]\",\n" +
            "                \"SSID\": \"ICTenergy-Test\",\n" +
            "                \"BSSID\": \"24:F2:7F:02:A1:B3\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"153\",\n" +
            "                \"SIGNAL\": \"27\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[52]\",\n" +
            "                \"SSID\": \".@ AIS SUPER WiFi\",\n" +
            "                \"BSSID\": \"24:F2:7F:02:A1:B0\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"153\",\n" +
            "                \"SIGNAL\": \"25\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[53]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"24:F2:7F:02:A1:B2\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"153\",\n" +
            "                \"SIGNAL\": \"25\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[54]\",\n" +
            "                \"SSID\": \"MU-WiFi\",\n" +
            "                \"BSSID\": \"24:F2:7F:02:A1:B1\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"153\",\n" +
            "                \"SIGNAL\": \"24\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[55]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"24:F2:7F:02:9E:42\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"1\",\n" +
            "                \"SIGNAL\": \"20\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"NAME\": \"AP[56]\",\n" +
            "                \"SSID\": \".@ TRUEWIFI\",\n" +
            "                \"BSSID\": \"24:F2:7F:02:9F:22\",\n" +
            "                \"MODE\": \"Infra\",\n" +
            "                \"CHAN\": \"11\",\n" +
            "                \"SIGNAL\": \"19\",\n" +
            "                \"SECURITY\": \"--\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"count\": 56\n" +
            "    }";
}
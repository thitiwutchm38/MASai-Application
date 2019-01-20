package com.example.bookthiti.masai2.devicediscoveryscreen;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.example.bookthiti.masai2.networksearchingscreen.OnRecyclerViewItemClickListener;
import com.example.bookthiti.masai2.networksearchingscreen.RouterModel;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.bookthiti.masai2.LogConstants.TAG_INFO;

public class DeviceDiscoveryActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {
    private Context mContext;
    private BluetoothManagementService mBluetoothManagementService;
    private boolean mBound = false;
    private boolean isRemoteDeviceConnected = false;
    private ArrayList<RouterModel> mRouterModelArrayList;

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: listen to ACTION_DEVICE_SCAN
//            String action = intent.getAction();
//            if (BluetoothManagementService.ACTION_WIFI_SCAN.equals(action)) {
//                Bundle bundle = intent.getExtras();
//                String jsonString = bundle.getString("payload");
//                Log.i(TAG_INFO, jsonString);
//                RecyclerView mainRecyclerView = findViewById(R.id.rv_router_list);
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchNetworkActivity.this,
//                        LinearLayoutManager.VERTICAL, false);
//                mainRecyclerView.setLayoutManager(linearLayoutManager);
//                //Recycler Adapter
//                mRouterModelArrayList = loadRouterModelList(jsonString);
//                RouterRecyclerAdapter routerRecyclerAdapter = new RouterRecyclerAdapter(SearchNetworkActivity.this, mRouterModelArrayList);
//                routerRecyclerAdapter.setOnRecyclerViewItemClickListener(SearchNetworkActivity.this);
//                mainRecyclerView.setAdapter(routerRecyclerAdapter);
//                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.GONE);
//            }
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
            jsonObject.addProperty("command", "deviceScan");
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

    private ArrayList<DeviceModel> mDeviceList;
    String device_ip;
    String device_mac;
    String device_type;

    private final int categoryIcon[] = {
            R.drawable.wifi_device_4,
            R.drawable.wifi_device_4,
            R.drawable.wifi_device_4,

            R.drawable.wifi_device_4,
            R.drawable.wifi_device_4,
            R.drawable.wifi_device_4,

            R.drawable.wifi_device_4,
            R.drawable.wifi_device_4,
            R.drawable.wifi_device_4

    };

    private final String categoryName[] = {
            "Apple",
            "Samsung",
            "MI",
            "Motorola",
            "Nokia",
            "Oppo",
            "Micromax",
            "Honor",
            "Lenovo"
    };

    private final String mac[] = {
            "4b:d3:f0:a9:ef:a5",
            "b7:66:6b:a7:01:57",
            "63:d4:41:a8:d0:b1",
            "a7:c4:13:42:56:3b",
            "32:54:f1:c5:49:07",
            "12:eb:32:e2:be:93",
            "17:1f:cb:24:6f:90",
            "e7:35:35:ea:5c:af",
            "17:cf:ff:09:42:d4"
    };
    private final String type[] = {
            "media device",
            "phone",
            "general purpose",
            "printer",
            "phone",
            "webcam",
            "phone",
            "router",
            "general purpose"
    };

    // RelativeLayout currentLayout;
    // Now get a handle to any View contained
    // within the main layout you are using
    //final View someView = findViewById(R.id.layout_device);



    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);


        final RecyclerView mainRecyclerView = findViewById(R.id.recyclerview_device_item);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mainRecyclerView.setLayoutManager(linearLayoutManager);


        //Recycler Adapter
        final ArrayList<DeviceModel> mainModelArrayList = prepareList();
        final DeviceAdapter mainRecyclerAdapter = new DeviceAdapter(this, mainModelArrayList);
        mainRecyclerAdapter.setOnRecyclerViewItemClickListener(DeviceDiscoveryActivity.this);
        mainRecyclerView.setAdapter(mainRecyclerAdapter);
    }

    private ArrayList<DeviceModel> prepareList() {


        mDeviceList = new ArrayList<>();

        //Convert JSON File
        String json = null;
        Integer count = null;

        try {
            InputStream is = getAssets().open("device.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        JSONObject reader = null;
        try {
            reader = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            count = Integer.parseInt(reader.getString("count"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        device_ip = null;
        device_mac = null;
        device_type= null;




        for (int i = 0; i < count; i++) {


            DeviceModel mainModel = new DeviceModel();

            JSONObject temp = null;

            try {
                temp = reader.getJSONObject(Integer.toString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                device_ip  = temp.getString("IP_Address");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                device_mac  = temp.getString("Mac_Address");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                device_type  = temp.getString("Device_Types");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            mainModel.setmIP_address(device_ip);
            mainModel.setmMac_address(device_mac);
            mainModel.setmDevice_types(device_type);



            switch(device_type) {
                case "phone":
                    mainModel.setOfferIcon(R.drawable.icons_phone);
                    break; // optional

                case "printer":
                    mainModel.setOfferIcon(R.drawable.icons_printer);
                    break; // optional

                case "router":
                    mainModel.setOfferIcon(R.drawable.icons_router);
                    break; // optional
                case "webcam":
                    mainModel.setOfferIcon(R.drawable.icons_cam);
                    break; // optional

                case "general purpose":
                mainModel.setOfferIcon(R.drawable.icons_general);
                break; // optional

                case "media device":
                    mainModel.setOfferIcon(R.drawable.icons_media);
                    break; // optional

                // You can have any number of case statements.
                default : // Optional
                    // Statements
            }

            //mainModel.setmIconSignalId(categoryIcon[i]);


            mDeviceList.add(mainModel);





        }
        return mDeviceList;
    }

    @Override
    public void onItemClick( int position, View view ) {
        DeviceModel mainModel = (DeviceModel) view.getTag();
        switch (view.getId()) {
            case R.id.layout_device:
                Toast.makeText(this, "Position clicked: " + String.valueOf(position) + ", " + mainModel.getmIP_address(), Toast.LENGTH_LONG).show();
                openActivity_port_info(position);
                break;
        }


    }

    public void openActivity_port_info(int position) {

        Intent intent = new Intent(this,Device_information.class);

        //Bundle
        Bundle bundle = new Bundle();
        bundle.putString("IP_Address", mDeviceList.get(position).getmIP_address());
        bundle.putString("Mac_Address", mDeviceList.get(position).getmMac_address());
        bundle.putString("Device_Types", mDeviceList.get(position).getmDevice_types());
        bundle.putInt("icon", mDeviceList.get(position).getOfferIcon());


        intent.putExtras(bundle);

        startActivity(intent);
    }

    private boolean isRemoteDeviceConnected() {
        if(mBluetoothManagementService != null && mBound) {
            return mBluetoothManagementService.isRemoteDeviceConnected();
        }
        return false;
    }

}
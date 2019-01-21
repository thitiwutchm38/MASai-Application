package com.example.bookthiti.masai2.devicediscoveryscreen;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.CVEModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.DeviceInformationActivity;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.DeviceModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.ServiceModel;
import com.example.bookthiti.masai2.networksearchingscreen.OnRecyclerViewItemClickListener;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import static com.example.bookthiti.masai2.LogConstants.TAG_INFO;

public class DeviceDiscoveryActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {
    private Context mContext;
    private BluetoothManagementService mBluetoothManagementService;
    private boolean mBound = false;
    private boolean isRemoteDeviceConnected = false;
    private List<DeviceModel> mDeviceModelList;
    private DeviceDiscoveryModel mDeviceDiscoveryModel;

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothManagementService.ACTION_DEVICE_SCAN.equals(action)) {
                Bundle bundle = intent.getExtras();
                String jsonString = bundle.getString("payload");
                Log.i(TAG_INFO, jsonString);
                RecyclerView mainRecyclerView = findViewById(R.id.recyclerview_device_item);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DeviceDiscoveryActivity.this,
                        LinearLayoutManager.VERTICAL, false);
                mainRecyclerView.setLayoutManager(linearLayoutManager);

                mDeviceModelList = loadDeviceModelList(jsonString);
                //        //Recycler Adapter
                DeviceDiscoveryRecyclerAdapter mainRecyclerAdapter = new DeviceDiscoveryRecyclerAdapter(mContext, mDeviceModelList);
                mainRecyclerAdapter.setOnRecyclerViewItemClickListener(DeviceDiscoveryActivity.this);
                mainRecyclerView.setAdapter(mainRecyclerAdapter);

                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_device_discovery);
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
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("command", "deviceScan");
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
        setContentView(R.layout.activity_device_list);
        mContext = getApplicationContext();

        Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
        if (!mBound) {
            bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothManagementService.ACTION_DEVICE_SCAN);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);

//        RecyclerView mainRecyclerView = findViewById(R.id.recyclerview_device_item);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
//                LinearLayoutManager.VERTICAL, false);
//        mainRecyclerView.setLayoutManager(linearLayoutManager);

//        //Recycler Adapter
//        final ArrayList<DeviceModel> mainModelArrayList = prepareList();
//        final DeviceDiscoveryRecyclerAdapter mainRecyclerAdapter = new DeviceDiscoveryRecyclerAdapter(this, mainModelArrayList);
//        mainRecyclerAdapter.setOnRecyclerViewItemClickListener(DeviceDiscoveryActivity.this);
//        mainRecyclerView.setAdapter(mainRecyclerAdapter);
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
    public void onItemClick(int position, View view) {
        DeviceModel deviceModel = (DeviceModel) view.getTag();
        switch (view.getId()) {
            case R.id.layout_device:
                Toast.makeText(this, "Position clicked: " + String.valueOf(position) + ", " + deviceModel.getIpAddress(), Toast.LENGTH_LONG).show();
                openActivityPortInfo(position);
                break;
        }

    }

    private void openActivityPortInfo(int position) {

        Intent intent = new Intent(this, DeviceInformationActivity.class);
        intent.putExtra("targetDeviceModel", mDeviceModelList.get(position));
//        bundle.putString("IP_Address", mDeviceModelList.get(position).getIpAddress());
//        bundle.putString("Mac_Address", mDeviceModelList.get(position).getMacAddress());
//        bundle.putString("Device_Types", mDeviceModelList.get(position).getDeviceType());
//        bundle.putInt("icon", mDeviceModelList.get(position).getIconId());
        startActivity(intent);
    }

    private List<DeviceModel> loadDeviceModelList(String jsonString) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(CVEModel.Severity.class, new CVEModel.SeverityDeserializer());
        gsonBuilder.registerTypeAdapter(ServiceModel.class, new ServiceModel.ServiceModelDeserializer());
        gsonBuilder.registerTypeAdapter(DeviceModel.class, new DeviceModel.DeviceModelDeserializer());
        gsonBuilder.registerTypeAdapter(DeviceDiscoveryModel.class, new DeviceDiscoveryModel.DeviceDiscoveryModelDeserializer());
        mDeviceDiscoveryModel = gsonBuilder.create().fromJson(jsonString, DeviceDiscoveryModel.class);
        List<DeviceModel> deviceModels = mDeviceDiscoveryModel.getHosts();
        return deviceModels;
//        mDeviceList = new ArrayList<>();
//
//        //Convert JSON File
//        String json = null;
//        Integer count = null;
//
//        try {
//            InputStream is = getAssets().open("device.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//
//        JSONObject reader = null;
//        try {
//            reader = new JSONObject(json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            count = Integer.parseInt(reader.getString("count"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        device_ip = null;
//        device_mac = null;
//        device_type= null;
//
//
//
//
//        for (int i = 0; i < count; i++) {
//
//
//            DeviceModel mainModel = new DeviceModel();
//
//            JSONObject temp = null;
//
//            try {
//                temp = reader.getJSONObject(Integer.toString(i));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                device_ip  = temp.getString("IP_Address");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                device_mac  = temp.getString("Mac_Address");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                device_type  = temp.getString("Device_Types");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            mainModel.setIpAddress(device_ip);
//            mainModel.setMacAddress(device_mac);
//            mainModel.setDeviceType(device_type);
//


//            switch(device_type) {
//                case "phone":
//                    mainModel.setOfferIcon(R.drawable.icons_phone);
//                    break; // optional
//
//                case "printer":
//                    mainModel.setOfferIcon(R.drawable.icons_printer);
//                    break; // optional
//
//                case "router":
//                    mainModel.setOfferIcon(R.drawable.icons_router);
//                    break; // optional
//                case "webcam":
//                    mainModel.setOfferIcon(R.drawable.icons_cam);
//                    break; // optional
//
//                case "general purpose":
//                mainModel.setOfferIcon(R.drawable.icons_general);
//                break; // optional
//
//                case "media device":
//                    mainModel.setOfferIcon(R.drawable.icons_media);
//                    break; // optional
//
//                // You can have any number of case statements.
//                default : // Optional
//                    // Statements
//            }

        //mainModel.setmIconSignalId(categoryIcon[i]);

//
//            mDeviceList.add(mainModel);
//
//
//
//
//
//        }
//        return mDeviceList;
    }

    private boolean isRemoteDeviceConnected() {
        if (mBluetoothManagementService != null && mBound) {
            return mBluetoothManagementService.isRemoteDeviceConnected();
        }
        return false;
    }

}
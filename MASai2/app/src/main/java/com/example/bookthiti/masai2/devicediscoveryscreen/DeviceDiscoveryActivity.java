package com.example.bookthiti.masai2.devicediscoveryscreen;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookthiti.masai2.OnRecyclerViewItemClickListener;
import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.CVEModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.DeviceInformationActivity;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.DeviceModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.ServiceModel;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static com.example.bookthiti.masai2.LogConstants.TAG_INFO;

public class DeviceDiscoveryActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {
    private Context mContext;
    private BluetoothManagementService mBluetoothManagementService;
    private boolean mBound = false;
    private boolean isRemoteDeviceConnected = false;
    private List<DeviceModel> mDeviceModelList;
    private DeviceDiscoveryModel mDeviceDiscoveryModel;
    private DeviceDiscoveryRecyclerAdapter mDeviceDiscoveryRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private static final String mockJson = "{\n" +
            "  \"timeStats\": {\n" +
            "    \"startTime\": \"Sun Jan 20 23:47:33 2019\",\n" +
            "    \"finishTime\": \"Sun Jan 20 23:49:16 2019\",\n" +
            "    \"elapsed\": \"106.65\"\n" +
            "  },\n" +
            "  \"hostStats\": {\n" +
            "    \"up\": \"8\",\n" +
            "    \"down\": \"248\",\n" +
            "    \"total\": \"256\"\n" +
            "  },\n" +
            "  \"hosts\": [\n" +
            "    {\n" +
            "      \"status\": \"up\",\n" +
            "      \"ipv4\": \"192.168.1.1\",\n" +
            "      \"deviceType\": \"general purpose\",\n" +
            "      \"osName\": \"Linux 2.6.23\",\n" +
            "      \"osVendor\": \"Linux\",\n" +
            "      \"osCpe\": [\"cpe:/o:linux:linux_kernel:2.6.23\"],\n" +
            "      \"services\": [\n" +
            "        {\n" +
            "          \"port\": \"21\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"ftp\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"22\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"ssh\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"23\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"telnet\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"53\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"domain\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"80\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"http\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"443\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"https\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"990\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"ftps\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        }\n" +
            "      ],\n" +
            "      \"mac\": \"8C:15:C7:22:CB:7F\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"status\": \"up\",\n" +
            "      \"ipv4\": \"192.168.1.4\",\n" +
            "      \"deviceType\": null,\n" +
            "      \"osName\": null,\n" +
            "      \"osVendor\": null,\n" +
            "      \"osCpe\": null,\n" +
            "      \"services\": [],\n" +
            "      \"mac\": \"BC:83:85:13:70:CE\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"status\": \"up\",\n" +
            "      \"ipv4\": \"192.168.1.5\",\n" +
            "      \"deviceType\": \"general purpose\",\n" +
            "      \"osName\": \"Microsoft Windows Server 2008 SP1 or Windows Server 2008 R2\",\n" +
            "      \"osVendor\": \"Microsoft\",\n" +
            "      \"osCpe\": [\n" +
            "        \"cpe:/o:microsoft:windows_server_2008::sp1\",\n" +
            "        \"cpe:/o:microsoft:windows_server_2008:r2\"\n" +
            "      ],\n" +
            "      \"services\": [\n" +
            "        {\n" +
            "          \"port\": \"135\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"msrpc\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"139\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"netbios-ssn\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"445\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"microsoft-ds\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        }\n" +
            "      ],\n" +
            "      \"mac\": \"D4:6A:6A:5B:CE:AF\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"status\": \"up\",\n" +
            "      \"ipv4\": \"192.168.1.6\",\n" +
            "      \"deviceType\": \"phone\",\n" +
            "      \"osName\": \"Apple iOS 11.0\",\n" +
            "      \"osVendor\": \"Apple\",\n" +
            "      \"osCpe\": [\"cpe:/o:apple:iphone_os:11.0\"],\n" +
            "      \"services\": [\n" +
            "        {\n" +
            "          \"port\": \"83\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"mit-ml-dev\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"458\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"appleqtc\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"777\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"multiling-http\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"1033\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"netinfo\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"1044\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"dcutility\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"1201\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"nucleus-sand\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"1533\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"virtual-places\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"1583\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"simbaexpress\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"1812\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"radius\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"1974\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"drp\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"8400\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"cvd\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"10004\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"emcrmirccd\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"16018\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"unknown\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"40911\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"unknown\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"62078\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"iphone-sync\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"64680\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"filtered\",\n" +
            "          \"name\": \"unknown\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        }\n" +
            "      ],\n" +
            "      \"mac\": \"14:20:5E:9A:D9:69\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"status\": \"up\",\n" +
            "      \"ipv4\": \"192.168.1.7\",\n" +
            "      \"deviceType\": null,\n" +
            "      \"osName\": \"Linux 2.6.32 - 3.10\",\n" +
            "      \"osVendor\": null,\n" +
            "      \"osCpe\": null,\n" +
            "      \"services\": [\n" +
            "        {\n" +
            "          \"port\": \"8008\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"http\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"8009\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"ajp13\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"8443\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"https-alt\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"9000\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"cslistener\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"10001\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"scp-config\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        }\n" +
            "      ],\n" +
            "      \"mac\": \"20:DF:B9:AC:92:10\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"status\": \"up\",\n" +
            "      \"ipv4\": \"192.168.1.9\",\n" +
            "      \"deviceType\": null,\n" +
            "      \"osName\": null,\n" +
            "      \"osVendor\": null,\n" +
            "      \"osCpe\": null,\n" +
            "      \"services\": [],\n" +
            "      \"mac\": \"B4:6B:FC:4C:F5:81\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"status\": \"up\",\n" +
            "      \"ipv4\": \"192.168.1.14\",\n" +
            "      \"deviceType\": \"specialized\",\n" +
            "      \"osName\": \"AVtech Room Alert 26W environmental monitor\",\n" +
            "      \"osVendor\": \"AVtech\",\n" +
            "      \"osCpe\": null,\n" +
            "      \"services\": [\n" +
            "        {\n" +
            "          \"port\": \"8081\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"blackice-icecap\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        }\n" +
            "      ],\n" +
            "      \"mac\": \"3C:F8:62:EB:FD:7A\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"status\": \"up\",\n" +
            "      \"ipv4\": \"192.168.1.13\",\n" +
            "      \"deviceType\": null,\n" +
            "      \"osName\": \"Linux 3.8 - 4.14\",\n" +
            "      \"osVendor\": null,\n" +
            "      \"osCpe\": null,\n" +
            "      \"services\": [\n" +
            "        {\n" +
            "          \"port\": \"22\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"ssh\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"5901\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"vnc-1\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"port\": \"6001\",\n" +
            "          \"protocol\": \"tcp\",\n" +
            "          \"state\": \"open\",\n" +
            "          \"name\": \"X11:1\",\n" +
            "          \"cpe\": [],\n" +
            "          \"cves\": []\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothManagementService.ACTION_DEVICE_SCAN.equals(action)) {
                Bundle bundle = intent.getExtras();
                String jsonString = bundle.getString("payload");
                Log.i(TAG_INFO, jsonString);
                setRecyclerView(jsonString);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_device_discovery);
                progressBar.setVisibility(View.GONE);
                TextView textView = (TextView) findViewById(R.id.text_progress);
                textView.setVisibility(View.GONE);
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

        // FIXME: uncomment for mockup
//        setRecyclerView(mockJson);
//        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_device_discovery);
//        progressBar.setVisibility(View.GONE);

        // FIXME: uncomment for real code
        Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
        if (!mBound) {
            bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothManagementService.ACTION_DEVICE_SCAN);
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

    private void setRecyclerView(String jsonString) {
        mRecyclerView = findViewById(R.id.recyclerview_device_item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DeviceDiscoveryActivity.this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mDeviceModelList = loadDeviceModelList(jsonString);
        //        //Recycler Adapter
        mDeviceDiscoveryRecyclerAdapter = new DeviceDiscoveryRecyclerAdapter(mContext, mDeviceModelList);
        mDeviceDiscoveryRecyclerAdapter.setOnRecyclerViewItemClickListener(DeviceDiscoveryActivity.this);
        mRecyclerView.setAdapter(mDeviceDiscoveryRecyclerAdapter);
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
//        gsonBuilder.registerTypeAdapter(DeviceDiscoveryModel.class, new DeviceDiscoveryModel.DeviceDiscoveryModelDeserializer());
        mDeviceDiscoveryModel = gsonBuilder.create().fromJson(jsonString, DeviceDiscoveryModel.class);
        List<DeviceModel> deviceModels = mDeviceDiscoveryModel.getHosts();
        return deviceModels;
    }

    private boolean isRemoteDeviceConnected() {
        if (mBluetoothManagementService != null && mBound) {
            return mBluetoothManagementService.isRemoteDeviceConnected();
        }
        return false;
    }

}
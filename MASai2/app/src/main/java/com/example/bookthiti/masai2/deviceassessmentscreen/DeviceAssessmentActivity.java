package com.example.bookthiti.masai2.deviceassessmentscreen;

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
import android.widget.TextView;

import com.example.bookthiti.masai2.OnRecyclerViewItemClickListener;
import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.example.bookthiti.masai2.bluetoothservice.INotificationId;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.CVEModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.DeviceModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.ServiceModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import static com.example.bookthiti.masai2.LogConstants.TAG_INFO;

public class DeviceAssessmentActivity extends AppCompatActivity {
    private Context mContext;
    private BluetoothManagementService mBluetoothManagementService;
    private boolean mBound = false;
    private boolean isRemoteDeviceConnected = false;

    private RecyclerView mRecyclerView;
    private DeviceAssessmentRecyclerAdapter mDeviceAssessmentRecyclerAdapter;

    private ProgressBar mProgressBar;
    private TextView mTextViewProgress;

    private List<ServiceModel> mServiceModelList;
    private DeviceModel mDeviceModel;

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothManagementService.ACTION_DEVICE_ASSESS.equals(action)) {
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
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("command", "deviceAssess");
            Gson gson = new Gson();
            String payloadJsonString = gson.toJson(mDeviceModel, DeviceModel.class);
            Log.i(TAG_INFO, payloadJsonString);
            JsonParser jsonParser = new JsonParser();
            JsonElement payloadJsonElement = jsonParser.parse(payloadJsonString);
            jsonObject.add("payload", payloadJsonElement);
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
        setContentView(R.layout.activity_device_assessment);
        mContext = getApplicationContext();
        mDeviceModel = getIntent().getParcelableExtra("deviceModel");

        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mTextViewProgress = (TextView) findViewById(R.id.text_progress);

        // FIXME: Uncomment for mockup
//        setRecyclerView(loadJsonFromAsset());

        // FIXME: Uncomment for real app
        if (getIntent().getBooleanExtra(INotificationId.FLAG_IS_FROM_NOTIFICATION, false)) {
            setResultFromIntent(getIntent());
        } else {
            Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
            if (!mBound) {
                bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothManagementService.ACTION_DEVICE_ASSESS);
            LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);
        }

    }

    private void setRecyclerView(String jsonString) {
        mRecyclerView = findViewById(R.id.recycler_services);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mServiceModelList = loadServiceModelList(jsonString);
        mDeviceAssessmentRecyclerAdapter = new DeviceAssessmentRecyclerAdapter(mContext, mServiceModelList);
        mRecyclerView.setAdapter(mDeviceAssessmentRecyclerAdapter);
    }

    private List<ServiceModel> loadServiceModelList(String jsonString) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(CVEModel.Severity.class, new CVEModel.SeverityDeserializer());
        gsonBuilder.registerTypeAdapter(ServiceModel.class, new ServiceModel.ServiceModelDeserializer());
        gsonBuilder.registerTypeAdapter(DeviceModel.class, new DeviceModel.DeviceModelDeserializer());
        DeviceModel deviceModel = gsonBuilder.create().fromJson(jsonString, DeviceModel.class);
        List<ServiceModel> serviceModels = deviceModel.getServiceModels();
        return serviceModels;
    }

    private String loadJsonFromAsset() {
        return "{\n" +
                "    \"status\": \"up\",\n" +
                "    \"ipv4\": \"192.168.1.1\",\n" +
                "    \"deviceType\": \"general purpose\",\n" +
                "    \"osName\": \"Linux 2.6.23 - 2.6.38\",\n" +
                "    \"osVendor\": \"Linux\",\n" +
                "    \"osCpe\": [\"cpe:/o:linux:linux_kernel:2.6\"],\n" +
                "    \"services\": [\n" +
                "        {\n" +
                "            \"port\": \"22\",\n" +
                "            \"protocol\": \"tcp\",\n" +
                "            \"state\": \"open\",\n" +
                "            \"name\": \"ssh\",\n" +
                "            \"product\": \"Dropbear sshd\",\n" +
                "            \"version\": \"2017.75\",\n" +
                "            \"cpe\": [\n" +
                "                \"cpe:/a:matt_johnston:dropbear_ssh_server:2017.75\",\n" +
                "                \"cpe:/o:linux:linux_kernel\"\n" +
                "            ],\n" +
                "            \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "            \"port\": \"23\",\n" +
                "            \"protocol\": \"tcp\",\n" +
                "            \"state\": \"open\",\n" +
                "            \"name\": \"telnet\",\n" +
                "            \"product\": \"BusyBox telnetd\",\n" +
                "            \"version\": \"1.14.0 or later\",\n" +
                "            \"cpe\": [\n" +
                "                \"cpe:/a:busybox:busybox:1.14.0 or later\"\n" +
                "            ],\n" +
                "            \"cves\": [\n" +
                "                {\n" +
                "                    \"id\": \"CVE-2011-2716\",\n" +
                "                    \"description\": \"The DHCP client (udhcpc) in BusyBox before 1.20.0 allows remote DHCP servers to execute arbitrary commands via shell metacharacters in the (1) HOST_NAME, (2) DOMAIN_NAME, (3) NIS_DOMAIN, and (4) TFTP_SERVER_NAME host name options.\",\n" +
                "                    \"severity\": {\n" +
                "                        \"severity\": \"moderate\",\n" +
                "                        \"topVulnerable\": false,\n" +
                "                        \"topAlert\": false,\n" +
                "                        \"cvss2\": [\n" +
                "                            {\n" +
                "                                \"accessComplexity\": \"high\",\n" +
                "                                \"accessVector\": \"adjacent_network\",\n" +
                "                                \"authentication\": \"none\",\n" +
                "                                \"availability\": \"complete\",\n" +
                "                                \"base\": \"6.8\",\n" +
                "                                \"confidentiality\": \"complete\",\n" +
                "                                \"exploitability\": \"3.2\",\n" +
                "                                \"impact\": \"10.0\",\n" +
                "                                \"integrity\": \"complete\",\n" +
                "                                \"vector\": \"AV:A/AC:H/Au:N/C:C/I:C/A:C\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"CVE-2013-1813\",\n" +
                "                    \"description\": \"util-linux/mdev.c in BusyBox before 1.21.0 uses 0777 permissions for parent directories when creating nested directories under /dev/, which allows local users to have unknown impact and attack vectors.\",\n" +
                "                    \"severity\": {\n" +
                "                        \"severity\": \"high\",\n" +
                "                        \"topVulnerable\": false,\n" +
                "                        \"topAlert\": false,\n" +
                "                        \"cvss2\": [\n" +
                "                            {\n" +
                "                                \"accessComplexity\": \"low\",\n" +
                "                                \"accessVector\": \"local\",\n" +
                "                                \"authentication\": \"none\",\n" +
                "                                \"availability\": \"complete\",\n" +
                "                                \"base\": \"7.2\",\n" +
                "                                \"confidentiality\": \"complete\",\n" +
                "                                \"exploitability\": \"3.9\",\n" +
                "                                \"impact\": \"10.0\",\n" +
                "                                \"integrity\": \"complete\",\n" +
                "                                \"vector\": \"AV:L/AC:L/Au:N/C:C/I:C/A:C\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"port\": \"80\",\n" +
                "            \"protocol\": \"tcp\",\n" +
                "            \"state\": \"open\",\n" +
                "            \"name\": \"http\",\n" +
                "            \"cpe\": [],\n" +
                "            \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "            \"port\": \"1900\",\n" +
                "            \"protocol\": \"tcp\",\n" +
                "            \"state\": \"open\",\n" +
                "            \"name\": \"upnp\",\n" +
                "            \"product\": \"Portable SDK for UPnP devices\",\n" +
                "            \"version\": \"1.6.19\",\n" +
                "            \"cpe\": [\n" +
                "                \"cpe:/o:linux:linux_kernel:2.6.36\"\n" +
                "            ],\n" +
                "            \"cves\": []\n" +
                "        }\n" +
                "    ],\n" +
                "    \"mac\": \"0C:80:63:C2:BB:B6\"\n" +
                "}";
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
        Log.i(TAG_INFO, "Receive ACTION_DEVICE_ASSESS: " + jsonString);
        //TODO: load payload to the result
        mProgressBar.setVisibility(View.GONE);
        mTextViewProgress.setVisibility(View.GONE);
        setRecyclerView(jsonString);
    }
}

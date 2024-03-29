package com.example.bookthiti.masai2.portattackscreen;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.example.bookthiti.masai2.bluetoothservice.INotificationId;
import com.example.bookthiti.masai2.database.MasaiViewModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.CVEModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.DeviceModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.ServiceModel;
import com.example.bookthiti.masai2.mainscreen.MainActivity;
import com.example.bookthiti.masai2.utils.LogConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import java.util.Calendar;

import static com.example.bookthiti.masai2.utils.LogConstants.TAG_INFO;

public class PortAttackActivity extends AppCompatActivity {

    private Context mContext;

    private BluetoothManagementService mBluetoothManagementService;
    private boolean mBound = false;
    private boolean isRemoteDeviceConnected = false;
    private long startTime;

    private TextView mTextViewResult;
    private TextView mTextViewName;
    private TextView mTextViewUsername;
    private TextView mTextViewPassword;
    private ProgressBar mProgressBar;
    private TextView mTextViewProgress;
    private ConstraintLayout mLayoutContainerPortAttackResult;

    private TextView mTextViewDeviceIp;
    private TextView mTextViewDeviceMac;
    private TextView mTextViewDeviceType;
    private TextView mTextViewDeviceOpenedPort;
    private ImageView mImageViewDeviceType;

    private TextView mTextPasswordSuggestion;
    private ImageView mImageView;

    private LinearLayout popup;

    private DeviceModel mDeviceModel;
    private String mTargetService;

    private PortAttackResult mPortAttackResult;

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothManagementService.ACTION_PORT_ATTACK.equals(action)) {
                setResultFromIntent(intent);
                Log.i(TAG_INFO, String.format("Port attack is finished using %.3f secs", (double) (System.nanoTime() - startTime) / 1000000000));
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
            jsonObject.addProperty("command", "devicePortAttack");
            Gson gson = new Gson();
            String hostString = gson.toJson(mDeviceModel, DeviceModel.class);
            Log.i(TAG_INFO, hostString);
            JsonParser jsonParser = new JsonParser();
            JsonElement hostElement = jsonParser.parse(hostString);
            JsonObject payloadObject = new JsonObject();
            payloadObject.add("host", hostElement);
            payloadObject.addProperty("targetService", mTargetService);
            jsonObject.add("payload", payloadObject);

            MasaiViewModel masaiViewModel = MainActivity.getViewModel();
            SharedPreferences sharedPreferences = getSharedPreferences("MASAI_SHARED_PREF", MODE_PRIVATE);
            long id = masaiViewModel.insertActivityLogEntity("Service Attacking Testing", "running", null, sharedPreferences.getLong("testing_id", 0));

            jsonObject.addProperty("activityId", id);
            String jsonString = jsonObject.toString();
            mBluetoothManagementService.sendMessageToRemoteDevice(jsonString + "|");
            startTime = System.nanoTime();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("running_activity_id", id);
            editor.commit();
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
        setContentView(R.layout.activity_port_attack2);
        setTitle("Service Attack Testing");
        this.mContext = getApplicationContext();

        mDeviceModel = getIntent().getParcelableExtra("deviceModel");
        mTargetService = getIntent().getStringExtra("targetService");

        mTextViewResult = findViewById(R.id.text_result);
        mTextViewName = findViewById(R.id.text_service_name);
        mTextViewUsername = findViewById(R.id.text_username);
        mTextViewPassword = findViewById(R.id.text_password);
        mProgressBar = findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);
        mTextViewProgress = findViewById(R.id.text_progress);
        mTextPasswordSuggestion = findViewById(R.id.textView_password_suggestion);
        mImageView = findViewById(R.id.imageView);
        mLayoutContainerPortAttackResult = findViewById(R.id.layout_container_port_attack_result);

        mTextPasswordSuggestion.setVisibility(View.INVISIBLE);
        mImageView.setVisibility(View.INVISIBLE);

        mTextViewDeviceIp = findViewById(R.id.text_data_ip);
        mTextViewDeviceMac = findViewById(R.id.text_data_mac);
        mTextViewDeviceType = findViewById(R.id.text_data_device_type);
        mTextViewDeviceOpenedPort = findViewById(R.id.text_data_port_opened);
        mImageViewDeviceType = findViewById(R.id.ic_bt_device_class);

        mTextPasswordSuggestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(v.getContext());
                builder.setContentView(R.layout.pop_up_password_suggestion);
                builder.setGravity(Gravity.CENTER);
                builder.setScaleRatio(0.2f);
                builder.setBlurRadius(5);
                builder.setTintColor(0x3000000);
                BlurPopupWindow blurPopupWindow = builder.build();
                blurPopupWindow.show();

                popup = (LinearLayout) blurPopupWindow.findViewById(R.id.relativeLayout_popup_pass_suggest);

                WebView webView = (WebView) blurPopupWindow.findViewById(R.id.webview_test);
                String str = "<p style=\"text-align: justify;\"><span style=\"color: #333399;\"><strong>- Has 12 Characters, Minimum</strong>:&nbsp;</span><span style=\"color: #339966;\">You need to choose a password that&rsquo;s long enough. There&rsquo;s no minimum password length everyone agrees on, but you should generally go for passwords that are a minimum of 12 to 14 characters in length. A longer password would be even better.</span></p> <p style=\"text-align: justify;\"><span style=\"color: #333399;\"><strong>- Includes Numbers, Symbols, Capital Letters, and Lower-Case Letters</strong>:&nbsp;</span><span style=\"color: #339966;\">Use a mix of different types of characters to make the password harder to crack.</span></p> <p style=\"text-align: justify;\"><span style=\"color: #333399;\"><strong>- Isn&rsquo;t a Dictionary Word or Combination of Dictionary Words:&nbsp;</strong></span><span style=\"color: #339966;\">Stay away from obvious dictionary words and combinations of dictionary words. Any word on its own is bad. Any combination of a few words, especially if they&rsquo;re obvious, is also bad.</span></p> <p style=\"text-align: justify;\"><span style=\"color: #333399;\"><strong>- Doesn&rsquo;t Rely on Obvious Substitutions</strong>:&nbsp;</span><span style=\"color: #339966;\">Don&rsquo;t use common substitutions, either &mdash; for example, &ldquo;H0use&rdquo; isn&rsquo;t strong just because you&rsquo;ve replaced an o with a 0. That&rsquo;s just obvious.</span></p>";
                webView.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);

                popup.setBackgroundResource(R.drawable.shape_score);
                popup.setBackgroundColor(Color.parseColor("#9FC5E8"));


            }
        });

        // FIXME: Uncomment for mockup
        if (LogConstants.IS_MOCK) {
            setViewFromResult(mockupJson2);
            MasaiViewModel masaiViewModel = MainActivity.getViewModel();
            SharedPreferences sharedPreferences = getSharedPreferences("MASAI_SHARED_PREF", MODE_PRIVATE);
            masaiViewModel.insertActivityLogEntity("Service Attacking Testing", "finish", mockupJson, sharedPreferences.getLong("testing_id", 0), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        }

        // FIXME: Uncomment for real application
        if (!LogConstants.IS_MOCK) {
            if (getIntent().getBooleanExtra(INotificationId.FLAG_IS_FROM_NOTIFICATION, false)) {
                setResultFromIntent(getIntent());
            } else {
                Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
                if (!mBound) {
                    bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
                }
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(BluetoothManagementService.ACTION_PORT_ATTACK);
                LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);
            }
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

    private boolean isRemoteDeviceConnected() {
        if (mBluetoothManagementService != null && mBound) {
            return mBluetoothManagementService.isRemoteDeviceConnected();
        }
        return false;
    }

    private PortAttackResult loadPortAttackResult(String jsonString) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(CVEModel.Severity.class, new CVEModel.SeverityDeserializer());
        gsonBuilder.registerTypeAdapter(ServiceModel.class, new ServiceModel.ServiceModelDeserializer());
        gsonBuilder.registerTypeAdapter(PortAttackResult.class, new PortAttackResult.PortAttackResultDeserializer());
        gsonBuilder.registerTypeAdapter(DeviceModel.class, new DeviceModel.DeviceModelDeserializer());
        PortAttackResult portAttackResult = gsonBuilder.create().fromJson(jsonString, PortAttackResult.class);
        return portAttackResult;
    }

    private void setResultFromIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String jsonString = bundle.getString("payload");
        Log.i(TAG_INFO, "Receive ACTION_PORT_ATTACK: " + jsonString);
        setViewFromResult(jsonString);
    }

    private void setViewFromResult(String jsonString) {
        mPortAttackResult = loadPortAttackResult(jsonString);
        if (mPortAttackResult != null) {
            mDeviceModel = mPortAttackResult.getDeviceModel();
            mTextViewResult.setText(mPortAttackResult.getResult().toUpperCase());
            if (mPortAttackResult.getResult().equals("success")) {
                mTextViewResult.setTextColor(Color.RED);
            } else {
                mTextViewResult.setTextColor(Color.GREEN);
            }
            mTextViewName.setText(mPortAttackResult.getServiceModel().getName());
            if (mPortAttackResult.getUsername() != null) {
                mTextViewUsername.setText(mPortAttackResult.getUsername());
            } else {
                mTextViewUsername.setText("-");
            }
            if (mPortAttackResult.getPassword() != null) {
                mTextViewPassword.setText(mPortAttackResult.getPassword());
            } else {
                mTextViewPassword.setText("-");
            }
        }
        mLayoutContainerPortAttackResult.setVisibility(View.VISIBLE);
        mTextPasswordSuggestion.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mTextViewProgress.setVisibility(View.GONE);

        if (mDeviceModel != null) {
            mTextViewDeviceIp.setText(mDeviceModel.getIpAddress());
            if(mDeviceModel.getMacAddress() == null || mDeviceModel.getMacAddress().equals("-")) mTextViewDeviceMac.setText("-");
            else mTextViewDeviceMac.setText(mDeviceModel.getMacAddress());

            if(mDeviceModel.getDeviceType() == null || mDeviceModel.getDeviceType().equals("-")) mTextViewDeviceType.setText("-");
            else mTextViewDeviceType.setText(mDeviceModel.getDeviceType());

            mTextViewDeviceOpenedPort.setText("" + mDeviceModel.getServiceModels().size());
            mImageViewDeviceType.setImageResource(mDeviceModel.getIconId());
        }
    }

    private String mockupJson = "{\n" +
            "    \"resultType\": \"portAttack\",\n" +
            "    \"payload\": {\n" +
            "        \"host\": {\n" +
            "            \"status\": \"up\",\n" +
            "            \"ipv4\": \"192.168.1.102\",\n" +
            "            \"deviceType\": null,\n" +
            "            \"osName\": \"Linux 3.8 - 4.14\",\n" +
            "            \"osVendor\": null,\n" +
            "            \"osCpe\": [],\n" +
            "            \"services\": [\n" +
            "                {\n" +
            "                    \"port\": \"22\",\n" +
            "                    \"protocol\": \"tcp\",\n" +
            "                    \"state\": \"open\",\n" +
            "                    \"name\": \"ssh\",\n" +
            "                    \"cpe\": [],\n" +
            "                    \"cves\": []\n" +
            "                },\n" +
            "                {\n" +
            "                    \"port\": \"5901\",\n" +
            "                    \"protocol\": \"tcp\",\n" +
            "                    \"state\": \"open\",\n" +
            "                    \"name\": \"vnc-1\",\n" +
            "                    \"cpe\": [],\n" +
            "                    \"cves\": []\n" +
            "                },\n" +
            "                {\n" +
            "                    \"port\": \"6001\",\n" +
            "                    \"protocol\": \"tcp\",\n" +
            "                    \"state\": \"open\",\n" +
            "                    \"name\": \"X11:1\",\n" +
            "                    \"cpe\": [],\n" +
            "                    \"cves\": []\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        \"service\": {\n" +
            "            \"port\": \"22\",\n" +
            "            \"protocol\": \"tcp\",\n" +
            "            \"state\": \"open\",\n" +
            "            \"name\": \"ssh\",\n" +
            "            \"cpe\": [],\n" +
            "            \"cves\": []\n" +
            "        },\n" +
            "        \"attackResult\": \"success\",\n" +
            "        \"username\": \"root\",\n" +
            "        \"password\": \"toor\"\n" +
            "    }\n" +
            "}";

    private String mockupJson2 = "{\n" +
            "        \"host\": {\n" +
            "            \"status\": \"up\",\n" +
            "            \"ipv4\": \"192.168.1.102\",\n" +
            "            \"deviceType\": null,\n" +
            "            \"osName\": \"Linux 3.8 - 4.14\",\n" +
            "            \"osVendor\": null,\n" +
            "            \"osCpe\": [],\n" +
            "            \"services\": [\n" +
            "                {\n" +
            "                    \"port\": \"22\",\n" +
            "                    \"protocol\": \"tcp\",\n" +
            "                    \"state\": \"open\",\n" +
            "                    \"name\": \"ssh\",\n" +
            "                    \"cpe\": [],\n" +
            "                    \"cves\": []\n" +
            "                },\n" +
            "                {\n" +
            "                    \"port\": \"5901\",\n" +
            "                    \"protocol\": \"tcp\",\n" +
            "                    \"state\": \"open\",\n" +
            "                    \"name\": \"vnc-1\",\n" +
            "                    \"cpe\": [],\n" +
            "                    \"cves\": []\n" +
            "                },\n" +
            "                {\n" +
            "                    \"port\": \"6001\",\n" +
            "                    \"protocol\": \"tcp\",\n" +
            "                    \"state\": \"open\",\n" +
            "                    \"name\": \"X11:1\",\n" +
            "                    \"cpe\": [],\n" +
            "                    \"cves\": []\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        \"service\": {\n" +
            "            \"port\": \"22\",\n" +
            "            \"protocol\": \"tcp\",\n" +
            "            \"state\": \"open\",\n" +
            "            \"name\": \"ssh\",\n" +
            "            \"cpe\": [],\n" +
            "            \"cves\": []\n" +
            "        },\n" +
            "        \"attackResult\": \"success\",\n" +
            "        \"username\": \"root\",\n" +
            "        \"password\": \"toor\"\n" +
            "    }";
}
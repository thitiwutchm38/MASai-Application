package com.example.bookthiti.masai2.bluetoothattackscreen;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.example.bookthiti.masai2.bluetoothservice.INotificationId;
import com.example.bookthiti.masai2.database.MasaiViewModel;
import com.example.bookthiti.masai2.mainscreen.MainActivity;
import com.example.bookthiti.masai2.utils.LogConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Calendar;

import static com.example.bookthiti.masai2.utils.LogConstants.TAG_INFO;

public class BluetoothAttackActivity extends AppCompatActivity {

    private Context context;

    private BluetoothManagementService mBluetoothManagementService;

    private TextView mTextViewDeviceName;
    private TextView mTextViewDeviceMac;
    private TextView mTextViewDeviceType;
    private TextView mTextViewDeviceClass;
    private TextView mTextViewResult;
    private TextView mTextViewResultDescription;
    private ImageView mImageViewDeviceClass;
    private ConstraintLayout mLayoutContainerResult;
    private ProgressBar mProgressBar;
    private TextView mTextViewProgress;

    private BluetoothDeviceModel mBluetoothDeviceModel;
    private BluetoothAttackResult mBluetoothAttackResult;

    private boolean mBound = false;
    private long startTime;
    private boolean isRemoteDeviceConnected;

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothManagementService.ACTION_BLUETOOTH_ATTACK.equals(action)) {
                setResultFromIntent(intent);
                Log.i(TAG_INFO, String.format("Device assessment is finished using %.3f secs", (double) (System.nanoTime() - startTime) / 1000000000));
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
            jsonObject.addProperty("command", "bluetoothAttack");
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String targetString = gson.toJson(mBluetoothDeviceModel, BluetoothDeviceModel.class);
            Log.i(TAG_INFO, targetString);
            JsonParser jsonParser = new JsonParser();
            JsonElement targetElement = jsonParser.parse(targetString);
            JsonObject payloadObject = new JsonObject();
            payloadObject.add("target", targetElement);
            jsonObject.add("payload", payloadObject);

            MasaiViewModel masaiViewModel = MainActivity.getViewModel();
            SharedPreferences sharedPreferences = getSharedPreferences("MASAI_SHARED_PREF", MODE_PRIVATE);
            long id = masaiViewModel.insertActivityLogEntity("Bluetooth Attacking", "running", null, sharedPreferences.getLong("testing_id", 0));

            jsonObject.addProperty("activityId", id);
            String jsonString = jsonObject.toString();
            mBluetoothManagementService.sendMessageToRemoteDevice(jsonString + "|");
            startTime = System.nanoTime();

            // TODO: Remove running_activity_id in sharedPreferences
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
        setContentView(R.layout.activity_bluetooth_attack2);
        setTitle("Bluetooth Attack Result");

        context = getApplicationContext();

        mBluetoothDeviceModel = getIntent().getParcelableExtra("target");

        mTextViewDeviceName = findViewById(R.id.text_bt_device_name);
        mTextViewDeviceMac = findViewById(R.id.text_bt_device_mac);
        mTextViewDeviceClass = findViewById(R.id.text_bt_device_class);
        mTextViewDeviceType = findViewById(R.id.text_bt_device_type);
        mTextViewResult = findViewById(R.id.text_result2);
        mTextViewResultDescription = findViewById(R.id.text_bt_result_description);
        mImageViewDeviceClass = findViewById(R.id.ic_bt_device_class);
        mLayoutContainerResult = findViewById(R.id.layout_container_result);
        mProgressBar = findViewById(R.id.progressBar2);
        mTextViewProgress = findViewById(R.id.text_progress2);

        // FIXME: Uncomment for mockup
        if (LogConstants.IS_MOCK) {
            setViewFromResult(mockupJson2);
            MasaiViewModel masaiViewModel = MainActivity.getViewModel();
            SharedPreferences sharedPreferences = getSharedPreferences("MASAI_SHARED_PREF", MODE_PRIVATE);
            masaiViewModel.insertActivityLogEntity("Bluetooth Attacking", "finish", mockupJson, sharedPreferences.getLong("testing_id", 0), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        } else {

            // FIXME: Uncomment for real application
            if (getIntent().getBooleanExtra(INotificationId.FLAG_IS_FROM_NOTIFICATION, false)) {
                setResultFromIntent(getIntent());
            } else {
                Intent bindServiceIntent = new Intent(this, BluetoothManagementService.class);
                if (!mBound) {
                    bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
                }
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(BluetoothManagementService.ACTION_BLUETOOTH_ATTACK);
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

    private BluetoothAttackResult loadBluetoothAttackResult(String jsonString) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(BluetoothDeviceModel.class, new BluetoothDeviceModel.BluetoothDeviceModelDeserializer());
        gsonBuilder.registerTypeAdapter(BluetoothAttackResult.class, new BluetoothAttackResult.BluetoothAttackResultDeserializer());
        BluetoothAttackResult bluetoothAttackResult = gsonBuilder.create().fromJson(jsonString, BluetoothAttackResult.class);
        return bluetoothAttackResult;
    }

    private void setResultFromIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String jsonString = bundle.getString("payload");
        Log.i(TAG_INFO, "Receive ACTION_BLUETOOTH_ATTACK: " + jsonString);
        setViewFromResult(jsonString);
    }

    private void setViewFromResult(String jsonString) {
        mProgressBar.setVisibility(View.GONE);
        mTextViewProgress.setVisibility(View.GONE);
        mLayoutContainerResult.setVisibility(View.VISIBLE);
        mBluetoothAttackResult = loadBluetoothAttackResult(jsonString);
        if (mBluetoothAttackResult != null) {
            mBluetoothDeviceModel = mBluetoothAttackResult.getBluetoothDeviceModel();
            String status = mBluetoothAttackResult.getStatus();
            if (status.equals("success")) {
                mTextViewResult.setTextColor(Color.RED);
                mTextViewResultDescription.setText(R.string.bt_attack_success_description);
            } else if (status.equals("failure")) {
                mTextViewResult.setTextColor(Color.BLUE);
                mTextViewResultDescription.setText(R.string.bt_attack_failure_description);
            }
            mTextViewResult.setText(status.toUpperCase());
        }

        if (mBluetoothDeviceModel != null) {
            if (mBluetoothDeviceModel.getName() == null)
                mTextViewDeviceName.setText("N/A");
            else
                mTextViewDeviceName.setText(mBluetoothDeviceModel.getName());

            mTextViewDeviceMac.setText(mBluetoothDeviceModel.getMac());
            mTextViewDeviceType.setText(mBluetoothDeviceModel.getDeviceType());
            mTextViewDeviceClass.setText(mBluetoothDeviceModel.getDeviceClass());
            mImageViewDeviceClass.setImageResource(BluetoothDeviceModel.getDeviceIconResource(mBluetoothDeviceModel.getDeviceClass()));
        }

    }

    private boolean isRemoteDeviceConnected() {
        if (mBluetoothManagementService != null && mBound) {
            return mBluetoothManagementService.isRemoteDeviceConnected();
        }
        return false;
    }

    private String mockupJson = "{\n" +
            "    \"resultType\": \"bluetoothAttack\",\n" +
            "    \"payload\": {\n" +
            "        \"bluetoothDevice\": {\n" +
            "            \"mac\": \"f0:79:59:2d:ed:b1\",\n" +
            "            \"name\": \"ASUS_Z002\",\n" +
            "            \"class\": \"PHONE\",\n" +
            "            \"type\": \"Classic\"\n" +
            "        },\n" +
            "        \"status\": \"success\"\n" +
            "    }\n" +
            "}";

    private String mockupJson2 = "{\n" +
            "        \"bluetoothDevice\": {\n" +
            "            \"mac\": \"f0:79:59:2d:ed:b1\",\n" +
            "            \"name\": \"ASUS_Z002\",\n" +
            "            \"class\": \"PHONE\",\n" +
            "            \"type\": \"Classic\"\n" +
            "        },\n" +
            "        \"status\": \"success\"\n" +
            "    }";
}

package com.example.bookthiti.masai2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.example.bookthiti.masai2.iotinformationscreen.IotInformationActivity;
import com.example.bookthiti.masai2.iotpentestmainscreen.IoTMainPentestActivity;
import com.example.bookthiti.masai2.mobileapplicationscanningscreen.MobileApplicationScannerActivity;
import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.example.bookthiti.masai2.bluetoothservice.ServiceTools;
import com.example.bookthiti.masai2.mobileapplicationscanningscreen.MobileApplicationScanningResultActivity;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "";
    private static int SPLASH_TIME = 4000; //This is 4 seconds

    private ImageButton button_MobileApp_att;

    private ImageButton button_iot_att;

    private ImageButton button_Tips;

    private ImageButton button__MASaibox_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        if(!ServiceTools.isServiceRunning(BluetoothManagementService.class, getApplicationContext())) {
            startService(new Intent(MainActivity.this, BluetoothManagementService.class));
        } else {
            Log.i("Log info", "Service is already started");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        

        button_MobileApp_att = (ImageButton)findViewById(R.id.imageButton_MobileApp_att);
        button_iot_att = (ImageButton)findViewById(R.id.imageButton_iot_att);
        button_Tips = (ImageButton)findViewById(R.id.imageButton_Tips);
        button__MASaibox_setting = (ImageButton)findViewById(R.id.imageButton_MASaibox);


        button_MobileApp_att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openActivity_MobileApp_att();
            }
        });

        button_iot_att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openActivity_iot_att();
            }
        });

        button__MASaibox_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openActivity_MASaibox_setting();
            }
        });

        button_Tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openActivity_Tips();
            }
        });

    }

    public void openActivity_MobileApp_att() {

        Intent intent = new Intent(this,MobileApplicationScannerActivity.class);
//        Intent intent = new Intent(this, MobileApplicationScanningResultActivity.class);
        startActivity(intent);
    }
    public void openActivity_iot_att() {

        Intent intent = new Intent(this,IoTMainPentestActivity.class);
        startActivity(intent);
    }
    public void openActivity_MASaibox_setting() {

        Intent intent = new Intent(this,MasaiSettingActivity.class);
        startActivity(intent);
    }

    public void openActivity_Tips() {

        Intent intent = new Intent(this, IotInformationActivity.class);
        startActivity(intent);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NotificationChannel.DEFAULT_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

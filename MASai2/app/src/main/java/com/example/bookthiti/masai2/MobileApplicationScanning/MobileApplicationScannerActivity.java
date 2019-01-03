package com.example.bookthiti.masai2.MobileApplicationScanning;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bookthiti.masai2.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileApplicationScannerActivity extends AppCompatActivity {
    private final static String TAG_INFO = "Log info";
    private final static String TAG_DEBUG = "Log debug";
    private final static String TAG_ERROR = "Log error";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile__appscan);
        mContext = this.getApplicationContext();
        setTitle("Mobile App Scan");
//        try {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.tomtop.home")));
//        } catch (android.content.ActivityNotFoundException anfe) {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.tomtop.home")));
//        }
//        MasaiServerAPI masaiServerAPI = RetrofitClientInstance.getRetrofitInstance().create(MasaiServerAPI.class);
//        Call<List<TargetApplicationInfo>> listCall = masaiServerAPI.getAllTargetApplicationInfo("koogeek");
//        listCall.enqueue(new Callback<List<TargetApplicationInfo>>() {
//            @Override
//            public void onResponse(Call<List<TargetApplicationInfo>> call, Response<List<TargetApplicationInfo>> response) {
//                List<TargetApplicationInfo> targetApplicationInfos = response.body();
//                Log.i(TAG_INFO, "Get response");
//                for(TargetApplicationInfo targetApplicationInfo:targetApplicationInfos) {
//                    Log.i(TAG_INFO, "appId: " + targetApplicationInfo.getAppId() + ", appName: " + targetApplicationInfo.getAppName());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<TargetApplicationInfo>> call, Throwable t) {
//                Toast.makeText(mContext, "Get failure response", Toast.LENGTH_SHORT).show();
//                Log.i(TAG_INFO, t.getMessage());
//            }
//        });
    }
}

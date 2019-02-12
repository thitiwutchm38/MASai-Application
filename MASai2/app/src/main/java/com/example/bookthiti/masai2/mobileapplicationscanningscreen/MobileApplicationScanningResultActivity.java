package com.example.bookthiti.masai2.mobileapplicationscanningscreen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;

public class MobileApplicationScanningResultActivity extends AppCompatActivity implements MobileApplicationScanningResultFragment.OnFragmentInteractionListener{

    private Context mContext;
    private TargetApplicationInfo targetApplicationInfo;

    private ImageView mImageViewAppIcon;
    private TextView mTextViewAppName;
    private TextView mTextViewAppAuthor;
    private TextView mTextViewAppVersion;
    private TextView mTextViewAppId;
    private TextView mTextViewAppCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_app_scan_result);
        mContext = getApplicationContext();

        targetApplicationInfo = (TargetApplicationInfo) getIntent().getParcelableExtra("selectedApplication");

        mImageViewAppIcon = findViewById(R.id.image_app_icon);
        mTextViewAppName = findViewById(R.id.text_app_name);
        mTextViewAppAuthor = findViewById(R.id.text_app_author);
        mTextViewAppVersion = findViewById(R.id.text_app_version);
        mTextViewAppId = findViewById(R.id.text_app_id);
        mTextViewAppCategory = findViewById(R.id.text_app_category);

        new DownloadImageTask(mImageViewAppIcon, targetApplicationInfo.getAppIcon().getBitmap()).execute(targetApplicationInfo.getAppIcon().getUrl());
        mTextViewAppName.setText(targetApplicationInfo.getAppName());
        mTextViewAppAuthor.setText(targetApplicationInfo.getAppAuthor());
        mTextViewAppVersion.setText(targetApplicationInfo.getAppVersionString());
        mTextViewAppId.setText(targetApplicationInfo.getAppId());
        mTextViewAppCategory.setText(targetApplicationInfo.getAppCategory());

        FragmentManager fragmentManager = getSupportFragmentManager();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

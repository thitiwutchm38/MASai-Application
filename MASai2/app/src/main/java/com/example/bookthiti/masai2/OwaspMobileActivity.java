package com.example.bookthiti.masai2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OwaspMobileActivity extends AppCompatActivity {
    private Context mContext;

    private TextView mTextViewTopic;
    private TextView mTextViewContent;

    private String detailM = null;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owasp_mobile2);
        setTitle("OWASP Mobile Top 10");
        mContext = getApplicationContext();
        Intent intent = getIntent();
        String tempM = intent.getStringExtra("M");
        mTextViewContent = findViewById(R.id.text_content);
        mTextViewTopic = findViewById(R.id.text_owasp_mobile_topic);

        switch(tempM) {
            case "M1" :
                mTextViewContent.setText(R.string.M1);
                mTextViewTopic.setText("M1 Improper Platform Usage");
                break;
            case "M2" :
                mTextViewContent.setText(R.string.M2);
                mTextViewTopic.setText("M2 Insecure Data Storage");
                break;
            case "M3" :
                mTextViewContent.setText(R.string.M3);
                mTextViewTopic.setText("M3 Insecure Communication");
                break;
            case "M4" :
                mTextViewContent.setText(R.string.M4);
                mTextViewTopic.setText("M4 Insecure Authentication");
                break;
            case "M5" :
                mTextViewContent.setText(R.string.M5);
                mTextViewTopic.setText("M5 Insufficient Cryptography");
                break;
            case "M6" :
                mTextViewContent.setText(R.string.M6);
                mTextViewTopic.setText("M6 Insecure Authorization");
                break;
            case "M7" :
                mTextViewContent.setText(R.string.M7);
                mTextViewTopic.setText("M7 Client Code Quality");
                break;
            case "M8" :
                mTextViewContent.setText(R.string.M8);
                mTextViewTopic.setText("M8 Code Tampering");
                break;
            case "M9" :
                mTextViewContent.setText(R.string.M9);
                mTextViewTopic.setText("M9 Reverse Engineering");
                break;
            case "M10" :
                mTextViewContent.setText(R.string.M10);
                mTextViewTopic.setText("M10 Extraneous Functionality");
                break;

            default :
        }
    }
}

package com.example.bookthiti.masai2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bookthiti.masai2.iotinformationscreen.OwaspModel;

public class OwaspMobileActivity extends AppCompatActivity {
    private Context mContext;

    private TextView mTextViewTopic;
    private TextView mTextViewContent;

    private OwaspModel mOwaspModel;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owasp_mobile2);
        setTitle("OWASP Mobile Top 10");
        mContext = getApplicationContext();
        Intent intent = getIntent();
        mOwaspModel = (OwaspModel) intent.getParcelableExtra("owaspModel");
        mTextViewContent = findViewById(R.id.text_content);
        mTextViewTopic = findViewById(R.id.text_owasp_mobile_topic);

        //mTextViewTopic.setText(mOwaspModel.getTopicId() + " " + mOwaspModel.getTopic());
        //mTextViewContent.setText(mOwaspModel.getGeneralDetail());
    }
}

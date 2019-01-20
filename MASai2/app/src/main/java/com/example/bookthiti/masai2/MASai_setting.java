package com.example.bookthiti.masai2;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MASai_setting extends AppCompatActivity {

    //Content variable

    private Boolean connectStatus = true;
    TextView mTextviewTopic;
    TextView mTextviewDescription;
    ImageView mPictureContent;
    Button mClickprocess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_masai_setting);


        mTextviewTopic = (TextView)findViewById(R.id.textview_box_instruct);
        mTextviewDescription =  (TextView)findViewById(R.id.textView_connect_setting_instruct);
        mPictureContent = (ImageView)findViewById(R.id.imageView_box_instruct);
        mClickprocess = (Button) findViewById(R.id.button_click_process);




        if(connectStatus){

            mTextviewTopic.setText("Instruction:");
            mTextviewDescription.setText("Click connect button and Scan QR Code on MASai Box to connect the box");
            mClickprocess.setText("Connect");

        }else{
            mTextviewTopic.setText("Connected:");
            mTextviewDescription.setText("Click disconnect button to disconnect MASai box and mobile application ");
            mPictureContent.setVisibility(View.INVISIBLE);
            mClickprocess.setText("Disconnect");


        }

    }


}


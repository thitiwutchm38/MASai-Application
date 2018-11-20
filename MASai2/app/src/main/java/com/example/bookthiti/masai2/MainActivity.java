package com.example.bookthiti.masai2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton button_MobileApp_att;
    //    imageButton_MobileApp_att

    private ImageButton button_iot_att;
    //    imageButton_iot_att

    private ImageButton button_Tips;
    //imageButton_Tips

    private ImageButton button__MASaibox_setting;

    //imageButton_MASaibox

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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






    }

    public void openActivity_MobileApp_att() {

        Intent intent = new Intent(this,Mobile_Appscan.class);
        startActivity(intent);
    }
    public void openActivity_iot_att() {

        Intent intent = new Intent(this,iot_main_pentest.class);
        startActivity(intent);
    }
    public void openActivity_MASaibox_setting() {

        Intent intent = new Intent(this,MASai_setting.class);
        startActivity(intent);
    }
}

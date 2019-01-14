package com.example.bookthiti.masai2;

import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class ScanResultActivity extends AppCompatActivity {

    LinearLayout line_layout;
    NotificationCompat.Builder mBuilder;

    //Show app detail
    TextView app_data_name;
    TextView app_data_id;
    TextView app_data_version;
    TextView app_data_type;

    String app_name;
    String app_id;
    String app_version;
    String app_type;


    //Show amount score
    TextView textView_warning_result;
    TextView textView_low_risk_result;
    TextView textView_medium_result;
    TextView textView_high_risk_result;

    RelativeLayout score_warning;

    LinearLayout popup;

    int warning_result;
    int low_risk_result;
    int medium_result;
    int high_risk_result;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);


        //Show app detail

        app_data_name = (TextView) findViewById(R.id.textView_data_ip_address);
        app_data_id = (TextView) findViewById(R.id.app_data_id);
        app_data_version = (TextView) findViewById(R.id.app_data_version);
        app_data_type = (TextView) findViewById(R.id.app_data_type);


        //Show amount score
        textView_warning_result = (TextView) findViewById(R.id.textView_warning_result);
        textView_low_risk_result = (TextView) findViewById(R.id.textView_low_risk_result);
        textView_medium_result = (TextView) findViewById(R.id.textView_medium_result);
        textView_high_risk_result = (TextView) findViewById(R.id.textView_high_risk_result);


        popup = (LinearLayout) findViewById(R.id.relativeLayout_popup);
        score_warning = (RelativeLayout) findViewById(R.id.score_warning);

        line_layout = (LinearLayout) findViewById(R.id.linearLayout_scan_result);

        line_layout.setVisibility(View.GONE);

        prepareList();

        //App detail
        app_data_name.setText(app_name);
        app_data_id.setText(app_id);
        app_data_version.setText(app_version);;
        app_data_type.setText(app_type);;

        //Amount score
        textView_warning_result.setText(Integer.toString(warning_result));
        textView_low_risk_result.setText(Integer.toString(low_risk_result));
        textView_medium_result.setText(Integer.toString(medium_result));
        textView_high_risk_result.setText(Integer.toString(high_risk_result));


//    int warning_result;
//    int low_risk_result;
//    int medium_result;
//    int high_risk_result;

        score_warning.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {



                new BlurPopupWindow.Builder(v.getContext())
                        .setContentView(R.layout.pop_up_result)
                        .bindClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "Click Button", Toast.LENGTH_SHORT).show();
                            }
                        }, R.id.relativeLayout_popup)
                        .setGravity(Gravity.CENTER)
                        .setScaleRatio(0.2f)
                        .setBlurRadius(10)
                        .setTintColor(0x30000000)
                        .build()
                        .show();


            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



                final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar_app_scan);
                progressBar.setVisibility(View.GONE);

                line_layout.setVisibility(View.VISIBLE);



            }
        }, 5000); // Millisecond 1000 = 1 sec
    }



    private void prepareList() {


        //Convert JSON File
        String json = null;
        Integer count = null;

        try {
            InputStream is = getAssets().open("appresult.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        JSONObject reader = null;
        try {
            reader = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JSONObject app_detail_temp = null;

        try {
            app_detail_temp = reader.getJSONObject("app_detail");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            app_name  = app_detail_temp.getString("App Name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            app_id  = app_detail_temp.getString("App ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

         try {
             app_version  = app_detail_temp.getString("App Version");
         } catch (JSONException e) {
             e.printStackTrace();
         }

        try {
             app_type  = app_detail_temp.getString("App Type");
        } catch (JSONException e) {
            e.printStackTrace();
        }


            JSONObject app_poten_security_temp = null;

            JSONObject app_warning_temp = null;

            JSONObject app_low_temp = null;

            JSONObject app_medium_temp = null;

            JSONObject app_high_temp = null;




        try {
            app_poten_security_temp = reader.getJSONObject("Poten_security");
         } catch (JSONException e) {
                e.printStackTrace();
         }

        try {
            app_warning_temp = app_poten_security_temp.getJSONObject("warning");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            app_low_temp = app_poten_security_temp.getJSONObject("low");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            app_medium_temp = app_poten_security_temp.getJSONObject("medium");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            app_high_temp = app_poten_security_temp.getJSONObject("high");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            warning_result  = app_warning_temp.getInt("amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            low_risk_result  = app_low_temp.getInt("amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            medium_result  = app_medium_temp.getInt("amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            high_risk_result  = app_high_temp.getInt("amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }




}

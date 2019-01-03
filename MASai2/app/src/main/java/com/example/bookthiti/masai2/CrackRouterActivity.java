package com.example.bookthiti.masai2;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class CrackRouterActivity extends AppCompatActivity{


    TextView TextView_mac;
    TextView TextView_sig ;

    TextView TextView_company;
    TextView TextView_sec;
    TextView TextView_fre;

    TextView TextView_cha;
    TextView TextView_ssid;
    ProgressBar progressBar;
    Button button;


    RelativeLayout pass_wifi_relative;

    EditText pass_result;
    TextView myprogress_crack;
    ImageButton copy_pass;

    MainModel information_data;

    Boolean pass_status = true;


    private ClipboardManager myClipboard;
    private ClipData myClip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crack_router);

        TextView_mac = (TextView)findViewById(R.id.mac_value);
        TextView_sig = (TextView)findViewById(R.id.sig_value);
        TextView_company = (TextView)findViewById(R.id.comp_value);
        TextView_sec = (TextView)findViewById(R.id.sec_value);
        TextView_fre = (TextView)findViewById(R.id.fre_value);
        TextView_cha = (TextView)findViewById(R.id.cha_value);
        TextView_ssid = (TextView)findViewById(R.id.textView_ssid);

        myprogress_crack= (TextView)findViewById(R.id.textView_progress_crack);

        progressBar = (ProgressBar)findViewById(R.id.progressBar_crack);

        copy_pass =  (ImageButton)findViewById(R.id.imageButton_copy_pass);

        pass_result = (EditText)findViewById(R.id.editText_pass_wifi);

        pass_wifi_relative =   (RelativeLayout) findViewById(R.id.pass_wifi_relative);


        //Status
        progressBar.setVisibility(View.INVISIBLE);
        myprogress_crack.setVisibility(View.INVISIBLE);
        pass_wifi_relative.setVisibility(View.INVISIBLE);

        information_data = (MainModel) getIntent().getParcelableExtra("router_information");


        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        //String test = information_data.getOfferSSID();
//        information_data.getOfferSignal();
//        information_data.getOfferCompany();
//        information_data.getOfferSecurity();
//        information_data.getOfferFrequency();
//        information_data.getOfferChannel();


        TextView_ssid.setText(information_data.getOfferSSID());


        TextView_sec.setText(information_data.getOfferMac_address());
        TextView_mac.setText(information_data.getOfferSecurity());


        TextView_sig.setText(information_data.getOfferSignal());
        TextView_company.setText(information_data.getOfferCompany());



        TextView_fre.setText(information_data.getOfferFrequency());
        TextView_cha.setText(information_data.getOfferChannel());


        copy_pass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text;
                text = pass_result.getText().toString();

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getApplicationContext(), "Text Copied",
                        Toast.LENGTH_SHORT).show();
            }
        });


        button=(Button)findViewById(R.id.crack_btn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                myprogress_crack.setVisibility(View.VISIBLE);

                myprogress_crack.setText("Cracking....."+information_data.getOfferSSID()+"'s password.");



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(pass_status){

                            pass_result.setText("Muict555");
                            pass_wifi_relative.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            myprogress_crack.setVisibility(View.INVISIBLE);
                        }else {


                            progressBar.setVisibility(View.INVISIBLE);
                            myprogress_crack.setVisibility(View.INVISIBLE);
                            aleartWrongPass(information_data.getOfferSSID());

                            return;
                        }
                    }
                }, 10000); // Millisecond 1000 = 1 sec

            }
        });

    }

    void aleartWrongPass(String ssid ) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(CrackRouterActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Cannot decrypt password");
        dialog.setMessage(ssid+"'s password is too strong");
        dialog.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() { // define the 'Cancel' button
            public void onClick(DialogInterface dialog, int which) {
                //Either of the following two lines should work.
                dialog.cancel();
                //dialog.dismiss();
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();

    }




}

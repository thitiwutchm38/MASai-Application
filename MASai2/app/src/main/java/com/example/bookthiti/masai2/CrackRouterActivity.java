package com.example.bookthiti.masai2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CrackRouterActivity extends AppCompatActivity {


    TextView TextView_mac;
    TextView TextView_sig ;

    TextView TextView_company;
    TextView TextView_sec;
    TextView TextView_fre;

    TextView TextView_cha;
    TextView TextView_ssid;




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


        MainModel information_data = (MainModel) getIntent().getParcelableExtra("router_information");



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



        //TextView TextView_fre = (TextView)findViewById(R.id.fre_value);



        //   tvTitle.setText("Title: " + movie.title);






    }
}

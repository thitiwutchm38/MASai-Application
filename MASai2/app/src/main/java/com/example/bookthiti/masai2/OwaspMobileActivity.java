package com.example.bookthiti.masai2;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OwaspMobileActivity extends AppCompatActivity {


    private String detailM = null;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owasp_mobile);

        Intent intent = getIntent();

        String tempM = intent.getStringExtra("M");

        TextView detailMobile = findViewById(R.id.tv_OWASP);



        TextView owaspNum = findViewById(R.id.textView_M);
        TextView owaspTopic = findViewById(R.id.textView_M_result);





//        String htmlText = " %s ";

        switch(tempM) {
            case "M1" :
                detailMobile.setText(R.string.M1);
                owaspNum.setText("M1");
                owaspTopic.setText("Improper Platform Usage");
                break;
            case "M2" :
                detailMobile.setText(R.string.M2);
                owaspNum.setText("M2");
                owaspTopic.setText("Insecure Data Storage");

                break;
            case "M3" :
                detailMobile.setText(R.string.M3);
                owaspNum.setText("M3");
                owaspTopic.setText("Insecure Communication");


                break;
            case "M4" :
                detailMobile.setText(R.string.M4);
                owaspNum.setText("M4");
                owaspTopic.setText("Insecure Authentication");


                break;
            case "M5" :
                detailMobile.setText(R.string.M5);
                owaspNum.setText("M5");
                owaspTopic.setText("Insufficient Cryptography");


                break;
            case "M6" :
                detailMobile.setText(R.string.M6);
                owaspNum.setText("M6");
                owaspTopic.setText("Insecure Authorization");


                break;
            case "M7" :
                detailMobile.setText(R.string.M7);
                owaspNum.setText("M7");
                owaspTopic.setText("Client Code Quality");


                break;
            case "M8" :
                detailMobile.setText(R.string.M8);
                owaspNum.setText("M8");
                owaspTopic.setText("Code Tampering");


                break;
            case "M9" :
                detailMobile.setText(R.string.M9);
                owaspNum.setText("M9");
                owaspTopic.setText("Reverse Engineering");


                break;
            case "M10" :
                detailMobile.setText(R.string.M10);
                owaspNum.setText("M10");
                owaspTopic.setText("Extraneous Functionality");



                break;

            default :
        }

//
//        String myData = " <html><body style=\"text-align:justify\">"+myresult+"</body></Html>";
//
//        WebView webView = (WebView) findViewById(R.id.webView_OWASP);
//        webView.setBackgroundColor(Color.TRANSPARENT);
//        webView.loadData(String.format(htmlText, myData), "text/html", "utf-8");
//
//        WebSettings webSettings = webView.getSettings();
//
//        webSettings.setDefaultFontSize(19);







    }






}

package com.example.bookthiti.masai2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.bookthiti.masai2.mobileapplicationscanningscreen.MobileApplicationScannerActivity;

public class OwaspSummaryActivity extends AppCompatActivity {



    private ImageView checkM1;
    private ImageView checkM2;
    private ImageView checkM3;
    private ImageView checkM4;
    private ImageView checkM5;
    private ImageView checkM6;
    private ImageView checkM7;
    private ImageView checkM8;
    private ImageView checkM9;
    private ImageView checkM10;

//    private Boolean tableCheckM1;
//    private Boolean tableCheckM2;
//    private Boolean tableCheckM3;
//    private Boolean tableCheckM4;
//    private Boolean tableCheckM5;
//    private Boolean tableCheckM6;
//    private Boolean tableCheckM7;
//    private Boolean tableCheckM8;
//    private Boolean tableCheckM9;
//    private Boolean tableCheckM10;


    private Boolean tableCheckM1  = true;
    private Boolean tableCheckM2  = false;
    private Boolean tableCheckM3  = true;
    private Boolean tableCheckM4  = true;
    private Boolean tableCheckM5  = true;
    private Boolean tableCheckM6  = true;
    private Boolean tableCheckM7  = false;
    private Boolean tableCheckM8  = true;
    private Boolean tableCheckM9  = true;
    private Boolean tableCheckM10 = true;









    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owasp_summary);


        checkM1 = (ImageView)findViewById(R.id.imageView_M1);
        checkM2 = (ImageView)findViewById(R.id.imageView_M2);
        checkM3 = (ImageView)findViewById(R.id.imageView_M3);
        checkM4 = (ImageView)findViewById(R.id.imageView_M4);
        checkM5 = (ImageView)findViewById(R.id.imageView_M5);
        checkM6 = (ImageView)findViewById(R.id.imageView_M6);
        checkM7 = (ImageView)findViewById(R.id.imageView_M7);
        checkM8 = (ImageView)findViewById(R.id.imageView_M8);
        checkM9 = (ImageView)findViewById(R.id.imageView_M9);
        checkM10 = (ImageView)findViewById(R.id.imageView_M10);



        checkM1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                        Intent intent = new Intent(OwaspSummaryActivity.this, SummaryMobileOwaspActivity.class);
                        startActivity(intent);

                    }
        });

        setupCheckOwasp();

    }



    public void setupCheckOwasp() {

        if (tableCheckM1) checkM1.setImageResource(R.drawable.owasp_check_yes) ;
        else checkM1.setImageResource(R.drawable.owasp_check_no);

        if(tableCheckM2) checkM2.setImageResource(R.drawable.owasp_check_yes);
        else checkM2.setImageResource(R.drawable.owasp_check_no) ;

        if(tableCheckM3) checkM3.setImageResource(R.drawable.owasp_check_yes);
        else checkM3.setImageResource(R.drawable.owasp_check_no) ;

        if(tableCheckM4) checkM4.setImageResource(R.drawable.owasp_check_yes);
        else checkM4.setImageResource(R.drawable.owasp_check_no) ;

        if(tableCheckM5) checkM5.setImageResource(R.drawable.owasp_check_yes);
        else checkM5.setImageResource(R.drawable.owasp_check_no) ;

        if(tableCheckM6) checkM6.setImageResource(R.drawable.owasp_check_yes);
        else checkM6.setImageResource(R.drawable.owasp_check_no) ;

        if(tableCheckM7) checkM7.setImageResource(R.drawable.owasp_check_yes);
        else checkM7.setImageResource(R.drawable.owasp_check_no) ;


        if(tableCheckM8) checkM8.setImageResource(R.drawable.owasp_check_yes);
        else checkM8.setImageResource(R.drawable.owasp_check_no) ;


        if(tableCheckM9) checkM9.setImageResource(R.drawable.owasp_check_yes);
        else checkM9.setImageResource(R.drawable.owasp_check_no) ;

        if(tableCheckM10) checkM10.setImageResource(R.drawable.owasp_check_yes);
        else checkM10.setImageResource(R.drawable.owasp_check_no) ;



    }
}

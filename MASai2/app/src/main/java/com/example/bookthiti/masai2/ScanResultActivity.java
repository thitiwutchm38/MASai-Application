package com.example.bookthiti.masai2;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;

public class ScanResultActivity extends AppCompatActivity {

    LinearLayout line_layout;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);



        line_layout = (LinearLayout) findViewById(R.id.linearLayout_scan_result);

        line_layout.setVisibility(View.GONE);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar_app_scan);
                progressBar.setVisibility(View.GONE);

                line_layout.setVisibility(View.VISIBLE);



            }
        }, 5000); // Millisecond 1000 = 1 sec
    }
}

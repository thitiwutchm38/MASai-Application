package com.example.bookthiti.masai2;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class Mobile_Appscan extends AppCompatActivity {

    Button button;
    ConstraintLayout ct;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_mobile__appscan);


        ct = (ConstraintLayout) findViewById(R.id.scan_result);

        button=(Button)findViewById(R.id.button_rescan);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                openActivity_Apps_Result();

            }
        });

        SearchView searchView = (SearchView)findViewById(R.id.SearchView_GooglePlay);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                Toast.makeText(context, "close", Toast.LENGTH_SHORT).show();
                return false;
            }
        });




        SearchView searchView2 = (SearchView)findViewById(R.id.SearchView_search_apk);

        searchView2.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                Toast.makeText(context, "close", Toast.LENGTH_SHORT).show();
                return false;
            }
        });



    }



    public void openActivity_Apps_Result() {

        Intent intent = new Intent(this,ScanResultActivity.class);
        startActivity(intent);
    }


}

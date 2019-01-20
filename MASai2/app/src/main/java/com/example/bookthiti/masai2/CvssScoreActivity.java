package com.example.bookthiti.masai2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alespero.expandablecardview.ExpandableCardView;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.CVEModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.CVEAdapter;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.DeviceModel;
import com.example.bookthiti.masai2.networksearchingscreen.OnRecyclerViewItemClickListener;

import java.util.ArrayList;

public class CvssScoreActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {

    ExpandableCardView card1 ;
    ExpandableCardView card2 ;



    RecyclerView mainRecyclerView;
    LinearLayoutManager linearLayoutManager;
    CVEAdapter mainRecyclerAdapter;


    private ArrayList<CVEModel> mainModelArrayList;
    TextView app_data_ip;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cvss_score_);

        mainRecyclerView = findViewById(R.id.recyclerview_cvss);
        Log.i("Info", "" + mainRecyclerView);
        linearLayoutManager = new LinearLayoutManager(CvssScoreActivity.this,
                LinearLayoutManager.VERTICAL, false);
        mainRecyclerView.setLayoutManager(linearLayoutManager);

        //Recycler Adapter
       mainModelArrayList = prepareList();
       mainRecyclerAdapter = new CVEAdapter(CvssScoreActivity.this, mainModelArrayList);
        mainRecyclerAdapter.setOnRecyclerViewItemClickListener(CvssScoreActivity.this);
        mainRecyclerView.setAdapter(mainRecyclerAdapter);

        app_data_ip = (TextView) findViewById(R.id.textView_data_ip_address);


        card1 = findViewById(R.id.profile);
        card2= findViewById(R.id.profile2);

        card1.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
            @Override
            public void onExpandChanged( View v, boolean isExpanded) {




                Toast.makeText(CvssScoreActivity.this, isExpanded ? "Expanded!" : "Collapsed!", Toast.LENGTH_SHORT).show();
            }
        });


        card2.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
            @Override
            public void onExpandChanged( View v, boolean isExpanded) {

                //app_data_ip.setText("4321");


                Toast.makeText(CvssScoreActivity.this, isExpanded ? "Expanded!" : "Collapsed!", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private ArrayList<CVEModel> prepareList() {

        ArrayList<CVEModel> mainModelList = new ArrayList<>();


//        //Convert JSON File
//        String json = null;
//        Integer count = null;
//
//        try {
//            InputStream is = getAssets().open("router.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//
//        JSONObject reader = null;
//        try {
//            reader = new JSONObject(json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            count = Integer.parseInt(reader.getString("count"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        String SSID = "CVEModel-2017-xxxx [high]";
        String  Signal = "Certain response codes in FTP connections can result in the use of uninitialized values for ports in FTP operations.";
        String Mode= "8.2";



        CVEModel mainModel1 = new CVEModel();
        CVEModel mainModel2 = new CVEModel();





//        mainModel1.setmCVE(SSID);
//        mainModel1.setmCVSS_score(Mode);
//        mainModel1.setmPort_des(Signal);
//
//        mainModel2.setmCVE(SSID);
//        mainModel2.setmCVSS_score(Mode);
//        mainModel2.setmPort_des(Signal);

        mainModelList.add(mainModel1);
        mainModelList.add(mainModel2);



        return mainModelList;

    }

    @Override
    public void onItemClick( int position, View view ) {
        DeviceModel mainModel = (DeviceModel) view.getTag();
        switch (view.getId()) {
            case R.id.layout_device:
                Toast.makeText(this, "Position clicked: " + String.valueOf(position) + ", " + mainModel.getIpAddress(), Toast.LENGTH_LONG).show();
                //openActivityPortInfo(position);
                break;
        }
    }
    }

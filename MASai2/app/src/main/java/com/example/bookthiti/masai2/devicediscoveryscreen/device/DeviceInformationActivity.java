package com.example.bookthiti.masai2.devicediscoveryscreen.device;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookthiti.masai2.CvssScoreActivity;
import com.example.bookthiti.masai2.Port_attackActivity;
import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.networksearchingscreen.OnRecyclerViewItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class DeviceInformationActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener  {


    // Arraylist of all ports

    ArrayList<ServiceModel> mainModelList;
    ArrayList<ServiceModel> mainModelArrayList;

    // Click textView for sorting
    TextView textView_show_portname;
    TextView  textView_show_portstatus;
    TextView textView_portnumber ;


    //Show device information
    TextView textView_data_ip ;
    TextView textView_data_mac;
    TextView  textView_data_device_type;
    TextView textView_data_port_opened;
    ImageView type_icon_sum;


    //Button click
    Button button_ports_attack;
    Button button_CVSS_score;

    //Recycleview&Swipe
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PortsAdapter mainRecyclerAdapter;



    //Check clicking with boolean
    boolean pName_temp = true;
    boolean pProtocal_temp = true;
    boolean pNumber_temp = true;

    Integer opened = null;
    Integer closed = null;
    String ip_address = null;



    private final String categoryName[] = {
            "Apple",
            "Samsung",
            "MI",
            "Motorola",
            "Nokia",
            "Oppo",
            "Micromax",
            "Honor",
            "Lenovo"
    };


    private final String type[] = {
            "media device",
            "phone",
            "general purpose",
            "printer",
            "phone",
            "webcam",
            "phone",
            "router",
            "general purpose"
    };



    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);

        Intent intent = getIntent();


            //List buttons textview click to sort
            textView_show_portname = (TextView) findViewById(R.id.textView_show_portname);
            textView_portnumber = (TextView) findViewById(R.id.textView_portnumber);
            textView_show_portstatus = (TextView) findViewById(R.id.textView_show_portstatus);


            //List all device summaries
            type_icon_sum = (ImageView) findViewById(R.id.type_icon_sum);
            textView_data_ip = (TextView) findViewById(R.id.textView_data_ip);
            textView_data_mac  = (TextView) findViewById(R.id.textView_data_mac);
            textView_data_device_type  = (TextView) findViewById(R.id.textView_data_device_type);
            textView_data_port_opened  = (TextView) findViewById(R.id.textView_data_port_opened);


            //Button id
           button_ports_attack = (Button)findViewById(R.id.button_ports_attack);
           button_CVSS_score  = (Button)findViewById(R.id.button_CVSS_score);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_ports);



        final RecyclerView mainRecyclerView = findViewById(R.id.recycle_ports_info);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
            mainRecyclerView.setLayoutManager(linearLayoutManager);


            //Recycler Adapter
            mainModelArrayList = prepareList();


            textView_data_ip.setText( intent.getStringExtra("IP_Address"));
            textView_data_mac.setText( intent.getStringExtra("Mac_Address"));
            textView_data_device_type.setText( intent.getStringExtra("Device_Types"));
            type_icon_sum.setImageResource(intent.getIntExtra("icon", -1));
            textView_data_port_opened.setText(opened.toString());


            mainRecyclerAdapter = new PortsAdapter(this, mainModelArrayList);
            mainRecyclerAdapter.setOnRecyclerViewItemClickListener(DeviceInformationActivity.this);
            mainRecyclerView.setAdapter(mainRecyclerAdapter);

        textView_show_portname.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sortpName();
            }
        });

        textView_show_portstatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sortpProtocal();
            }
        });

        textView_portnumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sortpNum() ;
            }
        });

        //Button click
        button_CVSS_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openActivity_CVSS_score();
            }
        });


        button_ports_attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


                openActivity_port_att();
            }
        });


    }


    private ArrayList<ServiceModel> prepareList() {

        mainModelList = new ArrayList<>();

        //Convert JSON File

        String json = null;


        JSONObject read_count = null;



        try {
            InputStream is = getAssets().open("port_information.json");
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

        try {
            read_count = reader.getJSONObject("port summary");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            opened  = Integer.parseInt(read_count.getString("opened"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            closed  = Integer.parseInt(read_count.getString("closed"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            ip_address  = read_count.getString("ip");

        } catch (JSONException e) {
            e.printStackTrace();
        }



        String p_number = null;
        String p_name = null;
        String p_protocal = null;



        for (int i = 0; i < opened; i++) {


            ServiceModel mainModel = new ServiceModel();

            JSONObject temp = null;

            try {
                temp = reader.getJSONObject(Integer.toString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                p_number  = temp.getString("p_number");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                p_protocal  = temp.getString("p_protocal");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                p_name  = temp.getString("p_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }



            mainModel.setName(p_name);
//            mainModel.setPort(p_number);
            mainModel.setProtocol(p_protocal);



            mainModelList.add(mainModel);

        }
        return mainModelList;
    }


    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        },1000);
    }

    @Override
    public void onItemClick( int position, View view ) {
        ServiceModel mainModel = (ServiceModel) view.getTag();
        switch (view.getId()) {
            case R.id.layout_ports:
                Toast.makeText(this, "Position clicked: " + String.valueOf(position) + ", " + mainModel.getName(), Toast.LENGTH_LONG).show();
                break;
        }

    }

    void sortpNum() {

        if (pNumber_temp == true){
            pNumber_temp =false;
            Collections.sort(mainModelList, ServiceModel.portNum_asc);
            mainRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(DeviceInformationActivity.this);
        }else{
            pNumber_temp =true;
            Collections.sort(mainModelList, ServiceModel.portNum_des);
            mainRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(DeviceInformationActivity.this);
        }
    }
    void sortpName() {

        if (pNumber_temp == true){
            pNumber_temp =false;
            Collections.sort(mainModelList, ServiceModel.portsName_asc);
            mainRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(DeviceInformationActivity.this);
        }else{
            pNumber_temp =true;
            Collections.sort(mainModelList, ServiceModel.portsName_des);
            mainRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(DeviceInformationActivity.this);
        }
    }
    void sortpProtocal() {

        if (pNumber_temp == true){
            pNumber_temp =false;
            Collections.sort(mainModelList, ServiceModel.portsProtocal_asc);
            mainRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(DeviceInformationActivity.this);
        }else{
            pNumber_temp =true;
            Collections.sort(mainModelList, ServiceModel.portsProtocal_des);
            mainRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(DeviceInformationActivity.this);
        }
    }

    public void openActivity_CVSS_score() {

        Intent intent = new Intent(this,CvssScoreActivity.class);
        startActivity(intent);
    }

    public void openActivity_port_att() {

        Intent intent = new Intent(this,Port_attackActivity.class);

        startActivity(intent);
    }




}

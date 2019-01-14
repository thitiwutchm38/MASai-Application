package com.example.bookthiti.masai2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;

import android.os.Handler;
import android.os.Parcelable;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Search_Network extends AppCompatActivity implements OnRecyclerViewItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener  {


    final String wifi_password = null;


    private ArrayList<MainModel> mainModelArrayList;

    private SwipeRefreshLayout mSwipeRefreshLayout;


    private MainRecyclerAdapter mainRecyclerAdapter;

    Button reScan_btt;


    TextView textView_ssid;
    TextView textView_mode;
    TextView textView_signal;


    LinearLayout lin_port_att;


    boolean ssid_temp = true;
    boolean mode_temp = true;
    boolean signal_temp = true;


        private final int categoryIcon[] = {
                R.drawable.wifi_device,
                R.drawable.wifi_device,
                R.drawable.wifi_device,
                R.drawable.wifi_device,
                R.drawable.wifi_device,
                R.drawable.wifi_device,
                R.drawable.wifi_device,
                R.drawable.wifi_device,
                R.drawable.wifi_device
        };

        private final String categorySSID[] = {
                "MU-WiFi",
                ".@ TRUEWIFI",
                ".@ AIS SUPER WIFI",
                "ICTenergy",
                "VoIP ICT",
                "whoa",
                "Free WiFi",
                "Aloha_124",
                "MAISAI A++"
        };

        private final String categoryMode[] = {
                "WPA2",
                "WPA2",
                "WPA2",
                "WEP",
                "WPA2",
                "WPA2",
                "WEP",
                "WPA2",
                "WPA2"
        };

        private final String categorySignal[] = {
                "High",
                "Medium",
                "High",
                "High",
                "Low",
                "High",
                "Low",
                "High",
                "Low"
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search__network);



            //Sort by Topic
            textView_ssid = (TextView)findViewById(R.id.textView_ssid);

            textView_mode  = (TextView)findViewById(R.id.textView_mode);

            textView_signal = (TextView)findViewById(R.id.textView_signal);



            reScan_btt = (Button) findViewById(R.id.button_rescan);

            lin_port_att = (LinearLayout) findViewById(R.id.lin_port_att);


            reScan_btt.setVisibility(View.GONE);
            lin_port_att.setVisibility(View.GONE);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

                    final RecyclerView mainRecyclerView = findViewById(R.id.rv_router_list);

                    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search_Network.this,
                            LinearLayoutManager.VERTICAL, false);

                    mainRecyclerView.setLayoutManager(linearLayoutManager);

                    //Recycler Adapter

                    mainModelArrayList = prepareList();

                    Collections.sort(mainModelArrayList, MainModel.modelSigno);


                    mainRecyclerAdapter = new MainRecyclerAdapter(Search_Network.this,mainModelArrayList);

                    mainRecyclerAdapter.setOnRecyclerViewItemClickListener(Search_Network.this);
                    mainRecyclerView.setAdapter(mainRecyclerAdapter);



                    final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.GONE);

                    lin_port_att.setVisibility(View.VISIBLE);


                    reScan_btt.setVisibility(View.VISIBLE);



                }
            }, 5000); // Millisecond 1000 = 1 sec


            textView_signal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sortSignal();

                }
            });

            textView_mode.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    sortMode();
                }
            });

            textView_ssid.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    sortSSID();
                }
            });


        }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.button_rescan:

        }
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

    private ArrayList<MainModel> prepareList(){

            ArrayList<MainModel> mainModelList = new ArrayList<>();
            //ArrayList<MainModel> sortModelList = new ArrayList<>();
            JSONArray values = null;


            //Convert JSON File
            String json = null;
            //Integer count = null;
            JSONArray jsonarray =null;


        try {
                InputStream is = getAssets().open("wifi_scan.json");
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


        JSONObject temp = null;

                try {
                    temp = reader.getJSONObject("payload");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            String SSID = null;
            String Mode = null;
            String Signal= null;

            String Frequency= null;
            String Channel= null;

            String Mac_address= null;

            String Security= null;


        try {
            values = temp.getJSONArray("routers");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < values.length(); i++) {

            MainModel mainModel = new MainModel();

            JSONObject jsonobject = null;

            try {
                jsonobject = values.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                SSID = jsonobject.getString("SSID");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Mode= jsonobject.getString("MODE");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Signal= jsonobject.getString("SIGNAL");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Frequency= jsonobject.getString("MODE");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                Mac_address  = jsonobject.getString("BSSID");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                Security  = jsonobject.getString("SECURITY");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                Channel  = jsonobject.getString("CHAN");
            } catch (JSONException e) {
                e.printStackTrace();
            }



            int signal = Integer.parseInt(Signal);

                if ((signal <= 100 )&&(signal>=75)){

                    mainModel.setOfferIcon( R.drawable.wifi_device_4);


                }else if((signal<75)&&(signal>=50)) {

                    mainModel.setOfferIcon( R.drawable.wifi_device_3);


                }else if((signal<50)&&(signal>=25)) {

                    mainModel.setOfferIcon( R.drawable.wifi_device_2);


                }else if(signal<25) {

                    mainModel.setOfferIcon( R.drawable.wifi_device_1);

                }


            mainModel.setOfferSSID(SSID);
            mainModel.setOfferMode(Mode);
            mainModel.setOfferSignal(Signal);

            mainModel.setOfferFrequency(Frequency);
            mainModel.setOfferChannel(Channel);
            //mainModel.setOfferCompany(Company);

            mainModel.setOfferMac_address(Mac_address);

            mainModel.setOfferSecurity(Security);

            mainModelList.add(mainModel);
        }
            return mainModelList;

        }







    @Override
        public void onItemClick(final int position, View view) {
        //    MainModel mainModel = (MainModel) view.getTag();

           Intent intent = getIntent();
           Intent pass_intent = new Intent(this,CrackRouterActivity.class);
           final Intent pass_intent_blu = new Intent(this,Device_list.class);


           switch (intent.getStringExtra("MyValue")) {
                case "device_att" :

                    //Toast.makeText(this,"Position clicked: " + String.valueOf(position) + ", "+ mainModel.getOfferSSID(),Toast.LENGTH_LONG).show();
                    //showAddItemDialog(this,prepareList().get(position).getOfferSSID() );

                    //pass_intent_blu.putExtra("router_information", prepareList().get(position));

                    promptForResult(new PromptRunnable(){
                        // put whatever code you want to run after user enters a result
                        public void run() {
                            // get the value we stored from the dialog
                            String value = this.getValue();
                            // do something with this value...
                            // In our example we are taking our value and passing it to
                            // an activity intent, then starting the activity.
                            //pass_intent_blu.putExtra("extraValue", value);
                            pass_intent_blu.putExtra("router_information", prepareList().get(position));
                            pass_intent_blu.putExtra("password", value);

                            System.out.println("Password is"+value);


                            if(!value.equals("12345")){
                                aleartWrongPass(prepareList().get(position).getOfferSSID());
                                return;

                            }
                            startActivity(pass_intent_blu);
                        }
                    },prepareList().get(position).getOfferSSID());


                    break;

                case "router_att" :

                    Toast.makeText(this,"Position clicked: " + String.valueOf(position) + ", "+ prepareList().get(position).getOfferSSID(),Toast.LENGTH_LONG).show();
                    //showAddItemDialog(this,prepareList().get(position).getOfferSSID() );

                    pass_intent.putExtra("router_information", prepareList().get(position));
                    //pass_intent.putExtra("iis", );
                    startActivity(pass_intent);

                    break;
            }
        }

    void promptForResult(final PromptRunnable postrun,String ssid ) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Please input your WiFi Password");
        alert.setMessage("Your SSID: "+ssid);
        // Create textbox to put into the dialog
        final EditText input = new EditText(this);
        // put the textbox into the dialog
        alert.setView(input);
        // procedure for when the ok button is clicked.
        alert.setPositiveButton("Confirmed", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                dialog.dismiss();
                // set value from the dialog inside our runnable implementation
                postrun.setValue(value);
                // ** HERE IS WHERE THE MAGIC HAPPENS! **
                // now that we have stored the value, lets run our Runnable
                postrun.run();
                return;
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                return;
            }
        });
        alert.show();
    }

    void aleartWrongPass(String ssid ) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(Search_Network.this);
        dialog.setCancelable(false);
        dialog.setTitle(ssid+"'s password is incorrect");
        dialog.setMessage("Check your password, then type your password again." );
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

    void sortSignal() {

        if (signal_temp == true){
            signal_temp =false;
            Collections.sort(mainModelArrayList, MainModel.modelSig_des);
            mainRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(Search_Network.this);
        }else{
            signal_temp =true;
            Collections.sort(mainModelArrayList, MainModel.modelSigno);
            mainRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(Search_Network.this);
        }
    }

    void sortSSID() {

        if (ssid_temp == true){
            ssid_temp =false;
            Collections.sort(mainModelArrayList, MainModel.modelssid_asc);
            mainRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(Search_Network.this);
        }else{
            ssid_temp =true;
            Collections.sort(mainModelArrayList, MainModel.modelssid_des);
            mainRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(Search_Network.this);
        }
    }


    void sortMode() {

        if (mode_temp == true){
            mode_temp =false;
            Collections.sort(mainModelArrayList, MainModel.modelmode_asc);
            mainRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(Search_Network.this);
        }else{
            mode_temp =true;
            Collections.sort(mainModelArrayList, MainModel.modelmode_des);
            mainRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(Search_Network.this);
        }
    }


    class PromptRunnable implements Runnable {
        private String v;
        void setValue(String inV) {
            this.v = inV;
        }
        String getValue() {
            return this.v;
        }
        public void run() {
            this.run();
        }
    }

}
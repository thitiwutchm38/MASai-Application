package com.example.bookthiti.masai2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Search_Network extends AppCompatActivity implements OnRecyclerViewItemClickListener  {

        final String wifi_password = null;
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

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    final RecyclerView mainRecyclerView = findViewById(R.id.rv_router_list);


                    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search_Network.this,
                            LinearLayoutManager.VERTICAL, false);


                    mainRecyclerView.setLayoutManager(linearLayoutManager);


                    //Recycler Adapter
                    final ArrayList<MainModel> mainModelArrayList = prepareList();

                    final MainRecyclerAdapter mainRecyclerAdapter = new MainRecyclerAdapter(Search_Network.this,mainModelArrayList);
                    mainRecyclerAdapter.setOnRecyclerViewItemClickListener(Search_Network.this);
                    mainRecyclerView.setAdapter(mainRecyclerAdapter);

                    final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.GONE);
                }
            }, 5000); // Millisecond 1000 = 1 sec





        }
        private ArrayList<MainModel> prepareList() {

            ArrayList<MainModel> mainModelList = new ArrayList<>();

            //Convert JSON File
            String json = null;
            Integer count = null;

            try {
                InputStream is = getAssets().open("router.json");
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
                count = Integer.parseInt(reader.getString("count"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String SSID = null;
            String Mode = null;
            String Signal= null;

            String Frequency= null;
            String Channel= null;
            String Company= null;

            String Mac_address= null;

            String Security= null;



            for (int i = 0; i < count; i++) {

               System.out.println(SSID);
               System.out.println(Mode);
               System.out.println(Signal);


                MainModel mainModel = new MainModel();
                JSONObject temp = null;

                try {
                    temp = reader.getJSONObject(Integer.toString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    SSID  = temp.getString("SSID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Mode  = temp.getString("Mode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Signal  = temp.getString("Signal");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Frequency  = temp.getString("Frequency");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Channel  = temp.getString("Channel");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Company  = temp.getString("Company");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Mac_address  = temp.getString("Mac_address");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Security  = temp.getString("Security");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                mainModel.setOfferIcon(categoryIcon[i]);

                mainModel.setOfferSSID(SSID);
                mainModel.setOfferMode(Mode);
                mainModel.setOfferSignal(Signal);

                mainModel.setOfferFrequency(Frequency);
                mainModel.setOfferChannel(Channel);
                mainModel.setOfferCompany(Company);

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
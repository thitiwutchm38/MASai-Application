package com.example.bookthiti.masai2;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
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

                mainModel.setOfferIcon(categoryIcon[i]);
                mainModel.setOfferSSID(SSID);
                mainModel.setOfferMode(Mode);
                mainModel.setOfferSignal(Signal);


                mainModelList.add(mainModel);
            }
            return mainModelList;
        }

        @Override
        public void onItemClick(int position, View view) {
            MainModel mainModel = (MainModel) view.getTag();
            switch (view.getId()) {
                case R.id.row_main_adapter_linear_layout:

                    //Toast.makeText(this,"Position clicked: " + String.valueOf(position) + ", "+ mainModel.getOfferSSID(),Toast.LENGTH_LONG).show();
                    showAddItemDialog(this,mainModel.getOfferSSID() );
                    break;
            }
        }

        private void showAddItemDialog(Context c, String ssid) {
            final EditText taskEditText = new EditText(c);
            AlertDialog dialog = new AlertDialog.Builder(c)
                    .setTitle("Please input your WiFi Password")
                    .setMessage("Your SSID: "+ssid)
                    .setView(taskEditText)
                    .setPositiveButton("Confirmed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          String  wifi_password = String.valueOf(taskEditText.getText());
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();

        }
    }
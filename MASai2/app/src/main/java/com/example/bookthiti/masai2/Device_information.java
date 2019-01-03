package com.example.bookthiti.masai2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Device_information extends AppCompatActivity implements OnRecyclerViewItemClickListener {

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



            final RecyclerView mainRecyclerView = findViewById(R.id.recycle_ports_info);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
            mainRecyclerView.setLayoutManager(linearLayoutManager);


            //Recycler Adapter
            final ArrayList<Ports> mainModelArrayList = prepareList();
            final PortsAdapter mainRecyclerAdapter = new PortsAdapter(this, mainModelArrayList);
            mainRecyclerAdapter.setOnRecyclerViewItemClickListener(Device_information.this);
            mainRecyclerView.setAdapter(mainRecyclerAdapter);

    }


    private ArrayList<Ports> prepareList() {

        ArrayList<Ports> mainModelList = new ArrayList<>();

        //Convert JSON File
//        String json = null;
       Integer count = null;
//
//        try {
//            InputStream is = getAssets().open("device.json");
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
//
//        String device_ip = null;
//        String device_mac = null;
//        String device_type= null;




        for (int i = 0; i < 6; i++) {


            Ports mainModel = new Ports();

            JSONObject temp = null;

//            try {
//                temp = reader.getJSONObject(Integer.toString(i));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                device_ip  = temp.getString("IP_Address");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                device_mac  = temp.getString("Mac_Address");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                device_type  = temp.getString("Device_Types");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


            mainModel.setP_name(categoryName[i]);
            mainModel.setP_number(type[i]);



            mainModelList.add(mainModel);

        }
        return mainModelList;
    }



    @Override
    public void onItemClick( int position, View view ) {
        Ports mainModel = (Ports) view.getTag();
        switch (view.getId()) {
            case R.id.layout_ports:
                Toast.makeText(this, "Position clicked: " + String.valueOf(position) + ", " + mainModel.getP_name(), Toast.LENGTH_LONG).show();
                break;
        }


    }
}

package com.example.bookthiti.masai2;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class Device_list extends AppCompatActivity implements OnRecyclerViewItemClickListener {



    private final int categoryIcon[] = {
            R.drawable.wifi_device_4,
            R.drawable.wifi_device_4,
            R.drawable.wifi_device_4,

            R.drawable.wifi_device_4,
            R.drawable.wifi_device_4,
            R.drawable.wifi_device_4,

            R.drawable.wifi_device_4,
            R.drawable.wifi_device_4,
            R.drawable.wifi_device_4

    };

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

    private final String mac[] = {
            "4b:d3:f0:a9:ef:a5",
            "b7:66:6b:a7:01:57",
            "63:d4:41:a8:d0:b1",
            "a7:c4:13:42:56:3b",
            "32:54:f1:c5:49:07",
            "12:eb:32:e2:be:93",
            "17:1f:cb:24:6f:90",
            "e7:35:35:ea:5c:af",
            "17:cf:ff:09:42:d4"
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

    // RelativeLayout currentLayout;
    // Now get a handle to any View contained
    // within the main layout you are using
    //final View someView = findViewById(R.id.layout_device);



    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        // currentLayout = (RelativeLayout) findViewById(R.id.layout_device_main);

        final RecyclerView mainRecyclerView = findViewById(R.id.recyclerview_device_item);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mainRecyclerView.setLayoutManager(linearLayoutManager);

       // currentLayout.setBackgroundColor(Color.parseColor("#D3DFEE"));

        //Recycler Adapter
        final ArrayList<Devices> mainModelArrayList = prepareList();
        final DeviceAdapter mainRecyclerAdapter = new DeviceAdapter(this, mainModelArrayList);
        mainRecyclerAdapter.setOnRecyclerViewItemClickListener(Device_list.this);
        mainRecyclerView.setAdapter(mainRecyclerAdapter);
    }

    private ArrayList<Devices> prepareList() {
        ArrayList<Devices> mainModelList = new ArrayList<>();

        for (int i = 0; i < categoryName.length; i++) {


            Devices mainModel = new Devices();

            mainModel.setmIP_address(categoryName[i]);
            mainModel.setmMac_address(mac[i]);
            mainModel.setmDevice_types(type[i]);


            mainModel.setOfferIcon(categoryIcon[i]);

            mainModelList.add(mainModel);





        }
        return mainModelList;
    }

    @Override
    public void onItemClick( int position, View view ) {
        Devices mainModel = (Devices) view.getTag();
        switch (view.getId()) {
            case R.id.layout_device:
                Toast.makeText(this, "Position clicked: " + String.valueOf(position) + ", " + mainModel.getmIP_address(), Toast.LENGTH_LONG).show();
                break;
        }


    }
}
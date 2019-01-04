package com.example.bookthiti.masai2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Device_information extends AppCompatActivity implements OnRecyclerViewItemClickListener {


    TextView textView_show_ip ;
    TextView textView_port_closed ;
    TextView textView_port_opened ;

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


            //TextView

            textView_show_ip = (TextView) findViewById(R.id.textView_show_ip);
            textView_port_closed = (TextView) findViewById(R.id.textView_port_closed);
            textView_port_opened = (TextView) findViewById(R.id.textView_port_opened);



            final RecyclerView mainRecyclerView = findViewById(R.id.recycle_ports_info);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
            mainRecyclerView.setLayoutManager(linearLayoutManager);


            //Recycler Adapter
            final ArrayList<Ports> mainModelArrayList = prepareList();

            textView_show_ip.setText(ip_address);
            textView_port_closed.setText(closed.toString()+" closed Ports");
            textView_port_opened.setText(opened.toString()+" opened Ports");


        final PortsAdapter mainRecyclerAdapter = new PortsAdapter(this, mainModelArrayList);
            mainRecyclerAdapter.setOnRecyclerViewItemClickListener(Device_information.this);
            mainRecyclerView.setAdapter(mainRecyclerAdapter);

    }


    private ArrayList<Ports> prepareList() {

        ArrayList<Ports> mainModelList = new ArrayList<>();

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


        for (int i = 0; i < opened; i++) {


            Ports mainModel = new Ports();

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
                p_name  = temp.getString("p_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }



            mainModel.setP_name(p_name);
            mainModel.setP_number(p_number);



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

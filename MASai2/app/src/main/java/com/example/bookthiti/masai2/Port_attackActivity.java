package com.example.bookthiti.masai2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookthiti.masai2.devicediscoveryscreen.Devices;
import com.example.bookthiti.masai2.networksearchingscreen.OnRecyclerViewItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Port_attackActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {




    ArrayList<Ports> mainModelList;
    ArrayList<Ports> checkList;

    PortAttdapter mainRecyclerAdapter;

    Integer opened = null;
    Integer closed = null;
    String ip_address = null;
    Button btn_port_att = null;




    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port_attack);


        final RecyclerView mainRecyclerView = findViewById(R.id.recycle_port_attack);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Port_attackActivity.this,
                LinearLayoutManager.VERTICAL, false);

        mainRecyclerView.setLayoutManager(linearLayoutManager);

        //Recycler Adapter
        ArrayList<PortAtt> mainModelArrayList =  new ArrayList<>();




        checkList = prepareList();


        for(int i=0 ; i < opened;i++){


            System.out.println(checkList.get(i).getP_number());

            if(checkList.get(i).getP_number().equals("22")){
                PortAtt att = new PortAtt();

                att.setPort_att_type("Bruteforce");
                att.setPortname("SSH");
                att.setPortnum("22");

                mainModelArrayList.add(att);
            }
            if(checkList.get(i).getP_number().equals("80")){
                PortAtt att = new PortAtt();

                att.setPort_att_type("Bruteforce");
                att.setPortname("HTTP");
                att.setPortnum("80");

                mainModelArrayList.add(att);
            }


        }

        mainRecyclerAdapter = new PortAttdapter(Port_attackActivity.this, mainModelArrayList);
        mainRecyclerAdapter.setOnRecyclerViewItemClickListener(Port_attackActivity.this);
        mainRecyclerView.setAdapter(mainRecyclerAdapter);

    }
    private ArrayList<Ports> prepareList() {

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
                p_protocal  = temp.getString("p_protocal");
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
            mainModel.setP_protocal(p_protocal);



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
                    //openActivity_port_info(position);
                    break;
            }
        }
}

package com.example.bookthiti.masai2.devicediscoveryscreen.device;

import android.content.Context;
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
import com.example.bookthiti.masai2.OnRecyclerViewItemClickListener;

import java.util.Collections;
import java.util.List;

public class DeviceInformationActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    private List<ServiceModel> mServiceModelList;
    private DeviceModel mDeviceModel;

    private TextView mTextViewPortNameLabel;
    private TextView mTextViewProtocolLabel;
    private TextView mTextViewPortIdLabel;
    private TextView mTextViewIpAddress;
    private TextView mTextViewMacAddress;
    private TextView mTextViewDeviceType;
    private TextView mTextViewOpenPortNumber;
    private ImageView mImageViewDeviceType;
    private Button mButtonPortAttack;
    private Button mButtonDeviceAssessment;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ServiceRecyclerAdapter mServiceRecyclerAdapter;

    boolean pName_temp = true;
    boolean pProtocal_temp = true;
    boolean pNumber_temp = true;

    Integer opened = null;
    Integer closed = null;
    String ip_address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);
        Intent intent = getIntent();
        mDeviceModel = intent.getExtras().getParcelable("targetDeviceModel");
        mContext = getApplicationContext();

        //List buttons textview click to sort
        mTextViewPortNameLabel = (TextView) findViewById(R.id.textView_show_portname);
        mTextViewPortIdLabel = (TextView) findViewById(R.id.textView_portnumber);
        mTextViewProtocolLabel = (TextView) findViewById(R.id.textView_show_portstatus);

        //List all device summaries
        mImageViewDeviceType = (ImageView) findViewById(R.id.type_icon_sum);
        mTextViewIpAddress = (TextView) findViewById(R.id.textView_data_ip);
        mTextViewMacAddress = (TextView) findViewById(R.id.textView_data_mac);
        mTextViewDeviceType = (TextView) findViewById(R.id.textView_data_device_type);
        mTextViewOpenPortNumber = (TextView) findViewById(R.id.textView_data_port_opened);


        //Button id
        mButtonPortAttack = (Button) findViewById(R.id.button_ports_attack);
        mButtonDeviceAssessment = (Button) findViewById(R.id.button_CVSS_score);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_ports);


        RecyclerView recyclerView = findViewById(R.id.recycle_ports_info);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Recycler Adapter
        mServiceModelList = mDeviceModel.getServiceModels();
        mTextViewIpAddress.setText(mDeviceModel.getIpAddress());
        mTextViewMacAddress.setText(mDeviceModel.getMacAddress());
        mTextViewDeviceType.setText(mDeviceModel.getDeviceType());
        mImageViewDeviceType.setImageResource(mDeviceModel.getIconId());
        mTextViewOpenPortNumber.setText(Integer.toString(mServiceModelList.size()));

        mServiceRecyclerAdapter = new ServiceRecyclerAdapter(mContext, mServiceModelList);
        mServiceRecyclerAdapter.setOnRecyclerViewItemClickListener(DeviceInformationActivity.this);
        recyclerView.setAdapter(mServiceRecyclerAdapter);
        mTextViewPortNameLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortpName();
            }
        });

        mTextViewProtocolLabel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sortpProtocal();
            }
        });

        mTextViewPortIdLabel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sortpNum();
            }
        });

        //Button click
        mButtonDeviceAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_CVSS_score();
            }
        });


        mButtonPortAttack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openActivity_port_att();
            }
        });

    }


    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }, 1000);
    }

    @Override
    public void onItemClick(int position, View view) {
        ServiceModel mainModel = (ServiceModel) view.getTag();
        switch (view.getId()) {
            case R.id.layout_ports:
                Toast.makeText(this, "Position clicked: " + String.valueOf(position) + ", " + mainModel.getName(), Toast.LENGTH_LONG).show();
                break;
        }

    }

    void sortpNum() {

        if (pNumber_temp == true) {
            pNumber_temp = false;
            Collections.sort(mServiceModelList, ServiceModel.portNum_asc);
            mServiceRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(DeviceInformationActivity.this);
        } else {
            pNumber_temp = true;
            Collections.sort(mServiceModelList, ServiceModel.portNum_des);
            mServiceRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(DeviceInformationActivity.this);
        }
    }

    void sortpName() {

        if (pNumber_temp == true) {
            pNumber_temp = false;
            Collections.sort(mServiceModelList, ServiceModel.portsName_asc);
            mServiceRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(DeviceInformationActivity.this);
        } else {
            pNumber_temp = true;
            Collections.sort(mServiceModelList, ServiceModel.portsName_des);
            mServiceRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(DeviceInformationActivity.this);
        }
    }

    void sortpProtocal() {

        if (pNumber_temp == true) {
            pNumber_temp = false;
            Collections.sort(mServiceModelList, ServiceModel.portsProtocal_asc);
            mServiceRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(DeviceInformationActivity.this);
        } else {
            pNumber_temp = true;
            Collections.sort(mServiceModelList, ServiceModel.portsProtocal_des);
            mServiceRecyclerAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setOnRefreshListener(DeviceInformationActivity.this);
        }
    }

    public void openActivity_CVSS_score() {

        Intent intent = new Intent(this, CvssScoreActivity.class);
        startActivity(intent);
    }

    public void openActivity_port_att() {

        Intent intent = new Intent(this, Port_attackActivity.class);

        startActivity(intent);
    }


}

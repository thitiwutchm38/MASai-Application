package com.example.bookthiti.masai2;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import java.util.ArrayList;

import static com.example.bookthiti.masai2.DeviceAdapter.Holder.*;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.Holder>{
    private ArrayList<Devices> mainModelArrayList;
    private Context context;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;


    public DeviceAdapter(Context context,ArrayList<Devices> mainModelArrayList) {
        this.context = context;
        this.mainModelArrayList = mainModelArrayList;
    }
    @Override
    public DeviceAdapter.Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_devices_item, viewGroup, false);

        return new DeviceAdapter.Holder(view);
    }
    @Override
    public void onBindViewHolder(DeviceAdapter.Holder Holder, int position) {
        final Devices offersListModel = mainModelArrayList.get(position);

        Holder.ip_Text.setText(offersListModel.getmIP_address());
        Holder.mac_Text.setText(offersListModel.getmMac_address());
        Holder.type_Text.setText(offersListModel.getmDevice_types());



        Holder.rowMainImage.setImageResource(offersListModel.getOfferIcon());

        Holder.rowMainParentLinearLayout.setTag(offersListModel);




        if(position % 2 == 0) {
        ((RelativeLayout)Holder.device_layout).setBackgroundColor(Color.parseColor("#A0D4EF")); }
        else
            {
        ((RelativeLayout)Holder.device_layout).setBackgroundColor(Color.parseColor("#D3DFEE"));
            }

    }



    @Override
    public int getItemCount() {
        return mainModelArrayList.size();
    }
    public class Holder extends RecyclerView.ViewHolder{

        private ImageView rowMainImage;
        private TextView ip_Text;
        private TextView mac_Text;
        private TextView type_Text;
        private RelativeLayout device_layout ;



        private RelativeLayout rowMainParentLinearLayout;
        public Holder(View view) {
            super(view);


            rowMainImage = view.findViewById(R.id.device_device_icon);

            ip_Text = view.findViewById(R.id.device_data_ip);
            mac_Text = view.findViewById(R.id.device_data_mac);
            type_Text = view.findViewById(R.id.device_data_type);
            device_layout = view.findViewById(R.id.layout_device);

            rowMainParentLinearLayout =  view.findViewById(R.id.layout_device);

            rowMainParentLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onRecyclerViewItemClickListener != null) {
                        onRecyclerViewItemClickListener.onItemClick(getAdapterPosition(), view);
                    }
                }
            });
        }
    }
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

}


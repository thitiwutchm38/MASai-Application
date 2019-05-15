package com.example.bookthiti.masai2.devicediscoveryscreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.DeviceModel;
import com.example.bookthiti.masai2.utils.OnRecyclerViewItemClickListener;

import java.util.List;

public class DeviceDiscoveryRecyclerAdapter extends RecyclerView.Adapter<DeviceDiscoveryRecyclerAdapter.Holder> {
    private List<DeviceModel> mDeviceModelList;
    private Context mContext;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;


    public DeviceDiscoveryRecyclerAdapter(Context mContext, List<DeviceModel> mDeviceModelList) {
        this.mContext = mContext;
        this.mDeviceModelList = mDeviceModelList;
    }

    @Override
    public DeviceDiscoveryRecyclerAdapter.Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_owasp, viewGroup, false);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device, viewGroup, false);
        return new DeviceDiscoveryRecyclerAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(DeviceDiscoveryRecyclerAdapter.Holder Holder, int position) {
        DeviceModel deviceModel = mDeviceModelList.get(position);

        Holder.mTextViewIpAddress.setText(deviceModel.getIpAddress());
        if (deviceModel.getMacAddress() != null)
            Holder.mTextViewMacAddress.setText(deviceModel.getMacAddress());
        else {
            Holder.mTextViewMacAddress.setText("-");
        }
        if (deviceModel.getDeviceType() != null) {
            Holder.mTextViewDeviceType.setText(deviceModel.getDeviceType());
        } else {
            Holder.mTextViewDeviceType.setText("-");
        }

        Holder.mImageViewDeviceIcon.setImageResource(deviceModel.getIconId());
        Holder.rowMainParentLinearLayout.setTag(deviceModel);
//        if(position % 2 == 0) {
//        ((RelativeLayout)Holder.device_layout).setBackgroundColor(Color.parseColor("#A0D4EF"));
//        }
//        else
//            {
//        ((RelativeLayout)Holder.device_layout).setBackgroundColor(Color.parseColor("#D3DFEE"));
//            }
//
    }

    @Override
    public int getItemCount() {
        return mDeviceModelList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView mImageViewDeviceIcon;
        private TextView mTextViewIpAddress;
        private TextView mTextViewMacAddress;
        private TextView mTextViewDeviceType;
        private RelativeLayout rowMainParentLinearLayout;

        public Holder(View view) {
            super(view);
            mImageViewDeviceIcon = view.findViewById(R.id.image_device_type);
            mTextViewIpAddress = view.findViewById(R.id.text_ip_address);
            mTextViewMacAddress = view.findViewById(R.id.text_mac_address);
            mTextViewDeviceType = view.findViewById(R.id.text_device_type);
            rowMainParentLinearLayout = view.findViewById(R.id.layout_device);

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


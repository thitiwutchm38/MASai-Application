package com.example.bookthiti.masai2.devicediscoveryscreen.device;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.networksearchingscreen.OnRecyclerViewItemClickListener;

import java.util.List;

public class ServiceRecyclerAdapter extends RecyclerView.Adapter<ServiceRecyclerAdapter.Holder>{

    private List<ServiceModel> mServiceModelList;
    private Context mContext;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public ServiceRecyclerAdapter(Context context, List<ServiceModel> serviceModelList) {
        this.mContext = context;
        this.mServiceModelList = serviceModelList;
    }

    @Override
    public ServiceRecyclerAdapter.Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_service, viewGroup, false);
        return new ServiceRecyclerAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(ServiceRecyclerAdapter.Holder Holder, int position) {
        ServiceModel serviceModel = mServiceModelList.get(position);
        Holder.mTextViewPortId.setText(Integer.toString(serviceModel.getPort()));
        Holder.mTextViewPortName.setText(serviceModel.getName());
        Holder.mTextViewProtocol.setText(serviceModel.getProtocol());
        Holder.rowMainParentLinearLayout.setTag(serviceModel);
//        if(position % 2 == 0) {
//           ((RelativeLayout)Holder.device_layout).setBackgroundColor(Color.parseColor("#A0D4EF"));
//        }
//        else
//        {
//            ((RelativeLayout)Holder.device_layout).setBackgroundColor(Color.parseColor("#D3DFEE"));
//        }
    }

    @Override
    public int getItemCount() {
        return mServiceModelList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private TextView mTextViewPortId;
        private TextView mTextViewPortName;
        private TextView mTextViewProtocol;
        private RelativeLayout rowMainParentLinearLayout;
        public Holder(View view) {
            super(view);
            mTextViewPortId = view.findViewById(R.id.textView_result_portnum);
            mTextViewPortName = view.findViewById(R.id.textView_result_portname);
            mTextViewProtocol = view.findViewById(R.id.textView_result_protocal);
            rowMainParentLinearLayout =  view.findViewById(R.id.layout_ports);
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

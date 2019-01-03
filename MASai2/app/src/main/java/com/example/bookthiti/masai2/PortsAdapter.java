package com.example.bookthiti.masai2;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PortsAdapter extends RecyclerView.Adapter<PortsAdapter.Holder>{

    private ArrayList<Ports> mainModelArrayList;


    private Context context;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public PortsAdapter(Context context,ArrayList<Ports> mainModelArrayList) {
        this.context = context;
        this.mainModelArrayList = mainModelArrayList;
    }

    @Override
    public PortsAdapter.Holder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_port_item, viewGroup, false);

        return new PortsAdapter.Holder(view);
    }


    @Override
    public void onBindViewHolder(PortsAdapter.Holder Holder, int position) {
        final Ports offersListModel = mainModelArrayList.get(position);

        Holder.p_num_Text.setText(offersListModel.getP_number());
        Holder.p_name_Text.setText(offersListModel.getP_name());



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


        private TextView p_num_Text;
        private TextView p_name_Text;
        private RelativeLayout device_layout ;





        private RelativeLayout rowMainParentLinearLayout;
        public Holder(View view) {
            super(view);



            p_num_Text = view.findViewById(R.id.textView_result_portnum);
            p_name_Text = view.findViewById(R.id.textView_result_portname);

            device_layout = view.findViewById(R.id.layout_ports);

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

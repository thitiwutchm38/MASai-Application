package com.example.bookthiti.masai2;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PortAttdapter extends RecyclerView.Adapter<PortAttdapter.Holder>{

    private ArrayList<PortAtt> mainModelArrayList;
    private Context context;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;



    public PortAttdapter( Context context, ArrayList<PortAtt> mainModelArrayList) {
        this.context = context;
        this.mainModelArrayList = mainModelArrayList;
    }
    @Override
    public PortAttdapter.Holder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.port_attack_adapter, viewGroup, false);

        return new PortAttdapter.Holder(view);
    }
    @Override
    public void onBindViewHolder( PortAttdapter.Holder Holder, int position) {
        final PortAtt offersListModel = mainModelArrayList.get(position);

        Holder.port_result_name.setText(offersListModel.getPortname());
        Holder.port_result_num.setText(offersListModel.getPortnum());
        Holder.port_result_att_type.setText(offersListModel.getPort_att_type());


        Holder.rowMainParentLinearLayout.setTag(offersListModel);

    }



    @Override
    public int getItemCount() {
        return mainModelArrayList.size();
    }
    public class Holder extends RecyclerView.ViewHolder{

        //private ImageView rowMainImage;
        private TextView port_result_name;
        private TextView port_result_num;
        private TextView port_result_att_type;


        private Button port_att_button;


        private LinearLayout device_layout ;


        private LinearLayout rowMainParentLinearLayout;

        public Holder(View view) {
            super(view);

            port_result_name = view.findViewById(R.id.textView_result_portname);
            port_result_num = view.findViewById(R.id.textView_result_port_num);
            port_result_att_type = view.findViewById(R.id.textView_result_att_type);

            port_att_button =  view.findViewById(R.id.btn_port_att);

            device_layout = view.findViewById(R.id.port_att_lay);

            rowMainParentLinearLayout =  view.findViewById(R.id.lin_port_att);

            port_att_button.setOnClickListener(new View.OnClickListener() {
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


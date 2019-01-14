package com.example.bookthiti.masai2;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CVEAdapter extends RecyclerView.Adapter<CVEAdapter.Holder>{

    private ArrayList<CVE> mainModelArrayList;
    private Context context;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;



    public CVEAdapter( Context context, ArrayList<CVE> mainModelArrayList) {
        this.context = context;
        this.mainModelArrayList = mainModelArrayList;
    }
    @Override
    public CVEAdapter.Holder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cvss_description, viewGroup, false);

        return new CVEAdapter.Holder(view);
    }
    @Override
    public void onBindViewHolder( CVEAdapter.Holder Holder, int position) {
        final CVE offersListModel = mainModelArrayList.get(position);

        Holder.data_cve_Text.setText(offersListModel.getmCVE());
        Holder.data_des_Text.setText(offersListModel.getmPort_des());
        Holder.data_cvss_score_Text.setText(offersListModel.getmCVSS_score());

        //Holder.rowMainImage.setImageResource(offersListModel.getOfferIcon());

        Holder.rowMainParentLinearLayout.setTag(offersListModel);

    }



    @Override
    public int getItemCount() {
        return mainModelArrayList.size();
    }
    public class Holder extends RecyclerView.ViewHolder{

        //private ImageView rowMainImage;
        private TextView data_cve_Text;
        private TextView data_des_Text;
        private TextView data_cvss_score_Text;
        private RelativeLayout device_layout ;





        private RelativeLayout rowMainParentLinearLayout;
        public Holder(View view) {
            super(view);



            data_cve_Text = view.findViewById(R.id.textView_data_ip_address);
            data_des_Text = view.findViewById(R.id.app_data_id);
            data_cvss_score_Text = view.findViewById(R.id.port_data_cvss_score);

            device_layout = view.findViewById(R.id.layout_cve);

            rowMainParentLinearLayout =  view.findViewById(R.id.layout_cve);

//            rowMainParentLinearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (onRecyclerViewItemClickListener != null) {
//                        onRecyclerViewItemClickListener.onItemClick(getAdapterPosition(), view);
//                    }
//                }
//            });
        }
    }
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

}


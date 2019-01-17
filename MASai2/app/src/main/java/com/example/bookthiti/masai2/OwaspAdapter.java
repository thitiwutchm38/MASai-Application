package com.example.bookthiti.masai2;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class OwaspAdapter extends RecyclerView.Adapter<OwaspAdapter.Holder>{
    private ArrayList<OwaspData> mainModelArrayList;
    private Context context;
    private OwaspData offersListModel;

    //private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;


    public OwaspAdapter( Context context, ArrayList<OwaspData> mainModelArrayList) {
        this.context = context;
        this.mainModelArrayList = mainModelArrayList;
    }
    @Override
    public OwaspAdapter.Holder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_owasp_item, viewGroup, false);

        return new OwaspAdapter.Holder(view);
    }
    @Override
    public void onBindViewHolder( OwaspAdapter.Holder Holder, final int position) {
        offersListModel = mainModelArrayList.get(position);

        Holder.mOwaspIcon.setText(offersListModel.getmOwaspNum());
        Holder.mwaspTopic.setText(offersListModel.getOwasptopic());
        Holder.mOwaspDetails.setText(offersListModel.getmOwaspDetail());

        Holder.rowMainParentLinearLayout.setTag(offersListModel);

        Holder.rowMainParentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mIntent = new Intent(context, OwaspMobileActivity.class);
                mIntent.putExtra("M", mainModelArrayList.get(position).getmOwaspNum());
                context.startActivity(mIntent);





            }
        });

    }



    @Override
    public int getItemCount() {
        return mainModelArrayList.size();
    }
    public class Holder extends RecyclerView.ViewHolder{

        TextView mOwaspIcon;
        TextView mwaspTopic;
        TextView mOwaspDetails;

        //RelativeLayout mLayout;



        private RelativeLayout rowMainParentLinearLayout;
        public Holder(View view) {
            super(view);


            mOwaspIcon = view.findViewById(R.id.tvOwaspIcon);

            mwaspTopic = view.findViewById(R.id.tvOwaspTopic);

            mOwaspDetails = view.findViewById(R.id.tvOwaspDetails);


            rowMainParentLinearLayout =  view.findViewById(R.id.layout_owasp);


        }
    }
//    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
//        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
//    }

}


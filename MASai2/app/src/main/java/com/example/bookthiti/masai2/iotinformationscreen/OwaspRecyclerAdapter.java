package com.example.bookthiti.masai2.iotinformationscreen;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bookthiti.masai2.OwaspMobileActivity;
import com.example.bookthiti.masai2.R;

import java.util.List;
import java.util.Random;

import static com.example.bookthiti.masai2.LogConstants.TAG_INFO;

public class OwaspRecyclerAdapter extends RecyclerView.Adapter<OwaspRecyclerAdapter.Holder>{
    private List<OwaspModel> owaspModelList;
    private Context context;

    public OwaspRecyclerAdapter(Context context, List<OwaspModel> owaspModelList) {
        this.context = context;
        this.owaspModelList = owaspModelList;
    }

    @Override
    public OwaspRecyclerAdapter.Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_owasp, viewGroup, false);
        return new OwaspRecyclerAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder Holder, final int position) {
        OwaspModel owaspModel = owaspModelList.get(position);

        Holder.mOwaspIcon.setText(owaspModel.getTopicId());
        Holder.mOwaspTopic.setText(owaspModel.getTopic());
        Holder.mOwaspDetails.setText(owaspModel.getGeneralDetail());
//        Random mRandom = new Random();
//        final int color = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
        String color = String.format("#ff%06X", (0xeeeeee & owaspModel.getTopicId().hashCode()));
        ((GradientDrawable) Holder.mOwaspIcon.getBackground()).setColor(Color.parseColor(color));
//        ((GradientDrawable) Holder.mOwaspIcon.getBackground()).setColor(color);
        Holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OwaspMobileActivity.class);
                intent.putExtra("owaspModel", owaspModelList.get(position));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return owaspModelList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        TextView mOwaspIcon;
        TextView mOwaspTopic;
        TextView mOwaspDetails;
        RelativeLayout mRelativeLayout;

        public Holder(View view) {
            super(view);
            mOwaspIcon = view.findViewById(R.id.tvOwaspIcon);
            mOwaspTopic = view.findViewById(R.id.tvOwaspTopic);
            mOwaspTopic.setSelected(true);
            mOwaspDetails = view.findViewById(R.id.tvOwaspDetails);
            mRelativeLayout = view.findViewById(R.id.layout_owasp);
        }
    }

}


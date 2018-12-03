package com.example.bookthiti.masai2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;


public class NewsAdapter extends RecyclerView.Adapter {


    private List<NewsData> mNewsData;
    private Context mContext;

    public NewsAdapter(Context mContext, List mNewsData) {
        this.mNewsData = mNewsData;
        this.mContext = mContext;
    }

    @Override
    public MailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_news_item,
                parent, false);
        return new MailViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder2, int position) {
        final MailViewHolder holder = (MailViewHolder)holder2;

        holder.mIcon.setText(mNewsData.get(position).getmSender().substring(0, 1));
        holder.mSender.setText(mNewsData.get(position).getmSender());
        holder.mEmailTitle.setText(mNewsData.get(position).getmTitle());
        holder.mEmailDetails.setText(mNewsData.get(position).getmDetails());
        holder.mEmailTime.setText(mNewsData.get(position).getmTime());
        Random mRandom = new Random();
        final int color = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
        ((GradientDrawable) holder.mIcon.getBackground()).setColor(color);

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, DetailActivity.class);
                mIntent.putExtra("sender", holder.mSender.getText().toString());
                mIntent.putExtra("title", holder.mEmailTitle.getText().toString());
                mIntent.putExtra("details", holder.mEmailDetails.getText().toString());
                mIntent.putExtra("time", holder.mEmailTime.getText().toString());
                mIntent.putExtra("icon", holder.mIcon.getText().toString());
                mIntent.putExtra("colorIcon", color);
                mContext.startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNewsData.size();
    }
}
class MailViewHolder extends RecyclerView.ViewHolder {

    TextView mIcon;
    TextView mSender;
    TextView mEmailTitle;
    TextView mEmailDetails;
    TextView mEmailTime;
    RelativeLayout mLayout;


    MailViewHolder(View itemView) {
        super(itemView);

        mIcon = itemView.findViewById(R.id.tvIcon);
        mSender = itemView.findViewById(R.id.tvEmailSender);
        mEmailTitle = itemView.findViewById(R.id.tvEmailTitle);
        mEmailDetails = itemView.findViewById(R.id.tvEmailDetails);
        mEmailTime = itemView.findViewById(R.id.tvEmailTime);
        mLayout = itemView.findViewById(R.id.layout);


    }


}
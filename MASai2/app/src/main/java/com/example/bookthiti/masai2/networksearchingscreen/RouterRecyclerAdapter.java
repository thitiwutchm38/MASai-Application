package com.example.bookthiti.masai2.networksearchingscreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.bookthiti.masai2.R;

public class RouterRecyclerAdapter extends RecyclerView.Adapter<RouterRecyclerAdapter.ViewHolder> {
    private ArrayList<RouterModel> mRouterModelArrayList;
    private Context mContext;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public RouterRecyclerAdapter(Context context, ArrayList<RouterModel> routerModelArrayList) {
        this.mContext = context;
        this.mRouterModelArrayList = routerModelArrayList;
    }

    @Override
    public RouterRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_router, viewGroup, false);
        return new RouterRecyclerAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        RouterModel routerModel = mRouterModelArrayList.get(position);
        viewHolder.mImageViewWifiSignal.setImageResource(routerModel.getIconSignalId());
        viewHolder.mTextViewSsid.setText(routerModel.getSsid());
        viewHolder.mTextViewSecurity.setText(routerModel.getSecurity());
        viewHolder.mTextViewSignal.setText(Float.toString(routerModel.getSignal()));
        viewHolder.mLinearLayout.setTag(routerModel);
    }

    @Override
    public int getItemCount() {
        return mRouterModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewWifiSignal;
        private TextView mTextViewSsid;
        private TextView mTextViewSecurity;
        private TextView mTextViewSignal;
        private LinearLayout mLinearLayout;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(View view) {
            super(view);
            mImageViewWifiSignal = view.findViewById(R.id.image_wifi_signal);
            mTextViewSsid = view.findViewById(R.id.text_ssid);
            mTextViewSecurity = view.findViewById(R.id.text_security);
            mTextViewSignal = view.findViewById(R.id.text_signal);
//            device_lin = view.findViewById(R.id.layout_router);
            mLinearLayout = view.findViewById(R.id.layout_router);
            mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onRecyclerViewItemClickListener != null) {
                        onRecyclerViewItemClickListener.onItemClick(getAdapterPosition(), view);
                    }
                }
            });
            mLinearLayout.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mLinearLayout.setBackgroundColor(Color.RED);
                            break;
                        case MotionEvent.ACTION_UP:
                            //set color back to default
                            mLinearLayout.setBackgroundColor(Color.WHITE);
                            break;
                    }
                    return false;
                }

            });
        }
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;

    }
}
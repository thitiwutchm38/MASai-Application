package com.example.bookthiti.masai2.networksearchingscreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.bookthiti.masai2.utils.OnRecyclerViewItemClickListener;
import com.example.bookthiti.masai2.R;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

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
        viewHolder.itemView.setLongClickable(true);
        RouterModel routerModel = mRouterModelArrayList.get(position);
        viewHolder.mImageViewWifiSignal.setImageResource(routerModel.getIconSignalId());
        viewHolder.mTextViewSsid.setText(routerModel.getSsid());
        if (routerModel.isConnecting()) viewHolder.mTextViewStat.setText("Connecting");
        else
            viewHolder.mTextViewStat.setText("Security: " + routerModel.getSecurity() + " Signal: " + Float.toString(routerModel.getSignal()));
        viewHolder.mConstraintLayout.setTag(routerModel);
    }

    @Override
    public int getItemCount() {
        return mRouterModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewWifiSignal;
        private TextView mTextViewSsid;
        private TextView mTextViewStat;
        private ConstraintLayout mConstraintLayout;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(View view) {
            super(view);
            mImageViewWifiSignal = view.findViewById(R.id.image_wifi_signal);
            mTextViewSsid = view.findViewById(R.id.text_ssid);
            mTextViewStat = view.findViewById(R.id.text_stat);
            mConstraintLayout = view.findViewById(R.id.layout_constraint_router);
//            device_lin = view.findViewById(R.id.layout_router);
            mConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onRecyclerViewItemClickListener != null) {
                        onRecyclerViewItemClickListener.onItemClick(getAdapterPosition(), view);
                    }
                }
            });
            mConstraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                    PopupMenu popupMenu = new PopupMenu(mContext, mConstraintLayout);
                    popupMenu.inflate(R.menu.menu_router_more_option);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch(menuItem.getItemId()) {
                                case R.id.menu_connect:
                                    if (onRecyclerViewItemClickListener != null) {
                                        onRecyclerViewItemClickListener.onItemClick(getAdapterPosition(), view);
                                    }
                                    break;
                                case R.id.menu_info:
                                    BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(view.getContext());
                                    builder.setContentView(R.layout.pop_up_wifi_connect);
                                    builder.setGravity(Gravity.BOTTOM);
                                    builder.setScaleRatio(0.2f);
                                    builder.setBlurRadius(5);
                                    builder.setTintColor(0x3000000);
                                    BlurPopupWindow blurPopupWindow = builder.build();
                                    TextView popupSSID = (TextView) blurPopupWindow.findViewById(R.id.tv_wifi_name_connect);
                                    TextView popupSignal = (TextView) blurPopupWindow.findViewById(R.id.tv_result_signal_wifi_connect);
                                    TextView popupChannel = (TextView) blurPopupWindow.findViewById(R.id.tv_result_chan_wifi_connect);
                                    TextView popupEncrypt= (TextView) blurPopupWindow.findViewById(R.id.tv_result_encrype_wifi_connect);
                                    TextView popupMac = (TextView) blurPopupWindow.findViewById(R.id.tv_result_mac_wifi_connect);
                                    TextView popupMode= (TextView) blurPopupWindow.findViewById(R.id.tv_result_mode_wifi_connect);
                                    popupSSID.setText("Wi-Fi: "+mRouterModelArrayList.get(getAdapterPosition()).getSsid());
                                    popupSignal.setText(Float.toString(mRouterModelArrayList.get(getAdapterPosition()).getSignal()));
                                    popupChannel.setText(Integer.toString(mRouterModelArrayList.get(getAdapterPosition()).getChannel()));
                                    popupEncrypt.setText(mRouterModelArrayList.get(getAdapterPosition()).getSecurity());
                                    popupMac.setText(mRouterModelArrayList.get(getAdapterPosition()).getBssid());
                                    popupMode.setText(mRouterModelArrayList.get(getAdapterPosition()).getMode());
                                    blurPopupWindow.show();
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.setGravity(Gravity.RIGHT);
                    popupMenu.show();
                    return false;
                }
            });

//            mConstraintLayout.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            mConstraintLayout.setBackgroundColor(Color.RED);
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            //set color back to default
//                            mConstraintLayout.setBackgroundColor(Color.WHITE);
//                            break;
//                    }
//                    return false;
//                }
//
//            });
        }
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;

    }
}
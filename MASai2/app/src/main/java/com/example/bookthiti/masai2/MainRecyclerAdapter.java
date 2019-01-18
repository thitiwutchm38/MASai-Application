package com.example.bookthiti.masai2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import android.view.ContextMenu;

import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>{
    private ArrayList<MainModel> mainModelArrayList;
    private Context context;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public MainRecyclerAdapter(Context context,ArrayList<MainModel> mainModelArrayList) {
        this.context = context;
        this.mainModelArrayList = mainModelArrayList;


    }

 
    @Override
    public MainRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_main_adapter, viewGroup, false);
        return new MainRecyclerAdapter.ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(MainRecyclerAdapter.ViewHolder viewHolder, int position) {
        final MainModel offersListModel = mainModelArrayList.get(position);

        viewHolder.rowMainImage.setImageResource(offersListModel.getOfferIcon());

        viewHolder.rowMainText_SSID.setText(offersListModel.getOfferSSID());
        viewHolder.rowMainText_Mode.setText(offersListModel.getOfferMode());
        viewHolder.rowMainText_Signal.setText(offersListModel.getOfferSignal());
        viewHolder.rowMainParentLinearLayout.setTag(offersListModel);

//        int signal = Integer.parseInt(offersListModel.getOfferSignal()) ;
//
//
//        if ((signal <= 100 )&&(signal>=75)){
//
//            viewHolder.device_lin.setBackgroundColor(Color.parseColor("#32CD32"));
//
//
//        }else if((signal<75)&&(signal>=50)) {
//
//            viewHolder.device_lin.setBackgroundColor(Color.parseColor("#9ACD32"));
//
//
//        }else if((signal<50)&&(signal>=25)) {
//
//            viewHolder.device_lin.setBackgroundColor(Color.parseColor("#F0E68C"));
//
//
//        }else if(signal<25) {
//
//            viewHolder.device_lin.setBackgroundColor(Color.parseColor("#E9967A"));
//
//        }





    }
    @Override
    public int getItemCount() {
        return mainModelArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView rowMainImage;

        private TextView rowMainText_SSID;
        private TextView rowMainText_Mode;
        final TextView rowMainText_Signal;
        private LinearLayout device_lin ;

        private LinearLayout rowMainParentLinearLayout;
        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder( View view) {
            super(view);
            rowMainImage = view.findViewById(R.id.row_main_adapter_iv);
            rowMainText_SSID = view.findViewById(R.id.row_main_adapter_ssid);
            rowMainText_Mode =   view.findViewById(R.id.row_main_adapter_mode);
            rowMainText_Signal =   view.findViewById(R.id.row_main_adapter_signal);



            device_lin = view.findViewById(R.id.layout_router);

            rowMainParentLinearLayout =  view.findViewById(R.id.layout_router);






            rowMainParentLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onRecyclerViewItemClickListener != null) {
                        onRecyclerViewItemClickListener.onItemClick(getAdapterPosition(), view);


                    }
                }
            });
            rowMainParentLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                        BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(v.getContext());
                        builder.setContentView(R.layout.pop_up_wifi_connect);
                        builder.setGravity(Gravity.BOTTOM);
                        builder.setScaleRatio(0.2f);
                        builder.setBlurRadius(5);
                        builder.setTintColor(0x3000000);
                        BlurPopupWindow blurPopupWindow = builder.build();
                        blurPopupWindow.show();

                    return false;
                }

            });


//            rowMainParentLinearLayout.setOnTouchListener(new View.OnTouchListener() {
//
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//
//                            // TODO Auto-generated method stub
//                            switch(event.getAction())
//                            {
//                                case MotionEvent.ACTION_DOWN:
//                                    rowMainParentLinearLayout.setBackgroundColor(Color.RED);
//                                    break;
//                                case MotionEvent.ACTION_UP:
//
//                                    //set color back to default
//                                    rowMainParentLinearLayout.setBackgroundColor(Color.WHITE);
//                                    break;
//                            }
//                            return false;
//                        }
//
//            });



        }
    }
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;

    }


}
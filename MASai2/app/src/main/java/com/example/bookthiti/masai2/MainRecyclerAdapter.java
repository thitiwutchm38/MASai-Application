package com.example.bookthiti.masai2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
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
    }
    @Override
    public int getItemCount() {
        return mainModelArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView rowMainImage;

        private TextView rowMainText_SSID;
        private TextView rowMainText_Mode;
        private TextView rowMainText_Signal;
        private Button rowMainButton_button;

        private RelativeLayout rowMainParentLinearLayout;
        public ViewHolder(View view) {
            super(view);
            rowMainImage = view.findViewById(R.id.row_main_adapter_iv);
            rowMainText_SSID = view.findViewById(R.id.row_main_adapter_ssid);
            rowMainText_Mode =   view.findViewById(R.id.row_main_adapter_mode);
            rowMainText_Signal =   view.findViewById(R.id.row_main_adapter_signal);
            rowMainButton_button = (Button)view.findViewById(R.id.btn_router);


            rowMainParentLinearLayout =  view.findViewById(R.id.row_main_adapter_linear_layout);

                rowMainButton_button.setOnClickListener(new View.OnClickListener() {
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
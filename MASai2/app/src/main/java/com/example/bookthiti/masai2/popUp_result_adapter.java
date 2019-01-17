package com.example.bookthiti.masai2;

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

public class popUp_result_adapter extends RecyclerView.Adapter<popUp_result_adapter.ViewHolder>{

    private ArrayList<popUp_result> mainModelArrayList;
    private Context context;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public popUp_result_adapter( Context context, ArrayList<popUp_result> mainModelArrayList) {

        this.context = context;
        this.mainModelArrayList = mainModelArrayList;
    }
    @Override
    public popUp_result_adapter.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_popup_result, viewGroup, false);
        return new popUp_result_adapter.ViewHolder(view);

    }
    @Override
    public void onBindViewHolder( popUp_result_adapter.ViewHolder viewHolder, int position) {

        final popUp_result offersListModel = mainModelArrayList.get(position);


        viewHolder.textView_problem_result.setText(offersListModel.getProblem());
        viewHolder.textView_OWAPS_num.setText(offersListModel.getOwasp_num());

        viewHolder.rowMainParentLinearLayout.setTag(offersListModel);



    }
    @Override
    public int getItemCount() {
        return mainModelArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{


        // Pop up content

        private TextView textView_problem_result;
        private TextView textView_OWAPS_num;

        //private LinearLayout device_lin ;
        private LinearLayout rowMainParentLinearLayout;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder( View view) {
            super(view);

            textView_problem_result = view.findViewById(R.id.textView_problem_result);

            textView_OWAPS_num =   view.findViewById(R.id.textView_OWAPS_num);


            //device_lin = view.findViewById(R.id.layout_pop_up_result);

            rowMainParentLinearLayout =  view.findViewById(R.id.layout_pop_up_result);


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
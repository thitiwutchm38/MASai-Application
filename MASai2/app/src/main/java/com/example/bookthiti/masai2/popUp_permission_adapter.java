package com.example.bookthiti.masai2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class popUp_permission_adapter extends RecyclerView.Adapter<popUp_permission_adapter.ViewHolder>{

    private ArrayList<PermissionModel> mainModelArrayList;
    private Context context;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public popUp_permission_adapter( Context context, ArrayList<PermissionModel> mainModelArrayList) {

        this.context = context;
        this.mainModelArrayList = mainModelArrayList;
    }

    @Override
    public popUp_permission_adapter.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_popup_permission_result, viewGroup, false);
        return new popUp_permission_adapter.ViewHolder(view);

    }
    @Override
    public void onBindViewHolder( popUp_permission_adapter.ViewHolder viewHolder, int position) {

        final PermissionModel offersListModel = mainModelArrayList.get(position);
        String colorString = null;

        //viewHolder.textView_problem_result.setText(offersListModel.getProblem());
       // viewHolder.textView_OWAPS_num.setText(offersListModel.getOwasp_num()+" - "+offersListModel.getProblem());

        viewHolder.mtextViewInfo.setText(offersListModel.getInformation());
        viewHolder.mtextDescription.setText(offersListModel.getDescription());

        viewHolder.rowMainParentLinearLayout.setTag(offersListModel);



        switch(offersListModel.getOwasp_num()) {
            case "M1":
                colorString = "#0066ff";
                break;
            case "M2":
                colorString = "#00cc66";
                break;
            case "M3":
                colorString = "#9900ff";
                break;
            case "M4":
                colorString = "#ff6666";
                break;
            case "M5":
                colorString = "#666633";
                break;
            case "M6":
                colorString = "#0066ff";
                break;
            case "M7":
                colorString = "#ff6699";
                break;
            case "M8":
                colorString = "#0099cc";
                break;
            case "M9":
                colorString = "#333399";
                break;
            case "M10":
                colorString = "#804000";
                break;

            default:
        }

 //       viewHolder.textView_OWAPS_num.setTextColor(Color.parseColor(colorString));
//        viewHolder.mtextViewInfo.setTextColor(Color.parseColor(colorString));
//        viewHolder.mtextDescription.setTextColor(Color.parseColor(colorString));

    }
    @Override
    public int getItemCount() {
        return mainModelArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{


        // Pop up content

        //private TextView textView_problem_result;


    //    private TextView textView_OWAPS_num;
        private TextView mtextViewInfo;
        private TextView mtextDescription;


        //private LinearLayout device_lin ;
        private LinearLayout rowMainParentLinearLayout;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder( View view) {
            super(view);

            //textView_problem_result = view.findViewById(R.id.textView_problem_result);

        //    textView_OWAPS_num = view.findViewById(R.id.textView_OWAPS_num);

            mtextViewInfo = view.findViewById(R.id.textView_info_detail);

            mtextDescription = view.findViewById(R.id.textView_description_detail);


            //device_lin = view.findViewById(R.id.layout_pop_up_result);

            rowMainParentLinearLayout =  view.findViewById(R.id.layout_pop_up_permission_result);

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
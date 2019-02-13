package com.example.bookthiti.masai2.mobileapplicationscanningscreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bookthiti.masai2.AndroidRuleModel;
import com.example.bookthiti.masai2.OnRecyclerViewItemClickListener;
import com.example.bookthiti.masai2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AndroidFindingRecyclerAdapter extends RecyclerView.Adapter<AndroidFindingRecyclerAdapter.ViewHolder>{

    private List<AppVulnerability> appVulnerabilityList;
    private Context context;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public AndroidFindingRecyclerAdapter(Context context, List<AppVulnerability> appVulnerabilityList) {
        this.context = context;
        this.appVulnerabilityList = appVulnerabilityList;
    }

    @Override
    public AndroidFindingRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_popup_android_rule_result, viewGroup, false);
        return new AndroidFindingRecyclerAdapter.ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(AndroidFindingRecyclerAdapter.ViewHolder viewHolder, int position) {

        AppVulnerability appVulnerability = appVulnerabilityList.get(position);
        viewHolder.textView_problem_result.setText(appVulnerability.getTitle());
        Pattern pattern = Pattern.compile("\\[([MI]\\d+).+\\]");
        Matcher matcher = pattern.matcher(appVulnerability.getOwaspId());
        if (matcher.matches()) {
            viewHolder.textView_OWAPS_num.setText(matcher.group(1));
        }

    }

    @Override
    public int getItemCount() {
        return appVulnerabilityList.size();
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
            textView_OWAPS_num =  view.findViewById(R.id.textView_OWAPS_num);
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
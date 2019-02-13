package com.example.bookthiti.masai2.mobileapplicationscanningscreen;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import java.util.ArrayList;
import java.util.List;

public class ContainerAndroidFindingOnClickListener implements View.OnClickListener {

    private Context context;
    private List<AppVulnerability> appVulnerabilityFromLevelList;
    private String level;

    public ContainerAndroidFindingOnClickListener(Context context, List<AppVulnerability> appVulnerabilityList, String level) {
        this.context = context;
        this.level = level;
        this.setAppVulnerabilityFromLevelList(appVulnerabilityList, level);
    }


    @Override
    public void onClick(View view) {
        BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(context);
        builder.setContentView(R.layout.pop_up_android_rule_result);
        builder.setGravity(Gravity.CENTER);
        builder.setScaleRatio(0.2f);
        builder.setBlurRadius(10);
        builder.setTintColor(0x3000000);
        BlurPopupWindow blurPopupWindow = builder.build();

        RecyclerView recyclerView = blurPopupWindow.findViewById(R.id.recycle_popup_list);
        LinearLayout popup = (LinearLayout)blurPopupWindow.findViewById(R.id.relativeLayout_popup);

        popup.setBackgroundResource(R.drawable.shape_score);

        TextView textViewNumResult = (TextView) blurPopupWindow.findViewById(R.id.textView_popup_show_found);
        textViewNumResult.setText(level + " Level: " + appVulnerabilityFromLevelList.size() +" Founded");
        popup.setBackgroundColor(Color.parseColor("#FFFFFF"));

        //Toast.makeText(mContext, ""+recyclerView, Toast.LENGTH_SHORT).show();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        AndroidFindingRecyclerAdapter androidFindingRecyclerAdapter = new AndroidFindingRecyclerAdapter(context, appVulnerabilityFromLevelList);
        recyclerView.setAdapter(androidFindingRecyclerAdapter);
        blurPopupWindow.show();
    }

    private void setAppVulnerabilityFromLevelList(List<AppVulnerability> appVulnerabilities, String level) {
        appVulnerabilityFromLevelList = new ArrayList<AppVulnerability>();
        for (AppVulnerability appVulnerability: appVulnerabilities) {
            if(appVulnerability.getLevel().equals(level)) {
                appVulnerabilityFromLevelList.add(appVulnerability);
            }
        }
    }
}

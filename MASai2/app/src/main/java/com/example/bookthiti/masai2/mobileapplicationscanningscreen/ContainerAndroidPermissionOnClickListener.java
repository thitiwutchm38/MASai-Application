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

public class ContainerAndroidPermissionOnClickListener implements View.OnClickListener {

    private Context context;
    private List<Permission> permissionListFromLevel;
    private String level;

    public ContainerAndroidPermissionOnClickListener(Context context, List<Permission> permissionList, String level) {
        this.context = context;
        this.level = level;
        setPermissionList(permissionList, level);
    }

    @Override
    public void onClick(View view) {
        BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(context);
        builder.setContentView(R.layout.pop_up_permission_result);
        builder.setGravity(Gravity.CENTER);
        builder.setScaleRatio(0.2f);
        builder.setBlurRadius(5);
        builder.setTintColor(0x3000000);
        BlurPopupWindow blurPopupWindow = builder.build();

        RecyclerView recyclerView = blurPopupWindow.findViewById(R.id.recycle_popup_permission_list);

        LinearLayout popup = (LinearLayout) blurPopupWindow.findViewById(R.id.relativeLayout_popup);
        popup.setBackgroundResource(R.drawable.shape_score);

        TextView textViewNumResult = (TextView) blurPopupWindow.findViewById(R.id.textView_popup_show_found);

        textViewNumResult.setText(level.toUpperCase() + ": " + permissionListFromLevel.size() + " permissions found");

        popup.setBackgroundColor(Color.parseColor("#FFFFFF"));

        //Toast.makeText(mContext, ""+recyclerView, Toast.LENGTH_SHORT).show();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        AndroidPermissionRecyclerAdapter androidPermissionRecyclerAdapter = new AndroidPermissionRecyclerAdapter(context, permissionListFromLevel);

//        mainRecyclerPAdapter.setOnRecyclerViewItemClickListener(ScanResultActivity.this);
        recyclerView.setAdapter(androidPermissionRecyclerAdapter);
        blurPopupWindow.show();
    }

    private void setPermissionList(List<Permission> permissionList, String level) {
        this.permissionListFromLevel = new ArrayList<Permission>();
        for (Permission permission : permissionList) {
            if(permission.getLevel().equals(level)) {
                permissionListFromLevel.add(permission);
            }
        }
    }
}

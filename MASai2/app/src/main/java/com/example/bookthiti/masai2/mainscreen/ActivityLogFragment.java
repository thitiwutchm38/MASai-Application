package com.example.bookthiti.masai2.mainscreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.database.model.ActivityLogEntity;

public class ActivityLogFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM_ACTIVTY_LOG = "param_activity_log";

//    private String mParam1;
//    private String mParam2;
    private ActivityLogEntity activityLogEntity;

    private TextView textViewActivityTitle;
    private TextView textViewActivityStatus;
    private TextView textViewActivityStartAt;
    private TextView textViewActivityFinishAt;
    private TextView textViewSeeDetails;

    public ActivityLogFragment() {
        // Required empty public constructor
    }

    public static ActivityLogFragment newInstance(ActivityLogEntity activityLogEntity) {
        ActivityLogFragment fragment = new ActivityLogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_ACTIVTY_LOG, activityLogEntity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            activityLogEntity = getArguments().getParcelable(ARG_PARAM_ACTIVTY_LOG);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_log, container, false);
        textViewActivityTitle = view.findViewById(R.id.text_activity_title);
        textViewActivityStatus = view.findViewById(R.id.text_activity_status);
        textViewActivityStartAt = view.findViewById(R.id.text_activity_start_at);
        textViewActivityFinishAt = view.findViewById(R.id.text_activity_finish_at);
        textViewSeeDetails = view.findViewById(R.id.text_btn_see_details);

        textViewActivityTitle.setText(activityLogEntity.getName());
        textViewActivityStatus.setText(activityLogEntity.getStatus());
        textViewActivityStartAt.setText(activityLogEntity.getStartTime().toString());
        if (activityLogEntity.getFinishTime() == null) {
            textViewActivityFinishAt.setText("-");
        } else {
            textViewActivityFinishAt.setText(activityLogEntity.getFinishTime().toString());
        }

        return view;
    }

}

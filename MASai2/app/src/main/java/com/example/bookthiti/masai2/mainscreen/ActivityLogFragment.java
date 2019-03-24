package com.example.bookthiti.masai2.mainscreen;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.bluetoothservice.INotificationId;
import com.example.bookthiti.masai2.database.model.ActivityLogEntity;
import com.example.bookthiti.masai2.deviceassessmentscreen.DeviceAssessmentActivity;
import com.example.bookthiti.masai2.devicediscoveryscreen.DeviceDiscoveryActivity;
import com.example.bookthiti.masai2.portattackscreen.PortAttackActivity;
import com.example.bookthiti.masai2.routercrackingscreen.CrackRouterActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ActivityLogFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM_ACTIVTY_LOG = "param_activity_log";

//    private String mParam1;
//    private String mParam2;
    private ActivityLogEntity activityLogEntity;
    private Context context;

    private TextView textViewActivityTitle;
    private TextView textViewActivityStatus;
    private TextView textViewActivityStartAt;
    private TextView textViewActivityFinishAt;
    private TextView textViewSeeDetails;
    private ImageView imageViewIcon;

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
        context = getContext();
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
        imageViewIcon = view.findViewById(R.id.image_activity_icon);

        textViewActivityTitle.setText(activityLogEntity.getName());
        textViewActivityStatus.setText(activityLogEntity.getStatus());
        textViewActivityStartAt.setText(activityLogEntity.getStartTime().toString());
        if (activityLogEntity.getFinishTime() == null) {
            textViewActivityFinishAt.setText("-");
        } else {
            textViewActivityFinishAt.setText(activityLogEntity.getFinishTime().toString());
        }

        // Set Icon of each activity
        switch (activityLogEntity.getName()) {
            case "Router Cracking Testing":
                imageViewIcon.setImageResource(R.drawable.router_pen);
                break;
            case "Device Discovery":
                imageViewIcon.setImageResource(R.drawable.iot_device_attack);
                break;
            case "Service Attacking Testing":
                imageViewIcon.setImageResource(R.drawable.icons_terminal);
                break;
            case "Device Assessment":
                imageViewIcon.setImageResource(R.drawable.device1_pen);
                break;
        }

        textViewSeeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String jsonOutput = activityLogEntity.getJsonOutput();
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(jsonOutput).getAsJsonObject();
//                String resultType = jsonObject.get("resultType").getAsString();
                String payload = gson.toJson(jsonObject.get("payload"));
                bundle.putString("payload", payload);
                intent.putExtras(bundle);
                intent.setAction(Intent.ACTION_MAIN);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(INotificationId.FLAG_IS_FROM_NOTIFICATION, true);
                switch (activityLogEntity.getName()) {
                    case "Router Cracking Testing":
                        intent.setClass(context, CrackRouterActivity.class);
                        startActivity(intent);
                        break;
                    case "Device Discovery":
                        intent.setClass(context, DeviceDiscoveryActivity.class);
                        startActivity(intent);
                        break;
                    case "Service Attacking Testing":
                        intent.setClass(context, PortAttackActivity.class);
                        startActivity(intent);
                        break;
                    case "Device Assessment":
                        intent.setClass(context, DeviceAssessmentActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        return view;
    }

}

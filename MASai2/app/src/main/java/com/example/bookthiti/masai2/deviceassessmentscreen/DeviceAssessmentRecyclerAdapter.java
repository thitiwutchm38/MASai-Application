package com.example.bookthiti.masai2.deviceassessmentscreen;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.CVEModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.CVERecyclerAdapter;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.ServiceModel;

import java.util.List;

public class DeviceAssessmentRecyclerAdapter extends RecyclerView.Adapter<DeviceAssessmentRecyclerAdapter.Holder>{
    private List<ServiceModel> mServiceModelList;
    private Context mContext;

    public DeviceAssessmentRecyclerAdapter(Context context, List<ServiceModel> serviceModelList) {
        this.mContext = context;
        this.mServiceModelList = serviceModelList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_service_asessment, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ServiceModel serviceModel = mServiceModelList.get(position);
        List<CVEModel> cveModels = serviceModel.getCves();
        float maxCvss = 0.0f, minCvss = 0.0f, averageCvss = 0.0f;
        float max = Float.MIN_VALUE, min = Float.MAX_VALUE;
        for(CVEModel cveModel : cveModels) {
            float cvss = cveModel.getSeverity().getCvssList().get(0).getBase();
            averageCvss += cvss;
            if(cvss > max) {
                maxCvss = cvss;
                max = maxCvss;
            }
            if(cvss < min) {
                minCvss = cvss;
                min = minCvss;
            }
        }
        if(cveModels.size() != 0) {
            averageCvss /= cveModels.size();
        } else {
            holder.mImageButtonMore.setVisibility(View.INVISIBLE);
        }

        holder.mTextViewTitle.setText(Integer.toString(serviceModel.getPort()) + "/" + serviceModel.getName());
        holder.mTextViewMaxCvss.setText(Float.toString(maxCvss));
        holder.mTextViewMinCvss.setText(Float.toString(minCvss));
        holder.mTextViewAverageCvss.setText(Float.toString(averageCvss));

        if (averageCvss > 0) {
            holder.mTextViewStatus.setText("( Dangerous )");
            holder.mTextViewStatus.setTextColor(Color.RED);
        }

//        switch(serviceModel.getPort()) {
//            case 23:
//                holder.mTextViewStatus.setText("( Dangerous )");
//                holder.mTextViewStatus.setTextColor(Color.RED);
//                break;
//            case 80:
//                holder.mTextViewStatus.setText("( Dangerous )");
//                holder.mTextViewStatus.setTextColor(Color.RED);
//                break;
//            case 20:
//                holder.mTextViewStatus.setText("( Dangerous )");
//                holder.mTextViewStatus.setTextColor(Color.RED);
//                break;
//
//            case 21:
//                holder.mTextViewStatus.setText("( Dangerous )");
//                holder.mTextViewStatus.setTextColor(Color.RED);
//                break;
//
//            case 389:
//                holder.mTextViewStatus.setText("( Dangerous )");
//                holder.mTextViewStatus.setTextColor(Color.RED);
//                break;
//
//            case 88:
//                holder.mTextViewStatus.setText("( Dangerous )");
//                holder.mTextViewStatus.setTextColor(Color.RED);
//                break;
//
//
//
//            default:
//        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        holder.mRecyclerViewCves.setLayoutManager(linearLayoutManager);
        CVERecyclerAdapter adapter = new CVERecyclerAdapter(mContext, cveModels);
        holder.mRecyclerViewCves.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return mServiceModelList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewStatus;
        private TextView mTextViewMaxCvss;
        private TextView mTextViewMinCvss;
        private TextView mTextViewAverageCvss;
        private ImageButton mImageButtonMore;
        private RecyclerView mRecyclerViewCves;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.text_title);
            mTextViewStatus = itemView.findViewById(R.id.text_status);
            mTextViewMaxCvss = itemView.findViewById(R.id.text_max_cvss);
            mTextViewMinCvss = itemView.findViewById(R.id.text_min_cvss);
            mTextViewAverageCvss = itemView.findViewById(R.id.text_average_cvss);
            mImageButtonMore = itemView.findViewById(R.id.button_more);
            mRecyclerViewCves = itemView.findViewById(R.id.recycler_cves);
            mImageButtonMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRecyclerViewCves.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}

package com.example.bookthiti.masai2.devicediscoveryscreen.device;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;

import java.util.List;

public class CVERecyclerAdapter extends RecyclerView.Adapter<CVERecyclerAdapter.Holder> {

    private List<CVEModel> mCVEModelList;
    private Context mContext;

    public CVERecyclerAdapter(Context context, List<CVEModel> cveModelList) {
        this.mContext = context;
        this.mCVEModelList = cveModelList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cve, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        CVEModel cveModel = mCVEModelList.get(position);
        holder.mTextViewCVEId.setText(cveModel.getId());
        holder.mTextViewCVEDescription.setText(cveModel.getDescription());
        CVEModel.Severity severity = cveModel.getSeverity();
        holder.mTextViewSeverity.setText(severity.getSeverity());
        CVEModel.CVSS cvss = severity.getCvssList().get(0);
        holder.mTextViewCVSS.setText("base score: " + Float.toString(cvss.getBase()) + " vector: " + cvss.getVector());
    }

    @Override
    public int getItemCount() {
        return mCVEModelList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView mTextViewCVEId;
        private TextView mTextViewCVEDescription;
        private TextView mTextViewSeverity;
        private TextView mTextViewCVSS;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mTextViewCVEId = itemView.findViewById(R.id.text_cve);
            mTextViewCVEDescription = itemView.findViewById(R.id.text_cve_description);
            mTextViewSeverity = itemView.findViewById(R.id.text_severity);
            mTextViewCVSS = itemView.findViewById(R.id.text_cvss);
        }
    }
}

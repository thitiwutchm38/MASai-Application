package com.example.bookthiti.masai2.iotinformationscreen;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookthiti.masai2.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.bookthiti.masai2.utils.LogConstants.TAG_INFO;

public class OwaspFragment extends Fragment {

    private String category;
    private RecyclerView mRecyclerView;
    private List<OwaspModel> mOwaspList = new ArrayList<OwaspModel>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_owasp_information, container,false);
        mRecyclerView  = view.findViewById(R.id.rv_owasp_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mOwaspList = getOwaspList(this.category);

        OwaspRecyclerAdapter mOwaspRecyclerAdapter = new OwaspRecyclerAdapter(getContext(), mOwaspList);
        mRecyclerView.setAdapter(mOwaspRecyclerAdapter);

        return view;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private List<OwaspModel> getOwaspList(String category) {
        List<OwaspModel> owaspModelList = new ArrayList<OwaspModel>();
        Resources res = getResources();
        String[] topicIds = new String[0];
        String[] topics = new String[0];
        String[] details = new String[0];
        String[] examples = new String[0];
        String[] guidelines = new String[0];
        Log.i(TAG_INFO, category);
        if ("IoT".equals(category)) {
            topicIds = res.getStringArray(R.array.i_topic_id);
            topics = res.getStringArray(R.array.i_topic);
            details = res.getStringArray(R.array.i_detail);
            examples = res.getStringArray(R.array.i_example);
            guidelines = res.getStringArray(R.array.i_guideline);
        } else if ("Mobile".equals(category)) {
            topicIds = res.getStringArray(R.array.m_topic_id);
            topics = res.getStringArray(R.array.m_topic);
            details = res.getStringArray(R.array.m_detail);
            examples = res.getStringArray(R.array.m_example);
            guidelines = res.getStringArray(R.array.m_guideline);
        }
        for (int i = 0; i < 10; i++) {
            OwaspModel owaspModel = new OwaspModel(topicIds[i], topics[i], details[i], examples[i], guidelines[i],0);
            owaspModelList.add(owaspModel);
        }
        return owaspModelList;
    }
}
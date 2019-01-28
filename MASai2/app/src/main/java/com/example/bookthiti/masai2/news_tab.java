package com.example.bookthiti.masai2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class news_tab extends Fragment {
    RecyclerView mRecyclerView;
    ArrayList<OwaspData> mOwaspList = new ArrayList<>();



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view =  inflater.inflate(R.layout.tips_tab,container,false);


            mRecyclerView  = view.findViewById(R.id.recyclerView_Owasp);

            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                    DividerItemDecoration.VERTICAL));

            mRecyclerView.setLayoutManager(mLinearLayoutManager);


            String m1 = getResources().getString(R.string.I1);
            String m2 = getResources().getString(R.string.I2);
            String m3 = getResources().getString(R.string.I3);
            String m4 = getResources().getString(R.string.I4);
            String m5 = getResources().getString(R.string.I5);
            String m6 = getResources().getString(R.string.I6);
            String m7 = getResources().getString(R.string.I7);
            String m8 = getResources().getString(R.string.I8);
            String m9 = getResources().getString(R.string.I9);
            String m10 = getResources().getString(R.string.I10);


            OwaspData owasp = new OwaspData("I1", "Insecure Web Interface", m1);

            mOwaspList.add(owasp);

            owasp = new OwaspData("I2", "Insufficient Authentication/Authorization",m2);
            mOwaspList.add(owasp);
            owasp = new OwaspData("I3", "Insecure Network Services",m3);
            mOwaspList.add(owasp);

            owasp = new OwaspData("I4", "Lack of Transport Encryption",m4);
            mOwaspList.add(owasp);

            owasp = new OwaspData("I5", "Privacy Concerns",m5);
            mOwaspList.add(owasp);

            owasp = new OwaspData("I6", "Insecure Cloud Interface",m6);
            mOwaspList.add(owasp);

            owasp = new OwaspData("I7", "Insecure Mobile Interface",m7);
            mOwaspList.add(owasp);


            owasp = new OwaspData("I8", "Insufficient Security Configurability",m8);
            mOwaspList.add(owasp);


            owasp = new OwaspData("I9", "Insecure Software/Firmware",m9);
            mOwaspList.add(owasp);


            owasp = new OwaspData("I10", "Poor Physical Security",m10);
            mOwaspList.add(owasp);




            OwaspAdapter mOwaspAdapter = new OwaspAdapter(getContext(), mOwaspList);
            mRecyclerView.setAdapter(mOwaspAdapter);



            return view;
        }

    }

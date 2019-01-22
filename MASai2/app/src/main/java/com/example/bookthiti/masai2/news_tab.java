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


            String m1 = getResources().getString(R.string.M1);
            String m2 = getResources().getString(R.string.M2);
            String m3 = getResources().getString(R.string.M3);
            String m4 = getResources().getString(R.string.M4);
            String m5 = getResources().getString(R.string.M5);
            String m6 = getResources().getString(R.string.M6);
            String m7 = getResources().getString(R.string.M7);
            String m8 = getResources().getString(R.string.M8);
            String m9 = getResources().getString(R.string.M9);
            String m10 = getResources().getString(R.string.M10);


            OwaspData owasp = new OwaspData("I1", "Improper Platform Usage", m1);

            mOwaspList.add(owasp);

            owasp = new OwaspData("I2", "Insecure Data Storage",m2);
            mOwaspList.add(owasp);
            owasp = new OwaspData("I3", "Insecure Communication",m3);
            mOwaspList.add(owasp);

            owasp = new OwaspData("I4", "Insecure Authentication",m4);
            mOwaspList.add(owasp);

            owasp = new OwaspData("I5", "Insufficient Cryptography",m5);
            mOwaspList.add(owasp);

            owasp = new OwaspData("I6", "Insecure Authorization",m6);
            mOwaspList.add(owasp);

            owasp = new OwaspData("I7", "Client Code Quality",m7);
            mOwaspList.add(owasp);


            owasp = new OwaspData("I8", "Code Tampering",m8);
            mOwaspList.add(owasp);


            owasp = new OwaspData("I9", "Reverse Engineering",m9);
            mOwaspList.add(owasp);


            owasp = new OwaspData("I10", "Extraneous Functionality",m10);
            mOwaspList.add(owasp);




            OwaspAdapter mOwaspAdapter = new OwaspAdapter(getContext(), mOwaspList);
            mRecyclerView.setAdapter(mOwaspAdapter);



            return view;
        }

    }

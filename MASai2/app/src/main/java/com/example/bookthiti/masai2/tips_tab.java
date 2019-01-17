package com.example.bookthiti.masai2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class tips_tab extends Fragment {


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


        OwaspData owasp = new OwaspData("M1", "Improper Platform Usage", m1);

        mOwaspList.add(owasp);

        owasp = new OwaspData("M2", "Insecure Data Storage",m2);
        mOwaspList.add(owasp);
        owasp = new OwaspData("M3", "Insecure Communication",m3);
        mOwaspList.add(owasp);

        owasp = new OwaspData("M4", "Insecure Authentication",m4);
        mOwaspList.add(owasp);

        owasp = new OwaspData("M5", "Insufficient Cryptography",m5);
        mOwaspList.add(owasp);

        owasp = new OwaspData("M6", "Insecure Authorization",m6);
        mOwaspList.add(owasp);

        owasp = new OwaspData("M7", "Client Code Quality",m7);
        mOwaspList.add(owasp);


        owasp = new OwaspData("M8", "Code Tampering",m8);
        mOwaspList.add(owasp);


        owasp = new OwaspData("M9", "Reverse Engineering",m9);
        mOwaspList.add(owasp);


        owasp = new OwaspData("M10", "Extraneous Functionality",m10);
        mOwaspList.add(owasp);




        OwaspAdapter mOwaspAdapter = new OwaspAdapter(getContext(), mOwaspList);
        mRecyclerView.setAdapter(mOwaspAdapter);



        return view;
    }



}
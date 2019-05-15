package com.example.bookthiti.masai2.mainscreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookthiti.masai2.R;

public class NoActivityFragment extends Fragment {
    public NoActivityFragment() {
        // Required empty public constructor
    }

    public static NoActivityFragment newInstance(){ //String param1, String param2) {
        NoActivityFragment fragment = new NoActivityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_activity, container, false);
    }

}

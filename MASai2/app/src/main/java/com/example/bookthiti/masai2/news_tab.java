package com.example.bookthiti.masai2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class news_tab extends Fragment {

    RecyclerView mRecyclerView;
    List<NewsData> mEmailData = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_tab, container, false);
        mRecyclerView  = rootView.findViewById(R.id.recyclerView_news);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        NewsData mNews = new NewsData("Sam", "Weekend adventure",
                "Let's go fishing with John and others. We will do some barbecue and have soo much fun",
                "10:42am");
        mEmailData.add(mNews);
        mNews = new NewsData("Facebook", "James, you have 1 new notification",
                "A lot has happened on Facebook since",
                "16:04pm");
        mEmailData.add(mNews);
        mNews = new NewsData("Google+", "Top suggested Google+ pages for you",
                "Top suggested Google+ pages for you",
                "18:44pm");
        mEmailData.add(mNews);
        mNews = new NewsData("Twitter", "Follow T-Mobile, Samsung Mobile U",
                "James, some people you may know",
                "20:04pm");
        mEmailData.add(mNews);
        mNews = new NewsData("Pinterest Weekly", "Pins you’ll love!",
                "Have you seen these Pins yet? Pinterest",
                "09:04am");
        mEmailData.add(mNews);
        mNews = new NewsData("Josh", "Going lunch", "Don't forget our lunch at 3PM in Pizza hut",
                "01:04am");
        mEmailData.add(mNews);

        NewsAdapter mMailAdapter = new NewsAdapter(getContext(), mEmailData);
        mRecyclerView.setAdapter(mMailAdapter);


        return rootView;
    }
}

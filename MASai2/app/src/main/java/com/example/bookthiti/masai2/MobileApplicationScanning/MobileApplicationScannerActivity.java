package com.example.bookthiti.masai2.MobileApplicationScanning;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.bookthiti.masai2.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileApplicationScannerActivity extends AppCompatActivity {
    private final static String TAG_INFO = "Log info";
    private final static String TAG_DEBUG = "Log debug";
    private final static String TAG_ERROR = "Log error";

    private Context mContext;
    private Button mSelectFromGooglePlayButton;
    private Menu mMenu;
    private SearchView mSearchView;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile__appscan);

        setTitle("Mobile App Scan");
        mContext = this.getApplicationContext();
//        hideKeyboard();
        mSelectFromGooglePlayButton = (Button) findViewById(R.id.button_select_from_google_play);
        mSelectFromGooglePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenu.findItem(R.id.search_google_play).setVisible(true);
                mMenu.findItem(R.id.search_google_play_icon).setVisible(true);
//                mSearchView= (SearchView) mMenu.findItem(R.id.search_google_play).getActionView();
                flag = !flag;
                if(flag) {
                    mSearchView.setIconified(false);
                    mSearchView.setVisibility(View.VISIBLE);
                    Log.i(TAG_INFO, "" + mSearchView.requestFocus());
                } else {
                    mSearchView.setVisibility(View.GONE);
                    mMenu.findItem(R.id.search_google_play_icon).setVisible(false);
                    mMenu.findItem(R.id.search_google_play).setVisible(false);
                }
            }
        });

//        MasaiServerAPI masaiServerAPI = RetrofitClientInstance.getRetrofitInstance().create(MasaiServerAPI.class);
//        Call<List<TargetApplicationInfo>> listCall = masaiServerAPI.getAllTargetApplicationInfo("koogeek");
//        listCall.enqueue(new Callback<List<TargetApplicationInfo>>() {
//            @Override
//            public void onResponse(Call<List<TargetApplicationInfo>> call, Response<List<TargetApplicationInfo>> response) {
//                List<TargetApplicationInfo> targetApplicationInfos = response.body();
//                Log.i(TAG_INFO, "Get response");
//                for(TargetApplicationInfo targetApplicationInfo:targetApplicationInfos) {
//                    Log.i(TAG_INFO, "appId: " + targetApplicationInfo.getAppId() + ", appName: " + targetApplicationInfo.getAppName());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<TargetApplicationInfo>> call, Throwable t) {
//                Toast.makeText(mContext, "Get failure response", Toast.LENGTH_SHORT).show();
//                Log.i(TAG_INFO, t.getMessage());
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_google_play_search, menu);
        mMenu = menu;
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView =
                (SearchView) menu.findItem(R.id.search_google_play).getActionView();
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
//        ActionBar.LayoutParams params = new ActionBar.LayoutParams();
//        searchView.setLayoutParams(params);
        mSearchView.setVisibility(View.GONE);
        mSearchView.setIconified(false);
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSearchView.setIconified(false);
                mSearchView.setVisibility(View.GONE);
                mMenu.findItem(R.id.search_google_play_icon).setVisible(false);
                mMenu.findItem(R.id.search_google_play).setVisible(false);
                flag = false;
                return true;
            }
        });

        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    flag = false;
                    mSearchView.setVisibility(View.GONE);
                    mMenu.findItem(R.id.search_google_play_icon).setVisible(false);
                    mMenu.findItem(R.id.search_google_play).setVisible(false);
                }
            }
        });

        return true;
    }

}

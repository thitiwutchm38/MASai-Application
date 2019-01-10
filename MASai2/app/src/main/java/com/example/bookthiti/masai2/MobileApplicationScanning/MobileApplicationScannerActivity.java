package com.example.bookthiti.masai2.MobileApplicationScanning;

import android.app.SearchManager;
import android.content.Context;
import android.database.MatrixCursor;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.bookthiti.masai2.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileApplicationScannerActivity extends AppCompatActivity {
    private final static String TAG_INFO = "Log info";
    private final static String TAG_DEBUG = "Log debug";
    private final static String TAG_ERROR = "Log error";
    private static final long STOP_TYPING_TIMEOUT = 1000; // 1 second

    private Context mContext;
    private Button mSelectFromGooglePlayButton;
    private Button mStartAppScanButton;
    private Menu mMenu;
    private SearchView mSearchView;
    private ProgressBar mSpinner;
    private boolean flag = false;
    private boolean hasSuggestionClicked = false;
    private String mQuery;

    private CursorAdapter mCursorAdapter;
    private List<TargetApplicationInfo> mSuggestions;

    private TargetApplicationInfo selectedApplication;

    private Handler stopTypingHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 0) {
                Log.i(TAG_INFO, "background send message to handler");
                if(!mQuery.equals("")) searchApplication(mQuery, mCursorAdapter, mSuggestions);
            }
            return true;
        }
    });

    private Runnable stopTypingCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            Log.i(TAG_INFO, "User stop typing");
            stopTypingHandler.sendEmptyMessage(0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_appscan);

        setTitle("Mobile App Scan");
        mContext = this.getApplicationContext();
        mSelectFromGooglePlayButton = (Button) findViewById(R.id.button_select_from_google_play);
        mSelectFromGooglePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenu.findItem(R.id.search_google_play).setVisible(true);
                flag = !flag;
                if (flag) {
                    mSearchView.setIconified(false);
                    mSearchView.setVisibility(View.VISIBLE);
                    Log.i(TAG_INFO, "" + mSearchView.requestFocus());
                } else {
                    mSearchView.setVisibility(View.GONE);
                    mMenu.findItem(R.id.search_google_play).setVisible(false);
                }
            }
        });

        mStartAppScanButton = (Button) findViewById(R.id.button_start_app_scan);
        mStartAppScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedApplication != null) {

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_google_play_search, menu);
        mMenu = menu;
        // Associate searchable configuration with the SearchView
        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView =
                (SearchView) mMenu.findItem(R.id.search_google_play).getActionView();
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        mMenu.findItem(R.id.search_google_play_spinner).setVisible(false);
        mSpinner = (ProgressBar) mMenu.findItem(R.id.search_google_play_spinner).getActionView();

        final CursorAdapter suggestionAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
                new int[]{android.R.id.text1},
                0);

        final List<TargetApplicationInfo> suggestions = new ArrayList<>();

        mCursorAdapter = suggestionAdapter;
        mSuggestions = suggestions;

        mSearchView.setSuggestionsAdapter(mCursorAdapter);
        mSearchView.setVisibility(View.GONE);
        mSearchView.setIconified(false);
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSearchView.setIconified(false);
                mSearchView.setVisibility(View.GONE);
                mMenu.findItem(R.id.search_google_play_spinner).setVisible(false);
                mMenu.findItem(R.id.search_google_play).setVisible(false);
                flag = false;
                Log.i(TAG_INFO, "Search view close");
                return true;
            }
        });

        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    flag = false;
                    mSearchView.setVisibility(View.GONE);
                    mMenu.findItem(R.id.search_google_play_spinner).setVisible(false);
                    mMenu.findItem(R.id.search_google_play).setVisible(false);
                    Log.i(TAG_INFO, "focus elsewhere");
                } else {
                    flag = true;
                    mSearchView.setVisibility(View.VISIBLE);
                    mMenu.findItem(R.id.search_google_play).setVisible(true);
                    Log.i(TAG_INFO, "focus at search");
                }
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.i(TAG_INFO, "onQueryTextSubmit: " + s);
//                resetStopTypingTime(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mQuery = s;
//                mSuggestions.clear();
//                String[] columns = {
//                        BaseColumns._ID,
//                        SearchManager.SUGGEST_COLUMN_TEXT_1,
//                        SearchManager.SUGGEST_COLUMN_INTENT_DATA
//                };
//                MatrixCursor cursor = new MatrixCursor(columns);
//                mCursorAdapter.swapCursor(cursor);
//                mCursorAdapter.notifyDataSetChanged();
                if(!hasSuggestionClicked) {
                    resetStopTypingTimer();
                }
                hasSuggestionClicked = false;
                return true;
            }
        });
        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                hasSuggestionClicked = true;
                mSearchView.setQuery(mSuggestions.get(i).getAppName(), false);
                Log.i(TAG_INFO, "" + mSuggestions.size());
                mSelectFromGooglePlayButton.setText(mSuggestions.get(i).getAppName());
                mSearchView.clearFocus();
                return true;
            }
        });
        return true;
    }

    private void searchApplication(String query, final CursorAdapter cursorAdapter, final List<TargetApplicationInfo> suggestions) {
        MasaiServerAPI masaiServerAPI = RetrofitClientInstance.getRetrofitInstance().create(MasaiServerAPI.class);
        Call<List<TargetApplicationInfo>> listCall = masaiServerAPI.getAllTargetApplicationInfo(query);
        mMenu.findItem(R.id.search_google_play_spinner).setVisible(true);
//        mSpinner.setVisibility(View.VISIBLE);
        listCall.enqueue(new Callback<List<TargetApplicationInfo>>() {
            @Override
            public void onResponse(Call<List<TargetApplicationInfo>> call, Response<List<TargetApplicationInfo>> response) {
                List<TargetApplicationInfo> targetApplicationInfos = response.body();
                mSuggestions.clear();
                mSuggestions.addAll(targetApplicationInfos);
                Log.i(TAG_INFO, "" + mSuggestions.size());
                String[] columns = {
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA
                };
                MatrixCursor cursor = new MatrixCursor(columns);
                Log.i(TAG_INFO, "Get response");
                int i = 0;
                for (TargetApplicationInfo targetApplicationInfo : targetApplicationInfos) {
                    Log.i(TAG_INFO, "appId: " + targetApplicationInfo.getAppId() + ", appName: " + targetApplicationInfo.getAppName());
//                    Log.i(TAG_INFO, "iconUrl: " + targetApplicationInfo.getAppIcon().getUrl());
                    String[] tmp = {Integer.toString(i), targetApplicationInfo.getAppName(), targetApplicationInfo.getAppName()};
                    cursor.addRow(tmp);
                    i++;
                }
                mCursorAdapter.swapCursor(cursor);
                mMenu.findItem(R.id.search_google_play_spinner).setVisible(false);
                mSearchView.setIconified(false);
            }

            @Override
            public void onFailure(Call<List<TargetApplicationInfo>> call, Throwable t) {
                Toast.makeText(mContext, "Get failure response", Toast.LENGTH_SHORT).show();
                mMenu.findItem(R.id.search_google_play_spinner).setVisible(false);
                Log.i(TAG_INFO, t.getMessage());
            }
        });
    }

    private void resetStopTypingTimer() {
        stopTypingHandler.removeCallbacks(stopTypingCallback);
        stopTypingHandler.postDelayed(stopTypingCallback, STOP_TYPING_TIMEOUT);
    }

    private void resetStopTypingTime(long timeout) {
        stopTypingHandler.removeCallbacks(stopTypingCallback);
        stopTypingHandler.postDelayed(stopTypingCallback, timeout);
    }
}

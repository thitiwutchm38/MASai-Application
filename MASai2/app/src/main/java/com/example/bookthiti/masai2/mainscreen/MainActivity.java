package com.example.bookthiti.masai2.mainscreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bookthiti.masai2.MasaiSettingActivity;
import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.database.MasaiViewModel;
import com.example.bookthiti.masai2.database.model.ActivityLogEntity;
import com.example.bookthiti.masai2.database.model.TestingEntity;
import com.example.bookthiti.masai2.iotinformationscreen.IotInformationActivity;
import com.example.bookthiti.masai2.iotpentestmainscreen.IoTMainPentestActivity;
import com.example.bookthiti.masai2.mainscreen.model.PostRequestBody;
import com.example.bookthiti.masai2.internet.MasaiServerAPI;
import com.example.bookthiti.masai2.mobileapplicationscanningscreen.appsearchscreen.MobileApplicationScannerActivity;
import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.example.bookthiti.masai2.bluetoothservice.ServiceTools;
import com.example.bookthiti.masai2.internet.RetrofitClientInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bookthiti.masai2.utils.LogConstants.TAG_INFO;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "CHANNEL_MASAI_NOTIFICATION";
    private static int SPLASH_TIME = 4000; //This is 4 seconds
    private Activity mActivity;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private PagerAdapter mPagerAdapter;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private ImageButton button_MobileApp_att;

    private ImageButton button_iot_att;

    private ImageButton button_Tips;

    private ImageButton button__MASaibox_setting;

    private List<TestingEntity> testingEntityList = new ArrayList<>();

    private List<ActivityLogEntity> activityLogEntityList = new ArrayList<>();

    private MasaiViewModel masaiViewModel;

    private TestingEntity currentTestingEntity;

    private int testingPosition = 0;

    private MutableLiveData<Long> testingId = new MutableLiveData<Long>();

    private LiveData<List<ActivityLogEntity>> activityLogEntitiesByTestingIdLiveData;

    private Observer<List<ActivityLogEntity>> observer = new Observer<List<ActivityLogEntity>>() {
        @Override
        public void onChanged(@Nullable List<ActivityLogEntity> activityLogEntities) {
            mPagerAdapter.fragmentList.clear();
            mPagerAdapter.addFragment(HomeIconFragment.newInstance(), "");
            Log.i(TAG_INFO, "getActivityLogEntitiesByTestingId was on changed");
            activityLogEntityList.clear();
            for (ActivityLogEntity activityLogEntity : activityLogEntities) {
                if (!activityLogEntity.getName().equals("Wifi Scanning") && !activityLogEntity.getName().equals("Mobile App Scan")) {
                    activityLogEntityList.add(activityLogEntity);
                }
            }
//                        activityLogEntityList.addAll(activityLogEntities);
            if (activityLogEntityList.size() == 0) {
                mPagerAdapter.addFragment(NoActivityFragment.newInstance(), "");
            } else {
                for (ActivityLogEntity activityLogEntity : activityLogEntityList) {
                    mPagerAdapter.addFragment(ActivityLogFragment.newInstance(activityLogEntity), "");
                }
            }
            mViewPager.setAdapter(mPagerAdapter);
//                        mPagerAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(0, true);
        }
    };

    private SharedPreferences sharedPreferences;

    private static MasaiViewModel viewModel;

    public static MasaiViewModel getViewModel() {
        return viewModel;
    }

    public static void setViewModel(MasaiViewModel viewModel) {
        MainActivity.viewModel = viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mActivity = this;
        createNotificationChannel();
        Log.i(TAG_INFO, "MainAcitivty onCreate");
        if (!ServiceTools.isServiceRunning(BluetoothManagementService.class, getApplicationContext())) {
            startService(new Intent(MainActivity.this, BluetoothManagementService.class));
        } else {
            Log.i("Log info", "Service is already started");
        }
        masaiViewModel = ViewModelProviders.of(this).get(MasaiViewModel.class);
        setViewModel(masaiViewModel);

        sharedPreferences = getSharedPreferences("MASAI_SHARED_PREF", MODE_PRIVATE);
        testingPosition = sharedPreferences.getInt("testing_position", 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_primary_color_24dp);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_main);
        mTabLayout = (TabLayout) findViewById(R.id.tab_dots);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTabLayout.setupWithViewPager(mViewPager, true);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        masaiViewModel.getAllTestingEntities().observe(this, new Observer<List<TestingEntity>>() {
            @Override
            public void onChanged(@Nullable List<TestingEntity> testingEntities) {
                testingEntityList.clear();
                testingEntityList.addAll(testingEntities);
                setMenu();
            }
        });

        testingId.observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long aLong) {
                Log.i(TAG_INFO, "Current testing id: " + aLong);
//                LiveData<List<ActivityLogEntity>> activityLogEntitiesByTestingIdLiveData = masaiViewModel.getActivityLogEntitiesByTestingId(aLong);
                if (activityLogEntitiesByTestingIdLiveData == null)
                    activityLogEntitiesByTestingIdLiveData = masaiViewModel.getActivityLogEntitiesByTestingId(aLong);
                if (activityLogEntitiesByTestingIdLiveData.hasObservers()) {
                    Log.i(TAG_INFO, "activityLogEntitiesByTestingIdLiveData has Observers, removesObservers");
                    activityLogEntitiesByTestingIdLiveData.removeObservers((LifecycleOwner) mActivity);
                }
                activityLogEntitiesByTestingIdLiveData = masaiViewModel.getActivityLogEntitiesByTestingId(aLong);
                activityLogEntitiesByTestingIdLiveData.observe((LifecycleOwner) mActivity, observer);

            }
        });

        button_MobileApp_att = (ImageButton) findViewById(R.id.imageButton_MobileApp_att);
        button_iot_att = (ImageButton) findViewById(R.id.imageButton_iot_att);
        button_Tips = (ImageButton) findViewById(R.id.imageButton_Tips);
        button__MASaibox_setting = (ImageButton) findViewById(R.id.imageButton_MASaibox);


        button_MobileApp_att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_MobileApp_att();
            }
        });

        button_iot_att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_iot_att();
            }
        });

        button__MASaibox_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_MASaibox_setting();
            }
        });

        button_Tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_Tips();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_generate_report:
                createPostRequestBody();
                return true;
            case R.id.menu_delete_testing:
                if (masaiViewModel != null && currentTestingEntity != null) {
                    masaiViewModel.deleteTestingEntity(currentTestingEntity);
                    currentTestingEntity = null;
                    testingPosition = 0;
                    Log.i(TAG_INFO, "Testing should be deleted");
                }
                return true;
            case R.id.menu_set_api_server_address:
                final EditText input = new EditText(mActivity);
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Set MASai API Server Address");
                builder.setMessage("Please enter a domain name (http://xxxx.ngrok.io/) of MASai API Server");
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            RetrofitClientInstance.getRetrofitInstance(input.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(mActivity.getApplicationContext(), "Wrong format of MASai API Server Domain", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createPostRequestBody() {
        if (masaiViewModel != null && sharedPreferences != null) {
            final PostRequestBody postRequestBody = new PostRequestBody();
            final LiveData<List<ActivityLogEntity>> activityLogEntitiesByTestingId = masaiViewModel.getActivityLogEntitiesByTestingId(sharedPreferences.getLong("testing_id", 0));
            activityLogEntitiesByTestingId.observe(this, new Observer<List<ActivityLogEntity>>() {
                @Override
                public void onChanged(@Nullable List<ActivityLogEntity> activityLogEntities) {
                    List<ActivityLogEntity> crackingRouter = new ArrayList<ActivityLogEntity>();
                    List<ActivityLogEntity> deviceDiscovery = new ArrayList<ActivityLogEntity>();
                    List<ActivityLogEntity> deviceAssessment = new ArrayList<ActivityLogEntity>();
                    List<ActivityLogEntity> portAttack = new ArrayList<ActivityLogEntity>();
                    List<ActivityLogEntity> bluetoothAttack = new ArrayList<>();
                    List<ActivityLogEntity> mobileAppScan = new ArrayList<>();
                    for (ActivityLogEntity activityLogEntity : activityLogEntities) {
                        if ("finish".equals(activityLogEntity.getStatus())) {
                            String name = activityLogEntity.getName();
                            switch (name) {
                                //TODO: add cases for generate report
                                case "Router Cracking Testing":
                                    crackingRouter.add(activityLogEntity);
                                    break;
                                case "Device Discovery":
                                    deviceDiscovery.add(activityLogEntity);
                                    break;
                                case "Device Assessment":
                                    deviceAssessment.add(activityLogEntity);
                                    break;
                                case "Service Attacking Testing":
                                    portAttack.add(activityLogEntity);
                                    break;
                                case "Bluetooth Attacking":
                                    bluetoothAttack.add(activityLogEntity);
                                    break;
                                case "Mobile App Scan":
                                    mobileAppScan.add(activityLogEntity);
                                    break;
                            }
                        }
                    }
                    postRequestBody.setTestingId(currentTestingEntity.getId());
                    postRequestBody.setCreatedAt(currentTestingEntity.getCreatedAt());
                    postRequestBody.setTestingName(currentTestingEntity.getTitle());
                    postRequestBody.setRouterCracking(crackingRouter);
                    postRequestBody.setDeviceDiscovery(deviceDiscovery);
                    postRequestBody.setDeviceAssessment(deviceAssessment);
                    postRequestBody.setPortAttack(portAttack);
                    postRequestBody.setBluetoothAttack(bluetoothAttack);
                    postRequestBody.setMobileAppScan(mobileAppScan);
                    sendRequest(postRequestBody);
                    Log.i(TAG_INFO, "Created post request");
                    activityLogEntitiesByTestingId.removeObserver(this);
                }
            });
        }
    }

    private void sendRequest(PostRequestBody postRequestBody) {
        MasaiServerAPI masaiServerAPI = RetrofitClientInstance.getRetrofitInstance().create(MasaiServerAPI.class);
        Call<okhttp3.ResponseBody> voidCall = masaiServerAPI.postGenerateReport(postRequestBody);
        voidCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                Log.i(TAG_INFO, "GET Response");
                try {
                    if (response.body() != null) {
                        String path = response.body().string();
                        Log.i(TAG_INFO, path);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(RetrofitClientInstance.BASE_URL + "api/testreport/" + path));
                        startActivity(browserIntent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                Log.i(TAG_INFO, call.request().toString());
                Log.i(TAG_INFO, "On Failure");
                Toast.makeText(mActivity, "Please check the Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void openActivity_MobileApp_att() {

        Intent intent = new Intent(this, MobileApplicationScannerActivity.class);
//        Intent intent = new Intent(this, MobileApplicationScanningResultActivity.class);
        startActivity(intent);
    }

    public void openActivity_iot_att() {

        Intent intent = new Intent(this, IoTMainPentestActivity.class);
        startActivity(intent);
    }

    public void openActivity_MASaibox_setting() {

        Intent intent = new Intent(this, MasaiSettingActivity.class);
        startActivity(intent);
    }

    public void openActivity_Tips() {

        Intent intent = new Intent(this, IotInformationActivity.class);
        startActivity(intent);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(MainActivity.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setMenu() {
        Log.i(TAG_INFO, "setMenu is called");
        final Menu menu = mNavigationView.getMenu();
        menu.clear();
        for (int i = 0; i < testingEntityList.size(); i++) {
            final int index = i;
            menu.add(Menu.NONE, 0, Menu.NONE, testingEntityList.get(i).getTitle());
            MenuItem menuItem = menu.getItem(index);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    for (int j = 0; j < testingEntityList.size(); j++) {
                        menu.getItem(j).setChecked(false);
                    }
                    menuItem.setChecked(true);
                    currentTestingEntity = testingEntityList.get(index);
                    testingPosition = index;
//                    testingId = currentTestingEntity.getId();
                    testingId.setValue(currentTestingEntity.getId());
                    if (sharedPreferences != null) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("testing_position", testingPosition);
                        editor.putLong("testing_id", currentTestingEntity.getId());
                        editor.putString("testing_name", currentTestingEntity.getTitle());
                        editor.commit();
                    }
                    Log.i(TAG_INFO, "current testing: " + currentTestingEntity.getTitle());
                    mDrawerLayout.closeDrawers();
                    return true;
                }
            });
        }
        if (currentTestingEntity == null && testingEntityList.size() != 0) {
            currentTestingEntity = testingEntityList.get(testingPosition);
//            testingId = currentTestingEntity.getId();
            testingId.setValue(currentTestingEntity.getId());
            menu.getItem(testingPosition).setChecked(true);
            if (sharedPreferences != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("testing_position", testingPosition);
                editor.putLong("testing_id", currentTestingEntity.getId());
                editor.putString("testing_name", currentTestingEntity.getTitle());
                editor.commit();
            }
        } else if (currentTestingEntity == null && testingEntityList.size() == 0) {
            masaiViewModel.insertTestingEntity("Default Testing");
        } else {
            menu.getItem(testingPosition).setChecked(true);
//            if (sharedPreferences != null) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt("testing_position", testingPosition);
//                editor.putInt("testing_id", currentTestingEntity.getId());
//                editor.putString("testing_name", currentTestingEntity.getTitle());
//            }
        }
        menu.add("New Testing");
        menu.getItem(testingEntityList.size()).setIcon(R.drawable.ic_add_indigo_a700_24dp)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        final EditText input = new EditText(mActivity);
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setTitle("Create a new testing");
                        builder.setMessage("Please enter a name of a new testing");
                        builder.setView(input);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                masaiViewModel.insertTestingEntity(input.getText().toString());
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                        return true;
                    }
                });
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<Fragment>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String tabTitle) {
            fragmentList.add(fragment);
        }
    }
}

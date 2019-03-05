package com.example.bookthiti.masai2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.bookthiti.masai2.homescreen.HomeIconFragment;
import com.example.bookthiti.masai2.iotinformationscreen.IotInformationActivity;
import com.example.bookthiti.masai2.iotpentestmainscreen.IoTMainPentestActivity;
import com.example.bookthiti.masai2.mobileapplicationscanningscreen.MobileApplicationScannerActivity;
import com.example.bookthiti.masai2.bluetoothservice.BluetoothManagementService;
import com.example.bookthiti.masai2.bluetoothservice.ServiceTools;

import java.util.ArrayList;
import java.util.List;

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

    private List<String> testingNameList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mActivity = this;
        createNotificationChannel();
        if(!ServiceTools.isServiceRunning(BluetoothManagementService.class, getApplicationContext())) {
            startService(new Intent(MainActivity.this, BluetoothManagementService.class));
        } else {
            Log.i("Log info", "Service is already started");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_indigo_a700_24dp);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_main);
        mTabLayout = (TabLayout) findViewById(R.id.tab_dots);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTabLayout.setupWithViewPager(mViewPager, true);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(HomeIconFragment.newInstance(), "");
        mPagerAdapter.addFragment(HomeIconFragment.newInstance(), "");
        mPagerAdapter.addFragment(HomeIconFragment.newInstance(), "");
        mViewPager.setAdapter(mPagerAdapter);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        // TODO: add list of test to the menu
        testingNameList.add("Default Testing");
        testingNameList.add("Default Testing2");
        setMenu();

        button_MobileApp_att = (ImageButton)findViewById(R.id.imageButton_MobileApp_att);
        button_iot_att = (ImageButton)findViewById(R.id.imageButton_iot_att);
        button_Tips = (ImageButton)findViewById(R.id.imageButton_Tips);
        button__MASaibox_setting = (ImageButton)findViewById(R.id.imageButton_MASaibox);


        button_MobileApp_att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openActivity_MobileApp_att();
            }
        });

        button_iot_att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openActivity_iot_att();
            }
        });

        button__MASaibox_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openActivity_MASaibox_setting();
            }
        });

        button_Tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
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
        }
        return super.onOptionsItemSelected(item);
    }

    public void openActivity_MobileApp_att() {

        Intent intent = new Intent(this,MobileApplicationScannerActivity.class);
//        Intent intent = new Intent(this, MobileApplicationScanningResultActivity.class);
        startActivity(intent);
    }
    public void openActivity_iot_att() {

        Intent intent = new Intent(this,IoTMainPentestActivity.class);
        startActivity(intent);
    }
    public void openActivity_MASaibox_setting() {

        Intent intent = new Intent(this,MasaiSettingActivity.class);
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

    private void addMenu(String title) {
        testingNameList.add(title);
        setMenu();
    }

    private void setMenu() {
        final Menu menu = mNavigationView.getMenu();
        menu.clear();
        for (int i = 0; i < testingNameList.size(); i++) {
            menu.add(Menu.NONE, 0, Menu.NONE, testingNameList.get(i));
            MenuItem menuItem = menu.getItem(i);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    for (int j = 0; j < testingNameList.size(); j++) {
                        menu.getItem(j).setChecked(false);
                    }
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                }
            });
        }
        menu.add("New Testing");
        menu.getItem(testingNameList.size()).setIcon(R.drawable.ic_add_indigo_a700_24dp)
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
                        addMenu(input.getText().toString());
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

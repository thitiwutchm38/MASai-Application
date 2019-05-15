package com.example.bookthiti.masai2.iotinformationscreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;

import static com.example.bookthiti.masai2.utils.LogConstants.TAG_INFO;

public class OwaspContentActivity extends AppCompatActivity implements OwaspContentFragment.OnFragmentInteractionListener{
    private Context mContext;

    private TextView mTextViewTopic;
//    private TextView mTextViewContent;

    private ImageView mImageView;

    private OwaspModel mOwaspModel;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_owasp_mobile2);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_owasp_content);

        mContext = getApplicationContext();
        Intent intent = getIntent();
        mOwaspModel = (OwaspModel) intent.getParcelableExtra("owaspModel");
        mTextViewTopic = findViewById(R.id.text_owasp_mobile_topic);
        mTextViewTopic.setText(mOwaspModel.getTopicId() + " " + mOwaspModel.getTopic());

        mImageView = (ImageView) findViewById(R.id.image_owasp);
        String owaspId = mOwaspModel.getTopicId().toLowerCase();
        mImageView.setImageResource(getResources().getIdentifier("drawable/" + owaspId, null, getPackageName()));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager_content);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below)
            switch (position) {
                case 0:
//                    news_tab news_tab = new news_tab();
                    Log.i(TAG_INFO, "here");
                    return OwaspContentFragment.newInstance("detail", mOwaspModel.getGeneralDetail());
                case 1:
                    return OwaspContentFragment.newInstance("example", mOwaspModel.getExample());
                case 2:
                    return OwaspContentFragment.newInstance("guideline", mOwaspModel.getGuideline());
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }
    }
}

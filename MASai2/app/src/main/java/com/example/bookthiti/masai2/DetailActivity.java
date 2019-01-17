package com.example.bookthiti.masai2;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView mIcon;
    TextView mSender;
    TextView mEmailTitle;
    TextView mEmailDetails;
    TextView mEmailTime;
    ImageView mFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mIcon = findViewById(R.id.tvOwaspIcon);
        mSender = findViewById(R.id.tvOwaspTopic);
        mEmailTitle = findViewById(R.id.tvEmailTitle);
        mEmailDetails = findViewById(R.id.tvOwaspDetails);
        mEmailTime = findViewById(R.id.tvEmailTime);
        mFavorite = findViewById(R.id.ivFavorite);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mIcon.setText(mBundle.getString("icon"));
            ((GradientDrawable) mIcon.getBackground()).setColor(mBundle.getInt("colorIcon"));
            mSender.setText(mBundle.getString("sender"));
            mEmailTitle.setText(mBundle.getString("title"));
            mEmailDetails.setText(mBundle.getString("details"));
            mEmailTime.setText(mBundle.getString("time"));
        }
        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFavorite.getColorFilter() != null) {
                    mFavorite.clearColorFilter();
                } else {
                    mFavorite.setColorFilter(ContextCompat.getColor(DetailActivity.this,
                            R.color.colorOrange));
                }
            }
        });
    }
}

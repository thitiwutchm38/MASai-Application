package com.example.bookthiti.masai2;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ActivitySplash extends AppCompatActivity {

    private static int SPLASH_TIME = 4000; //This is 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);


        //Code to start timer and take action after the timer ends
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do any action here. Now we are moving to next page
                Intent mySuperIntent = new Intent(ActivitySplash.this, MainActivity.class);
                startActivity(mySuperIntent);
                /* This 'finish()' is for exiting the app when back button pressed
                 *  from Home page which is ActivityHome
                 */
                finish();
            }
        }, SPLASH_TIME);



        AnimationDrawable animation_ict = new AnimationDrawable();
        AnimationDrawable animation_mu = new AnimationDrawable();

        animation_ict.addFrame(getResources().getDrawable(R.drawable.logo_ict), 3000);
        animation_mu.addFrame(getResources().getDrawable(R.drawable.logo_mu), 3000);


        animation_ict.setOneShot(false);
        animation_mu.setOneShot(false);

        View imageAnim_mu = findViewById(R.id.view_temp_mu);

        View imageAnim_ict = findViewById(R.id.view_temp_ict);




        imageAnim_ict.setBackgroundDrawable(animation_ict);
        imageAnim_mu.setBackgroundDrawable(animation_mu);


        // start the animation!
        animation_ict.start();
        animation_mu.start();
    }
}
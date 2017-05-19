package com.example.subratkumar.attendanceex;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
    }
    @Override
    protected void onResume() {
        super.onResume();
        int splashtime = 6000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=null;
                i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
            }
        }, splashtime);

    }
}

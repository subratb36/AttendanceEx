package com.example.subratkumar.attendanceex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import static com.example.subratkumar.attendanceex.R.drawable.attendance;

/**
 * Created by subratkumar on 14-05-2017.
 */

public class DashBoard extends AppCompatActivity  implements View.OnClickListener{

    Button attendance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        attendance=(Button)findViewById(R.id.attendance_bt);
        attendance.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v==attendance){
            Intent intent=new Intent(DashBoard.this,AttendanceActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

    }
}

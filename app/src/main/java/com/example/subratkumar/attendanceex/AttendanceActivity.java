package com.example.subratkumar.attendanceex;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by subratkumar on 14-05-2017.
 */

public class AttendanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }




    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, DashBoard.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.attendance_tollbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_exit:
                onExit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onExit(){
        final Dialog dialog = new Dialog(AttendanceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.exit_menu_click);
        Button cancel=(Button)dialog.findViewById(R.id.cancel_menu_bt);
        Button logout=(Button)dialog.findViewById(R.id.logout_menu_bt);
        Button exit=(Button)dialog.findViewById(R.id.exit_menu_bt);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                startActivity(intent);
                finish();
                System.exit(0);
                //Getting out sharedpreferences
                SharedPreferences presh = getSharedPreferences(Config.SHARED_PREF_NAME,0);
                SharedPreferences.Editor editor = presh.edit();
                //Getting editor

                    editor.commit();
                dialog.dismiss();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting out sharedpreferences
                SharedPreferences pre = getSharedPreferences(Config.SHARED_PREF_NAME,0);
                //Getting editor
                SharedPreferences.Editor editor = pre.edit();

                //Puting the value false for loggedin
                editor.putBoolean(Config.SHARED_PREF_LOGGED_IN, false);
                //Putting blank value to email
                //Getting editor
                    editor.commit();

                //Starting login activity
                Intent intent = new Intent(AttendanceActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }
}

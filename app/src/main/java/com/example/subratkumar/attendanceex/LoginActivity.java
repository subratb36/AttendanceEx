package com.example.subratkumar.attendanceex;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by subratkumar on 14-05-2017.
 */

public class LoginActivity  extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG=LoginActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 200;
    private String STORAGE="android.permission.READ_EXTERNAL_STORAGE";
    DataBaseHelper dbHelper=null;
    File fileShow;

    private CheckBox checkBox_remember;
    private TextInputLayout inputLayoutName,inputLayoutPassword;
    private EditText userName_editText;
    private ShowHidePasswordEditText password_editText;
    private Button login_btn;
    private Context context;

    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;
    private boolean saveLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //is to initialize views
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_userName);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        userName_editText=(EditText)findViewById(R.id.userId_et);
        password_editText=(ShowHidePasswordEditText) findViewById(R.id.android_hide_show_edittext_password);
        userName_editText.addTextChangedListener((TextWatcher) new MyTextWatcher(userName_editText));
        password_editText.addTextChangedListener(new MyTextWatcher(password_editText));
        login_btn=(Button)findViewById(R.id.empLogin_btn);
        checkBox_remember = (CheckBox) findViewById(R.id.checkBox_remember);

        //is to initialize listeners
        context=this;
        login_btn.setOnClickListener(this);

        //
        if (!checkPermission()) {
            requestPermission();
        }

        final String dirPlot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/LoginDb/";
        fileShow = new File(dirPlot);
        if (!fileShow.exists()) {
            fileShow.mkdirs();
            if (!fileShow.mkdirs()) {
                Log.d("FileParser", "failed to create directory fileShowPdf");
            }
        }

        //initialize objects to be used
        dbHelper = new DataBaseHelper(LoginActivity.this,fileShow);
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.e("", e.getMessage());
        }

    }

    /**
     * This method is to check the storage permission
     */
    private boolean checkPermission() {
        int storage = ContextCompat.checkSelfPermission(getApplicationContext(), STORAGE);
        return storage == PackageManager.PERMISSION_GRANTED ;
    }
    /**
     * This method is for request the storage permission
     */
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if ( storageAccepted ) {
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.mainLayout), "Permission Granted, Now you can access Storage.", Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
                    }else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{STORAGE},PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    /**
     * This method is to show dialog box
     */
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.empLogin_btn:
               if (!validateName()) {
                   return;
               }else if (!validatePassword()) {
                   return;
               } else {
                   // get The User name and Password
                   String userId=userName_editText.getText().toString();
                   String password=password_editText.getText().toString();

                   // fetch the Password form database for respective user name
                   String storedPassword=dbHelper.getSinlgeEntry(userId);

                   // check if the Stored password matches with  Password entered by user
                   if(password.equals(storedPassword))
                   {
                       Snackbar snackbar = Snackbar.make(findViewById(R.id.mainLayout), "Success",
                               Snackbar.LENGTH_LONG);
                       View sbView = snackbar.getView();
                       TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                       textView.setTextColor(Color.RED);
                       snackbar.show();

                       Intent intent=new Intent(this, DashBoard.class);
                       startActivity(intent);
                       finish();
                   }
                   else
                   {
                       Snackbar snackbar = Snackbar.make(findViewById(R.id.mainLayout), "User Name or Password does not match",
                               Snackbar.LENGTH_LONG);
                       View sbView = snackbar.getView();
                       TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                       textView.setTextColor(Color.RED);
                       snackbar.show();
                   }
                   //Creating a shared preference
                   SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                   //Creating editor to store values to shared preferences
                   SharedPreferences.Editor editor = sharedPreferences.edit();
                   if(checkBox_remember.isChecked()){
                       //Adding values to editor
                       editor.putBoolean(Config.SHARED_PREF_SAVED_LOGGED_IN, true);
                       editor.putString(Config.SHARED_PREF_USERID, userId);
                       editor.putString(Config.SHARED_PREF_PASSWORD,password);

                       //Saving values to editor
                       editor.commit();

                   }else{
                       editor.clear();
                       editor.commit();
                   }
               }
               break;
       }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.userId_et:
                    validateName();
                    break;
                case R.id.android_hide_show_edittext_password:
                    validatePassword();
                    break;
            }
        }

    }

    private boolean validatePassword() {
        if (password_editText.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(password_editText);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateName() {
        if (userName_editText.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(userName_editText);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    /**
     * on resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        //In onResume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.SHARED_PREF_LOGGED_IN, false);
        saveLoggedIn = sharedPreferences.getBoolean(Config.SHARED_PREF_SAVED_LOGGED_IN, false);

        //If we will get true
        if (loggedIn) {
            //We will start the LoginView Activity
            Intent intent = new Intent(context, DashBoard.class);
            startActivity(intent);
            finish();
        }

        if (saveLoggedIn){
            userName_editText.setText(sharedPreferences.getString(Config.SHARED_PREF_USERID,"Not Available"));
            password_editText.setText(sharedPreferences.getString(Config.SHARED_PREF_PASSWORD,"Not Available"));
            checkBox_remember.setChecked(true);
        }
    }

}

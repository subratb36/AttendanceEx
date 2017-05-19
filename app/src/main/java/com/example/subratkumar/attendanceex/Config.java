package com.example.subratkumar.attendanceex;

/**
 * Created by subratkumar on 14-05-2017.
 */

public class Config {
    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "LoginApp";
    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String SHARED_PREF_SAVED_LOGGED_IN = "SaveLoggedIn";
    public static final String SHARED_PREF_LOGGED_IN = "LoggedIn";
    //This would be used to store the username of current Save logged in user
    public static final String SHARED_PREF_USERID = "userNameSaved";
    //This would be used to store the password of current Save logged in user
    public static final String SHARED_PREF_PASSWORD = "PasswordSaved";
}

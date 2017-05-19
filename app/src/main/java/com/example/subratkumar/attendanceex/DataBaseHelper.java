package com.example.subratkumar.attendanceex;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by subratkumar on 14-05-2017.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    // Creating tag
    private final static String TAG = DataBaseHelper.class.getSimpleName();
    //It's a context
    private final Context myContext;
    // Data base Name
    private static final String DATABASE_NAME = "Login.db";
    // Data base version
    private static final int DATABASE_VERSION = 1;
    //String variable to save file path
    private String pathToSaveDBFile;
    /**
     * This is a Constructor
     * @param context
     * @param  filePath
     */
    public DataBaseHelper(Context context, File filePath) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        pathToSaveDBFile = new StringBuffer(String.valueOf(filePath)).append("/").append(DATABASE_NAME).toString();
        Log.i(TAG,pathToSaveDBFile);
    }
    /**
     * This method is to check if database is exist
     * or not
     */
    public void prepareDatabase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            Log.d(TAG, "Database exists.");
            int currentDBVersion = getVersionId();
            if (DATABASE_VERSION > currentDBVersion) {
                Log.d(TAG, "Database version is higher than old.");
                deleteDb();
                try {
                    copyDataBase();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else {
            try {
                this.getReadableDatabase();
                copyDataBase();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
    /**
     * This method is to copy database in storage location
     */
    private void copyDataBase() throws IOException {
        OutputStream os = new FileOutputStream(pathToSaveDBFile);
        InputStream is = myContext.getAssets().open(DATABASE_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.flush();
        os.close();
    }
    /**
     * This method is to delete the database
     * Optional When DB Version will change
     */
    public void deleteDb() {
        File file = new File(pathToSaveDBFile);
        if (file.exists()) {
            file.delete();
            Log.d(TAG, "Database deleted.");
        }
    }
    /**
     * This method is to check the DB Version
     */
    private int getVersionId() {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null,SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT version_id FROM dbVersion";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int v = cursor.getInt(0);
        db.close();
        return v;
    }
    /**
     * This method is to check the DB
     * Return True
     * Or False
     */
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            File file = new File(pathToSaveDBFile);
            checkDB = file.exists();
        } catch (SQLiteException e) {
            Log.d(TAG, e.getMessage());
        }
        return checkDB;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
    }
    /**
     * This method is To check the version
     * If version Is new then it will copy the data to new DB
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {
                copyDataBase();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public String getSinlgeEntry(String userName)
    {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor=db.query("Login", null, " user_name=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
    }

}

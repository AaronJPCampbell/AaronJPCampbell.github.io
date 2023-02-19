package com.example.projectthreeaaroncampbell;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginDB extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";

    public LoginDB(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase loginDB) {
        loginDB.execSQL("create Table users(username TEXT primary key, password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase loginDB, int oldVersion, int newVersion) {
        loginDB.execSQL("drop Table if exists users");
    }

    //input new username and password into the database
    public Boolean insertData(String username, String password) {
        SQLiteDatabase loginDB = this.getWritableDatabase();
        ContentValues loginValues = new ContentValues();
        loginValues.put("username", username);
        loginValues.put("password", password);
        long result = loginDB.insert("users", null, loginValues);

        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    //check if a username exists in the database
    public Boolean checkUsername(String username) {
        SQLiteDatabase loginDB = this.getWritableDatabase();
        Cursor cursor = loginDB.rawQuery("Select * from users where username = ?", new String[] {username});

        if (cursor.getCount() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //check if both a username and a password exist in the database
    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase loginDB = this.getWritableDatabase();
        Cursor cursor = loginDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username, password});

        if(cursor.getCount() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}

package com.example.projectthreeaaroncampbell;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CaloriesDB extends SQLiteOpenHelper {
    public CaloriesDB(Context context) {
        super(context, "CaloriesData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase cdb) {
        cdb.execSQL("create Table CaloriesDetails(date TEXT primary key, calories TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase cdb, int oldVersion, int newVersion) {
        cdb.execSQL("drop Table if exists CaloriesDetails");
    }

    //method to insert new data, checks to make sure that data for the specified date does not already exist (returns false if it does)
    public Boolean insertCaloriesData(String date, String calories) {
        SQLiteDatabase cdb = this.getWritableDatabase();
        ContentValues caloriesValues = new ContentValues();
        caloriesValues.put("date", date);
        caloriesValues.put("calories", calories);

        long result = cdb.insert("CaloriesDetails", null,caloriesValues);
        if (result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    //method to update data based on entered date, checks to make sure that data for the specified date already exists (returns false if it does not)
    public Boolean updateCaloriesData(String date, String calories) {
        SQLiteDatabase cdb = this.getWritableDatabase();
        ContentValues caloriesValues = new ContentValues();
        caloriesValues.put("calories", calories);
        Cursor cursor = cdb.rawQuery("Select * from CaloriesDetails where date = ?", new String[] {date});

        if(cursor.getCount() > 0){ //if data is found with a matching date
            long result = cdb.update("CaloriesDetails", caloriesValues, "date=?", new String[] {date});
            if(result == -1){
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }

    }

    //method to delete data based on entered date, checks to make sure that data for the specified date already exists (returns false if it does not)
    public Boolean deleteCaloriesData(String date) {
        SQLiteDatabase cdb = this.getWritableDatabase();
        Cursor cursor = cdb.rawQuery("Select * from CaloriesDetails where date = ?", new String[] {date});

        if(cursor.getCount() > 0){ //if data is found with a matching date
            long result = cdb.delete("CaloriesDetails", "date=?", new String[] {date});
            if(result == -1){
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }

    }

    //method to get/display all data
    public Cursor getCaloriesData() {
        SQLiteDatabase cdb = this.getWritableDatabase();
        Cursor cursor = cdb.rawQuery("Select * from CaloriesDetails", null);
        return cursor;
    }

    //method to return the weight of a specific date
    public String getCaloriesDataDate(String date) {
        String todayCalories = "0";
        SQLiteDatabase cdb = this.getWritableDatabase();
        Cursor cursor = cdb.rawQuery("Select * from CaloriesDetails where date = ?", new String[] {date});
        cursor.moveToFirst();
        if(cursor.getCount() > 0) { //if data is found with a matching date
            todayCalories = cursor.getString(1);
        }
        return todayCalories;
    }
}


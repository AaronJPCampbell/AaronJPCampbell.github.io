package com.example.projectthreeaaroncampbell;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import com.jjoe64.graphview.series.DataPoint;

public class WeightDB extends SQLiteOpenHelper {
    public WeightDB(Context context) {
        super(context, "WeightData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table WeightDetails(date TEXT primary key, weight TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists WeightDetails");
    }

    //method to insert new data, checks to make sure that data for the specified date does not already exist (returns false if it does)
    public Boolean insertWeightData(String date, String weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues weightValues = new ContentValues();
        weightValues.put("date", date);
        weightValues.put("weight", weight);

        long result = db.insert("WeightDetails", null,weightValues);
        if (result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    //method to update data based on entered date, checks to make sure that data for the specified date already exists (returns false if it does not)
    public Boolean updateWeightData(String date, String weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues weightValues = new ContentValues();
        weightValues.put("weight", weight);
        Cursor cursor = db.rawQuery("Select * from WeightDetails where date = ?", new String[] {date});

        if(cursor.getCount() > 0){ //if data is found with a matching date
            long result = db.update("WeightDetails", weightValues, "date=?", new String[] {date});
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
    public Boolean deleteWeightData(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from WeightDetails where date = ?", new String[] {date});

        if(cursor.getCount() > 0){ //if data is found with a matching date
            long result = db.delete("WeightDetails", "date=?", new String[] {date});
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
    public Cursor getWeightData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from WeightDetails", null);
        return cursor;
    }

    //method to return the weight of a specific date
    public String getWeightDataDate(String date) {
        String todayWeight = "0";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from WeightDetails where date = ?", new String[] {date});
        cursor.moveToFirst();
        if(cursor.getCount() > 0) { //if data is found with a matching date
            todayWeight = cursor.getString(1);
        }
        return todayWeight;
    }
}

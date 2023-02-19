package com.example.projectthreeaaroncampbell;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

//imports for graphs
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class DatabaseActivity extends AppCompatActivity {
    //initialize necessary XML variables
    EditText date;
    EditText weight;
    EditText calories;
    Button newW;
    Button updateW;
    Button deleteW;
    Button viewW;
    Button newC;
    Button updateC;
    Button deleteC;
    Button viewC;
    Button viewTarget; //View Target Weight screen, can be used to refresh this page as well
    TextView XWLTextView; //Expected Weight Loss Text View
    TextView caloriesBudgetTextView;
    GraphView weightGraph;

    //Initialize databases
    WeightDB db;
    CaloriesDB cdb;

    //date variables, necessary for data uniformity as well as some of the algorithms
    LocalDate today = LocalDate.now();
    String day0 = today.toString();
    String day1 = today.minusDays(1).toString();
    String day2 = today.minusDays(2).toString();
    String day3 = today.minusDays(3).toString();
    String day4 = today.minusDays(4).toString();
    String day5 = today.minusDays(5).toString();
    String day6 = today.minusDays(6).toString();

    //Initialize algorithm variables with default values
    //The variables are set as Strings due to difficulties with XML displaying them otherwise
    String day0WeightText = "0";
    String day1WeightText = "0";
    String day2WeightText = "0";
    String day3WeightText = "0";
    String day4WeightText = "0";
    String day5WeightText = "0";
    String day6WeightText = "0";
    String day0CaloriesText = "0";
    double caloriesRemaining = 2000;
    String caloriesRemainingText = "2000";
    double expectedWeightLoss = 0;
    String expectedWeightLossText = "0";
    double weightChange = Double.parseDouble(day0WeightText) - Double.parseDouble(day1WeightText);
    String weightChangeText = String.valueOf(weightChange);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        //set variables to corresponding XML objects
        date = findViewById(R.id.newDate);
        weight = findViewById(R.id.newWeight);
        newW = findViewById(R.id.newWeightButton);
        updateW = findViewById(R.id.updateWeightButton);
        deleteW = findViewById(R.id.deleteWeightButton);
        viewW = findViewById(R.id.viewWeightButton);
        calories = findViewById(R.id.newCalories);
        newC = findViewById(R.id.newCaloriesButton);
        updateC = findViewById(R.id.updateCaloriesButton);
        deleteC = findViewById(R.id.deleteCaloriesButton);
        viewC = findViewById(R.id.viewCaloriesButton);
        viewTarget = findViewById(R.id.viewTargetWeightButton);
        weightGraph = findViewById(R.id.weightGraph);
        XWLTextView = findViewById(R.id.XWLTextView);
        caloriesBudgetTextView = findViewById(R.id.calorieBudgetTextView);

        //set databases
        db = new WeightDB(this);
        cdb = new CaloriesDB(this);

        //methods to populate each of the day 'x' weight and calorie values
        day0WeightText = db.getWeightDataDate(day0);
        day1WeightText = db.getWeightDataDate(day1);
        day2WeightText = db.getWeightDataDate(day2);
        day3WeightText = db.getWeightDataDate(day3);
        day4WeightText = db.getWeightDataDate(day4);
        day5WeightText = db.getWeightDataDate(day5);
        day6WeightText = db.getWeightDataDate(day6);
        day0CaloriesText = cdb.getCaloriesDataDate(day0); //we only need today's calories for this program

        //set values for calories variables
        caloriesRemaining = 2000 - Double.parseDouble(day0CaloriesText);
        caloriesRemainingText = String.valueOf(caloriesRemaining) + " cal.";
        expectedWeightLoss = Math.round((2000 - caloriesRemaining) * 0.028) / 100.0; //this rounds the result to 2 decimal places
        expectedWeightLossText = String.valueOf(expectedWeightLoss) + " lbs.";

        //set the TextViews to display the updated data
        caloriesBudgetTextView.setText(caloriesRemainingText);
        XWLTextView.setText(expectedWeightLossText);

        /*------------------------------------------------GRAPH SECTION------------------------------------------------*/
        //create data points for the weight change graph
        //the data points are in reverse order, as the first x coordinate is on the left-most part of the graph, and we want the points to go from oldest to newest
        LineGraphSeries<DataPoint> weightSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, Double.parseDouble(day6WeightText)),
                new DataPoint(2, Double.parseDouble(day5WeightText)),
                new DataPoint(3, Double.parseDouble(day4WeightText)),
                new DataPoint(4, Double.parseDouble(day3WeightText)),
                new DataPoint(5, Double.parseDouble(day2WeightText)),
                new DataPoint(6, Double.parseDouble(day1WeightText)),
                new DataPoint(7, Double.parseDouble(day0WeightText))
        });

        //set the title for the graph
        weightGraph.setTitle("Weight Change (Past Week)");

        //set the text size for the graph title
        weightGraph.setTitleTextSize(60); //60 is about the same size as the other text from the XML file (20sp)

        //add the above data points to the graph
        weightGraph.addSeries(weightSeries);

        //set custom x upper and lower limits for the graph (if this is not done, the graph will have "empty" spots
        weightGraph.getViewport().setMinX(1);
        weightGraph.getViewport().setMaxX(7);
        weightGraph.getViewport().setXAxisBoundsManual(true); //allows the custom limits to work

        /*------------------------------------------------BUTTON SECTION------------------------------------------------*/
        //setup record weight button (see WeightDB.java)
        newW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                /*------------------------------------------------DATE NORMALIZATION AND TESTING------------------------------------------------*/
                //String dateText = date.getText().toString(); //Use this for testing, requires manual entry of date to record weight/calories, comment out when done testing
                String dateText = today.toString(); //Use this for final build, sets date for weight/calories entry (new) to today's date, used to keep data uniform
                /*------------------------------------------------DATE NORMALIZATION AND TESTING------------------------------------------------*/
                String weightText = weight.getText().toString();
                Boolean checkInsertData = db.insertWeightData(dateText, weightText); //if data for the specified date does not exist, insert new data, otherwise do nothing
                //Creates messages based on the above result
                if(checkInsertData == true){
                    Toast.makeText(DatabaseActivity.this, "Weight Recorded", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(DatabaseActivity.this, "Error, Weight not Recorded", Toast.LENGTH_LONG).show();
                }
            }
        });

        //setup update weight button (see WeightDB.java)
        updateW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                String dateText = date.getText().toString();
                String weightText = weight.getText().toString();
                Boolean checkUpdateData = db.updateWeightData(dateText, weightText); //if data for the specified date exists, update the weight data, otherwise do nothing
                //Creates messages based on the above result
                if(checkUpdateData == true){
                    Toast.makeText(DatabaseActivity.this, "Weight Entry Updated", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(DatabaseActivity.this, "Error, Weight Entry not Updated", Toast.LENGTH_LONG).show();
                }
            }
        });

        //setup delete weight button (see WeightDB.java)
        deleteW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                String dateText = date.getText().toString();
                Boolean checkDeleteData = db.deleteWeightData(dateText); //if data for the specified date exists, delete the weight data for that date, otherwise do nothing
                //Creates messages based on the above result
                if(checkDeleteData == true){
                    Toast.makeText(DatabaseActivity.this, "Weight Entry Removed", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(DatabaseActivity.this, "Error, Weight Entry not Removed", Toast.LENGTH_LONG).show();
                }
            }
        });

        //setup view weight entries
        viewW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Cursor weightList = db.getWeightData();

                //if there are no entries in the database, display a message stating such
                if(weightList.getCount() == 0) {
                    Toast.makeText(DatabaseActivity.this, "No Entries to Display", Toast.LENGTH_LONG).show();
                }

                //loop to go through the database, creating a "table" that will display the date and weight, then move on to the next entry
                StringBuffer buffer = new StringBuffer();
                while(weightList.moveToNext()) {
                    buffer.append("Date: " + weightList.getString(0) + "\n");
                    buffer.append("Weight: " + weightList.getString(1) + "\n");
                }

                //takes the above "table" and displays it as an alert box
                AlertDialog.Builder builder = new AlertDialog.Builder(DatabaseActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Weight Entries");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });

        //setup record calories button (see CaloriesDB.java)
        newC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                /*------------------------------------------------DATE NORMALIZATION AND TESTING------------------------------------------------*/
                //String dateTextC = date.getText().toString(); //Use this for testing, requires manual entry of date to record weight/calories, comment out when done testing
                String dateTextC = today.toString(); //Use this for final build, sets date for weight/calories entry (new) to today's date, used to keep data uniform
                /*------------------------------------------------DATE NORMALIZATION AND TESTING------------------------------------------------*/
                String caloriesText = calories.getText().toString();
                Boolean checkInsertData = cdb.insertCaloriesData(dateTextC, caloriesText); //if data for the specified date does not exist, insert new data, otherwise do nothing
                //Creates messages based on the above result
                if(checkInsertData == true){
                    Toast.makeText(DatabaseActivity.this, "Calories Recorded", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(DatabaseActivity.this, "Error, Calories not Recorded", Toast.LENGTH_LONG).show();
                }
            }
        });

        //setup update calories button (see CaloriesDB.java)
        updateC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                String dateTextC = date.getText().toString();
                String caloriesText = calories.getText().toString();
                Boolean checkUpdateData = cdb.updateCaloriesData(dateTextC, caloriesText); //if data for the specified date exists, update the calories data for that date, otherwise do nothing
                //Creates messages based on the above result
                if(checkUpdateData == true){
                    Toast.makeText(DatabaseActivity.this, "Calories Updated", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(DatabaseActivity.this, "Error, Calories not Updated", Toast.LENGTH_LONG).show();
                }
            }
        });

        //setup delete calories button, (see CaloriesDB.java)
        deleteC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                String dateTextC = date.getText().toString();
                Boolean checkDeleteData = cdb.deleteCaloriesData(dateTextC); //if data for the specified date exists, delete the calories data for that date, otherwise do nothing
                //Creates messages based on the above result
                if(checkDeleteData == true){
                    Toast.makeText(DatabaseActivity.this, "Calories Entry Removed", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(DatabaseActivity.this, "Error, Calories Entry not Removed", Toast.LENGTH_LONG).show();
                }
            }
        });

        //setup view calories button
        viewC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Cursor caloriesList = cdb.getCaloriesData();

                //if there are no entries in the database, display a message stating such
                if(caloriesList.getCount() == 0) {
                    Toast.makeText(DatabaseActivity.this, "No Entries to Display", Toast.LENGTH_LONG).show();
                }

                //loop to go through the database, creating a "table" that will display the date and calories, then move on to the next entry
                StringBuffer buffer = new StringBuffer();
                while(caloriesList.moveToNext()) {
                    buffer.append("Date: " + caloriesList.getString(0) + "\n");
                    buffer.append("Calories: " + caloriesList.getString(1) + "\n");
                }

                //takes the above "table" and displays it as an alert box
                AlertDialog.Builder builder = new AlertDialog.Builder(DatabaseActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Calories Entries");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });

        //setup button to bring you to target weight screen
        //this button can be used to refresh the current screen (go to target weight screen and then come back to this screen)
        viewTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTarget = new Intent(DatabaseActivity.this, TargetWeightScreenActivity.class);
                startActivity(intentTarget);
            }
        });
    }
}
package com.example.projectthreeaaroncampbell;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DatabaseActivity extends AppCompatActivity {
    //initialize necessary variables
    EditText date;
    EditText weight;
    Button newW;
    Button updateW;
    Button deleteW;
    Button viewW;
    Button viewTarget;
    WeightDB db;

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
        viewTarget = findViewById(R.id.viewTargetWeightButton);
        db = new WeightDB(this);

        //setup record weight button
        newW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                String dateText = date.getText().toString();
                String weightText = weight.getText().toString();
                Boolean checkInsertData = db.insertWeightData(dateText, weightText);
                if(checkInsertData == true){
                    Toast.makeText(DatabaseActivity.this, "Weight Recorded", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(DatabaseActivity.this, "Error, Weight not Recorded", Toast.LENGTH_LONG).show();
                }
            }
        });

        //setup update weight button
        updateW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                String dateText = date.getText().toString();
                String weightText = weight.getText().toString();
                Boolean checkUpdateData = db.updateWeightData(dateText, weightText);
                if(checkUpdateData == true){
                    Toast.makeText(DatabaseActivity.this, "Weight Entry Updated", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(DatabaseActivity.this, "Error, Weight Entry not Updated", Toast.LENGTH_LONG).show();
                }
            }
        });

        //setup delete weight button
        deleteW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                String dateText = date.getText().toString();
                Boolean checkDeleteData = db.deleteWeightData(dateText);
                if(checkDeleteData == true){
                    Toast.makeText(DatabaseActivity.this, "Weight Entry Removed", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(DatabaseActivity.this, "Error, Weight Entry not Removed", Toast.LENGTH_LONG).show();
                }
            }
        });

        //setup view weight button
        viewW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Cursor weightList = db.getWeightData();

                if(weightList.getCount() == 0) {
                    Toast.makeText(DatabaseActivity.this, "No Entries to Display", Toast.LENGTH_LONG).show();
                }

                StringBuffer buffer = new StringBuffer();
                while(weightList.moveToNext()) {
                    buffer.append("Date: " + weightList.getString(0) + "\n");
                    buffer.append("Weight: " + weightList.getString(1) + "\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(DatabaseActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Weight Entries");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });

        //setup button to bring you to target weight and calorie budget screen
        viewTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTarget = new Intent(DatabaseActivity.this, TargetWeightScreenActivity.class);
                startActivity(intentTarget);
            }
        });
    }
}
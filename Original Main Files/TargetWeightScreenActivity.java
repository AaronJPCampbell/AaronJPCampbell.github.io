package com.example.projectthreeaaroncampbell;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TargetWeightScreenActivity extends AppCompatActivity {
    //initialize necessary variables
    Button saveB;
    EditText target;
    Button viewD;
    Boolean smsp = false; //variable used to store permissions for sending SMS messages

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_weight_screen);

        //set variables to corresponding XML objects
        saveB = findViewById(R.id.saveButton);
        target = findViewById(R.id.targetWeight);
        viewD = findViewById(R.id.viewDatabaseButton);

        //Commented out, as this currently serves no purpose
        //initialize SMS permissions dialogue box, set permission variable smsp based on user response
        /*
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        smsp = true;
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        smsp = false;
                        break;
                }
            }
        };
        */

        //setup button to lock in target weight
        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (target.getText().toString().trim().length() != 0) {
                    target.setEnabled(false);
                }
            }
        });

        //setup button to bring you to target weight screen
        viewD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDatabase = new Intent(TargetWeightScreenActivity.this, DatabaseActivity.class);
                startActivity(intentDatabase);
            }
        });

        //display SMS permissions dialogue box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Allow this app to send SMS notifications?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }
}
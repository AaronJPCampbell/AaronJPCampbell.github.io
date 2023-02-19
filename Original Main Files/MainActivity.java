package com.example.projectthreeaaroncampbell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


//NOTE: a test user (username: "test" / password: "test") has been created already

public class MainActivity extends Activity  {
    Button loginB;
    Button registerB;
    EditText userET;
    EditText passET;
    LoginDB DB = new LoginDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set variables to corresponding XML objects
        loginB = findViewById(R.id.loginButton);
        registerB = findViewById(R.id.registerButton);
        userET = findViewById(R.id.usernameText);
        passET = findViewById(R.id.passwordText);

        //setup login button action
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userET.getText().toString();
                String password = passET.getText().toString();

                //if username or password is blank, let the user know
                if (username.equals("") || password.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Please enter both a username and a password", Toast.LENGTH_LONG).show();
                }
                //if both username and password are filled in
                else
                {
                    //check to make sure the database contains the username/password combination
                    Boolean checkUsernamePassword = DB.checkUsernamePassword(username, password);
                    //if the combination exists, allow the user to login
                    if (checkUsernamePassword == true)
                    {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                        //send the user to the weight tracking screen
                        Intent intentLogin = new Intent(MainActivity.this, DatabaseActivity.class);
                        startActivity(intentLogin);
                    }
                    //if the combination does not exist, alert the user
                    else
                    {
                        Toast.makeText(MainActivity.this, "Invalid Username/Password", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //setup register button action
        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userET.getText().toString();
                String password = passET.getText().toString();

                //if username or password is blank, let the user know
                if (username.equals("") || password.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Please enter both a username and a password", Toast.LENGTH_LONG).show();
                }
                //if both username and password are filled in
                else
                {
                    //make sure that the username does not already exist in the database
                    Boolean checkUsername = DB.checkUsername(username);
                    //insert the username/password into the database
                    if (checkUsername == false)
                    {
                        Boolean insert = DB.insertData(username, password);

                        //let the user know the data was entered into the database
                        if(insert == true)
                        {
                            Toast.makeText(MainActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                            //send the user to the weight tracking screen
                        }

                        //if username already exists, alert the user
                        else
                        {
                            Toast.makeText(MainActivity.this, "User Already Exists", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }
}
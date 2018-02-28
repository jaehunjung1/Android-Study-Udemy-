package com.jayjung.twitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    static String inputID;
    static String inputPassword;
    static Intent intent;

    public void onClick(View view) {
        inputID = ((TextView)findViewById(R.id.idText)).getText().toString();
        inputPassword = ((TextView)findViewById(R.id.passwordText)).getText().toString();

        //Check if id already exists
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", inputID);
        query.setLimit(1);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null && !objects.isEmpty()) {
                    ParseUser user= objects.get(0);
                    login(inputID, inputPassword);
                } else if (e == null) {
                    signup(inputID, inputPassword);
                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    private void login(String id, String password) {
        ParseUser.logInInBackground(id, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.i("Login", "Success");
                    intent = new Intent(getApplicationContext(), UserFeedActivity.class);
                    startActivity(intent);
                } else {
                    Log.i("Login", "Failed");
                    e.printStackTrace();
                }
            }
        });
    }

    private void signup(String id, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(id);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Signup", "Success");
                    intent = new Intent(getApplicationContext(), UserFeedActivity.class);
                    startActivity(intent);
                } else
                    e.printStackTrace();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Twitter :: Login");

        ParseUser.logOut();

    }
}

package com.jayjung.instagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

    Intent intent;
    EditText idText;
    EditText passwordText;
    ParseObject parseObject;
    static String idInput;
    static String passwordInput;

    public void login(View view) {
        idInput = idText.getText().toString();
        passwordInput = passwordText.getText().toString();

        // Check if id and password all submitted
        if (idInput.equals("") || passwordInput.equals(""))
            Toast.makeText(MainActivity.this, "Put in ID and Password properly!", Toast.LENGTH_SHORT).show();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Instagram");
        query.whereEqualTo("id", idInput);
        query.setLimit(1);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects != null) {
                    ParseObject object = objects.get(0);
                    if (object.getString("password").equals(passwordInput)) {
                        intent = new Intent(getApplicationContext(), UserListActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    }
            } else {
                    e.printStackTrace();
                }
            }
        });

    }

    public void signup(View view) {
        idInput = idText.getText().toString();
        passwordInput = passwordText.getText().toString();

        // Check if id and password all submitted
        if (idInput.equals("") || passwordInput.equals(""))
            Toast.makeText(MainActivity.this, "Put in ID and Password properly!", Toast.LENGTH_SHORT).show();

        parseObject.put("id", idInput);
        parseObject.put("password", passwordInput);
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    Toast.makeText(MainActivity.this, "Sign Up Complete!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // EditText Initialization
        idText = (EditText)findViewById(R.id.idText);
        passwordText = (EditText)findViewById(R.id.passwordText);

        // parseuser login as jay
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.logOut();

        ParseUser.logInInBackground("Jay", "happy1203", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.i("LogintoDB", "Successful");
                }
                else {
                    Log.i("LogintoDB", "Failed");
                    e.printStackTrace();
                }
            }
        });

        parseObject = new ParseObject("Instagram");

        //KeyBoard slides back if user presses elsewhere
        passwordText.setOnKeyListener(this);

    }

    //keyboard event continued
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        // if the keyboard input(i) is equal to enter and keyEvent is action down(only once)
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
            login(view);
        return false;
    }

}

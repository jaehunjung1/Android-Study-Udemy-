package com.jayjung.demoapp;


import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            SQLiteDatabase db = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR, age INT(3))");

//            db.execSQL("INSERT INTO users (name, age) VALUES ('Roy', 24)");
//            db.execSQL("INSERT INTO users (name, age) VALUES ('Jay', 25)");
//            db.execSQL("INSERT INTO users (name, age) VALUES ('Henny', 22)");
//            db.execSQL("INSERT INTO users (name, age) VALUES ('Dude', 21)");

            Cursor c = db.rawQuery("SELECT * FROM users WHERE name LIKE 'R%'", null);
            int name_index = c.getColumnIndex("name");
            int age_index = c.getColumnIndex("age");

            c.moveToFirst();

            while(c != null) {
                Log.i("name", c.getString(name_index));
                Log.i("age", Integer.toString(c.getInt(age_index)));
                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

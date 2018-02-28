package com.jayjung.demoapp;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


// Alert에서 선택한 언어를 sharedPreferences에 저장, textview에서 표시하는 app.
public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String language;
    TextView textView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.english)
            setLanguage("English");
        else
            setLanguage("Korean");

        return true;
    }

    public void setLanguage(String language) {

        sharedPreferences.edit().putString("language", language).apply();


        textView.setText(sharedPreferences.getString("language", "default"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        sharedPreferences = this.getSharedPreferences("com.jayjung.demoapp", Context.MODE_PRIVATE);
        language = sharedPreferences.getString("language", "default");
        if (language.equals("default")) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Choose Language")
                    .setMessage("You can change at settings")
                    .setPositiveButton("Korean", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            language = "Korean";
                            setLanguage(language);
                        }
                    })
                    .setNegativeButton("English", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            language = "English";
                            setLanguage(language);
                        }
                    })
                    .show();

        } else {
            textView.setText(language);
        }



    }
}

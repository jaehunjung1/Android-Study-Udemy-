package com.jayjung.noteapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    /*Menu 누르면 add note하고, 각 list 목록 누르면 alert 떠서 삭제. sharedpreferences 구현하고...
     ArrayList<String>에 노트들 저장할거임. 그리고 sharedpreferences와 연동시켜서(ObjectSerializer),
     삭제 및 새로 만들기 등이 가능하도록.삭제 및 새로 만들기할 때마다 listview 업데이트.
    */

    static ArrayList<String> noteData = new ArrayList<String>();
    SharedPreferences sharedPreferences;
    static ArrayAdapter<String> arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
        startActivity(intent);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Context context = this;
        sharedPreferences = getApplicationContext().getSharedPreferences("com.jayjung.noteapp", MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>)sharedPreferences.getStringSet("notes", null);

        if (set==null) {
            noteData.add("Example Note");
        } else {
            noteData = new ArrayList<String>(set);
        }

        ListView listView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noteData);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra("noteId", i);
                startActivity(intent);
            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int item, long l) {
                final int d = item;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you Sure?")
                        .setMessage("Do you want to delete this?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                noteData.remove(d);
                                arrayAdapter.notifyDataSetChanged();

                                sharedPreferences = getApplicationContext().getSharedPreferences("com.jayjung.noteapp", Context.MODE_PRIVATE);

                                HashSet<String> set = new HashSet(MainActivity.noteData);

                                sharedPreferences.edit().putStringSet("notes", set).apply();

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            }
        });
}
}

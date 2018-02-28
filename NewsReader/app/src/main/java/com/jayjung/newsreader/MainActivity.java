package com.jayjung.newsreader;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> linkArr = new ArrayList<String>(); // Top News 링크의 id 값의  List
    static ArrayList<String> titleArr = new ArrayList<String>(); // News Title들의 List
    static ArrayList<String> urlArr = new ArrayList<String>(); // News Link들의 List
    static SQLiteDatabase db;

    ListView listView;

    // link downloading Task
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    result += (char)data;
                    data = reader.read();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    // json string downloading task
    public class JsonTask extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(ArrayList<String>... urls) {
            ArrayList<String> resultArr = new ArrayList<String>();
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            for (int i = 0; i < 10; i++) {
                try {
                    result = "";
                    url = new URL("https://hacker-news.firebaseio.com/v0/item/" + urls[0].get(i) + ".json");
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int data = reader.read();

                    while (data != -1) {
                        result += (char) data;
                        data = reader.read();
                    }
                    resultArr.add(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return resultArr;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Top News의 link들의 json파일을 받아옴.
        String result = null;
        DownloadTask linkDownloadTask = new DownloadTask();
        try {
            result = linkDownloadTask.execute("https://hacker-news.firebaseio.com/v0/topstories.json").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // topstory link json file Parsing
        linkArr = new ArrayList<String>(Arrays.asList(result.substring(2, result.length() - 2).split(",")));

        // title and url parsing to titleArr
        try {
            JsonTask jsonTask = new JsonTask();
            ArrayList<String> jsonArr = jsonTask.execute(linkArr).get();
            for (int i = 0; i < jsonArr.size(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArr.get(i));

                if (!jsonObject.isNull("title")) //title이 없는 경우 JSONException 날 수도 있으므로 catch 하나 더 만들어줌.
                    titleArr.add(jsonObject.getString("title"));
                if (!jsonObject.isNull("url"))
                    urlArr.add(jsonObject.getString("url"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // listview setting
        listView = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleArr);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                intent.putExtra("articleIndex", i);
                startActivity(intent);
            }
        });
    }
}

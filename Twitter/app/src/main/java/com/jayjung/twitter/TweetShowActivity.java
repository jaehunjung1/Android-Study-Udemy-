package com.jayjung.twitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TweetShowActivity extends AppCompatActivity {

    ArrayList<String> selectedUserList;
    ArrayList<HashMap<String, String>> tweetList;
    HashMap<String, String> tweet;
    ListView listView;
    SimpleAdapter simpleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_show);

        listView = (ListView)findViewById(R.id.tweetListView);
        selectedUserList = getIntent().getStringArrayListExtra("selectedUserList");
        tweetList = new ArrayList<HashMap<String, String>>();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tweet");
            query.whereContainedIn("username", selectedUserList);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null && !objects.isEmpty()) {
                        for (int index = 0; index < objects.size(); index++) {
                            tweet = new HashMap<String, String>();
                            tweet.put("username", objects.get(index).getString("username"));
                            tweet.put("tweetText", objects.get(index).getString("tweetText"));
                            tweetList.add(tweet);
                        }
                        simpleAdapter.notifyDataSetChanged();

                    } else if (objects.isEmpty()) {
                        Toast.makeText(TweetShowActivity.this, "No one tweeted yet!", Toast.LENGTH_SHORT).show();
                    } else {
                        e.printStackTrace();
                    }
                }
            });



        simpleAdapter = new SimpleAdapter(TweetShowActivity.this, tweetList, android.R.layout.simple_list_item_2, new String[]{"tweetText", "username"},
                new int[]{android.R.id.text1, android.R.id.text2});
        simpleAdapter.notifyDataSetChanged();
        listView.setAdapter(simpleAdapter);


    }
}

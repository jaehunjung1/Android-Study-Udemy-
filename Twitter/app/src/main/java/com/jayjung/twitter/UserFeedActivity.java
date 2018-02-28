package com.jayjung.twitter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class UserFeedActivity extends AppCompatActivity {

    CustomAdapter customAdapter;
    ArrayList<String> selectedUserList;
    ListView userListView;
    ArrayList<String> userList;


    public class ListViewItem {
        String username;
    }

    class CustomAdapter extends BaseAdapter {

        ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

        @Override
        public int getCount() {
            return listViewItemList.size();
        }

        @Override
        public Object getItem(int i) {
            return listViewItemList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {

            final int pos = i;
            Context context = parent.getContext();
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.user_feed, parent, false);
            }

            TextView textView = (TextView)view.findViewById(R.id.textView1);
            CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkBox1);

            final ListViewItem listViewItem = listViewItemList.get(i);
            textView.setText(listViewItem.username);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.isSelected()) {
                        view.setSelected(false);
                        selectedUserList.remove(listViewItem.username);
                    }
                    else {
                        view.setSelected(true);
                        selectedUserList.add(listViewItem.username);
                    }
                }
            });


            return view;
        }

        public void addItem(String username) {
            ListViewItem listViewItem = new ListViewItem();
            listViewItem.username = username;

            listViewItemList.add(listViewItem);
        }
    }

    public void tweet(String tweetText, String username) {
        ParseObject parseObject = new ParseObject("Tweet");
        parseObject.put("username", username);
        parseObject.put("tweetText", tweetText);

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(UserFeedActivity.this, "Tweet Success!!!", Toast.LENGTH_SHORT).show();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.tweet:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Tweet");

                final EditText input = new EditText(this);
                builder.setView(input);

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tweet(input.getText().toString(), ParseUser.getCurrentUser().getUsername());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
                return true;
            case R.id.userfeed:
                intent = new Intent(getApplicationContext(), TweetShowActivity.class);
                intent.putStringArrayListExtra("selectedUserList", selectedUserList);
                startActivity(intent);
                return true;
            case R.id.logout:
                ParseUser.logOut();
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        getSupportActionBar().setTitle("User Feed");

        userListView = (ListView)findViewById(R.id.userListView);
        userList = new ArrayList<String>();
        selectedUserList = new ArrayList<String>();

        customAdapter = new CustomAdapter();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.addAscendingOrder("username");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null && !objects.isEmpty()) {
                    for (int i = 0; i < objects.size(); i++) {
                        System.out.println(objects.get(i).getUsername());
                        userList.add(objects.get(i).getUsername());
                    }

                    for (int i = 0; i < userList.size(); i++) {
                        customAdapter.addItem(userList.get(i));
                    }

                    userListView.setAdapter(customAdapter);
                } else {
                    e.printStackTrace();
                }
            }
        });

        customAdapter.notifyDataSetChanged();




    }
}

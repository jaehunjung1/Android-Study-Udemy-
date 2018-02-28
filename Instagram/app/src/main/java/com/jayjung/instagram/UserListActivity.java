package com.jayjung.instagram;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UserListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> nameArr = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;


    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 제일 밑의 onActivityResult를 실행시키게 됨.
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }

    // 메뉴 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // share button 눌렀을 시
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //If version is ge than marshmallow, we have to explicitly get permission for external storage
                if (this.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {
                    getPhoto();

                }

            } else
                getPhoto();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        getSupportActionBar().setTitle("User Feed");
        listView = (ListView)findViewById(R.id.listView);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Instagram");
        query.findInBackground(new FindCallback<ParseObject>() {
            //nameArr에 모든 user의 이름을 넣음.
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects != null) {
                    for (int i = 0; i < objects.size(); i++) {
                        nameArr.add(objects.get(i).getString("id"));
                    }
                    // background에서 query를 찾기 때문에, 무조건 밑의 코드보다 먼저 실행되지는 않을 수도 있음.
                    // 그래서 datasetchanged를 써줘야함.
                    arrayAdapter.notifyDataSetChanged();
                    Log.i("Insertion to nameArr", "Done!");
                } else {
                    Log.i("Insertion to nameArr", "Failed");
                    e.printStackTrace();
                }
            }
        });

        //ListView setting
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameArr);
        listView.setAdapter(arrayAdapter);

        // User Feed에서 ID 클릭시 각 user의 feed(imagefeedActivity)로 넘어감.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), UserfeedActivity.class);
                intent.putExtra("id", nameArr.get(i));
                startActivity(intent);
            }
        });

    }

    // Image Photo 받는 Activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                Log.i("Photo", "Received");

                // 받은 비트맵 파일을 압축, parse Server에 보내는 것.(Image Class
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                ParseFile file = new ParseFile("image.png", byteArray);
                ParseObject parseObject = new ParseObject("Image");
                parseObject.put("image", file);
                parseObject.put("username", MainActivity.idInput);

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(UserListActivity.this, "Image Shared!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(UserListActivity.this, "Image Not Shared - Please Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

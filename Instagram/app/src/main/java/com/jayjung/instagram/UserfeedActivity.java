package com.jayjung.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserfeedActivity extends AppCompatActivity {

    LinearLayout linearLayout;

    public void addImage(byte[] file) {
        //TODO;
        // TODO linearLayout에 image 추가

        Bitmap bitmap = BitmapFactory.decodeByteArray(file, 0, file.length);
        ImageView addedImage = new ImageView(this);
        addedImage.setImageBitmap(bitmap);
        linearLayout.addView(addedImage);

        // imageview의 margin 설정 (서로 띄워놓기 위해서)
        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams)addedImage.getLayoutParams();
        param.setMargins(0, 0, 0, 20);
        addedImage.requestLayout();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userfeed);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        getSupportActionBar().setTitle(id+"'s Feed");
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        // 서버의 Image Class에서 id와 같은 username을 가진 row의 이미지를 가져와, linearlayout에 추가
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        query.whereEqualTo("username", id);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects != null) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject object = objects.get(i);
                        ParseFile file = object.getParseFile("image");
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                addImage(data);
                            }
                        });
                    }


                } else {
                    Log.i("Image Show on User Feed", "Failed");
                    e.printStackTrace();
                }

            }
        });

    }
}

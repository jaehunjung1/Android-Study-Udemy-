package com.jayjung.timetable;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView test = (TextView)findViewById(R.id.test);
        TextView test3 = (TextView)findViewById(R.id.test3);
        GridLayout.LayoutParams params = (GridLayout.LayoutParams)test.getLayoutParams();
        params.rowSpec = GridLayout.spec(0, 2, GridLayout.FILL);
        test.setLayoutParams(params);
        test.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
        test3.setVisibility(View.VISIBLE);  //이거 안하면 위의 cell rowspan 변경했을시 자동으로 invisible로 바뀜.

    }
}

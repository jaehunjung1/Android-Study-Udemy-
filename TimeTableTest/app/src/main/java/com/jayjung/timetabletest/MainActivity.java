package com.jayjung.timetabletest;

import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    GridLayout gridLayout;
    ArrayList<Integer> addedCellArr; // 추가된 cell의 id를 가지고 있는 ArrayList (그 순서 : 추가한 순서)
    int randomNumber;


    protected void setCellSize() {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;

        for (int index = 0; index < gridLayout.getChildCount(); index++) {
            TextView cell = (TextView)gridLayout.getChildAt(index);
            cell.setMaxWidth((int)(displayWidth*(0.9)*(0.166)/1));
            cell.setMinWidth((int)(displayWidth*(0.9)*(0.166)/1));
            if(index %6 !=0){
                cell.setMaxHeight((int)(displayWidth*(0.8)*(0.04)/1));
                cell.setMinHeight((int)(displayWidth*(0.8)*(0.04)/1));
            }

        }
    }


    protected void addCell(int row, int col, int spanSize, String lectureName) {

        TextView overLapCell = new TextView(MainActivity.this);

        randomNumber = new Random().nextInt(6) + 1;
        int randomColor = getResources().getIdentifier("tableColor" + randomNumber, "color", getApplicationContext().getPackageName());

        overLapCell.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), randomColor));

        int cellId = View.generateViewId();
        overLapCell.setId(cellId);
        addedCellArr.add(cellId);

        overLapCell.setText(lectureName);
        overLapCell.setGravity(Gravity.CENTER);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row, spanSize, GridLayout.FILL, 1f);
        layoutParams.columnSpec = GridLayout.spec(col, 1, GridLayout.FILL, 1f);
        overLapCell.setLayoutParams(layoutParams);

        gridLayout.addView(overLapCell);
    }

    protected void deleteCell(int addedIndex) { // ex. deleteCell(3) -> 두번째로 추가한 강의를 삭제해라
        int cellId = addedCellArr.get(addedIndex);

        TextView cellToDelete = (TextView)findViewById(cellId);

        gridLayout.removeView(cellToDelete);
        addedCellArr.remove(addedIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        addedCellArr = new ArrayList<Integer>();


        addCell(3,2,4, "HaHa Course"); // 좌표 (1,1)에, row로 4칸짜리 cell을 haha course란 text 넣어서 만들어라
        addCell(5, 1, 6, "HaHa Course 3");
        addCell(2,2, 5, "celltodelete");
        deleteCell(2); // 세번째로 추가한 강좌를 삭제해라
        System.out.println(addedCellArr.get(0));

        setCellSize(); // text가 길어도 cell size 바뀌지 않도록 하는 함수





    }
}
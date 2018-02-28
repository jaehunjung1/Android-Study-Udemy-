package com.jayjung.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button button1, button2, button3, button4;
    TextView timerText, questionText, scoreText, gameoverText;
    LinearLayout linearLayout;

    int first, next, answer; //Question's first, next number and answer
    int[] arr; //Number's for buttons
    Button[] buttonArr = new Button[4];

    public void start(View view) {
        timerText.setText("30s");
        scoreText.setText("0/0");
        view.setVisibility(View.INVISIBLE);
        update();
        new CountDownTimer(30000, 1000 ) {
            @Override
            public void onTick(long leftTime) {
                String text = (leftTime/1000)+"s";
                timerText.setText(text);
            }

            @Override
            public void onFinish() {
                timerText.setText("0s");
                String text = scoreText.getText().toString();
                gameoverText.setText(gameoverText.getText().toString()+text);
                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);
                button4.setEnabled(false);
                linearLayout.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    // (정답 제출하면) Question 생성, Button Update
    private void update() {
        Random random = new Random();
        first = random.nextInt(50)+1;
        next = random.nextInt(50)+1;

        String question = String.format("%d + %d", first, next);
        answer = first + next;

        questionText.setText(question);

        arr = new int[4];
        for (int i = 0; i < arr.length; i++)
            arr[i] = answer+random.nextInt(30)-15;

        // 정답 버튼 생성
        int answerIndex = random.nextInt(4);

        for (int i = 0; i < buttonArr.length; i++) {
            if (i == answerIndex)
                buttonArr[i].setText(Integer.toString(answer));
            else
                buttonArr[i].setText(Integer.toString(arr[i]));
        }
    }

    // 버튼 누르면 정답이면 점수 업데이트, 아니면 문제 수만 올라가고, update() 실행
    public void answer(View view) {
        int myAnswer = Integer.parseInt(((Button)view).getText().toString());
        int curScore = Integer.parseInt(scoreText.getText().toString().split("/")[0]);
        int curQuestion = Integer.parseInt(scoreText.getText().toString().split("/")[1]);
        String score;

        if (myAnswer == answer) {
            score = (++curScore)+"/"+(++curQuestion);
        } else {
            score = curScore+"/"+(++curQuestion);
        }
        scoreText.setText(score);

        update();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View Initialization
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        linearLayout.setVisibility(View.INVISIBLE);

        buttonArr[0] = button1 = (Button)findViewById(R.id.button1);
        buttonArr[1] = button2 = (Button)findViewById(R.id.button2);
        buttonArr[2] = button3 = (Button)findViewById(R.id.button3);
        buttonArr[3] = button4 = (Button)findViewById(R.id.button4);


        timerText = (TextView)findViewById(R.id.timerText);
        questionText = (TextView)findViewById(R.id.questionText);
        scoreText = (TextView)findViewById(R.id.scoreText);
        gameoverText = (TextView)findViewById(R.id.gameoverText);


    }
}

package com.jayjung.demoapp;

        import android.app.Activity;
        import android.os.CountDownTimer;
        import android.os.Handler;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.SeekBar;

        import java.util.ArrayList;
        import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Method 1 : CountDownTimer
        new CountDownTimer(10000, 1000) { //CountDown every second for 10 seconds;

            public void onTick(long millisecondsUntilDone) {
                Log.i("Seconds Left", String.valueOf(millisecondsUntilDone/1000));
            }

            public void onFinish() {
                Log.i("Done!", "CountDown Timer Finished");
            }
        }.start();

        // Method 2 : Handler
        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {

                //Now Code that has to be recurred
                Log.i("Runnable", "Second");
                handler.postDelayed(this, 1000);



            }
        };
        handler.post(run);


    }
}
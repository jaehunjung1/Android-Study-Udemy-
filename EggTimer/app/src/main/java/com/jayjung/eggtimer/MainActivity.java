package com.jayjung.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static int time;
    TextView timeText;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;

    public void start(View view) {
        timer();
    }

    public void restart(View view) {
        timeText.setText("00:00");
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        linearLayout.setVisibility(View.INVISIBLE);
        findViewById(R.id.start).setVisibility(View.VISIBLE);
        seekBar.setEnabled(true);
    }

    private void timer() {
        seekBar.setEnabled(false);
        new CountDownTimer(time*1000, 1000) {
            @Override
            public void onTick(long leftmilliseconds) {
                String timeStr = String.format("%02d:%02d", leftmilliseconds/(1000*60), (leftmilliseconds/1000)%60);
                timeText.setText(timeStr);
            }

            @Override
            public void onFinish() {
                mediaPlayer.start();
                seekBar.setProgress(0);
                findViewById(R.id.start).setVisibility(View.INVISIBLE);
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
                linearLayout.bringToFront();
                linearLayout.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start).bringToFront();
        // MediaPlayer Initalize
        mediaPlayer = MediaPlayer.create(this, R.raw.airhorn);

        // Time Text Initialize
        timeText = (TextView)findViewById(R.id.timeText);
        timeText.setText("00:00");

        // SeekBar Initialize -> Time Text Showing
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setMax(600); // maximum : 600 seconds (10 minutes)
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean b) {
                time = progress;
                String timeStr = String.format("%02d:%02d", time / 60, time % 60);
                timeText.setText(timeStr);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}

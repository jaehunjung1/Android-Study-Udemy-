package com.jayjung.gameconnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static int activePlayer = 0; //red = 1, yellow = 0;
    static int[] gameState = {2,2,2,2,2,2,2,2,2}; //2 means unplayed;
    static int[][] winningPos = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};
    static boolean gameisActive = true;

    public void dropin(View view) {
        ImageView counter = (ImageView) view;
        int counterLoc = Integer.parseInt(counter.getTag().toString());

        if (gameState[counterLoc] == 2 && gameisActive) {

            gameState[counterLoc] = activePlayer;
            counter.setTranslationY(-1000f);

            if (activePlayer == 0) {
                counter.setImageResource(R.drawable.red);
                activePlayer = 1;

            } else {
                counter.setImageResource(R.drawable.yellow);
                activePlayer = 0;
            }
            counter.animate().translationYBy(1000f).setDuration(300);

            for (int i = 0; i < winningPos.length; i++) {

                if (gameState[winningPos[i][0]]!=2 &&
                        gameState[winningPos[i][0]] == gameState[winningPos[i][1]] &&
                        gameState[winningPos[i][0]] == gameState[winningPos[i][2]]) {
                    gameisActive = false;
                    String won = gameState[winningPos[i][0]] == 0? "Red" : "Yellow";
                    TextView message = (TextView)findViewById(R.id.message);
                    message.setText(won+" has won!");
                    LinearLayout playagainLayout = (LinearLayout)findViewById(R.id.playagainLayout);
                    playagainLayout.bringToFront();
                    playagainLayout.setVisibility(View.VISIBLE);
                    return;
                }

            }
            for (int i : gameState)
                if (i == 2) return;
            TextView message = (TextView)findViewById(R.id.message);
            message.setText("It's draw.");
            LinearLayout playagainLayout = (LinearLayout)findViewById(R.id.playagainLayout);
            playagainLayout.bringToFront();
            playagainLayout.setVisibility(View.VISIBLE);
        }
    }

    public void regame(View view) {
        gameisActive = true;
        LinearLayout playagainLayout = (LinearLayout)findViewById(R.id.playagainLayout);
        playagainLayout.setVisibility(View.INVISIBLE);

        for (int i = 0; i < gameState.length; i++) gameState[i] = 2;
        activePlayer = 0;

        GridLayout gridLayout = (GridLayout)findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ((ImageView)gridLayout.getChildAt(i)).setImageResource(0);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

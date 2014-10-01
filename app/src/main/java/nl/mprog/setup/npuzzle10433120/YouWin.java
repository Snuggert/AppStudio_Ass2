package nl.mprog.setup.npuzzle10433120;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Set;

/*
 * Abe Wiersma
 * 10433120
 * abe.wiersma@hotmail.nl
 */

public class YouWin extends Activity {

    TextView timeSpentView;
    TextView movesView;
    TextView highScoreView;
    TextView highMovesView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youwin_activity);

        int highscoreTimeSpent, timeSpent, highscoreMoves, moves;
        String difficulty;

        Intent mIntent = getIntent();
        timeSpent = mIntent.getIntExtra("timeSpent", 0);
        moves = mIntent.getIntExtra("moves", 0);
        difficulty = mIntent.getStringExtra("difficulty");

        timeSpentView = (TextView) findViewById(R.id.scoreView);
        movesView = (TextView) findViewById(R.id.movesView);

        highScoreView = (TextView) findViewById(R.id.highScore);
        highMovesView = (TextView) findViewById(R.id.highMoves);

        timeSpentView.setText("Score: " + timeSpent + " sec");
        movesView.setText("Moves: " + moves);

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String highscore = sharedPref.getString(difficulty, "99999,99999");
        if(highscore != null) {
            highscoreTimeSpent = Integer.parseInt(highscore.split(",")[0]);
            highscoreMoves = Integer.parseInt(highscore.split(",")[1]);
            if(highscoreTimeSpent > timeSpent){
                editor.clear();
                editor.putString(difficulty, timeSpent + "," + moves);
                editor.commit();
                highScoreView.setText("Score: " + timeSpent + " sec");
                highMovesView.setText("Moves: " + moves);
            }else{
                highScoreView.setText("Score: " + highscoreTimeSpent + " sec");
                highMovesView.setText("Moves: " + highscoreMoves);
            }
        }
    }
}

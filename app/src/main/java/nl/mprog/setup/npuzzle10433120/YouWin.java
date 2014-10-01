package nl.mprog.setup.npuzzle10433120;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

/*
 * Abe Wiersma
 * 10433120
 * abe.wiersma@hotmail.nl
 */

public class YouWin extends ActionBarActivity {

    TextView timeSpentView;
    TextView movesView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youwin_activity);

        Intent mIntent = getIntent();
        int timeSpent = mIntent.getIntExtra("timeSpent", 0);
        int moves = mIntent.getIntExtra("moves", 0);

        timeSpentView = (TextView) findViewById(R.id.scoreView);
        movesView = (TextView) findViewById(R.id.movesView);

        timeSpentView.setText("Score: " + timeSpent + " sec");
        movesView.setText("Moves: " + moves);
    }
}

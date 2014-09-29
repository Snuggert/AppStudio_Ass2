/* Abe Wiersma 10433120 */

package nl.mprog.setup.npuzzle10433120;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    public void startGame(View view){
        Intent gameIntent = new Intent(MainActivity.this, GamePlay.class);
        startActivity(gameIntent);
    }
}

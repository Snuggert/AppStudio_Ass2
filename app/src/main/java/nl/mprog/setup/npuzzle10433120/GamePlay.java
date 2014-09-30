package nl.mprog.setup.npuzzle10433120;

/*
 * Abe Wiersma
 * 10433120
 * abe.wiersma@hotmail.nl
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class GamePlay extends ActionBarActivity {

    private static final int GALLERY = 1;
    private final CharSequence difs[]
            = new CharSequence[] {"easy", "medium", "hard"};
    private final int[] sizes = {3,4,5};

    private static ImageAdapter imageAdapter;
    private static GridView gridView;
    private static AlertDialog.Builder builder;
    private static TextView finishText;
    private static MenuItem gameButton;
    private static Intent galleryIntent;

    private static int whichIndex;
    private static boolean started;
    private static int moves;
    private static long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        whichIndex = 1;
        moves = 0;
        started = false;

        finishText = (TextView) findViewById(R.id.finishView);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setNumColumns(4);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
            if(imageAdapter.trySwitch(position) && started){
                moves ++;
                imageAdapter.notifyDataSetChanged();
                if(imageAdapter.isFinished()){
                    finishText.setText("Finished " +
                                       stopTimer(startTime) +
                                       " seconden.");
                    started = false;
                }
            }
            }
        });

        imageAdapter = new ImageAdapter(this);
        imageAdapter.setSize(4, 4);
        gridView.setAdapter(imageAdapter);

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a difficulty");
        builder.setSingleChoiceItems(difs, whichIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                whichIndex = which;
                gridView.setNumColumns(sizes[which]);
                imageAdapter.setSize(sizes[which], sizes[which]);
                imageAdapter.notifyDataSetChanged();
            }
        });

        galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu, add items to the action bar if present. */
        getMenuInflater().inflate(R.menu.main, menu);
        gameButton = (MenuItem) findViewById(R.id.game_button);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * Handle action bar item clicks here. The action bar will
         * automatically handle clicks on the Home/Up button, so long
         * as you specify a parent activity in AndroidManifest.xml.
         */
        int id = item.getItemId();
        if (id == R.id.change_difficulty) {
            builder.show();
        }else if (id == R.id.change_picture) {
            startActivityForResult(
                Intent.createChooser(galleryIntent, "Select Picture"), GALLERY);
        }else if (id == R.id.game_button) {
            if(started){
                resetGame();
            }else{
                startGame();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /* Method that is called after an Intent returns a result. */
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data){
        /*
         * When the intent that returns was the pickGallery intent handle the
         * new picture.
         */
        if (requestCode == GALLERY && resultCode != 0) {
            Uri mImageUri = data.getData();
            try {
                Bitmap Image = MediaStore.Images.Media.getBitmap(
                    this.getContentResolver(), mImageUri);
                imageAdapter.setBitmap(Image, gridView.getWidth(),
                                       gridView.getHeight());
                imageAdapter.notifyDataSetChanged();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * Method wrapper in which all methods called when starting a game are
     * present.
     */
    private void startGame(){
        started = true;

        imageAdapter.shuffleBitmap(1000);
        imageAdapter.notifyDataSetChanged();

        startTime = startTimer();

        if(gameButton != null)
            gameButton.setTitle("Reset");
    }

    /*
     * Method wrapper in which all methods called when resetting a game are
     * present.
     */
    private void resetGame(){
        started = false;

        imageAdapter.resetBitmap();
        imageAdapter.notifyDataSetChanged();

        finishText.setText("");

        if(gameButton != null)
            gameButton.setTitle("Start");
    }

    /* Method to start a timer. */
    private long startTimer(){
        return SystemClock.uptimeMillis();
    }

    /* Method the stop the timer based on a previous start time. */
    private long stopTimer(long startTime){
        return SystemClock.uptimeMillis() - startTime;
    }
}

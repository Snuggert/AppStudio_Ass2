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

    private static int whichIndex;
    private static boolean started;
    private static long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        whichIndex = 1;
        started = false;

        finishText = (TextView) findViewById(R.id.finishView);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setNumColumns(4);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
            if(imageAdapter.trySwitch(position)){
                imageAdapter.notifyDataSetChanged();
                if(imageAdapter.isFinished()){
                    long endTime = stopTimer(startTime);
                    finishText.setText("Finished " + (endTime / 1000) + " seconden.");

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        gameButton = (MenuItem) findViewById(R.id.game_button);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.change_difficulty) {
            builder.show();
        }else if (id == R.id.change_picture) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"), GALLERY);
        }else if (id == R.id.game_button) {
            if(this.started){
                this.started = false;

                if(gameButton != null)
                    gameButton.setTitle("Start");

                imageAdapter.resetBitmap();
                imageAdapter.notifyDataSetChanged();
                finishText.setText("");
                startTime = startTimer();
            }else{
                this.started = true;
                imageAdapter.shuffleBitmap(1000);
                imageAdapter.notifyDataSetChanged();
                if(gameButton != null)
                    gameButton.setTitle("Reset");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data){
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

    private long startTimer(){
        return SystemClock.uptimeMillis();
    }

    private long stopTimer(long startTime){
        return SystemClock.uptimeMillis() - startTime;
    }
}

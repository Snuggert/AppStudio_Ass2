package nl.mprog.setup.npuzzle10433120;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class GameActivity extends ActionBarActivity {

    private static final int GALLERY = 1;
    private static ImageAdapter imageAdapter;
    private static GridView gridView;
    private static int nCols = 4;
    private static int nRows = 4;
    private static int whichIndex = 1;
    private final CharSequence difs[]
        = new CharSequence[] {"easy", "medium", "hard"};
    private final int[] sizes = {3,4,5};
    private static boolean started = false;
    private static AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setNumColumns(nCols);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
            }
        });

        imageAdapter = new ImageAdapter(this);
        imageAdapter.setSize(nCols, nRows);
        gridView.setAdapter(imageAdapter);

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a difficulty");
        builder.setSingleChoiceItems(difs, whichIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                whichIndex = which;
                nCols = sizes[which];
                nRows = sizes[which];
                gridView.setNumColumns(nCols);
                imageAdapter.setSize(nCols, nRows);
                gridView.setAdapter(imageAdapter);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
                imageAdapter.resetBitmap();
                gridView.setAdapter(imageAdapter);
                this.started = !this.started;
            }else{
                this.started = true;
                imageAdapter.shuffleBitmap();
                gridView.setAdapter(imageAdapter);
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
                gridView.setAdapter(imageAdapter);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

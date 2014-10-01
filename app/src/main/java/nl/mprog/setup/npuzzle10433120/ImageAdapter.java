package nl.mprog.setup.npuzzle10433120;

/*
 * Abe Wiersma
 * 10433120
 * abe.wiersma@hotmail.nl
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ImageAdapter extends BaseAdapter {
    long seed;
    private Context mContext;
    private Bitmap gridBitmap;
    private ArrayList<GridData> dataArray = new ArrayList<GridData>();
    private int nRows, nCols;

    /* Constructor for the ImageAdapter. */
    public ImageAdapter(Context c) {
        seed = System.nanoTime();
        mContext = c;
    }

    public int getCount() {
        return nRows * nCols;
    }

    public GridData getItem(int position) {
        return dataArray.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    /* Check if a switch is allowed. */
    public boolean trySwitch(int position){
        int left, top, right, bottom;

        left = position - 1;
        top = position - nRows;
        right = position + 1;
        bottom = position + nRows;

        /* Check if a move is allowed. If not return false */
        if(left >= 0 && (position % nRows != 0 && position != 0)
            && dataArray.get(left).empty){
            Collections.swap(dataArray, left, position);
            return true;
        }else if(top >= 0 && dataArray.get(top).empty){
            Collections.swap(dataArray, top, position);
            return true;
        } else if(right % nRows != 0 && dataArray.get(right).empty){
            Collections.swap(dataArray, right, position);
            return true;
        } else if(bottom < nRows * nCols && dataArray.get(bottom).empty){
            Collections.swap(dataArray, bottom, position);
            return true;
        }
        return false;
    }

    /* Set the bitmap of the adapter to a new bitmap. */
    public void setBitmap(Bitmap bitmap, int width, int height){
        this.gridBitmap = createRatioScaledBitmap(bitmap, width, height);
        if(gridBitmap != null){
            divideBitmap();
        }
    }

    /* Method to create scaled bitmaps maintaining ratio. */
    public Bitmap createRatioScaledBitmap(Bitmap bitmap, int width, int height){
        Bitmap background = Bitmap.createBitmap((int)width, (int)height,
                                                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        Matrix transformation = new Matrix();

        float originalWidth = bitmap.getWidth();
        float originalHeight = bitmap.getHeight();
        float scale;

        /* Scale according to largest side length. */
        if(originalWidth > originalHeight){
            scale = width/originalWidth;
        }else{
            scale = height/originalHeight;
        }
        float xTranslation = (width - originalWidth * scale)/ 2.0f ;
        float yTranslation = (height - originalHeight * scale)/2.0f;

        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        canvas.drawBitmap(bitmap, transformation, paint);
        return background;
    }

    /* Reset the bitmap when the gridBitmap isn't null. */
    public void resetBitmap(){
        if(gridBitmap != null){
            divideBitmap();
        }
    }

    /*
     * Method to shuffle the tiles of the gridview, it does times tries to move
     * in a random direction.
     */
    public void shuffleBitmap(int times){
        int directionIndex, randomIndex, direction, dataIndex;

        Random random = new Random();
        dataIndex = (nCols * nRows) - 1;
        int directions[] = {-1, -nRows, 1, nRows};

        for(int index = 0; index < times; index++){
            /* Get a random direction. */
            directionIndex = random.nextInt(4);
            direction =  directions[directionIndex];

            /* The new randomIndex is the dataIndex + the random direction. */
            randomIndex = dataIndex + direction;

            /*
             * Check for all directions and the bounds that are coupled with
             * these directions.
             */
            if(direction == -1 && randomIndex >= 0 &&
                (randomIndex % nRows != 0 && randomIndex != 0)){
                Collections.swap(dataArray, randomIndex, dataIndex);
            } else if(direction == -nRows && randomIndex >= 0){
                Collections.swap(dataArray, randomIndex, dataIndex);
            } else if(direction  == 1 && randomIndex % nRows != 0){
                Collections.swap(dataArray, randomIndex, dataIndex);
            } else if(direction == nRows && randomIndex < nRows * nCols){
                Collections.swap(dataArray, randomIndex, dataIndex);
            } else{
                continue;
            }
            /* The dataIndex changes to randomIndex when swapping is allowed. */
            dataIndex = randomIndex;
        }
    }

    /*
     * Set the new size of the gridView, so the bitmap can be redivided
     * correctly.
     */
    public void setSize(int cols, int rows){
        this.nCols = cols;
        this.nRows = rows;
        if(gridBitmap != null){
            divideBitmap();
        }
    }

    /* Method to check if all tiles are in their right places*/
    public boolean isFinished(){
        for(int index = 0; index < nCols * nRows; index++){
            if(dataArray.get(index).position != index){
                return false;
            }
        }
        return true;
    }

    /* Method to divide the bitmap into equally sized tiles. */
    private void divideBitmap(){
        int width, height;
        /* Empty the ArrayList. */
        this.dataArray.clear();

        width = gridBitmap.getWidth() / nCols;
        height = gridBitmap.getHeight() / nRows;

        /* Loop through the tiles and fill all bitmaps but the last. */
        for(int index = 0; index < (nCols * nRows) - 1; index++){
            this.dataArray.add(new GridData(
                Bitmap.createBitmap(gridBitmap, (index % nCols) * width,
                    (index / nRows) * height, width, height), index, false));
        }
        /* Fill the last entry of the dataArray with a white colored bitmap. */
        this.dataArray.add(new GridData(
                Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444),
                                    (nCols * nRows) - 1, true));
    }

    /* Create a new ImageView for each item referenced by the Adapter. */
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }
        if(this.gridBitmap != null) {
            imageView.setImageBitmap(this.dataArray.get(position).picture);
        }
        return imageView;
    }
}
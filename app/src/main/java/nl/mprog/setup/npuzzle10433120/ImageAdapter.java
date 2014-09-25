package nl.mprog.setup.npuzzle10433120;

import android.content.Context;
import android.graphics.Bitmap;
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

    // Constructor
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

    public boolean trySwitch(int position){
        int left, top, right, bottom;
        left = position - 1;
        top = position - nRows;
        right = position + 1;
        bottom = position + nRows;
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

    public void setBitmap(Bitmap bitmap, int width, int height){
        this.gridBitmap =
                Bitmap.createScaledBitmap(bitmap, width, height, false);
        if(gridBitmap != null){
            divideBitmap();
        }
    }

    public void resetBitmap(){
        if(gridBitmap != null){
            divideBitmap();
        }
    }

    public void shuffleBitmap(int times){
        int directionIndex, randomIndex, direction;

        Random random = new Random();
        int dataIndex = (nCols * nRows) - 1;
        int directions[] = {-1, -nRows, 1, nRows};

        for(int index = 0; index < times; index++){
            directionIndex = random.nextInt(4);
            direction =  directions[directionIndex];
            randomIndex = dataIndex + direction;

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
            dataIndex = randomIndex;
        }
    }

    public void setSize(int cols, int rows){
        this.nCols = cols;
        this.nRows = rows;
        if(gridBitmap != null){
            divideBitmap();
        }
    }

    public boolean isFinished(){
        for(int index = 0; index < nCols * nRows; index++){
            if(dataArray.get(index).position != index){
                return false;
            }
        }
        return true;
    }

    private void divideBitmap(){
        int width, height;
        this.dataArray.clear();

        width = gridBitmap.getWidth() / nCols;
        height = gridBitmap.getHeight() / nRows;
        for(int index = 0; index < (nCols * nRows) - 1; index++){
            this.dataArray.add(new GridData(
                Bitmap.createBitmap(gridBitmap, (index % nCols) * width,
                    (index / nRows) * height, width, height), index, false));
        }
        this.dataArray.add(new GridData(
                Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444),
                                    (nCols * nRows) - 1, true));
    }

    // create a new ImageView for each item referenced by the Adapter
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
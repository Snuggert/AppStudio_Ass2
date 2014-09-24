package nl.mprog.setup.npuzzle10433120;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Bitmap gridBitmap;
    private Bitmap[] gridBitmapArray;
    private ArrayList<GridData> dataArray = new ArrayList<GridData>();
    private int nRows, nCols;

    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return nRows * nCols;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;

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

    public void setSize(int cols, int rows){
        this.nCols = cols;
        this.nRows = rows;
        if(gridBitmap != null){
            divideBitmap();
        }
    }

    private void divideBitmap(){
        int width, height;
        this.dataArray.clear();

        width = gridBitmap.getWidth() / nCols;
        height = gridBitmap.getHeight() / nRows;
        for(int index = 0; index < (nCols * nRows) - 1; index++){
            this.dataArray.add(new GridData(
                Bitmap.createBitmap(gridBitmap, (index % nCols) * width,
                    (index / nRows) * height, width, height), index));
        }
        this.dataArray.add(new GridData(
                Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444),
                                    (nCols * nRows) - 1));
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
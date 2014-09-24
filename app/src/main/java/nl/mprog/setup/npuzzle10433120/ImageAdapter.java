package nl.mprog.setup.npuzzle10433120;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by TheAbe on 24-Sep-14.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Bitmap gridBitmap;
    private Bitmap[] gridBitmapArray;
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

    public void setBitmap(Bitmap bitmap){
        this.gridBitmap = bitmap;
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

    private int divideBitmap(){
        int width, height;
        this.gridBitmapArray = new Bitmap[nCols * nRows];

        width = gridBitmap.getWidth() / nCols;
        height = gridBitmap.getHeight() / nRows;
        for(int index = 0; index < nCols * nRows; index++){
            this.gridBitmapArray[index] =
                Bitmap.createBitmap(gridBitmap, (index % nCols) * width,
                                    (index / nRows) * height, width, height);
        }
        return 1;
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
            imageView.setImageBitmap(this.gridBitmapArray[position]);
        }
        return imageView;
    }
}

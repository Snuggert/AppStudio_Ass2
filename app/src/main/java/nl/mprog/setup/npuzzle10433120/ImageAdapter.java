package nl.mprog.setup.npuzzle10433120;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by TheAbe on 24-Sep-14.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Bitmap gridBitmap;

    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return 16;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public int setBitmap(Bitmap bitmap){
        this.gridBitmap = bitmap;
        return 1;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(this.gridBitmap);
        return imageView;
    }
}

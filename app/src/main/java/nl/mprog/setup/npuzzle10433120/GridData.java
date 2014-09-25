package nl.mprog.setup.npuzzle10433120;

import android.graphics.Bitmap;

/**
 * Created by TheAbe on 25-Sep-14.
 */
public class GridData {
        public Bitmap picture;
        public int position;
        public boolean empty;
    public GridData(Bitmap picture, int position, boolean empty){
        this.picture = picture;
        this.position = position;
        this.empty = empty;
    }
}

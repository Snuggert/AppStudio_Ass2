package nl.mprog.setup.npuzzle10433120;

import android.graphics.Bitmap;

/*
 * Abe Wiersma
 * 10433120
 * abe.wiersma@hotmail.nl
 */

/* Class to store data assigned to a tile bitmap. */
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

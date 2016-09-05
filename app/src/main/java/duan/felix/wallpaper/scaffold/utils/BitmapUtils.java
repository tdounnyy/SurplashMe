package duan.felix.wallpaper.scaffold.utils;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * @author Felix.Duan.
 */

public class BitmapUtils {

    private static final String TAG = "BitmapUtils";

    // TODO: ** Tall image doesn't fit wide wallpaper as expected
    public static Bitmap cropCenterInside(Bitmap src, Rect rect) {
        int width = Math.min(src.getWidth(), rect.width());
        int height = Math.min(src.getHeight(), rect.height());
        int[] rowData = new int[width];
        int stride = src.getWidth();
        int x = (src.getWidth() - width) / 2;
        int y = (src.getHeight() - height) / 2;

        Bitmap dest = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int delta = 0;
        while (delta < height) {
            src.getPixels(rowData, 0, stride, x, y + delta, width, 1);
            dest.setPixels(rowData, 0, stride, 0, delta, width, 1);
            delta++;
        }
        return dest;
    }

    public static Bitmap resizeOuterFit(Bitmap src, Rect rect) {

        float aspectRatio = Math.max((float) rect.width() / src.getWidth(),
                (float) rect.height() / src.getHeight());
        int newWidth = (int) (src.getWidth() * aspectRatio);
        int newHeight = (int) (src.getHeight() * aspectRatio);
        return Bitmap.createScaledBitmap(src, newWidth, newHeight, false);
    }

}

package duan.felix.wallpaper.scaffold.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Felix.Duan.
 */

public class ToastUtils {

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}

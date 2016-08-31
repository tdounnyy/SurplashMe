package duan.felix.wallpaper.scaffold.utils;

import android.util.Log;

/**
 * @author Felix.Duan.
 */

public class LogUtils {

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        Log.e(tag, msg, throwable);
    }
}

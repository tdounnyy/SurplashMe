package duan.felix.wallpaper.scaffold.utils;

import android.util.Log;

import duan.felix.wallpaper.scaffold.event.Bus;
import duan.felix.wallpaper.scaffold.event.ToastEvent;

/**
 * @author Felix.Duan.
 */

public class LogUtils {

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        Log.e(tag, msg, throwable);
        Bus.post(new ToastEvent(String.format("Err at %s: %s", tag, throwable)));
    }
}

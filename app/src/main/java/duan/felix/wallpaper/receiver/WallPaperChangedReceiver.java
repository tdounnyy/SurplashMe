package duan.felix.wallpaper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import duan.felix.wallpaper.scaffold.event.Bus;
import duan.felix.wallpaper.scaffold.event.ToastEvent;
import duan.felix.wallpaper.scaffold.utils.LogUtils;

/**
 * @author Felix.Duan.
 */

public class WallPaperChangedReceiver extends BroadcastReceiver {
    private static final String TAG = "WallPaperChangedReceive";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: make a toast to restore previous wallpaper
        LogUtils.d(TAG, "WallPaperChanged");
        Bus.post(new ToastEvent("Wallpaper changed"));
    }
}

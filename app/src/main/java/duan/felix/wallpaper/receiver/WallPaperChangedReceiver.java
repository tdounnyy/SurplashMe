package duan.felix.wallpaper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import duan.felix.wallpaper.core.event.InvokeHomeEvent;
import duan.felix.wallpaper.scaffold.event.Bus;
import duan.felix.wallpaper.scaffold.event.ToastEvent;
import duan.felix.wallpaper.scaffold.utils.LogUtils;

/**
 * @author Felix.Duan.
 */

public class WallPaperChangedReceiver extends BroadcastReceiver {
    private static final String TAG = "WallPaperChangedReceiver";

    private static final int LAUNCH_DELAY = 2000;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: * make a toast to restore previous wallpaper
        LogUtils.d(TAG, "changed");
        Bus.post(new ToastEvent("Wallpaper changed"));

        // TODO: ** move delay into the nice transition process
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bus.post(new InvokeHomeEvent());
            }
        }, LAUNCH_DELAY);
    }
}

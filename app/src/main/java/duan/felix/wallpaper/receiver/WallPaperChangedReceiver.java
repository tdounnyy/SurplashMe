package duan.felix.wallpaper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import duan.felix.wallpaper.scaffold.utils.LogUtils;
import duan.felix.wallpaper.scaffold.utils.ToastUtils;

/**
 * @author Felix.Duan.
 */

public class WallPaperChangedReceiver extends BroadcastReceiver {
    private static final String TAG = "WallPaperChangedReceive";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: make a toast to restore previous wallpaper
        LogUtils.d(TAG, "WallPaperChanged");
        ToastUtils.toast(context, "wallPaperChanged");
    }
}

package duan.felix.wallpaper.scaffold.app;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import duan.felix.wallpaper.receiver.WallPaperChangedReceiver;

/**
 * @author Felix.Duan.
 */

public class MyApplication extends Application {

    private Global mGlobal;

    @Override
    public void onCreate() {
        super.onCreate();
        mGlobal = new Global(this);
        registerReceiver(new WallPaperChangedReceiver(),
                new IntentFilter(Intent.ACTION_WALLPAPER_CHANGED));
    }

}

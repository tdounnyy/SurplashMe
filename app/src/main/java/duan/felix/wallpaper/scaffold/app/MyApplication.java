package duan.felix.wallpaper.scaffold.app;

import android.app.Application;

/**
 * @author Felix.Duan.
 */

public class MyApplication extends Application {

    private Global mGlobal;

    @Override
    public void onCreate() {
        super.onCreate();
        mGlobal = new Global(this);
    }
}

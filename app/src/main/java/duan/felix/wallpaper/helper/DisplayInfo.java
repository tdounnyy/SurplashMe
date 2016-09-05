package duan.felix.wallpaper.helper;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.utils.LogUtils;

/**
 * @author Felix.Duan.
 */

public class DisplayInfo {

    private static final String TAG = "DisplayInfo";

    private Rect screenRect;
    private Rect rootViewRect;
    private Rect appRect;

    /**
     * NO_NAV:              screenRect = rootViewRect = appRect
     * NAV:                 screenRect > rootViewRect = appRect
     * TRANSLUCENT_NAV:     screenRect = rootViewRect > appRect
     */
    enum Type {
        NO_NAV, NAV, TRANSLUCENT_NAV
    }

    Type mType;

    public DisplayInfo() {
    }

    public void update(View view) {
        WindowManager windowManager = (WindowManager) Global.App.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        Point point = new Point();
        display.getRealSize(point);
        screenRect = new Rect(0, 0, point.x, point.y);

        View rootView = view.getRootView();
        rootViewRect = new Rect(0, 0, rootView.getWidth(), rootView.getHeight());

        appRect = new Rect();
        display.getRectSize(appRect);

        determineType();
    }

    private void determineType() {
        mType = !screenRect.equals(rootViewRect) ? Type.NAV :
                rootViewRect.equals(appRect) ? Type.NO_NAV
                        : Type.TRANSLUCENT_NAV;
    }

    public Rect getScreenRect() {
        return screenRect;
    }

    public Rect getRootViewRect() {
        return rootViewRect;
    }

    public Rect getAppRect() {
        return appRect;
    }

    public Type getType() {
        return mType;
    }

    public void print() {
        LogUtils.d(TAG, "screenRect:\t" + getScreenRect());
        LogUtils.d(TAG, "rootViewSize:\t" + getRootViewRect());
        LogUtils.d(TAG, "appSize:\t\t" + getAppRect());
        LogUtils.d(TAG, "type:\t\t" + getType());
    }

    @Override
    public String toString() {
        return "DisplayInfo{" +
                "screenRect=" + screenRect +
                ", rootViewRect=" + rootViewRect +
                ", appRect=" + appRect +
                ", mType=" + mType +
                '}';
    }
}

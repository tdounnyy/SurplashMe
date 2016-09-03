package duan.felix.wallpaper.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import duan.felix.wallpaper.widget.PhotoItemContainer;

public class FloatService extends Service {

    private static final String TAG = "FloatService";

    public static final String EXTRA_PHOTO = "extra_photo";

    private WindowManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d(TAG, "onCreate");
        manager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(TAG, "onStartCommand");

        Photo photo = intent.getParcelableExtra(EXTRA_PHOTO);
        addFloatView(photo);

        return super.onStartCommand(intent, flags, startId);
    }

    private View addFloatView(Photo photo) {

        PhotoItemContainer view = (PhotoItemContainer) LayoutInflater.from(this).inflate(R.layout.photo_item, null);
        view.setFloating(true);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                0, 0,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                PixelFormat.TRANSLUCENT);
        manager.addView(view, params);
        view.setPhoto(photo);
        return view;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

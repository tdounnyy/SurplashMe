package duan.felix.wallpaper.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import duan.felix.wallpaper.R;
import duan.felix.wallpaper.scaffold.utils.LogUtils;

public class FloatService extends Service {

    private static final String TAG = "FloatService";
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

        final View view = buildFloatView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.removeView(view);
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    private View buildFloatView() {

        View view = LayoutInflater.from(this).inflate(R.layout.dummy, null);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                0, 0,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                PixelFormat.TRANSLUCENT);
        manager.addView(view, params);
        return view;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

}
